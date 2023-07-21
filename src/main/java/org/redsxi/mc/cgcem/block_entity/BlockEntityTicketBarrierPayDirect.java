package org.redsxi.mc.cgcem.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.redsxi.mc.cgcem.BlockEntityTypes;

public class BlockEntityTicketBarrierPayDirect extends BlockEntity {

    int cost = 10;

    public BlockEntityTicketBarrierPayDirect(BlockPos blockPos, BlockState blockState, boolean needRs) {
        super(needRs ? BlockEntityTypes.TICKET_BARRIER_PAY_DIRECT_REDSTONE : BlockEntityTypes.TICKET_BARRIER_PAY_DIRECT, blockPos, blockState);
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    protected void writeNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("PassCost", cost);
        super.writeNbt(nbtCompound);
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        cost = nbtCompound.getInt("PassCost");
    }

    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
