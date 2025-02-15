package eu.pintergabor.ironpipes.networking;

import eu.pintergabor.ironpipes.networking.packet.ModNoteParticlePacket;

import net.minecraft.network.RegistryByteBuf;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModNetworking {

    public static void init() {
        PayloadTypeRegistry<RegistryByteBuf> registry =
            PayloadTypeRegistry.playS2C();
        registry.register(
            ModNoteParticlePacket.PACKET_TYPE,
            ModNoteParticlePacket.CODEC);
    }
}
