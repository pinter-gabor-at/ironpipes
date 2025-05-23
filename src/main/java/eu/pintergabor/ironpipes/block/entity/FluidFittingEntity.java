package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.BaseBlock.getTickPos;
import static eu.pintergabor.ironpipes.block.util.TickUtil.TickPos;

import eu.pintergabor.ironpipes.block.util.DripActionUtil;
import eu.pintergabor.ironpipes.block.util.FluidFittingUtil;
import eu.pintergabor.ironpipes.block.util.FluidUtil;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class FluidFittingEntity extends BaseFittingEntity {

	public FluidFittingEntity(
		@NotNull BlockPos pos, @NotNull BlockState state
	) {
		super(ModBlockEntities.FLUID_FITTING_ENTITY, pos, state);
	}

	/**
	 * Called at every tick on the server.
	 */
	public static void serverTick(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@NotNull FluidFittingEntity entity
	) {
		final TickPos tickPos = getTickPos(level, state);
		final ServerLevel serverLevel = (ServerLevel) level;
		if (tickPos == TickPos.START) {
			// Pull fluid.
			FluidFittingUtil.pull(serverLevel, pos, state, entity);
			// Clogging.
			FluidUtil.clog(serverLevel, pos, state);
		}
		if (tickPos == TickPos.MIDDLE) {
			final boolean powered = state.getValueOrElse(BlockStateProperties.POWERED, false);
			if (!powered) {
				// Drip.
				DripActionUtil.dripDown(serverLevel, pos, state);
				// Break.
				FluidFittingUtil.breakFire(serverLevel, pos, state);
			}
		}
	}
}
