package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.config.ModConfig;
import org.jetbrains.annotations.NotNull;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;


public class ModSoundEvents {
    public static final SoundEvent ITEM_IN = register("block.copper_pipe.item_in");
    public static final SoundEvent ITEM_OUT = register("block.copper_pipe.item_out");
    public static final SoundEvent LAUNCH = register("block.copper_pipe.launch");
    public static final SoundEvent TURN = register("block.copper_pipe.turn");

    @NotNull
    public static SoundEvent register(@NotNull String path) {
        Identifier id = Global.modId(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        // Everything is done by static initializers.
    }

    /**
     * Play dispensing sound.
     */
    public static void playDispenseSound(ServerWorld world, BlockPos soundPos) {
        if (ModConfig.get().dispenseSounds) {
            world.playSound(
                null, soundPos, LAUNCH,
                SoundCategory.BLOCKS, 0.2f, (world.random.nextFloat() * 0.25f) + 0.8f);
        }
    }
}
