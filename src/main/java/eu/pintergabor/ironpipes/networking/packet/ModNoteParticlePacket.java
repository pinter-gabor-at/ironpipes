package eu.pintergabor.ironpipes.networking.packet;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public record ModNoteParticlePacket(
    BlockPos blockPos, int pitch, Direction direction)
    implements CustomPayload {

    public static final Id<ModNoteParticlePacket> PACKET_TYPE = new Id<>(
        Global.modId("note_particle")
    );

    public static final PacketCodec<PacketByteBuf, ModNoteParticlePacket> CODEC =
        PacketCodec.of(ModNoteParticlePacket::write, ModNoteParticlePacket::new);

    public ModNoteParticlePacket(@NotNull PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readVarInt(), buf.readEnumConstant(Direction.class));
    }

    public static void sendToAll(ServerWorld serverLevel, BlockPos pos, int pitch, Direction direction) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(serverLevel, pos)) {
            ServerPlayNetworking.send(
                player,
                new ModNoteParticlePacket(pos, pitch, direction));
        }
    }

    public void write(@NotNull PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeVarInt(this.pitch);
        buf.writeEnumConstant(this.direction);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_TYPE;
    }
}
