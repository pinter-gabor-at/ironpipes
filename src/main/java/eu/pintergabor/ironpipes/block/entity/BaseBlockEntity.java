package eu.pintergabor.ironpipes.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public abstract sealed class BaseBlockEntity extends BlockEntity
	permits BaseFittingEntity, BasePipeEntity {

	public BaseBlockEntity(
		BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
	) {
		super(blockEntityType, blockPos, blockState);
	}
}
