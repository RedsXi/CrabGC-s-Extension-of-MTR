package org.redsxi.mc.cgcem.util;

import java.util.UUID;

public class UuidUtil {
    public static UUID convertIntArrayToUuid(int[] ints) {
        assert ints.length == 16 : "data must be 4 ints in length";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        long msb = ((long)ints[0]) << 32;
        msb += ((long)ints[1]) & 0xffffffffL;
        long lsb = ((long)ints[2]) << 32;
        lsb += ((long)ints[3]) & 0xffffffffL;
        return new UUID(msb, lsb);
    }

    public static int[] convertUuidToIntArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        return new int[] {
                (int)((msb & 0xffffffff00000000L) >> 32),
                (int)((msb & 0x00000000ffffffffL)),
                (int)((lsb & 0xffffffff00000000L) >> 32),
                (int)((lsb & 0x00000000ffffffffL))
        };
    }
}
