package org.redsxi.mc.cgcem.network.exception;

import net.minecraft.text.Text;

public class ClientKilledError extends IndexOutOfBoundsException {
    public ClientKilledError(Text t) {
        super(String.format("%s killed your client!", t.asString()));
    }
}
