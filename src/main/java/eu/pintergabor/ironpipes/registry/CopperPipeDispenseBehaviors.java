package eu.pintergabor.ironpipes.registry;

import java.util.Map;

import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class CopperPipeDispenseBehaviors {

    private static final Map<ItemConvertible, PoweredDispense> ITEMS_TO_DISPENSES =
        new Object2ObjectLinkedOpenHashMap<>();

    private static final PoweredDispense PROJECTILE_ITEM_DISPENSE = (
        world, stack, i, direction,
        position, state, pos, pipe) -> {
        if (stack.getItem() instanceof ProjectileItem projectileItem) {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            ProjectileItem.Settings dispenseConfig = projectileItem.getProjectileSettings();
            ProjectileEntity.spawnWithVelocity(
                projectileItem.createEntity(world, new Vec3d(d, e, f), stack, direction),
                world,
                stack,
                direction.getOffsetX(),
                direction.getOffsetY(),
                direction.getOffsetZ(),
                dispenseConfig.power() * 2F,
                dispenseConfig.uncertainty() * 2F
            );
        }
    };

    @Nullable
    public static PoweredDispense getDispense(ItemConvertible item) {
        if (ITEMS_TO_DISPENSES.containsKey(item)) {
            return ITEMS_TO_DISPENSES.get(item);
        } else if (item instanceof ProjectileItem) {
            return PROJECTILE_ITEM_DISPENSE;
        }
        return null;
    }

    public static double getYOffset(Direction.Axis axis, double e) {
        if (axis == Direction.Axis.Y) {
            return e - 0.125D;
        } else {
            return e - 0.15625D;
        }
    }

    public static double getRandom(@NotNull Random random) {
        return (random.nextDouble() * 0.6D) - 0.3D;
    }

    public static double getVelX(Direction.Axis axis, int offX, int i) {
        return axis == Direction.Axis.X ? (i * offX) * 0.1D : 0D;
    }

    public static double getVelY(Direction.Axis axis, int offY, int i) {
        return axis == Direction.Axis.Y ? (i * offY) * 0.1D : 0D;
    }

    public static double getVelZ(Direction.Axis axis, int offZ, int i) {
        return axis == Direction.Axis.Z ? (i * offZ) * 0.1D : 0D;
    }

    public static void init() {
    }

    @FunctionalInterface
    public interface PoweredDispense {
        void dispense(
            ServerWorld world, ItemStack itemStack, int i, Direction direction,
            Position position, BlockState state, BlockPos pos, CopperPipeEntity pipe);
    }

    public static void register(ItemConvertible item, PoweredDispense dispense) {
        ITEMS_TO_DISPENSES.put(item, dispense);
    }
}
