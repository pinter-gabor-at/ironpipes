package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.BaseBlock.getTickPos;
import static eu.pintergabor.ironpipes.block.util.TickUtil.TickPos;

import eu.pintergabor.ironpipes.block.util.DripActionUtil;
import eu.pintergabor.ironpipes.block.util.FluidDispenseUtil;
import eu.pintergabor.ironpipes.block.util.FluidPullUtil;
import eu.pintergabor.ironpipes.block.util.FluidPushUtil;
import eu.pintergabor.ironpipes.block.util.FluidUtil;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class ItemPipeEntity extends BasePipeEntity {

	public ItemPipeEntity(
		@NotNull BlockPos pos, @NotNull BlockState state
	) {
		super(ModBlockEntities.ITEM_PIPE_ENTITY, pos, state);
	}

	/**
	 * Called at every tick on the server.
	 */
	public static void serverTick(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@NotNull ItemPipeEntity entity
	) {
//		final TickPos tickPos = getTickPos(level, state);
//		final ServerLevel serverLevel = (ServerLevel) level;
//		if (tickPos == TickPos.START) {
//			// Pull fluid.
//			FluidPullUtil.pull(serverLevel, pos, state, entity);
//			// Clogging.
//			FluidUtil.clog(serverLevel, pos, state);
//		}
//		if (tickPos == TickPos.MIDDLE) {
//			// Push fluid into blocks that are not capable of pulling it.
//			FluidPushUtil.push(serverLevel, pos, state);
//			// Dispense fluid.
//			FluidDispenseUtil.dispense(level, pos, state);
//			// Drip.
//			DripActionUtil.dripDown(serverLevel, pos, state);
//			// Break.
//			FluidDispenseUtil.breakFire(serverLevel, pos, state);
//		}
	}
}
