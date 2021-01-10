package dev.alexnader.server_config.api;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class ConfigKey<C extends Config> {
    private final Identifier id;
    private final Supplier<C> defaultFactory;

    public ConfigKey(Identifier id, Supplier<C> defaultFactory) {
        this.id = id;
        this.defaultFactory = defaultFactory;
    }

    public Identifier id() {
        return id;
    }

    public C create() {
        return defaultFactory.get();
    }
}
