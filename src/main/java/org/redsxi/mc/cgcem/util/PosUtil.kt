package org.redsxi.mc.cgcem.util

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.util.math.Vec3i

object PosUtil {
    fun roundVec(doubleVec: Vec3d): Vec3i {
        return Vec3i(doubleVec.x, doubleVec.y, doubleVec.z)
    }

    fun roundVec(floatVec: Vec3f): Vec3i {
        return Vec3i(floatVec.x.toDouble(), floatVec.y.toDouble(), floatVec.z.toDouble())
    }
}