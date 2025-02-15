package eu.pintergabor.ironpipes.registry;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PipeMovementRestrictions {

    public static Map<Identifier, PipeMovementRestriction> PIPE_MOVEMENT_RESTRICTIONS = new Object2ObjectLinkedOpenHashMap<>();

    public static <T extends BlockEntity> void register(Identifier id, CanTransferTo<T> canTransferTo, CanTakeFrom<T> canTakeFrom) {
        PIPE_MOVEMENT_RESTRICTIONS.put(id, new PipeMovementRestriction<T>(canTransferTo, canTakeFrom));
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(Identifier id) {
        if (PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return PIPE_MOVEMENT_RESTRICTIONS.get(id).canTransferTo;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(Identifier id) {
        if (PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return PIPE_MOVEMENT_RESTRICTIONS.get(id).canTakeFrom;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(@NotNull T entity) {
        return getCanTransferTo(Registries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).get().getValue());
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(@NotNull T entity) {
        return getCanTakeFrom(Registries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).get().getValue());
    }

    public static void init() {

    }

    @FunctionalInterface
    public interface CanTransferTo<T extends BlockEntity> {
        boolean canTransfer(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom<T extends BlockEntity> {
        boolean canTake(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    public record PipeMovementRestriction<T extends BlockEntity>(
        CanTransferTo<T> canTransferTo,
        CanTakeFrom<T> canTakeFrom
    ) {
    }
}
