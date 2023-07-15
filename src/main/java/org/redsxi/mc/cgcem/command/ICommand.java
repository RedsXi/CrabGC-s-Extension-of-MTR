package org.redsxi.mc.cgcem.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ICommand {
    public abstract LiteralArgumentBuilder<ServerCommandSource> getCommand();
}
