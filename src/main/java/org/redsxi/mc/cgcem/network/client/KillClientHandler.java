package org.redsxi.mc.cgcem.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.redsxi.mc.cgcem.network.exception.ClientKilledError;

public class KillClientHandler implements ClientPlayNetworking.PlayChannelHandler {
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Text source = buf.readText();
        throw new ClientKilledError(source);
    }
}
