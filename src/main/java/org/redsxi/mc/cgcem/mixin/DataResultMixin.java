package org.redsxi.mc.cgcem.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.redsxi.mc.cgcem.util.ObjectSaveUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(value = DataResult.class,remap = false)
public abstract class DataResultMixin<R> {
    @Shadow public abstract <T> DataResult<T> map(Function<? super R, ? extends T> function);

    @Inject(at=@At("HEAD"),method="getOrThrow",cancellable=true)
    private void injectGetOrThrow(boolean allowPartial, Consumer<String> onError, CallbackInfoReturnable<R> cir) {
        cir.setReturnValue(getResult().map(
                l -> l,
                r -> {
                    String message = r.message();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(message));
                    NbtCompound compound;
                    try {
                        compound = NbtIo.read(new DataInputStream(byteArrayInputStream));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Optional<R> partialResult = (Optional<R>) ObjectSaveUtil.getObject(compound.getLong("SavedIndex"));
                    onError.accept(compound.getString("Message"));
                    if (allowPartial && partialResult.isPresent()) {
                        return partialResult.get();
                    }
                    return null;
                }
        ));
    }

    @Accessor
   abstract Either<R, DataResult.PartialResult<R>> getResult();
}
