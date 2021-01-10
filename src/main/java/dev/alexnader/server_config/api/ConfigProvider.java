package dev.alexnader.server_config.api;

public interface ConfigProvider {
    <C extends Config> C config(ConfigKey<C> key);
}
