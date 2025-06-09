package eu.pintergabor.ironpipes.block;

import eu.pintergabor.ironpipes.block.entity.ItemPipeEntity;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


/**
 * An item pipe can carry items.
 */
public abstract class ItemPipe extends BasePipe {
	// Block properties.
	public final int inventorySize;

	public ItemPipe(
		Properties props,
		int tickRate, int inventorySize
	) {
		super(props, tickRate);
		this.inventorySize = inventorySize;
	}

	/**
	 * Create a block entity.
	 */
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ItemPipeEntity(pos, state);
	}

	/**
	 * Use empty hand on a pipe.
	 * <p>
	 * Display contents.
	 */
	@Override
	protected @NotNull InteractionResult useWithoutItem(
		BlockState state, Level level, BlockPos pos,
		Player player, BlockHitResult hit
	) {
		if (level.getBlockEntity(pos) instanceof ItemPipeEntity itemPipeEntity) {
			player.openMenu(itemPipeEntity);
			player.awardStat(ModStats.INTERACTIONS);
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, hit);
	}

	/**
	 * Use item on a pipe.
	 * <p>
	 * If it is another piece of pipe or fitting then place it,
	 * otherwise continue with the default action.
	 */
	@Override
	protected @NotNull InteractionResult useItemOn(
		@NotNull ItemStack stack,
		@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
		@NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit
	) {
		if (stack.is(ModItemTags.ITEM_PIPES_AND_FITTINGS)) {
			// Allow placing pipes next to pipes and fittings.
			return InteractionResult.PASS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
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
				blockEntityType, ModBlockEntities.ITEM_PIPE_ENTITY,
				ItemPipeEntity::serverTick);
		}
		return null;
	}
}
