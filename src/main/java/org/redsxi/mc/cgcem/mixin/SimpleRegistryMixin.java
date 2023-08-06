package org.redsxi.mc.cgcem.mixin;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger("SimpleRegistryMixin");

    @Inject(at=@At(value = "INVOKE", target = "Ljava/lang/IllegalStateException;<init>(Ljava/lang/String;)V", shift = At.Shift.BEFORE), method = "freeze", cancellable = true)
    private void returnAfterThrow(CallbackInfoReturnable<Registry<T>> cir) {
        LOGGER.warn("Seems a IllegalStateException thrown. Ignore it");
        cir.setReturnValue((SimpleRegistry<T>)(Object)this);
    }
}
