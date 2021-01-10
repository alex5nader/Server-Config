package dev.alexnader.server_config.api;

import java.util.function.Consumer;

public interface ServerConfigEntrypoint {
    void registerConfigs(Consumer<ConfigKey<?>> registrar);
}
