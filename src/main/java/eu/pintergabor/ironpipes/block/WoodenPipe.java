package eu.pintergabor.ironpipes.block;

import static eu.pintergabor.ironpipes.block.entity.leaking.DripUtil.*;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.block.base.BaseFluidPipe;
import eu.pintergabor.ironpipes.block.entity.WoodenPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeDripBehaviors;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.ModConfig;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.tag.ModItemTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;


public class WoodenPipe extends BaseFluidPipe {
    public static final MapCodec<WoodenPipe> CODEC =
        RecordCodecBuilder.mapCodec((instance) -> instance.group(
            createSettingsCodec()
        ).apply(instance, WoodenPipe::new));

    public WoodenPipe(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * Update the associated block entity parameters and behaviour.
     * <p>
     * Called when the state of this pipe block, or the state of its neighbours change.
     *
     * @param world This world
     * @param pos   Pipe position
     * @param state New state
     */
    private static void updateBlockEntityValues(World world, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof WoodenPipe) {
            Direction direction = state.get(Properties.FACING);
            // The state of the block in front of the pipe.
            BlockState frontState = world.getBlockState(pos.offset(direction));
            Block frontBlock = frontState.getBlock();
            // The state of the block behind the pipe.
            BlockState backState = world.getBlockState(pos.offset(direction.getOpposite()));
            Block backBlock = backState.getBlock();
            // Always true.
            if (world.getBlockEntity(pos) instanceof WoodenPipeEntity pipeEntity) {
                // The pipe can dispense if there is air or water in front of it.
                pipeEntity.canDispense = (frontState.isAir() || frontBlock == Blocks.WATER);
                pipeEntity.hasWater = ModConfig.get().carryWater &&
                    ((backBlock == Blocks.WATER) || state.get(Properties.WATERLOGGED) ||
                        (backState.contains(Properties.WATERLOGGED) && backState.get(Properties.WATERLOGGED)));
                pipeEntity.hasLava = ModConfig.get().carryLava &&
                    (backBlock == Blocks.LAVA);
                if (pipeEntity.hasWater && pipeEntity.hasLava) {
                    pipeEntity.hasWater = false;
                    pipeEntity.hasLava = false;
                }
            }
        }
    }

    /**
     * Determine the initial state of the pipe based on its surroundings.
     *
     * @return the initial state of the block
     */
    @NotNull
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        BlockPos pos = context.getBlockPos();
        return state
            .with(WATERLOGGED, context.getWorld().getFluidState(pos).getFluid() == Fluids.WATER);
    }

    /**
     * Handle state changes when the neighboring block's state changes.
     *
     * @return the state of the pipe after a neighboring block's state changes.
     */
    @Override
    protected @NotNull BlockState getStateForNeighborUpdate(
        @NotNull BlockState blockState,
        WorldView world,
        ScheduledTickView scheduledTickAccess,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        Random random) {
        if (blockState.get(WATERLOGGED)) {
            scheduledTickAccess.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        Direction facing = blockState.get(FACING);
        return blockState
            .with(FRONT_CONNECTED, needFrontExtension(world, pos, facing))
            .with(BACK_CONNECTED, needBackExtension(world, pos, facing))
            .with(SMOOTH, isSmooth(world, pos, facing));
    }

    /**
     * Handle side effects when the neighboring block's state changes.
     */
    @Override
    protected void neighborUpdate(
        @NotNull BlockState blockState, @NotNull World world,
        BlockPos blockPos, Block block, @Nullable WireOrientation orientation, boolean notify) {
//        boolean powered = isReceivingRedstonePower(world, blockPos);
//        if (powered != blockState.get(POWERED)) {
//            world.setBlockState(blockPos, blockState.with(POWERED, powered));
//        }
//        updateBlockEntityValues(world, blockPos, blockState);
    }

    /**
     * Create a block entity.
     */
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenPipeEntity(pos, state);
    }

    /**
     * Create a ticker, which will be called at every tick both on the client and on the server.
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull World world, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!world.isClient()) {
            return validateTicker(
                blockEntityType, ModBlockEntities.WOODEN_PIPE_ENTITY,
                (world1, pos1, state1, pipeEntity) ->
                    pipeEntity.serverTick(world1, pos1, state1)
            );
        }
        return null;
    }

    @Override
    public void onPlaced(
        World world, BlockPos blockPos, BlockState blockState,
        LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        super.onPlaced(world, blockPos, blockState, livingEntity, itemStack);
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    @NotNull
    public FluidState getFluidState(@NotNull BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }

    /**
     * Use item on a pipe.
     * <p>
     * If it is another piece of pipe or fitting then place it,
     * otherwise open the GUI.
     */
    @Override
    protected @NotNull ActionResult onUseWithItem(
        @NotNull ItemStack stack,
        BlockState state, World world, BlockPos pos,
        PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        // Allow placing pipes next to pipes and fittings.
        if (stack.isIn(ModItemTags.PIPES_AND_FITTINGS)) {
            return ActionResult.PASS;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, FLUID);
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerWorld serverLevel, BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        boolean isLava = blockState.get(FLUID) == PipeFluid.LAVA;
        boolean isWater = blockState.get(FLUID) == PipeFluid.WATER;
        // Water can drip from any pipe containing water.
        // Lava can drip from any pipe containing lava, except those that face upwards.
        if (isWater || (isLava && direction != Direction.UP)) {
            // Adjust probability.
            if (random.nextFloat() <= (isLava ? 0.05859375F : 0.17578125F) * 2) {
                BlockPos.Mutable mutableBlockPos = blockPos.mutableCopy();
                if (direction != Direction.DOWN) {
                    mutableBlockPos.move(direction);
                }
                // Search down to 12 blocks.
                for (int i = 0; i < 12; i++) {
                    mutableBlockPos.move(Direction.DOWN);
                    BlockState state = serverLevel.getBlockState(mutableBlockPos);
                    if (serverLevel.getFluidState(mutableBlockPos).isEmpty()) {
                        // A block that reacts with the drip stops the drip.
                        LeakingPipeDripBehaviors.DripOn dripOn =
                            LeakingPipeDripBehaviors.getDrip(state.getBlock());
                        if (dripOn != null) {
                            dripOn.dripOn(isLava, serverLevel, mutableBlockPos, state);
                            break;
                        }
                        // A solid block stops the drip.
                        if (state.getCollisionShape(serverLevel, mutableBlockPos) != VoxelShapes.empty()) {
                            break;
                        }
                    } else {
                        // A block containing any liquid stops the drip.
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean hasRandomTicks(@NotNull BlockState blockState) {
        return blockState.get(FLUID) == PipeFluid.WATER ||
            blockState.get(FLUID) == PipeFluid.LAVA;
    }

    @Override
    public void randomDisplayTick(
        @NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        BlockPos offsetPos = blockPos.offset(direction);
        BlockState offsetState = world.getBlockState(offsetPos);
        FluidState fluidState = offsetState.getFluidState();
        boolean canWater = blockState.get(FLUID) == PipeFluid.WATER && direction != Direction.UP;
        boolean canLava = blockState.get(FLUID) == PipeFluid.LAVA && direction != Direction.UP &&
            random.nextInt(2) == 0;
        boolean canWaterOrLava = canWater || canLava;
        if (canWaterOrLava) {
            double outX = blockPos.getX() + getDripX(direction, random);
            double outY = blockPos.getY() + getDripY(direction, random);
            double outZ = blockPos.getZ() + getDripZ(direction, random);
            if ((fluidState.isEmpty() || ((fluidState.getHeight(world, offsetPos)) + (double) offsetPos.getY()) < outY)) {
                world.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA,
                    outX, outY, outZ, 0, 0, 0);
            }
            if ((!offsetState.isAir() && fluidState.isEmpty())) {
                double x = blockPos.getX() + getDripX(direction, random);
                double y = blockPos.getY() + getDripY(direction, random);
                double z = blockPos.getZ() + getDripZ(direction, random);
                if (direction == Direction.DOWN) {
                    world.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA,
                        x, y, z, 0, 0, 0);
                }
            }
        }
        // If the pipe is in water.
        if (fluidState.isIn(FluidTags.WATER))
            if (random.nextFloat() < 0.1F || offsetState.getCollisionShape(world, offsetPos).isEmpty()) {
                world.addParticle(ParticleTypes.BUBBLE,
                    blockPos.getX() + getDripX(direction, random),
                    blockPos.getY() + getDripY(direction, random),
                    blockPos.getZ() + getDripZ(direction, random),
                    direction.getOffsetX() * 0.7D,
                    direction.getOffsetY() * 0.7D,
                    direction.getOffsetZ() * 0.7D);
                if (canLava && random.nextFloat() < 0.5F) {
                    world.addParticle(ParticleTypes.SMOKE,
                        blockPos.getX() + getDripX(direction, random),
                        blockPos.getY() + getDripY(direction, random),
                        blockPos.getZ() + getDripZ(direction, random),
                        direction.getOffsetX() * 0.05D,
                        direction.getOffsetY() * 0.05D,
                        direction.getOffsetZ() * 0.05D);
                }
            }
    }

    /**
     * The pipe was removed or its state changed.
     */
    @Override
    public void onStateReplaced(
        BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (oldState.isOf(newState.getBlock())) {
            // Update block entity with new values.
            updateBlockEntityValues(world, pos, newState);
        } else {
            // Remove block and block entity.
            world.removeBlockEntity(pos);
        }
    }

    @Override
    protected @NotNull MapCodec<? extends WoodenPipe> getCodec() {
        return CODEC;
    }
}
