package org.redsxi.mc.cgcem.mixin;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.redsxi.mc.cgcem.util.ObjectSaveUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Mixin(value = DataResult.PartialResult.class, remap = false)
public class PartialResultMixin<R> {
    @Final
        @Shadow
        private String message;

        @Final
        @Shadow
        private Optional<R> partialResult;

        @Inject(at=@At("HEAD"), method="message", cancellable=true)
        public void message(CallbackInfoReturnable<String> cir) throws IOException {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("Message", message);
            long savedIndex = ObjectSaveUtil.storeObject(partialResult);
            nbtCompound.putLong("SavedIndex", savedIndex);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dOutput = new DataOutputStream(byteArrayOutputStream);
            NbtIo.write(nbtCompound, dOutput);
            cir.setReturnValue(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }
}
