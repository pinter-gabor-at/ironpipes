package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.block.entity.FluidFittingEntity;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.block.settings.FluidBlockSettings;
import eu.pintergabor.ironpipes.block.util.DripShowUtil;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import eu.pintergabor.ironpipes.tag.ModItemTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;


public class FluidFitting extends BaseFitting implements FluidCarryBlock {
    // BlockState properties.
	public static final EnumProperty<PipeFluid> FLUID =
		ModProperties.FLUID;
	// Block properties.
	public final float cloggingProbability;
	public final boolean canCarryWater;
	public final boolean canCarryLava;
	public final float fireBreakProbability;
	public final float fireDripProbability;
	public final float wateringProbability;
	public final float waterDrippingProbability;
	public final float lavaDrippingProbability;
	public final float waterFillingProbability;
	public final float lavaFillingProbability;
	// Matching CODEC.
	public static final MapCodec<FluidFitting> CODEC =
		RecordCodecBuilder.mapCodec((instance) -> instance.group(
			propertiesCodec(),
			Codec.INT.fieldOf("tick_rate")
				.forGetter((fitting) -> fitting.tickRate),
			Codec.BOOL.fieldOf("can_carry_water")
				.forGetter((fitting) -> fitting.canCarryWater),
			Codec.BOOL.fieldOf("can_carry_lava")
				.forGetter((fitting) -> fitting.canCarryLava),
			Codec.FLOAT.fieldOf("clogging_probability")
				.forGetter((fitting) -> fitting.cloggingProbability),
			Codec.FLOAT.fieldOf("fire_break_probability")
				.forGetter((fitting) -> fitting.fireBreakProbability),
			Codec.FLOAT.fieldOf("fire_drip_probability")
				.forGetter((fitting) -> fitting.fireDripProbability),
			Codec.FLOAT.fieldOf("watering_probability")
				.forGetter((fitting) -> fitting.wateringProbability),
			Codec.FLOAT.fieldOf("water_dripping_probability")
				.forGetter((fitting) -> fitting.waterDrippingProbability),
			Codec.FLOAT.fieldOf("lava_dripping_probability")
				.forGetter((fitting) -> fitting.lavaDrippingProbability),
			Codec.FLOAT.fieldOf("water_filling_probability")
				.forGetter((fitting) -> fitting.waterFillingProbability),
			Codec.FLOAT.fieldOf("lava_filling_probability")
				.forGetter((fitting) -> fitting.lavaFillingProbability)
		).apply(instance, FluidFitting::new));

	/**
	 * Create fitting as the CODEC requires it.
	 */
	public FluidFitting(
		Properties props,
		int tickRate, boolean canCarryWater, boolean canCarryLava,
		float cloggingProbability, float fireBreakProbability,
		float fireDripProbability, float wateringProbability,
		float waterDrippingProbability, float lavaDrippingProbability,
		float waterFillingProbability, float lavaFillingProbability
	) {
		super(props, tickRate);
		this.canCarryWater = canCarryWater;
		this.canCarryLava = canCarryLava;
		this.cloggingProbability = cloggingProbability;
		this.fireBreakProbability = fireBreakProbability;
		this.fireDripProbability = fireDripProbability;
		this.wateringProbability = wateringProbability;
		this.waterDrippingProbability = waterDrippingProbability;
		this.lavaDrippingProbability = lavaDrippingProbability;
		this.waterFillingProbability = waterFillingProbability;
		this.lavaFillingProbability = lavaFillingProbability;
		registerDefaultState(defaultBlockState()
			.setValue(FLUID, PipeFluid.NONE));
	}

	/**
	 * Create fitting using {@link FluidBlockSettings}.
	 */
	public FluidFitting(Properties props, FluidBlockSettings modSettings) {
		this(
			props,
			modSettings.tickRate(), modSettings.canCarryWater(), modSettings.canCarryLava(),
			modSettings.cloggingProbability(), modSettings.fireBreakProbability(),
			modSettings.fireDripProbability(), modSettings.wateringProbability(),
			modSettings.waterDrippingProbability(), modSettings.lavaDrippingProbability(),
			modSettings.waterFillingProbability(), modSettings.lavaFillingProbability()
		);
	}

	@Override
	protected void createBlockStateDefinition(
		@NotNull StateDefinition.Builder<Block, BlockState> builder
	) {
		super.createBlockStateDefinition(builder);
		builder.add(FLUID);
	}

	/**
	 * Create a block entity.
	 */
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FluidFittingEntity(pos, state);
	}

	/**
	 * Use item on a fitting.
	 * <p>
	 * If it is another piece of pipe or fitting then place it,
	 * otherwise continue with the default action.
	 */
	@Override
	protected @NotNull InteractionResult useItemOn(
		@NotNull ItemStack stack,
		@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos,
		@NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit
	) {
		// Allow placing fittings next to pipes and fittings.
		if (stack.is(ModItemTags.FLUID_PIPES_AND_FITTINGS)) {
			return InteractionResult.PASS;
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	/**
	 * Dripping visualization.
	 */
	@Override
	public void animateTick(
		@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
		@NotNull RandomSource random
	) {
		super.animateTick(state, level, pos, random);
		DripShowUtil.showDrip(level, pos, state, 0.0);
	}


	/**
	 * The fitting was removed.
	 */
	@Override
	protected void affectNeighborsAfterRemoval(
		@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos,
		boolean moved
	) {
		level.removeBlockEntity(pos);
	}

	/**
	 * Create a ticker, which will be called at every tick both on the client and on the server.
	 */
	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
		@NotNull Level level, @NotNull BlockState state,
		@NotNull BlockEntityType<T> blockEntityType
	) {
		if (!level.isClientSide) {
			// Need a tick only on the server to implement the pipe logic.
			return createTickerHelper(
				blockEntityType, ModBlockEntities.FLUID_FITTING_ENTITY,
				FluidFittingEntity::serverTick);
		}
		return null;
	}

	@Override
	public boolean canCarryWater() {
		return canCarryWater;
	}

	@Override
	public boolean canCarryLava() {
		return canCarryLava;
	}

	@Override
	public float getCloggingProbability() {
		return cloggingProbability;
	}

	@Override
	public float getFireBreakProbability() {
		return fireBreakProbability;
	}

	@Override
	public float getWaterDrippingProbability() {
		return waterDrippingProbability;
	}

	@Override
	public float getLavaDrippingProbability() {
		return lavaDrippingProbability;
	}

	@Override
	public float getWaterFillingProbability() {
		return waterFillingProbability;
	}

	@Override
	public float getLavaFillingProbability() {
		return lavaFillingProbability;
	}

	@Override
	protected @NotNull MapCodec<? extends FluidFitting> codec() {
		return CODEC;
	}
}
