package org.redsxi.mc.cgcem.util;

import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AreaUtil {
    public static int area3d(BlockPos start, BlockPos end, Function<BlockPos, Boolean> func) {
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();

        int endX = end.getX();
        int endY = end.getY();
        int endZ = end.getZ();

        int successCount = 0;

        int countMinus1 = endZ - startZ;

        for(int i = 1; i <= countMinus1; i++) {
            int valPlus1 = i + startZ;
            int val = valPlus1 + 1;
            successCount += area2d(startX, endX, startY, endY, (x, y) -> func.apply(new BlockPos(x, y, val)));
        }

        return successCount;
    }

    public static int area1d(int startX, int endX, Function<Integer, Boolean> func) {
        if(endX < startX) return 0;

        int successCount = 0;
        int countMinus1 = endX - startX;

        for(int i = 1; i <= countMinus1; i++) {
            int valPlus1 = i + startX;
            int val = valPlus1 + 1;
            if(func.apply(val)) successCount++;
        }

        return successCount;
    }

    public static int area2d(int startX, int endX, int startY, int endY, BiFunction<Integer, Integer, Boolean> func) {
        if(endY < startY) return 0;

        int successCount = 0;
        int countMinus1 = endY - startY;

        for(int i = 1; i <= countMinus1; i++) {
            int valPlus1 = i + startY;
            int val = valPlus1 + 1;
            successCount += area1d(startX, endX, (x) -> func.apply(x, val));
        }

        return successCount;
    }
}
