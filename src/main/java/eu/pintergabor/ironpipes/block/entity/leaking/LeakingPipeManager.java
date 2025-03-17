package eu.pintergabor.ironpipes.block.entity.leaking;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;


public class LeakingPipeManager {
    private static final ArrayList<LeakingPipePos> leakingPipePositions0 = new ArrayList<>();
    private static final ArrayList<LeakingPipePos> leakingPipePositions1 = new ArrayList<>();
    private static boolean isActive1;

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
        ArrayList<LeakingPipePos> activeList = getActiveList();
//        @SuppressWarnings("unchecked")
//        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getActiveList().clone();
        // Target coordinates.
        int x = entity.getBlockX();
        int y = entity.getBlockY();
        int z = entity.getBlockZ();
        Vec3d entityPos = entity.getPos();
        World world = entity.getWorld();
        Identifier dimension = getDimension(world);
        for (LeakingPipePos leakingPos : activeList) {
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
                            BlockHitResult hitResult = world.raycast(
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
     * @param world World
     * @param pos   Target position
     * @param range X and Z range [-range..+range]
     * @return true if there is a leaking water pipe in range.
     */
    public static boolean isWaterPipeNearby(BlockView world, BlockPos pos, int range) {
        ArrayList<LeakingPipePos> activeList = getActiveList();
//        @SuppressWarnings("unchecked")
//        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getActiveList().clone();
        // Target coordinates.
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Identifier dimension = world instanceof World w ?
            getDimension(w) : World.OVERWORLD.getValue();
        for (LeakingPipePos leakingPos : activeList) {
            if (leakingPos.dimension.equals(dimension)) {
                int xDistance = leakingPos.pos.getX() - x;
                // If X distance is within range of the leaking position.
                if (-range <= xDistance && xDistance <= range) {
                    int zDistance = leakingPos.pos.getZ() - z;
                    // If Z distance is within range of the leaking position.
                    if (-range <= zDistance && zDistance <= range) {
                        int leakY = leakingPos.pos.getY();
                        // If Y distance is within range of the leaking position.
                        if (leakY - 12 <= y && y < leakY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<LeakingPipePos> getActiveList() {
        return !isActive1 ? leakingPipePositions0 : leakingPipePositions1;
    }

    public static ArrayList<LeakingPipePos> getInactiveList() {
        return isActive1 ? leakingPipePositions0 : leakingPipePositions1;
    }

    /**
     * Clear all position when the server stops.
     */
    public static void clearAll() {
        leakingPipePositions0.clear();
        leakingPipePositions1.clear();
    }

    /**
     * Called at the beginning of the server tick.
     */
    public static void switchAndClear() {
        // Activate the other list.
        isActive1 = !isActive1;
        // Clear the inactive list.
        getInactiveList().clear();
    }

    /**
     * Add position to the inactive leaking positions.
     */
    public static void addPos(World world, BlockPos pos) {
        getInactiveList().add(new LeakingPipePos(getDimension(world), pos));
    }

    /**
     * Get dimension.
     *
     * @return {@link World#OVERWORLD}.getValue(), {@link World#NETHER}.getValue() or {@link World#END}.getValue().
     */
    private static Identifier getDimension(World world) {
        return world.getRegistryKey().getValue();
    }

    /**
     * World dimension and position pairs.
     */
    public record LeakingPipePos(Identifier dimension, BlockPos pos) {
    }
}
