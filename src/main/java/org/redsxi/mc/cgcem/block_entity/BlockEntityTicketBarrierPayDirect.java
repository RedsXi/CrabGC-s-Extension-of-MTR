package org.redsxi.mc.cgcem.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.redsxi.mc.cgcem.BlockEntityTypes;

public class BlockEntityTicketBarrierPayDirect extends BlockEntityTicketBarrierBase {

    int cost = 10;

    public BlockEntityTicketBarrierPayDirect(BlockPos blockPos, BlockState blockState, boolean needRs) {
        super(blockPos, blockState, needRs ?
                BlockEntityTypes.TICKET_BARRIER_PAY_DIRECT_REDSTONE:
                BlockEntityTypes.TICKET_BARRIER_PAY_DIRECT);
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
}
