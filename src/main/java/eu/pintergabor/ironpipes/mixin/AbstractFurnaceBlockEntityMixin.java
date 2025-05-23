package eu.pintergabor.ironpipes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;


@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

	/**
	 * Remove the fuel slot from the list of bottom slots.
	 */
	@Shadow
	@SuppressWarnings("unused")
	private static final int[] SLOTS_FOR_DOWN = new int[]{2};
}
