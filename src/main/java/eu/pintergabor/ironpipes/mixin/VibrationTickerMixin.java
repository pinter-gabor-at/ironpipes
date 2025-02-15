package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.Vibrations;

@Mixin(Vibrations.Ticker.class)
public interface VibrationTickerMixin {

    @WrapWithCondition(
        method = "method_51408",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"
        )
    )
    private static boolean scheduleVibration(
        ServerWorld world, ParticleEffect parameters,
        double x, double y, double z,
        int count, double offsetX, double offsetY, double offsetZ, double speed,
        Vibrations.ListenerData listenerData, Vibrations.Callback callback
    ) {
        return !(callback instanceof CopperPipeEntity.VibrationCallback);
    }

    @Inject(at = @At("HEAD"), method = "spawnVibrationParticle", cancellable = true)
    private static void spawnVibrationParticle(
        ServerWorld world,
        Vibrations.ListenerData listenerData, Vibrations.Callback callback,
        CallbackInfo ci) {
        if (callback instanceof CopperPipeEntity.VibrationCallback) {
            ci.cancel();
        }
    }
}
