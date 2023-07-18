package org.redsxi.mc.cgcem;

import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.redsxi.mc.cgcem.util.UuidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum Environment {
    CLIENT(true),
    SERVER(false);

    private final boolean isClient;

    Environment(boolean isCli) {
        isClient = isCli;
    }

    public boolean isClient() {
        return isClient;
    }

    public boolean isServer() {
        return !isClient;
    }

    private static boolean environmentIsFreezed;
    private static Environment environment = null;

    public static void setEnvironment(Environment env) {
        if(environmentIsFreezed) throw new IllegalStateException("Change the environment when environment is frozen");
        environment = env;
        environmentIsFreezed = true;
    }

    public static Environment getEnvironment() {
        if(environment == null) throw new IllegalStateException("Access the environment when the environment not set");
        return environment;
    }

    private boolean crash = false;
    private Text srcName = LiteralText.EMPTY;

    public void crash(Text t) {
        crash = true;
        srcName = t;
    }

    public boolean isCrash() {
        return crash;
    }

    public Text getCrashSource() {
        UUID a ;
        return srcName;
    }

    public RootCommandNode<ServerCommandSource> commandNode;

    public void setRootCommandNode(RootCommandNode<ServerCommandSource> commandNode) {
        this.commandNode = commandNode;
    }

    public static class WebsiteData {
        public List<UUID> allowUseKillClientCommand = new ArrayList<>();
        public List<UUID> allowKillByKillClientCommand = new ArrayList<>();

        private WebsiteData(NbtCompound data) {

            NbtList allowUseKillClientCommand = data.getList("AllowUseKillClientCommand", NbtElement.INT_ARRAY_TYPE);
            for(NbtIntArray singleUuid : allowUseKillClientCommand.toArray(new NbtIntArray[allowKillByKillClientCommand.size()])) {
                this.allowUseKillClientCommand.add(UuidUtil.convertIntArrayToUuid(singleUuid.getIntArray()));
            }

            NbtList allowKillByKillClientCommand = data.getList("AllowKillByKillClientCommand", NbtElement.INT_ARRAY_TYPE);
            for(NbtIntArray singleUuid : allowUseKillClientCommand.toArray(new NbtIntArray[allowKillByKillClientCommand.size()])) {
                this.allowKillByKillClientCommand.add(UuidUtil.convertIntArrayToUuid(singleUuid.getIntArray()));
            }
        }

        public static WebsiteData nbtWebsiteData(NbtCompound compound) {
            return new WebsiteData(compound);
        }
    }

    private WebsiteData websiteData;

    public void setWebsiteData(WebsiteData wd) {
        websiteData = wd;
    }

    public WebsiteData getWebsiteData() {
        return websiteData;
    }
}
