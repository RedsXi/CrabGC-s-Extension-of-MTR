package org.redsxi.mc.cgcem.block;

import mtr.SoundEvents;
import mtr.block.BlockTicketBarrier;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Utilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.redsxi.mc.cgcem.PassThroughManager;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierPayDirect;

import java.util.List;
import java.util.Objects;


public class BlockTicketBarrierPayDirect extends BlockRedstoneTicketBarrierBase implements BlockEntityProvider {
    public BlockTicketBarrierPayDirect(boolean needRs) {
        super(needRs);
    }

    public void onEntityPass(BlockState state, World world, BlockPos pos, Entity entity) {
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        if (!world.isClient && entity instanceof PlayerEntity) {
            Direction facing = IBlock.getStatePropertySafe(state, FACING);
            Vec3d playerPosRotated = entity.getPos().subtract((double)pos.getX() + 0.5, 0.0, (double)pos.getZ() + 0.5).rotateY((float)Math.toRadians(facing.asRotation()));
            TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);
            if (open.isOpen() && playerPosRotated.z > 0.0) {
                world.setBlockState(pos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
            } else if (!open.isOpen() && playerPosRotated.z < 0.0) {
                TicketSystem.EnumTicketBarrierOpen newOpen = PassThroughManager.passThrough(
                        world,
                        pos,
                        (PlayerEntity)entity,
                        SoundEvents.TICKET_BARRIER,
                        null,
                        ((BlockEntityTicketBarrierPayDirect) Objects.requireNonNull(world.getBlockEntity(pos))).getCost()
                )? TicketSystem.EnumTicketBarrierOpen.OPEN : TicketSystem.EnumTicketBarrierOpen.CLOSED;
                world.setBlockState(pos, state.with(OPEN, newOpen));
                if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !world.getBlockTickScheduler().isQueued(pos, this)) {
                    Utilities.scheduleBlockTick(world, pos, this, 40);
                }
            }
        }
    }

    public void appendTooltip(ItemStack itemStack, @Nullable BlockView blockView, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, blockView, list, tooltipContext);
        list.add(new TranslatableText("tooltip.cgcem.ticket_barrier_pay_direct"));
    }

    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityTicketBarrierPayDirect(blockPos, blockState);
    }
}
