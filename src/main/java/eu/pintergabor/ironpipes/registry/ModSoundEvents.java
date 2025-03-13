package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;


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
    }
}
