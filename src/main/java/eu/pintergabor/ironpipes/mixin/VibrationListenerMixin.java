package eu.pintergabor.ironpipes.mixin;

import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.Vibrations;

@Mixin(Vibrations.VibrationListener.class)
public abstract class VibrationListenerMixin {
    @Inject(
        method = "listen*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/event/Vibrations$VibrationListener;listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/Vibrations$ListenerData;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)V",
            shift = At.Shift.AFTER
        )
    )
    private void listen(
        ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos,
        CallbackInfoReturnable<Boolean> info
    ) {
        BlockEntity blockEntity = world.getBlockEntity(BlockPos.ofFloored(emitterPos));
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            if (pipeEntity.inputGameEventPos != null &&
                pipeEntity.gameEventNbtVec3d != null &&
                pipeEntity.noteBlockCooldown <= 0) {
                double x = pipeEntity.gameEventNbtVec3d.getX();
                double y = pipeEntity.gameEventNbtVec3d.getY();
                double z = pipeEntity.gameEventNbtVec3d.getZ();
                world.spawnParticles(
                    new VibrationParticleEffect(new BlockPositionSource(pipeEntity.inputGameEventPos), 5),
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
                pipeEntity.inputGameEventPos = null;
                pipeEntity.gameEventNbtVec3d = null;
            }
        }
    }
}
