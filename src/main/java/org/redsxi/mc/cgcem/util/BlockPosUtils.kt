package org.redsxi.mc.cgcem.util

import net.minecraft.util.math.BlockPos

interface BlockPosUtils {
    fun toString(pos: BlockPos): String

    companion object: BlockPosUtils {
        override fun toString(pos: BlockPos): String {
            return "${pos.x}, ${pos.y}, ${pos.z}"
            // return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
        }
    }
}