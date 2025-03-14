package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class WoodenPipeEntity extends BaseFluidPipeEntity {

    public WoodenPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_PIPE_ENTITY, blockPos, blockState);
    }

    public void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState) {
        if (!world.isClient()) {
//            boolean validWater = isValidFluidNBT(waterNbt) && ModConfig.get().carryWater;
//            boolean validLava = isValidFluidNBT(lavaNbt) && ModConfig.get().carryLava;
//            if (this.hasWater && this.hasLava) {
//                validWater = false;
//                validLava = false;
//            }
//            if (state.contains(ModBlockStateProperties.FLUID)) {
//                state = state.with(ModBlockStateProperties.FLUID,
//                    validWater ? PipeFluid.WATER : validLava ? PipeFluid.LAVA : PipeFluid.NONE);
//            }
            // TODO: Dispensing.
            // Dripping.
            if (blockState.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER &&
                blockState.get(Properties.FACING) != Direction.UP) {
                LeakingPipeManager.addPos(world, blockPos);
            }
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        waterCooldown = nbtCompound.getInt("waterCooldown");
        hasWater = nbtCompound.getBoolean("hasWater");
        hasLava = nbtCompound.getBoolean("hasLava");
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        nbtCompound.putInt("waterCooldown", waterCooldown);
        nbtCompound.putBoolean("hasWater", hasWater);
        nbtCompound.putBoolean("hasLava", hasLava);
    }
}
