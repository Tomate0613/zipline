package dev.doublekekse.zipline.registry;

import dev.doublekekse.zipline.Zipline;
import dev.doublekekse.zipline.item.ZiplineItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ZiplineItems {
    public static final Item ZIPLINE = register(ZiplineItem::new, new Item.Properties(), "zipline");

    private static Item register(Function<Item.Properties, Item> factory, Item.Properties properties, String path) {
        final var location = Zipline.id(path);
        final var key = ResourceKey.create(Registries.ITEM, location);

        return Items.registerItem(key, factory, properties);
    }

    public static void register() {
    }
}
