package org.redsxi.mc.cgcem.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.redsxi.mc.cgcem.Environment;
import org.redsxi.mc.cgcem.NetworkIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class KillClient extends ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger("KillClient");

    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return literal("kill").requires(source -> source.hasPermissionLevel(4)).then(literal("client").then(argument("player", EntityArgumentType.player()).executes(source -> {
            killClient(EntityArgumentType.getPlayer(source, "player"), source.getSource());
            return 1;
        })));
    }

    private void killClient(ServerPlayerEntity entity, ServerCommandSource src) throws CommandSyntaxException {
        if(Environment.getEnvironment().getWebsiteData().allowUseKillClientCommand.contains(src.getPlayer().getUuid())) {
            if(Environment.getEnvironment().getWebsiteData().allowKillByKillClientCommand.contains(entity.getUuid())) {
                PacketByteBuf packet = PacketByteBufs.create();
                packet.writeText(src.getDisplayName());
                ServerPlayNetworking.send(entity, NetworkIds.KILL_CLIENT, packet);
                LOGGER.info("Sent KillClient package to " + entity.getDisplayName().asString());
                src.sendFeedback(new TranslatableText("command.cgcem.kill.client.success", src.getDisplayName()), true);
            } else {
                src.sendError(new TranslatableText("command.cgcem.kill.client.fail", src.getDisplayName()));
                LOGGER.warn("You can't use /kill client to " + src.getDisplayName().asString());
            }
        } else {
            src.sendError(new TranslatableText("command.cgcem.kill.client.forbidden"));
            LOGGER.warn("You can't use /kill client!");
        }

    }
}
