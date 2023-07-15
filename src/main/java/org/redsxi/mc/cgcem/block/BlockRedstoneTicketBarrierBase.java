package org.redsxi.mc.cgcem.block;

import mtr.block.IBlock;
import mtr.data.TicketSystem;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

abstract class BlockRedstoneTicketBarrierBase extends BlockRedstonePoweredBase {

    public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.of("open", TicketSystem.EnumTicketBarrierOpen.class);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    BlockRedstoneTicketBarrierBase(boolean needRs) {
        super(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(2.0F).luminance((state) -> 5).nonOpaque(), needRs);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos pos, ShapeContext collisionContext) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0, 15.0, 16.0, facing);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);

        VoxelShape base = IBlock.getVoxelShapeByDirection(15.0, 0.0, 0.0, 16.0, 24.0, 16.0, facing);
        return open.isOpen() ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0.0, 0.0, 7.0, 16.0, 24.0, 9.0, facing), base);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public abstract void onEntityPass(BlockState state, World world, BlockPos pos, Entity entity);

    public final void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!isEnabledByRedstone(state)) {
            onEntityPassWhenNotEnabledByRedstone(state, world, pos, entity);
            return;
        }
        onEntityPass(state, world, pos, entity);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, OPEN);
    }


    public void onEntityPassWhenNotEnabledByRedstone(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity.isPlayer()) ((PlayerEntity)entity).sendMessage(new TranslatableText("gui.cgcem.barrier_not_open"), true);
    }

    public void tick(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
    }
}
