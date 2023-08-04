package org.redsxi.mc.cgcem;

import mtr.CreativeModeTabs;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.redsxi.mc.cgcem.command.ICommand;
import org.redsxi.mc.cgcem.command.KillClient;
import org.redsxi.mc.cgcem.command.SetPassCost;
import org.redsxi.mc.cgcem.network.GetWebsiteData;
import org.redsxi.mc.cgcem.network.client.KillClientHandler;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Objects;

public class CrabGcsExtensionOfMc implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {

    public static final String MOD_ID = "cgcem";

    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((d, u) -> {
            Environment.getEnvironment().setRootCommandNode(d.getRoot());
        });

        File f = new File("addons");
        System.out.println(f.getAbsolutePath());

        BlockEntityTypes.checkClassLoad();
        registerBlock("ticket_barrier_entrance_redstone", Blocks.TICKET_BARRIER_ENTRANCE_REDSTONE, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerBlock("ticket_barrier_exit_redstone", Blocks.TICKET_BARRIER_EXIT_REDSTONE, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerBlock("ticket_barrier_pay_direct", Blocks.TICKET_BARRIER_PAY_DIRECT, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerBlock("ticket_barrier_pay_direct_redstone", Blocks.TICKET_BARRIER_PAY_DIRECT_REDSTONE, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerCommand(SetPassCost.class);
        registerCommand(KillClient.class);
    }

    public void onInitializeClient() {
        Environment.setEnvironment(Environment.CLIENT);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_ENTRANCE_REDSTONE);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_EXIT_REDSTONE);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_PAY_DIRECT);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_PAY_DIRECT_REDSTONE);

        clientRegisterNetworkReceiver(NetworkIds.KILL_CLIENT, KillClientHandler.class);

        afterSideLoaded();
    }

    public void onInitializeServer() {
        Environment.setEnvironment(Environment.SERVER);

        afterSideLoaded();
    }

    private static void registerCutOutBlockRender(Block b) {
        BlockRenderLayerMap.INSTANCE.putBlock(b, RenderLayer.getCutout());
    }

    private static void registerBlock(String path, Block block) {
        Registry.register(Registry.BLOCK, idOf(path), block);
    }

    private static void registerBlock(String path, Block block, ItemGroup creativeModeTab) {
        registerBlock(path, block);
        Objects.requireNonNull(creativeModeTab);
        BlockItem blockItem = new BlockItem(block, RegistryUtilities.createItemProperties(() -> creativeModeTab));
        Registry.register(Registry.ITEM, idOf(path), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab, blockItem);
    }

    public static Identifier idOf(String path) {
        return new Identifier(MOD_ID, path);
    }

    private static void registerCommand(ICommand cmd) {
        CommandRegistrationCallback.EVENT.register((d, u) -> d.register(cmd.getCommand()));
    }

    private static <T extends ICommand> void registerCommand(Class<T> classOfCmd) {
        try {
            Constructor<T> c = classOfCmd.getConstructor();
            registerCommand(c.newInstance());
        } catch (Exception ignored) {
        }
    }

    private static
    <T extends ClientPlayNetworking.PlayChannelHandler>
    void clientRegisterNetworkReceiver(Identifier id, Class<T> classOfReceiver) {
        try {
            Constructor<T> constructor = classOfReceiver.getConstructor();
            ClientPlayNetworking.registerGlobalReceiver(id, constructor.newInstance());
        } catch (Exception ignored) {}
    }

    private static
    <T extends ServerPlayNetworking.PlayChannelHandler>
    void serverRegisterNetworkReceiver(Identifier id, Class<T> classOfReceiver) {
        try {
            Constructor<T> constructor = classOfReceiver.getConstructor();
            ServerPlayNetworking.registerGlobalReceiver(id, constructor.newInstance());
        } catch (Exception ignored) {}
    }

    private void afterSideLoaded() {
        Environment.getEnvironment().setWebsiteData(Environment.WebsiteData.nbtWebsiteData(GetWebsiteData.getWebsiteData()));
    }
}
