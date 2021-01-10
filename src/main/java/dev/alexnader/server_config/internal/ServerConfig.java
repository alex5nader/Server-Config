package dev.alexnader.server_config.internal;

import dev.alexnader.server_config.api.ServerConfigProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;

public class ServerConfig implements ModInitializer {
    public static final Identifier MANY_PACKET = new Identifier("server_config", "many");
    public static final Identifier SINGLE_PACKET = new Identifier("server_config", "single");

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register(
            (handler, sender, server) -> ((ServerConfigProvider) server).syncAllTo(handler.player)
        );
        ServerLifecycleEvents.SERVER_STOPPING.register(
            server -> ((ServerConfigProvider) server).saveAll()
        );
    }
}
