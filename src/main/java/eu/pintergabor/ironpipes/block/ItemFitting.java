package eu.pintergabor.ironpipes.block;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.ItemFittingEntity;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.stats.Stats;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.BlockHitResult;


public abstract class ItemFitting extends BaseFitting {
	// Block properties.
	public final int inventorySize;

	public ItemFitting(
		Properties props,
		int tickRate, int inventorySize
	) {
		super(props, tickRate);
		this.inventorySize = inventorySize;
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

	@Override
	protected @NotNull InteractionResult useWithoutItem(
		BlockState state, Level level, BlockPos pos,
		Player player, BlockHitResult hit
	) {
		Global.LOGGER.info("Inventory size: {}", inventorySize);
		if (level.getBlockEntity(pos) instanceof ItemFittingEntity itemFittingEntity) {
			player.openMenu(itemFittingEntity);
			player.awardStat(ModStats.INTERACTIONS);
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, hit);
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
