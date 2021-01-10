package dev.alexnader.server_config.api;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
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

    public Path path() {
        return FabricLoader.getInstance()
            .getConfigDir()
            .resolve(id.getNamespace())
            .resolve(id.getPath());
    }
}
