package org.redsxi.mc.cgcem.block;

import mtr.block.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class BlockRedstonePoweredBase extends Block {

    public static final BooleanProperty ENABLED = BooleanProperty.of("enabled");

    public boolean needRs;

    BlockRedstonePoweredBase(Settings settings, boolean needRedstone) {
        super(settings);
        needRs = needRedstone;
        StateManager.Builder<Block, BlockState> builder = new StateManager.Builder(this);
        appendProperties(builder);
        stateManager = builder.build(Block::getDefaultState, BlockState::new);
        setDefaultState(this.stateManager.getDefaultState());
        if(needRedstone) setDefaultState(getDefaultState().with(ENABLED, false));
    }

    public void neighborUpdate(
            BlockState state,
            World world,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean notify
    ) {
        if (!world.isClient) {
            boolean bl = state.get(ENABLED);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.createAndScheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, state.cycle(ENABLED), 2);
                }
            }
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        tick(state, world, pos);
        if (!needRs) return;
        if (state.get(ENABLED) != world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(ENABLED), 2);
        }
    }

    public void tick(BlockState state, World world, BlockPos pos) {

    }

    public boolean isEnabledByRedstone(BlockState state) {
        if(needRs) {
            return IBlock.getStatePropertySafe(state, ENABLED);
        } else return true;
    }

    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if(needRs) tooltip.add(new TranslatableText("gui.cgcem.need_enable_by_redstone"));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if(needRs) builder.add(ENABLED);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (needRs) {
            return getDefaultState().with(ENABLED, false);
        } else return getDefaultState();
    }

}
