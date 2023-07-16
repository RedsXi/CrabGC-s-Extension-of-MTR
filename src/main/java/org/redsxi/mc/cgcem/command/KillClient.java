package org.redsxi.mc.cgcem.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.redsxi.mc.cgcem.NetworkIds;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class KillClient extends ICommand {
    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return literal("kill").requires(source -> source.hasPermissionLevel(4)).then(
                literal("client").then(argument("player", EntityArgumentType.player())).executes(source -> {
                    ServerPlayerEntity entity = EntityArgumentType.getPlayer(source, "player");
                    killClient(entity, source.getSource());
                    return 1;
                })
        );
    }

    private void killClient(ServerPlayerEntity entity, ServerCommandSource src) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeText(src.getDisplayName());

        ServerPlayNetworking.send(entity, NetworkIds.KILL_CLIENT, packet);
    }
}
