package org.redsxi.mc.cgcem.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import static org.redsxi.mc.cgcem.BlockEntityTypes.*;

public class BlockEntityTicketBarrier extends BlockEntityTicketBarrierBase {
    public BlockEntityTicketBarrier(BlockPos pos, BlockState state, boolean isEntrance, boolean needRs) {
        super(pos, state, needRs?
                isEntrance?
                        TICKET_BARRIER_ENTRANCE_REDSTONE:
                        TICKET_BARRIER_EXIT_REDSTONE:
                isEntrance?
                        TICKET_BARRIER_ENTRANCE:
                        TICKET_BARRIER_EXIT
                );
    }
}
