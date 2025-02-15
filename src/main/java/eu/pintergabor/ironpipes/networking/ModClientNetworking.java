package eu.pintergabor.ironpipes.networking;

import eu.pintergabor.ironpipes.networking.packet.ModNoteParticlePacket;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ModClientNetworking {

    public static void registerPacketReceivers() {
        receiveNoteParticlePacket();
    }

    public static void receiveNoteParticlePacket() {
        ClientPlayNetworking.registerGlobalReceiver(
            ModNoteParticlePacket.PACKET_TYPE,
            (packet, context) -> {
                ClientWorld world = context.client().world;
                if (world != null) {
                    BlockPos pos = packet.blockPos();
                    Direction direction = packet.direction();
                    double x = direction.getOffsetX() * 0.6;
                    double y = direction.getOffsetY() * 0.6;
                    double z = direction.getOffsetZ() * 0.6;
                    world.addParticle(
                        ParticleTypes.NOTE,
                        (double) pos.getX() + 0.5 + x,
                        (double) pos.getY() + 0.5 + y,
                        (double) pos.getZ() + 0.5 + z,
                        (double) packet.pitch() / 24.0,
                        0.0,
                        0.0
                    );
                }
            });
    }
}
