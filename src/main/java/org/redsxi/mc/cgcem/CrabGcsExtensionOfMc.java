package org.redsxi.mc.cgcem;

import mtr.CreativeModeTabs;
import mtr.Items;
import mtr.RegistryClient;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.redsxi.mc.cgcem.command.ICommand;
import org.redsxi.mc.cgcem.command.SetPassCost;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.Objects;

public class CrabGcsExtensionOfMc implements ModInitializer, ClientModInitializer {

    public static final String MOD_ID = "cgcem";

    public void onInitialize() {
        BlockEntityTypes.checkClassLoad();
        registerBlock("ticket_barrier_entrance_redstone", Blocks.TICKET_BARRIER_ENTRANCE_REDSTONE, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerBlock("ticket_barrier_exit_redstone", Blocks.TICKET_BARRIER_EXIT_REDSTONE, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerBlock("ticket_barrier_pay_direct", Blocks.TICKET_BARRIER_PAY_DIRECT, CreativeModeTabs.RAILWAY_FACILITIES.get());
        registerCommand(SetPassCost.class);
        CustomPortalBuilder.beginPortal()
                .frameBlock(net.minecraft.block.Blocks.IRON_BLOCK)
                .lightWithFluid(Fluids.WATER)
                .destDimID(idOf("superflat"))
                .tintColor(24,196,0)
                .registerPortal();
        ThreadedAnvilChunkStorage a;
    }

    public void onInitializeClient() {
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_ENTRANCE_REDSTONE);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_EXIT_REDSTONE);
        registerCutOutBlockRender(Blocks.TICKET_BARRIER_PAY_DIRECT);
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

    private void registerDimensionType(String name, DimensionType dt) {
    }
}
