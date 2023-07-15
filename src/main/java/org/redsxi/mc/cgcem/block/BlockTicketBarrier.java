package org.redsxi.mc.cgcem.block;

import mtr.SoundEvents;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Utilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockTicketBarrier extends BlockRedstoneTicketBarrierBase {

    private final boolean isEntrance;

    public BlockTicketBarrier(boolean isEntrance, boolean needRedstone) {
        super(needRedstone);
        this.isEntrance = isEntrance;
    }



    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add();
    }

    public void onEntityPass(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            Direction facing = IBlock.getStatePropertySafe(state, FACING);
            Vec3d playerPosRotated = entity.getPos().subtract((double)pos.getX() + 0.5, 0.0, (double)pos.getZ() + 0.5).rotateY((float)Math.toRadians((double)facing.asRotation()));
            TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);
            if (open.isOpen() && playerPosRotated.z > 0.0) {
                world.setBlockState(pos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
            } else if (!open.isOpen() && playerPosRotated.z < 0.0) {
                TicketSystem.EnumTicketBarrierOpen newOpen = TicketSystem.passThrough(world, pos, (PlayerEntity)entity, this.isEntrance, !this.isEntrance, SoundEvents.TICKET_BARRIER, SoundEvents.TICKET_BARRIER_CONCESSIONARY, SoundEvents.TICKET_BARRIER, SoundEvents.TICKET_BARRIER_CONCESSIONARY, null, false);
                world.setBlockState(pos, state.with(OPEN, newOpen));
                if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !world.getBlockTickScheduler().isQueued(pos, this)) {
                    Utilities.scheduleBlockTick(world, pos, this, 40);
                    ChatHud a;
                }
            }
        }
    }
}
