package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    private boolean simpleCopperPipes$hadWaterPipeNearby;

    @Inject(at = @At("HEAD"), method = "updateWaterState")
    private void updateInWaterState(CallbackInfoReturnable<Boolean> info) {
        if (!getWorld().isClient) {
            Entity that = (Entity) (Object) this;
            simpleCopperPipes$hadWaterPipeNearby =
                LeakingPipeManager.isWaterPipeNearby(that, 2);
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isBeingRainedOn")
    private boolean isBeingRainedOn(boolean original) {
        return original ||
            simpleCopperPipes$hadWaterPipeNearby;
    }

    @Shadow
    public World getWorld() {
        throw new AssertionError("Mixin error.");
    }
}
