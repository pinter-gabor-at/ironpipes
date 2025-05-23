package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.block.entity.FluidFittingEntity;
import eu.pintergabor.ironpipes.block.entity.ItemFittingEntity;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.block.settings.FluidBlockSettings;
import eu.pintergabor.ironpipes.block.util.DripShowUtil;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import eu.pintergabor.ironpipes.tag.ModItemTags;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class ItemFitting extends BaseFitting {
	/**
	 * Create fitting as the CODEC requires it.
	 */
	public ItemFitting(
		Properties props,
		int tickRate
	) {
		super(props, tickRate);
	}

	@Override
	protected void createBlockStateDefinition(
		@NotNull StateDefinition.Builder<Block, BlockState> builder
	) {
		super.createBlockStateDefinition(builder);
	}

	/**
	 * Create a block entity.
	 */
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ItemFittingEntity(pos, state);
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
		if (stack.is(ModItemTags.ITEM_PIPES_AND_FITTINGS)) {
			return InteractionResult.PASS;
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
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
				blockEntityType, ModBlockEntities.ITEM_FITTING_ENTITY,
				ItemFittingEntity::serverTick);
		}
		return null;
	}
}
