package dev.alexnader.server_config.mixin.server;

import dev.alexnader.server_config.api.Config;
import dev.alexnader.server_config.api.ConfigKey;
import dev.alexnader.server_config.api.ConfigProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ConfigProvider {
    @Shadow
    private @Final MinecraftServer server;

    @Override
    public <C extends Config> C config(ConfigKey<C> key) {
        return ((ConfigProvider) server).config(key);
    }
}
