package dev.alexnader.server_config.internal.client;

import dev.alexnader.server_config.internal.ServerConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ServerConfigClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
            ServerConfig.MANY_PACKET,
            (client, handler, buf, responseSender) -> ((RemoteConfigProvider) client).initConfig(buf)
        );
        ClientPlayNetworking.registerGlobalReceiver(
            ServerConfig.SINGLE_PACKET,
            (client, handler, buf, responseSender) -> ((RemoteConfigProvider) client).updateConfig(buf)
        );
    }
}
