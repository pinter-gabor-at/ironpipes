package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.ItemFitting;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.world.inventory.HopperMenu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;


public class ItemFittingEntity extends BaseFittingEntity implements RandomizableContainer, MenuProvider {
	public @Nullable ResourceKey<LootTable> lootTable;
	protected long lootTableSeed = 0L;
	public NonNullList<ItemStack> inventory;

	public ItemFittingEntity(
		@NotNull BlockPos pos, @NotNull BlockState state
	) {
		super(ModBlockEntities.ITEM_FITTING_ENTITY, pos, state);
		final ItemFitting block = (ItemFitting) state.getBlock();
		inventory = NonNullList.withSize(block.inventorySize, ItemStack.EMPTY);
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

	@Override
	public int getContainerSize() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public @NotNull ItemStack getItem(int slot) {
		return inventory.get(slot);
	}

	@Override
	public @NotNull ItemStack removeItem(int slot, int amount) {
		ItemStack itemStack = ContainerHelper.removeItem(inventory, slot, amount);
		if (!itemStack.isEmpty()) {
			setChanged();
		}
		return itemStack;
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		stack.limitSize(getMaxStackSize(stack));
		setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		inventory.clear();
	}

	@Override
	public @Nullable ResourceKey<LootTable> getLootTable() {
		return lootTable;
	}

	@Override
	public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public long getLootTableSeed() {
		return lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long seed) {
		this.lootTableSeed = seed;
	}

	@Override
	public void loadAdditional(
		@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries
	) {
		super.loadAdditional(tag, registries);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!tryLoadLootTable(tag)) {
			ContainerHelper.loadAllItems(tag, inventory, registries);
		}
	}

	@Override
	protected void saveAdditional(
		@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries
	) {
		super.saveAdditional(tag, registries);
		if (!trySaveLootTable(tag)) {
			ContainerHelper.saveAllItems(tag, inventory, registries);
		}
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new HopperMenu(i, inventory, this);
	}
}
