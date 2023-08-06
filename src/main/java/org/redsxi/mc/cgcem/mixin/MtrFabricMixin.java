package org.redsxi.mc.cgcem.mixin;

import mtr.CreativeModeTabs;
import mtr.MTRFabric;
import mtr.RegistryObject;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.RegistryUtilities;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.redsxi.mc.cgcem.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

import static org.redsxi.mc.cgcem.CrabGcsExtensionOfMc.mtrIdOf;

@Mixin(value = MTRFabric.class, remap = false)
public class MtrFabricMixin {

    /**
     * @author RedsXi
     * @reason For my mod lol
     */
    @Overwrite
    private static void registerBlock(String path, RegistryObject<Block> blockObj, CreativeModeTabs.Wrapper creativeModeTab) {
        Block block = switch(path) {
            case "ticket_barrier_entrance_1" -> Blocks.TICKET_BARRIER_ENTRANCE;
            case "ticket_barrier_exit_1" -> Blocks.TICKET_BARRIER_EXIT;
            default -> blockObj.get();
        };
        Registry.register(Registry.BLOCK, mtrIdOf(path), block);
        Objects.requireNonNull(creativeModeTab);
        BlockItem blockItem = new BlockItem(block, RegistryUtilities.createItemProperties(creativeModeTab::get));
        Registry.register(RegistryUtilities.registryGetItem(), new Identifier("mtr", path), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab.get(), blockItem);
    }
}
