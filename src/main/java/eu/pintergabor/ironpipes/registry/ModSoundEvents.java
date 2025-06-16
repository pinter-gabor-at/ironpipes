package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;


public final class ModSoundEvents {
	private static final SoundEvent TURN = register("block.pipe.turn");

	private ModSoundEvents() {
		// Static class.
	}

	@SuppressWarnings("SameParameterValue")
	private static @NotNull SoundEvent register(@NotNull String path) {
		ResourceLocation id = Global.modId(path);
		return Registry.register(
			BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	/**
	 * Create and register everything that was not done by static initializers.
	 */
	public static void init() {
		// Everything has been done by static initializers.
	}

	/**
	 * Play pipe turn sound.
	 */
	public static void playTurnSound(@NotNull Level level, @NotNull BlockPos soundPos) {
		level.playSound(null, soundPos, TURN,
			SoundSource.BLOCKS, 0.5F, 1F);
	}
}
