package eu.pintergabor.ironpipes.registry;

import static eu.pintergabor.ironpipes.block.CopperPipe.FACING;
import static net.minecraft.block.NoteBlock.INSTRUMENT;
import static net.minecraft.block.NoteBlock.NOTE;

import java.util.Map;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.AbstractModBlockEntity;
import eu.pintergabor.ironpipes.block.entity.CopperFittingEntity;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import eu.pintergabor.ironpipes.block.entity.nbt.MoveablePipeDataHandler;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.networking.packet.ModNoteParticlePacket;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;

public class RegisterPipeNbtMethods {
    public static final Identifier WATER = Global.modId("water");
    public static final Identifier LAVA = Global.modId("lava");
    public static final Identifier SMOKE = Global.modId("smoke");
    private static final Map<Identifier, UniquePipeNbt> UNIQUE_PIPE_NBTS = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(Identifier id, DispenseMethod dispense, OnMoveMethod move, TickMethod tick, CanMoveMethod canMove) {
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
        register(Identifier.of("lunade", "default"), (nbt, world, pos, blockState, pipe) -> {
            boolean noteBlock = false;
            Optional<RegistryEntry.Reference<GameEvent>> optionalGameEvent = Registries.GAME_EVENT.getEntry(nbt.getSavedID());
            if (optionalGameEvent.isPresent() && optionalGameEvent.get().value() == GameEvent.NOTE_BLOCK_PLAY.value()) {
                pipe.noteBlockCooldown = 40;
                float volume = 3.0F;
                BlockPos originPos = BlockPos.ofFloored(nbt.getVec3dd());
                BlockState state = world.getBlockState(originPos);
                noteBlock = state.isOf(Blocks.NOTE_BLOCK);
                if (noteBlock) {
                    int k = state.get(NOTE);
                    float f = (float) Math.pow(2.0D, (double) (k - 12) / 12.0D);
                    world.playSound(null, pos, state.get(INSTRUMENT).getSound().value(), SoundCategory.RECORDS, volume, f);
                    //Send NoteBlock Particle Packet To Client
                    ModNoteParticlePacket.sendToAll(world, pos, k, world.getBlockState(pos).get(FACING));
                }
            }
            world.emitGameEvent(nbt.getEntity(world), optionalGameEvent.orElse(GameEvent.BLOCK_CHANGE), pos);
            if (noteBlock || pipe.noteBlockCooldown > 0) {
                if (nbt.useCount == 0) {
                    world.spawnParticles(
                        new VibrationParticleEffect(new BlockPositionSource(nbt.getBlockPos()), 5),
                        nbt.vec3d.x, nbt.vec3d.y, nbt.vec3d.z,
                        1, 0.0, 0.0, 0.0, 0.0);
                    nbt.useCount = 1;
                }
            }
            pipe.inputGameEventPos = nbt.getBlockPos();
            pipe.gameEventNbtVec3d = nbt.vec3d;
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (nbt.foundEntity != null) {
                nbt.vec3d2 = nbt.foundEntity.getPos();
            }
        }, (nbt, world, pos, blockState, blockEntity) -> true);


        register(WATER, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3d(11, 0, 0);
            } else if (!blockEntity.canWater && blockEntity.moveType == AbstractModBlockEntity.MoveType.FROM_PIPE) {
                nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                if (nbt.vec3d.getX() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carryWater) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
            if (movablePipeNbt != null) {
                return movablePipeNbt.vec3d == null || movablePipeNbt.vec3d.getX() <= nbt.vec3d.getX() - 1;
            }
            return true;
        });

        register(LAVA, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER) == null) {
                if (blockEntity instanceof CopperFittingEntity) {
                    nbt.vec3d = new Vec3d(11, 0, 0);
                } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractModBlockEntity.MoveType.FROM_PIPE) {
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
        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt lavaNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
            MoveablePipeDataHandler.SaveableMovablePipeNbt waterNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
            MoveablePipeDataHandler.SaveableMovablePipeNbt smokeNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(SMOKE);
            if (waterNBT != null && lavaNBT != null) {
                lavaNBT.vec3d = Vec3d.ZERO;
                lavaNBT.shouldSave = false;
                lavaNBT.shouldMove = false;
                waterNBT.vec3d = Vec3d.ZERO;
                waterNBT.shouldSave = false;
                waterNBT.shouldMove = false;
                if (smokeNBT == null) {
                    blockEntity.moveablePipeDataHandler.setMoveablePipeNbt(SMOKE, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3dd(new Vec3d(11, 0, 0)).withShouldCopy(true).withNBTID(SMOKE));
                } else {
                    smokeNBT.vec3d = new Vec3d(11, 0, 0);
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carryLava) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
            if (movablePipeNbt != null) {
                return movablePipeNbt.vec3d == null || movablePipeNbt.getVec3dd().getX() <= nbt.getVec3dd().getX() - 1;
            }
            return true;
        });


        register(SMOKE, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3d(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractModBlockEntity.MoveType.FROM_PIPE) {
                nbt.vec3d = nbt.getVec3dd().add(-1, 0, 0);
                if (nbt.getVec3dd().getX() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carrySmoke) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(SMOKE);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3dd() == null || movablePipeNbt.getVec3dd().getX() <= nbt.getVec3dd().getX() - 1;
            }
            return true;
        });

    }

    @FunctionalInterface
    public interface DispenseMethod {
        void dispense(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe);
    }

    @FunctionalInterface
    public interface OnMoveMethod {
        void onMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractModBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface TickMethod {
        void tick(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractModBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface CanMoveMethod {
        boolean canMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractModBlockEntity blockEntity);
    }

    public record UniquePipeNbt(
        DispenseMethod dispenseMethod,
        OnMoveMethod onMoveMethod,
        TickMethod tickMethod,
        CanMoveMethod canMoveMethod
    ) {
    }


}
