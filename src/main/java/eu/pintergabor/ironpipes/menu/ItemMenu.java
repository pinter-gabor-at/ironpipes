package eu.pintergabor.ironpipes.menu;

import eu.pintergabor.ironpipes.block.ItemFitting;
import eu.pintergabor.ironpipes.block.ItemPipe;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;


/**
 * Menu for {@link ItemPipe} and {@link ItemFitting}.
 */
public class ItemMenu extends ChestMenu {

	public ItemMenu(
		@NotNull MenuType<?> type, int containerId, @NotNull Inventory playerInventory,
		@NotNull Container container, int rows
	) {
		super(type, containerId, playerInventory, container, rows);
	}

	/**
	 * @param container Size must be (1..6) * 9.
	 */
	@SuppressWarnings("PointlessArithmeticExpression")
	public static ItemMenu create(
		int containerId, Inventory playerInventory, Container container
	) {
		return switch (container.getContainerSize()) {
			case 1 * 9 -> new ItemMenu(MenuType.GENERIC_9x1, containerId, playerInventory, container, 1);
			case 2 * 9 -> new ItemMenu(MenuType.GENERIC_9x2, containerId, playerInventory, container, 2);
			case 3 * 9 -> new ItemMenu(MenuType.GENERIC_9x3, containerId, playerInventory, container, 3);
			case 4 * 9 -> new ItemMenu(MenuType.GENERIC_9x4, containerId, playerInventory, container, 4);
			case 5 * 9 -> new ItemMenu(MenuType.GENERIC_9x5, containerId, playerInventory, container, 5);
			case 6 * 9 -> new ItemMenu(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6);
			default -> throw new IllegalStateException(
				"Unsupported container size: " + container.getContainerSize());
		};
	}
}
