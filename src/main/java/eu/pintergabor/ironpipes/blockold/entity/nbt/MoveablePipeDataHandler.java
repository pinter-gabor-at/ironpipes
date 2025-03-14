package eu.pintergabor.ironpipes.blockold.entity.nbt;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import eu.pintergabor.ironpipes.blockold.entity.base.BaseBlockEntity;
import eu.pintergabor.ironpipes.blockold.entity.CopperPipeEntity;
import eu.pintergabor.ironpipes.registry.RegisterPipeNbtMethods;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class MoveablePipeDataHandler {

	private static final Logger LOGGER = LogUtils.getLogger();
	public ArrayList<SaveableMovablePipeNbt> savedList = new ArrayList<>();
	public ArrayList<Identifier> savedIds = new ArrayList<>();

	public MoveablePipeDataHandler() {

	}

	public void readNbt(@NotNull NbtCompound nbtCompound) {
		if (nbtCompound.contains("saveableMoveableNbtList", 9)) {
			this.clear();
			DataResult<List<SaveableMovablePipeNbt>> var10000 = SaveableMovablePipeNbt.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getList("saveableMoveableNbtList", 10)));
			Logger var10001 = LOGGER;
			Objects.requireNonNull(var10001);
			Optional<List<SaveableMovablePipeNbt>> list = var10000.resultOrPartial(var10001::error);

			if (list.isPresent()) {
				for (SaveableMovablePipeNbt saveableMovablePipeNbt : list.get()) {
					if (saveableMovablePipeNbt.shouldSave) {
						this.addSaveableMoveablePipeNbt(saveableMovablePipeNbt);
					}
				}
			}
		}
	}

	public void writeNbt(NbtCompound nbtCompound) {
		DataResult<NbtElement> var10000 = SaveableMovablePipeNbt.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.savedList);
		Logger var10001 = LOGGER;
		Objects.requireNonNull(var10001);
		var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> nbtCompound.put("saveableMoveableNbtList", nbtElement));
	}

	public void addSaveableMoveablePipeNbt(@NotNull SaveableMovablePipeNbt nbt) {
		if (!this.savedIds.contains(nbt.getNbtID())) {
			this.savedList.add(nbt);
			this.savedIds.add(nbt.getNbtID());
		}
	}

	@Nullable
	public SaveableMovablePipeNbt getMoveablePipeNbt(Identifier id) {
		if (this.savedIds.contains(id) && !this.savedList.isEmpty()) {
			return this.savedList.get(this.savedIds.indexOf(id));
		}
		return null;
	}

	public void removeMoveablePipeNbt(Identifier id) {
		if (this.savedIds.contains(id)) {
			this.savedList.remove(this.savedIds.indexOf(id));
			this.savedIds.remove(id);
		}
	}

	public void setMoveablePipeNbt(Identifier id, SaveableMovablePipeNbt nbt) {
		if (this.savedIds.contains(id)) {
			this.savedList.set(this.savedIds.indexOf(id), nbt);
		} else {
			this.savedIds.add(id);
			this.savedList.add(nbt);
		}
	}

	public void clear() {
		this.savedList.clear();
		this.savedIds.clear();
	}

	public void clearAllButNonMoveable() {
		ArrayList<SaveableMovablePipeNbt> nbtToRemove = new ArrayList<>();
		this.savedList.clear();
		this.savedIds.clear();
		for (SaveableMovablePipeNbt nbt : this.savedList) {
			if (nbt.getShouldMove()) {
				nbtToRemove.add(nbt);
			}
		}
		for (SaveableMovablePipeNbt nbt : nbtToRemove) {
			if (this.savedList.contains(nbt)) {
				int index = this.savedList.indexOf(nbt);
				this.savedList.remove(index);
				this.savedIds.remove(index);
			}
		}
	}

	public void clearAllButMoveable() {
		ArrayList<SaveableMovablePipeNbt> nbtToRemove = new ArrayList<>();
		this.savedList.clear();
		this.savedIds.clear();
		for (SaveableMovablePipeNbt nbt : this.savedList) {
			if (!nbt.getShouldMove()) {
				nbtToRemove.add(nbt);
			}
		}
		for (SaveableMovablePipeNbt nbt : nbtToRemove) {
			if (this.savedList.contains(nbt)) {
				int index = this.savedList.indexOf(nbt);
				this.savedList.remove(index);
				this.savedIds.remove(index);
			}
		}
	}

	public ArrayList<SaveableMovablePipeNbt> getSavedNbtList() {
		return this.savedList;
	}

	public static class SaveableMovablePipeNbt {

		public static final Codec<SaveableMovablePipeNbt> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Identifier.CODEC.fieldOf("savedID").forGetter(SaveableMovablePipeNbt::getSavedID),
			Vec3d.CODEC.fieldOf("vec3d").forGetter(SaveableMovablePipeNbt::getVec3dd),
			Vec3d.CODEC.fieldOf("vec3d2").forGetter(SaveableMovablePipeNbt::getVec3dd2),
			Codec.STRING.fieldOf("string").forGetter(SaveableMovablePipeNbt::getString),
			Codec.INT.fieldOf("useCount").forGetter(SaveableMovablePipeNbt::getUseCount),
			BlockPos.CODEC.fieldOf("blockPos").forGetter(SaveableMovablePipeNbt::getBlockPos),
			Codec.BOOL.fieldOf("shouldSave").forGetter(SaveableMovablePipeNbt::getShouldSave),
			Codec.BOOL.fieldOf("shouldMove").forGetter(SaveableMovablePipeNbt::getShouldMove),
			Codec.BOOL.fieldOf("canOnlyBeUsedOnce").forGetter(SaveableMovablePipeNbt::getCanOnlyBeUsedOnce),
			Codec.BOOL.fieldOf("canOnlyGoThroughOnePipe").forGetter(SaveableMovablePipeNbt::getCanOnlyGoThroughOnePipe),
			Codec.BOOL.fieldOf("shouldCopy").forGetter(SaveableMovablePipeNbt::getShouldCopy),
			Identifier.CODEC.fieldOf("nbtId").forGetter(SaveableMovablePipeNbt::getNbtID)
		).apply(instance, SaveableMovablePipeNbt::new));
		public Identifier savedID;
		public Vec3d vec3d;
		public Vec3d vec3d2;
		public String string;
		public int useCount;
		public BlockPos blockPos;
		public boolean shouldSave;
		public boolean shouldMove;
		//TEMP STORAGE
		public Entity foundEntity;
		private boolean canOnlyBeUsedOnce;
		private boolean canOnlyGoThroughOnePipe;
		private boolean shouldCopy;
		private Identifier nbtID;

		public SaveableMovablePipeNbt(Identifier id, Vec3d vec3d, Vec3d vec3d2, String string, int useCount, BlockPos blockPos, boolean shouldSave, boolean shouldMove, boolean canOnlyBeUsedOnce, boolean canOnlyGoThroughOnePipe, boolean shouldCopy, Identifier nbtId) {
			this.savedID = id;
			this.vec3d = vec3d;
			this.vec3d2 = vec3d2;
			this.string = string;
			this.useCount = useCount;
			this.blockPos = blockPos;
			this.shouldSave = shouldSave;
			this.shouldMove = shouldMove;
			this.canOnlyBeUsedOnce = canOnlyBeUsedOnce;
			this.canOnlyGoThroughOnePipe = canOnlyGoThroughOnePipe;
			this.shouldCopy = shouldCopy;
			this.nbtID = nbtId;
		}

		public SaveableMovablePipeNbt(GameEvent event, Vec3d originPos, @Nullable GameEvent.Emitter emitter, BlockPos pipePos) {
			this.savedID = Registries.GAME_EVENT.getKey(event).get().getValue();
			this.vec3d = originPos;
			this.vec3d2 = originPos;
			if (emitter != null && emitter.sourceEntity() != null) {
				this.string = emitter.sourceEntity().getUuidAsString();
			} else {
				this.string = "noEntity";
			}
			this.blockPos = pipePos;
			this.nbtID = Identifier.of("lunade", "default");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableMovablePipeNbt(GameEvent event, Vec3d originPos, @Nullable Entity entity, BlockPos pipePos) {
			this.savedID = Registries.GAME_EVENT.getKey(event).get().getValue();
			this.vec3d = originPos;
			this.vec3d2 = originPos;
			if (entity != null) {
				this.string = entity.getUuidAsString();
			} else {
				this.string = "noEntity";
			}
			this.blockPos = pipePos;
			this.nbtID = Identifier.of("lunade", "default");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableMovablePipeNbt() {
			this.savedID = Identifier.of("lunade", "none");
			this.vec3d = new Vec3d(0, -64, 0);
			this.vec3d2 = new Vec3d(0, -64, 0);
			this.string = "none";
			this.blockPos = new BlockPos(0, -64, 0);
			this.nbtID = Identifier.of("lunade", "none");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableMovablePipeNbt withSavedId(Identifier id) {
			this.setNbtID(id);
			return this;
		}

		public SaveableMovablePipeNbt withVec3dd(Vec3d pos) {
			this.vec3d = pos;
			return this;
		}

		public SaveableMovablePipeNbt withVec3dd2(Vec3d pos) {
			this.vec3d2 = pos;
			return this;
		}

		public SaveableMovablePipeNbt withString(String string) {
			this.string = string;
			return this;
		}

		public SaveableMovablePipeNbt withUseCount(int count) {
			this.useCount = count;
			return this;
		}

		public SaveableMovablePipeNbt withBlockPos(BlockPos pos) {
			this.blockPos = pos;
			return this;
		}

		public SaveableMovablePipeNbt withShouldSave(boolean bool) {
			this.shouldSave = bool;
			return this;
		}

		public SaveableMovablePipeNbt withShouldMove(boolean bool) {
			this.shouldMove = bool;
			return this;
		}

		public SaveableMovablePipeNbt withOnlyUseableOnce(boolean bool) {
			this.canOnlyBeUsedOnce = bool;
			return this;
		}

		public SaveableMovablePipeNbt withOnlyThroughOnePipe(boolean bool) {
			this.canOnlyGoThroughOnePipe = bool;
			return this;
		}

		public SaveableMovablePipeNbt withShouldCopy(boolean bool) {
			this.shouldCopy = bool;
			return this;
		}

		public SaveableMovablePipeNbt withNBTID(Identifier id) {
			this.setNbtID(id);
			return this;
		}

		public void dispense(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipeEntity) {
			RegisterPipeNbtMethods.DispenseMethod method = RegisterPipeNbtMethods.getDispense(this.getNbtID());
			if (method != null) {
				method.dispense(this, world, pos, state, pipeEntity);
			}
		}

		public void onMove(ServerWorld world, BlockPos pos, BlockState state, BaseBlockEntity blockEntity) {
			RegisterPipeNbtMethods.OnMoveMethod method = RegisterPipeNbtMethods.getMove(this.getNbtID());
			if (method != null) {
				method.onMove(this, world, pos, state, blockEntity);
			}
		}

		public void tick(ServerWorld world, BlockPos pos, BlockState state, BaseBlockEntity blockEntity) { //Will be called at the CURRENT location, not the Pipe/Fitting it moves to on that tick - it can run this method and be dispensed on the same tick.
			RegisterPipeNbtMethods.TickMethod method = RegisterPipeNbtMethods.getTick(this.getNbtID());
			if (method != null) {
				method.tick(this, world, pos, state, blockEntity);
			}
		}

		public boolean canMove(ServerWorld world, BlockPos pos, BlockState state, BaseBlockEntity blockEntity) {
			RegisterPipeNbtMethods.CanMoveMethod method = RegisterPipeNbtMethods.getCanMove(this.getNbtID());
			if (method != null) {
				return method.canMove(this, world, pos, state, blockEntity);
			} else {
				return true;
			}
		}

		@Nullable
		public Entity getEntity(World world) {
			if (!this.string.equals("noEntity")) {
				if (this.foundEntity != null) {
					if (this.foundEntity.getUuidAsString().equals(this.string)) {
						return this.foundEntity;
					} else {
						this.foundEntity = null;
					}
				}
				Box box = new Box(this.vec3d2.add(-32, -32, -32), this.vec3d2.add(32, 32, 32));
				List<Entity> entities = world.getNonSpectatingEntities(Entity.class, box);
				for (Entity entity : entities) {
					if (entity.getUuidAsString().equals(this.string)) {
						this.foundEntity = entity;
						this.vec3d2 = entity.getPos();
						return entity;
					}
				}
			}
			return null;
		}

		public Identifier getSavedID() {
			return this.savedID;
		}

		public Vec3d getVec3dd() {
			return this.vec3d;
		}

		public Vec3d getVec3dd2() {
			return this.vec3d2;
		}

		public String getString() {
			return this.string;
		}

		public int getUseCount() {
			return this.useCount;
		}

		public BlockPos getBlockPos() {
			return this.blockPos;
		}

		public boolean getShouldSave() {
			return this.shouldSave;
		}

		public boolean getShouldMove() {
			return this.shouldMove;
		}

		public boolean getCanOnlyBeUsedOnce() {
			return this.canOnlyBeUsedOnce;
		}

		public boolean getCanOnlyGoThroughOnePipe() {
			return this.canOnlyGoThroughOnePipe;
		}

		public boolean getShouldCopy() {
			return this.shouldCopy;
		}

		public Identifier getNbtID() {
			return this.nbtID;
		}

		public void setNbtID(Identifier id) {
			this.nbtID = id;
		}

		public SaveableMovablePipeNbt copyOf() {
			return new SaveableMovablePipeNbt(this.savedID, this.vec3d, this.vec3d2, this.string, this.useCount, this.blockPos, this.shouldSave, this.shouldMove, this.canOnlyBeUsedOnce, this.canOnlyGoThroughOnePipe, this.shouldCopy, this.nbtID);
		}
	}

}
