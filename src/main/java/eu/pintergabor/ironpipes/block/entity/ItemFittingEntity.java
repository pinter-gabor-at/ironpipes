package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.util.DripActionUtil;
import eu.pintergabor.ironpipes.block.util.FluidFittingUtil;
import eu.pintergabor.ironpipes.block.util.FluidUtil;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;

import static eu.pintergabor.ironpipes.block.BaseBlock.getTickPos;
import static eu.pintergabor.ironpipes.block.util.TickUtil.TickPos;


public class ItemFittingEntity extends BaseFittingEntity {

	public ItemFittingEntity(
		@NotNull BlockPos pos, @NotNull BlockState state
	) {
		super(ModBlockEntities.ITEM_FITTING_ENTITY, pos, state);
	}

	/**
	 * Called at every tick on the server.
	 */
	public static void serverTick(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@NotNull ItemFittingEntity entity
	) {
//		final TickPos tickPos = getTickPos(level, state);
//		final ServerLevel serverLevel = (ServerLevel) level;
//		if (tickPos == TickPos.START) {
//			// Pull fluid.
//			FluidFittingUtil.pull(serverLevel, pos, state, entity);
//			// Clogging.
//			FluidUtil.clog(serverLevel, pos, state);
//		}
//		if (tickPos == TickPos.MIDDLE) {
//			final boolean powered = state.getValueOrElse(BlockStateProperties.POWERED, false);
//			if (!powered) {
//				// Drip.
//				DripActionUtil.dripDown(serverLevel, pos, state);
//				// Break.
//				FluidFittingUtil.breakFire(serverLevel, pos, state);
//			}
//		}
	}
}
