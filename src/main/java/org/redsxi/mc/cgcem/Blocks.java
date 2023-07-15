package org.redsxi.mc.cgcem;

import net.minecraft.block.Block;
import org.redsxi.mc.cgcem.block.BlockTicketBarrier;
import org.redsxi.mc.cgcem.block.BlockTicketBarrierPayDirect;

public interface Blocks {
    Block TICKET_BARRIER_ENTRANCE_REDSTONE = new BlockTicketBarrier(true, true);
    Block TICKET_BARRIER_EXIT_REDSTONE = new BlockTicketBarrier(false, true);
    Block TICKET_BARRIER_PAY_DIRECT = new BlockTicketBarrierPayDirect(false);
    //Block TICKET_BARRIER_PAY_DIRECT_REDSTONE = new BlockTicketBarrierPayDirect(true);
}
