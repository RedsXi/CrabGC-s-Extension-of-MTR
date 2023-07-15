package org.redsxi.mc.cgcem;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierPayDirect;

import static org.redsxi.mc.cgcem.CrabGcsExtensionOfMc.idOf;

public interface BlockEntityTypes {
    BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            idOf("ticket_barrier_pay_direct"),
            FabricBlockEntityTypeBuilder.create(BlockEntityTicketBarrierPayDirect::new, Blocks.TICKET_BARRIER_PAY_DIRECT).build()
    );

    static void checkClassLoad() {

    }
}
