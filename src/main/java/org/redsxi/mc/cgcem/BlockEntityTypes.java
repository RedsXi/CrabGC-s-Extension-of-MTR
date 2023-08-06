package org.redsxi.mc.cgcem;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrier;
import org.redsxi.mc.cgcem.block_entity.BlockEntityTicketBarrierPayDirect;

import static org.redsxi.mc.cgcem.CrabGcsExtensionOfMc.idOf;
import static org.redsxi.mc.cgcem.CrabGcsExtensionOfMc.mtrIdOf;

public interface BlockEntityTypes {
    BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            idOf("ticket_barrier_pay_direct"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrierPayDirect(p, s, false),
                    Blocks.TICKET_BARRIER_PAY_DIRECT
            ).build()
    );

    BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT_REDSTONE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            idOf("ticket_barrier_pay_direct_redstone"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrierPayDirect(p, s, false),
                    Blocks.TICKET_BARRIER_PAY_DIRECT_REDSTONE
            ).build()
    );

    BlockEntityType<BlockEntityTicketBarrier> TICKET_BARRIER_ENTRANCE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            mtrIdOf("ticket_barrier_entrance_1"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrier(p, s, true, false),
                    Blocks.TICKET_BARRIER_ENTRANCE
            ).build()
    );

    BlockEntityType<BlockEntityTicketBarrier> TICKET_BARRIER_EXIT = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            mtrIdOf("ticket_barrier_exit_1"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrier(p, s, false, false),
                    Blocks.TICKET_BARRIER_EXIT
            ).build()
    );

    BlockEntityType<BlockEntityTicketBarrier> TICKET_BARRIER_ENTRANCE_REDSTONE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            idOf("ticket_barrier_entrance_redstone"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrier(p, s, true, true),
                    Blocks.TICKET_BARRIER_ENTRANCE_REDSTONE
            ).build()
    );

    BlockEntityType<BlockEntityTicketBarrier> TICKET_BARRIER_EXIT_REDSTONE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            idOf("ticket_barrier_exit_redstone"),
            FabricBlockEntityTypeBuilder.create(
                    (p, s) -> new BlockEntityTicketBarrier(p, s, false, true),
                    Blocks.TICKET_BARRIER_EXIT_REDSTONE
            ).build()
    );

    static void checkClassLoad() {

    }
}
