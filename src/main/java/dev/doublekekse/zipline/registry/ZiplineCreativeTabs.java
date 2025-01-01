package dev.doublekekse.zipline.registry;

import dev.doublekekse.zipline.Zipline;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ZiplineCreativeTabs {
    public static final CreativeModeTab ZIPLINE_TAB = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ZiplineItems.ZIPLINE))
        .title(Component.translatable("itemGroup.zipline.zipline"))
        .displayItems((context, entries) -> {
            entries.accept(ZiplineItems.ZIPLINE);
        })
        .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Zipline.id("zipline"), ZIPLINE_TAB);
    }
}
