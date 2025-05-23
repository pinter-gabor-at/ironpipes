package eu.pintergabor.ironpipes.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public abstract non-sealed class BaseFittingEntity extends BaseBlockEntity {

	public BaseFittingEntity(
		BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
	) {
		super(blockEntityType, blockPos, blockState);
	}
}
