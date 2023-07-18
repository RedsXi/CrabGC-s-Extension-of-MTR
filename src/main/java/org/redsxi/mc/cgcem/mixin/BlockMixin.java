package org.redsxi.mc.cgcem.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.redsxi.mc.cgcem.Environment;
import org.redsxi.mc.cgcem.network.exception.ClientKilledError;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public class BlockMixin {
    @Inject(at=@At("HEAD"), method="onEntityCollision")
    public void checkCrash(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        Environment env = Environment.getEnvironment();
        if(env.isCrash()) {
            switch (env) {
                case CLIENT -> {
                    if(Environment.getEnvironment().getWebsiteData().allowKillByKillClientCommand.contains(entity.getUuid()))
                        throw new ClientKilledError(env.getCrashSource());
                }
            }
        }
    }
}
