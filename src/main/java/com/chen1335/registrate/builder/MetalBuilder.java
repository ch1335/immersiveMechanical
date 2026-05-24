package com.chen1335.registrate.builder;

import com.chen1335.registrate.IERegistrate;
import com.chen1335.registrate.IMetalType;
import com.chen1335.registrate.MetalDefinition;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class MetalBuilder {
    private final IERegistrate registrate;
    private final String name;
    private final Map<IMetalType, ItemEntry<Item>> typeItemMap = new HashMap<>();

    public MetalBuilder(IERegistrate registrate, String name) {
        this.registrate = registrate;
        this.name = name;
    }

    public MetalBuilder type(IMetalType type) {
        ItemEntry<Item> register = registrate.item(type.format(name), Item::new)
                .tag(type.getTag(name))
                .tag(type.getTypTag())
                .register();
        typeItemMap.put(type, register);
        return this;
    }

    public MetalBuilder type(IMetalType... types) {
        for (IMetalType type : types) {
            type(type);
        }
        return this;
    }

    public MetalDefinition register() {
        MetalDefinition metalDefinition = new MetalDefinition(name, registrate.getModid(), typeItemMap);
        registrate.getMetalDefinitions().add(metalDefinition);
        return metalDefinition;
    }
}
