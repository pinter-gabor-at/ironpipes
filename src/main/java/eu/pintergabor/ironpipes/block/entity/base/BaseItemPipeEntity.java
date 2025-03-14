package eu.pintergabor.ironpipes.block.entity.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BaseItemPipeEntity extends BasePipeEntity implements LootableInventory {
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
        generateLoot(null);
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

    /**
     * Set inventory slot to itemStack.
     */
    @Override
    public void setStack(int slot, ItemStack itemStack) {
        generateLoot(null);
        if (itemStack != null) {
            getHeldStacks().set(slot, itemStack);
            if (getMaxCountPerStack() < itemStack.getCount()) {
                itemStack.setCount(getMaxCountPerStack());
            }
        }
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
        if (!readLootTable(nbtCompound)) {
            Inventories.readNbt(nbtCompound, inventory, lookupProvider);
        }
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        if (!writeLootTable(nbtCompound)) {
            Inventories.writeNbt(nbtCompound, inventory, lookupProvider);
        }
    }

    @Override
    @NotNull
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> defaultedList) {
        inventory = defaultedList;
    }

    @Override
    @NotNull
    protected Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    @NotNull
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return null;
    }

    @Override
    public @Nullable RegistryKey<LootTable> getLootTable() {
        return null;
    }

    @Override
    public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {

    }

    @Override
    public long getLootTableSeed() {
        return 0;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {

    }

    @Override
    public void clear() {

    }
}
