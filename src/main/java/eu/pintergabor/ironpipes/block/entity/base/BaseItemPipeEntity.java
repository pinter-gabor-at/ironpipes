package eu.pintergabor.ironpipes.block.entity.base;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class BaseItemPipeEntity extends BasePipeEntity implements Inventory {
    public DefaultedList<ItemStack> inventory;

    public BaseItemPipeEntity(
        BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    }

    /**
     * Choose one of the non-empty slots of the inventory with equal probability.
     *
     * @return The slot number of one of the non-empty slots, or -1 if all slots are empty.
     */
    public int chooseNonEmptySlot(Random random) {
        int chosenSlot = -1;
        int nonEmptySlotCount = 1;
        for (int i = 0; i < inventory.size(); i++) {
            // Choose the next non-empty slot with 1/nonEmptySlotCount probability.
            if (!inventory.get(i).isEmpty() && random.nextInt(nonEmptySlotCount++) == 0) {
                chosenSlot = i;
            }
        }
        return chosenSlot;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    public void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState) {
        if (!world.isClient()) {
            // TODO
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
        Inventories.readNbt(nbtCompound, inventory, lookupProvider);
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        Inventories.writeNbt(nbtCompound, inventory, lookupProvider);
    }

    @NotNull
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> defaultedList) {
        inventory = defaultedList;
    }

//    @NotNull
//    protected Text getContainerName() {
//        return Text.translatable(getCachedState().getBlock().getTranslationKey());
//    }
//
//    @NotNull
//    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        return new HopperScreenHandler(syncId, playerInventory, this);
//    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : getHeldStacks()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return getHeldStacks().get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(getHeldStacks(), slot, amount);
        if (!itemStack.isEmpty()) {
            markDirty();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(getHeldStacks(), slot);
    }

    /**
     * Set inventory {@code slot} to {@code stack}.
     */
    @Override
    public void setStack(int slot, ItemStack stack) {
        if (stack != null) {
            getHeldStacks().set(slot, stack);
            stack.capCount(getMaxCount(stack));
            markDirty();
        }
    }

    /**
     * Clear the inventory.
     */
    @Override
    public void clear() {
        getHeldStacks().clear();
    }
}
