package org.redsxi.mc.cgcem.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class BlockEntityTicketBarrierBase extends BlockEntity {

    private double costMultiplier = 1;

    public BlockEntityTicketBarrierBase(BlockPos pos, BlockState state, BlockEntityType<?> type) {
        super(type, pos, state);
    }


    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        costMultiplier = nbt.getDouble("CostMultiplier");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putDouble("CostMultiplier", costMultiplier);
    }

    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
