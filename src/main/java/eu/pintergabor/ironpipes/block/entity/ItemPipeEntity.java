package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.ItemPipe;
import eu.pintergabor.ironpipes.menu.ItemMenu;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class ItemPipeEntity extends BasePipeEntity implements Container, MenuProvider {
	public final @NotNull NonNullList<ItemStack> inventory;

	public ItemPipeEntity(
		@NotNull BlockPos pos, @NotNull BlockState state
	) {
		super(ModBlockEntities.ITEM_PIPE_ENTITY, pos, state);
		final ItemPipe block = (ItemPipe) state.getBlock();
		inventory = NonNullList.withSize(block.inventorySize, ItemStack.EMPTY);
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
	public void loadAdditional(
		@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries
	) {
		super.loadAdditional(tag, registries);
		ContainerHelper.loadAllItems(tag, inventory, registries);
	}

	@Override
	protected void saveAdditional(
		@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries
	) {
		super.saveAdditional(tag, registries);
		ContainerHelper.saveAllItems(tag, inventory, registries);
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(
		int containerId, Inventory playerInventory, Player player
	) {
		return ItemMenu.create(containerId, playerInventory, this);
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
