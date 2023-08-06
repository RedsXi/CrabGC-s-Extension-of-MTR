package org.redsxi.mc.cgcem.block;

import mtr.SoundEvents;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Utilities;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrier;

public class BlockTicketBarrier extends BlockRedstoneTicketBarrierBase implements BlockEntityProvider {
    
    private final boolean isEntrance;
    public BlockTicketBarrier(boolean isEntrance, boolean needRs) {
        super(needRs);
        this.isEntrance = isEntrance;
    }

    public void onEntityPass(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            Direction facing = IBlock.getStatePropertySafe(state, FACING);
            Vec3d playerPosRotated = entity.getPos().subtract((double)pos.getX() + 0.5, 0.0, (double)pos.getZ() + 0.5).rotateY((float)Math.toRadians(facing.asRotation()));
            TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);
            if (open.isOpen() && playerPosRotated.z > 0.0) {
                world.setBlockState(pos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
            } else if (!open.isOpen() && playerPosRotated.z < 0.0) {
                TicketSystem.EnumTicketBarrierOpen newOpen = TicketSystem.passThrough(world, pos, (PlayerEntity)entity, isEntrance, isEntrance, SoundEvents.TICKET_BARRIER, SoundEvents.TICKET_BARRIER_CONCESSIONARY, SoundEvents.TICKET_BARRIER, SoundEvents.TICKET_BARRIER_CONCESSIONARY, null, false);
                world.setBlockState(pos, state.with(OPEN, newOpen));
                if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !world.getBlockTickScheduler().isQueued(pos, this)) {
                    Utilities.scheduleBlockTick(world, pos, this, 40);
                }
            }
        }
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTicketBarrier(pos, state, isEntrance, needRs);
    }
}
