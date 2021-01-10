package dev.alexnader.server_config.api;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerConfigProvider extends ConfigProvider {
    <C extends Config> void save(ConfigKey<C> key);
    void saveAll();

    <C extends Config> void syncTo(ConfigKey<C> key, ServerPlayerEntity player);
    <C extends Config> void syncToAll(ConfigKey<C> key);
    void syncAllTo(ServerPlayerEntity player);
    void syncAllToAll();
}
