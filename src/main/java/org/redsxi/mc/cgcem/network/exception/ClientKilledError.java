package org.redsxi.mc.cgcem.network.exception;

import net.minecraft.text.Text;

public class ClientKilledError extends Error {
    public ClientKilledError(Text t) {
        super(t.toString() + " killed your client!");
    }
}
