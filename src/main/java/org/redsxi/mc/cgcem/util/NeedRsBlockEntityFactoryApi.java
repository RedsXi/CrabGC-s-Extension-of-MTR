package org.redsxi.mc.cgcem.util;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NeedRsBlockEntityFactoryApi<T extends BlockEntity> implements FabricBlockEntityTypeBuilder.Factory<T> {

    private NeedRsBlockEntityFactoryApi(Class<T> tClass, boolean needRs) {
        try {
            constOfT = tClass.getConstructor(BlockPos.class, BlockState.class, boolean.class);
            nrs = needRs;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Constructor should be (BlockPos, BlockState, boolean)", e);
        }
    }

    private final Constructor<T> constOfT;
    private final boolean nrs;

    public T create(BlockPos blockPos, BlockState blockState) {
        try {
            return constOfT.newInstance(blockPos, blockState, nrs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends BlockEntity> NeedRsBlockEntityFactoryApi<T> as(Class<T> clazz, boolean needRs) {
        return new NeedRsBlockEntityFactoryApi<>(clazz, needRs);
    }
}
