package org.redsxi.mc.cgcem.nbt;

import net.minecraft.nbt.NbtIntArray;
import org.redsxi.mc.cgcem.util.UuidUtil;

import java.util.UUID;

public final class NbtUUID extends NbtIntArray {

    public NbtUUID(UUID uuid) {
        super(UuidUtil.convertUuidToIntArray(uuid));
    }

    public static NbtUUID fromNbtIntArray(NbtIntArray array) {
        if(array.size() != 4) throw new IllegalArgumentException("Not a UUID IntArray");
        return (NbtUUID) array;
    }

    public UUID getUUID() {
        return UuidUtil.convertIntArrayToUuid(getIntArray());
    }
}
