package eu.pintergabor.ironpipes.registry;

import java.util.Map;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.AbstractModBlockEntity;
import eu.pintergabor.ironpipes.block.entity.CopperFittingEntity;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import eu.pintergabor.ironpipes.block.entity.nbt.MoveablePipeDataHandler;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class RegisterPipeNbtMethods {
    public static final Identifier WATER = Global.modId("water");
    public static final Identifier LAVA = Global.modId("lava");
    public static final Identifier SMOKE = Global.modId("smoke");
    private static final Map<Identifier, UniquePipeNbt> UNIQUE_PIPE_NBTS = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(
        Identifier id,
        DispenseMethod dispense,
        OnMoveMethod move,
        TickMethod tick,
        CanMoveMethod canMove) {
        UNIQUE_PIPE_NBTS.put(id, new UniquePipeNbt(dispense, move, tick, canMove));
    }

    @Nullable
    public static UniquePipeNbt getUniquePipeNbt(Identifier id) {
        if (UNIQUE_PIPE_NBTS.containsKey(id)) {
            return UNIQUE_PIPE_NBTS.get(id);
        }
        return null;
    }

    @Nullable
    public static DispenseMethod getDispense(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.dispenseMethod();
        }
        return null;
    }

    @Nullable
    public static OnMoveMethod getMove(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.onMoveMethod();
        }
        return null;
    }

    @Nullable
    public static TickMethod getTick(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.tickMethod();
        }
        return null;
    }

    @Nullable
    public static CanMoveMethod getCanMove(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.canMoveMethod();
        }
        return null;
    }

    public static void init() {
        // Register water action.
        register(WATER,
            // Dispense.
            (nbt, world, pos, blockState, pipe) -> {
                // Nothing
            },
            // Move.
            (nbt, world, pos, blockState, blockEntity) -> {
                if (blockEntity instanceof CopperFittingEntity) {
                    nbt.vec3d = new Vec3d(11, 0, 0);
                } else if (!blockEntity.canWater && blockEntity.moveType == AbstractModBlockEntity.MoveType.FROM_PIPE) {
                    nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                    if (nbt.vec3d.getX() <= 0) {
                        nbt.shouldSave = false;
                        nbt.shouldMove = false;
                    }
                }
            },
            // Tick.
            (nbt, world, pos, blockState, blockEntity) -> {
                // Nothing
            },
            // Can move.
            (nbt, world, pos, blockState, blockEntity) -> {
                if (!SimpleCopperPipesConfig.get().carryWater) {
                    return false;
                }
                MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt =
                    blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
                if (movablePipeNbt != null) {
                    return movablePipeNbt.vec3d == null || movablePipeNbt.vec3d.getX() <= nbt.vec3d.getX() - 1;
                }
                return true;
            });
        // Register lava action.
        register(LAVA,
            // Dispense.
            (nbt, world, pos, blockState, pipe) -> {
                // Nothing.
            },
            // Move.
            (nbt, world, pos, blockState, blockEntity) -> {
                if (blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER) == null) {
                    if (blockEntity instanceof CopperFittingEntity) {
                        nbt.vec3d = new Vec3d(11, 0, 0);
                    } else if (blockEntity.moveType == AbstractModBlockEntity.MoveType.FROM_PIPE) {
                        nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                        if (nbt.vec3d.getX() <= 0) {
                            nbt.shouldSave = false;
                            nbt.shouldMove = false;
                        }
                    }
                } else {
                    nbt.vec3d = Vec3d.ZERO;
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            },
            // Tick.
            (nbt, world, pos, blockState, blockEntity) -> {
                MoveablePipeDataHandler.SaveableMovablePipeNbt lavaNBT =
                    blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
                MoveablePipeDataHandler.SaveableMovablePipeNbt waterNBT =
                    blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
                if (waterNBT != null && lavaNBT != null) {
                    lavaNBT.vec3d = Vec3d.ZERO;
                    lavaNBT.shouldSave = false;
                    lavaNBT.shouldMove = false;
                    waterNBT.vec3d = Vec3d.ZERO;
                    waterNBT.shouldSave = false;
                    waterNBT.shouldMove = false;
                }
            },
            // Can move.
            (nbt, world, pos, blockState, blockEntity) -> {
                if (!SimpleCopperPipesConfig.get().carryLava) {
                    return false;
                }
                MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt =
                    blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
                if (movablePipeNbt != null) {
                    return movablePipeNbt.vec3d == null || movablePipeNbt.getVec3dd().getX() <= nbt.getVec3dd().getX() - 1;
                }
                return true;
            });
    }

    @FunctionalInterface
    public interface DispenseMethod {
        void dispense(
            MoveablePipeDataHandler.SaveableMovablePipeNbt nbt,
            ServerWorld world,
            BlockPos pos,
            BlockState state,
            CopperPipeEntity pipe);
    }

    @FunctionalInterface
    public interface OnMoveMethod {
        void onMove(
            MoveablePipeDataHandler.SaveableMovablePipeNbt nbt,
            ServerWorld world,
            BlockPos pos,
            BlockState state,
            AbstractModBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface TickMethod {
        void tick(
            MoveablePipeDataHandler.SaveableMovablePipeNbt nbt,
            ServerWorld world,
            BlockPos pos,
            BlockState state,
            AbstractModBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface CanMoveMethod {
        boolean canMove(
            MoveablePipeDataHandler.SaveableMovablePipeNbt nbt,
            ServerWorld world,
            BlockPos pos,
            BlockState state,
            AbstractModBlockEntity blockEntity);
    }

    public record UniquePipeNbt(
        DispenseMethod dispenseMethod,
        OnMoveMethod onMoveMethod,
        TickMethod tickMethod,
        CanMoveMethod canMoveMethod
    ) {
    }
}
