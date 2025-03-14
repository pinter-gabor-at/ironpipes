package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import eu.pintergabor.ironpipes.blockold.CopperPipe;
import eu.pintergabor.ironpipes.registry.ModSoundEvents;

import net.minecraft.advancement.criterion.Criteria;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(HoeItem.class)
public abstract class HoeItemMixin {

    @Inject(
        method = "useOnBlock",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/HoeItem;TILLING_ACTIONS:Ljava/util/Map;",
            opcode = Opcodes.GETSTATIC,
            ordinal = 0
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void useOnBlock(
        ItemUsageContext context, CallbackInfoReturnable<ActionResult> info,
        @Local World world, @Local BlockPos blockPos
    ) {
        BlockState blockState = world.getBlockState(blockPos);
        // If the hoe is used on a pipe block.
        if (blockState.getBlock() instanceof CopperPipe) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            // Increase statistics on the server.
            if (playerEntity instanceof ServerPlayerEntity player) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
            }
            // Turn the pipe, if it is facing any other direction.
            Direction face = context.getSide();
            if (face != blockState.get(CopperPipe.FACING)) {
                BlockState state = blockState
                    .with(CopperPipe.FACING, face)
                    .with(CopperPipe.BACK_CONNECTED, CopperPipe.needBackExtension(world, blockPos, face))
                    .with(CopperPipe.FRONT_CONNECTED, CopperPipe.needFrontExtension(world, blockPos, face))
                    .with(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, face));
                world.setBlockState(blockPos, state);
                world.playSound(null, blockPos, ModSoundEvents.TURN,
                    SoundCategory.BLOCKS, 0.5F, 1F);
                // Damage the hoe.
                if (playerEntity != null) {
                    context.getStack()
                        .damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                }
            }
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
