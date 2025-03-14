package eu.pintergabor.ironpipes.block.entity.leaking;

import java.util.ArrayList;

import eu.pintergabor.ironpipes.blockold.CopperPipe;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class LeakingPipeManager {
    private static final ArrayList<LeakingPipePos> leakingPipePosesOne = new ArrayList<>();
    private static final ArrayList<LeakingPipePos> leakingPipePosesTwo = new ArrayList<>();
    private static boolean isAlt;

    /**
     * Check if there is a leaking water pipe within range.
     * <p>
     * Y range is fixed [1..12].
     *
     * @param entity Target entity
     * @param range  X and Z range [-range..+range]
     * @return true if there is a leaking water pipe in range.
     */
    public static boolean isWaterPipeNearby(Entity entity, int range) {
        @SuppressWarnings("unchecked")
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        // Target coordinates.
        int x = entity.getBlockX();
        int y = entity.getBlockY();
        int z = entity.getBlockZ();
        Vec3d entityPos = entity.getPos();
        Identifier dimension = entity.getWorld().getRegistryKey().getValue();
        for (LeakingPipePos leakingPos : copiedList) {
            if (leakingPos.dimension.equals(dimension)) {
                BlockPos leakPos = leakingPos.pos;
                double xDistance = leakPos.getX() - x;
                // If X distance is within range of the leaking position.
                if (-range <= xDistance && xDistance <= range) {
                    double zDistance = leakPos.getZ() - z;
                    // If Z distance is within range of the leaking position.
                    if (-range <= zDistance && zDistance <= range) {
                        int leakY = leakPos.getY();
                        // If Y distance is within range of the leaking position.
                        if (leakY - 12 <= y && y < leakY) {
                            BlockHitResult hitResult = entity.getWorld().raycast(
                                new RaycastContext(entityPos,
                                    new Vec3d(leakPos.getX() + 0.5, leakPos.getY() + 0.5, leakPos.getZ() + 0.5),
                                    RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
                            if (hitResult.getBlockPos().equals(leakPos)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if there is a leaking water pipe within range.
     * <p>
     * Y range is fixed [1..12].
     *
     * @param view World
     * @param pos    Target position
     * @param range       X and Z range [-range..+range]
     * @return true if there is a leaking water pipe in range.
     */
    public static boolean isWaterPipeNearby(BlockView view, BlockPos pos, int range) {
        @SuppressWarnings("unchecked")
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        // Target coordinates.
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (LeakingPipePos leakingPos : copiedList) {
            int xDistance = leakingPos.pos.getX() - x;
            // If X distance is within range of the leaking position.
            if (-range <= xDistance && xDistance <= range) {
                int zDistance = leakingPos.pos.getZ() - z;
                // If Z distance is within range of the leaking position.
                if (-range <= zDistance && zDistance <= range) {
                    int leakY = leakingPos.pos.getY();
                    // If Y distance is within range of the leaking position.
                    if (leakY - 12 <= y && y < leakY) {
                        // If it can leak water.
                        BlockState state = view.getBlockState(leakingPos.pos);
                        if (state.getBlock() instanceof CopperPipe) {
                            return state.get(Properties.FACING) != Direction.UP &&
                                state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<LeakingPipePos> getPoses() {
        return !isAlt ? leakingPipePosesOne : leakingPipePosesTwo;
    }

    public static ArrayList<LeakingPipePos> getAltList() {
        return isAlt ? leakingPipePosesOne : leakingPipePosesTwo;
    }

    public static void clear() {
        getPoses().clear();
    }

    public static void clearAll() {
        leakingPipePosesOne.clear();
        leakingPipePosesTwo.clear();
    }

    public static void clearAndSwitch() {
        clear();
        isAlt = !isAlt;
    }

    public static void addPos(World level, BlockPos pos) {
        getAltList().add(new LeakingPipePos(pos, level.getRegistryKey().getValue()));
    }

    public record LeakingPipePos(BlockPos pos, Identifier dimension) {

    }
}
