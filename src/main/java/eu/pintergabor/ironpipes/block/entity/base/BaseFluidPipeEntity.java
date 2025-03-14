package eu.pintergabor.ironpipes.block.entity.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class BaseFluidPipeEntity extends BasePipeEntity {
    public int waterCooldown;
    public boolean canDispense;
    public boolean hasWater;
    public boolean hasLava;

    public BaseFluidPipeEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.waterCooldown = -1;
    }

    public void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState) {
        if (!world.isClient()) {
            // TODO
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        hasWater = nbtCompound.getBoolean("hasWater");
        hasLava = nbtCompound.getBoolean("hasLava");
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        nbtCompound.putBoolean("hasWater", hasWater);
        nbtCompound.putBoolean("hasLava", hasLava);
    }
}
