package dev.alexnader.server_config.mixin.client;

import dev.alexnader.server_config.api.Config;
import dev.alexnader.server_config.api.ConfigKey;
import dev.alexnader.server_config.api.ConfigProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements ConfigProvider {
    @Shadow
    private @Final MinecraftClient client;

    @Override
    public <C extends Config> C config(ConfigKey<C> key) {
        return ((ConfigProvider) client).config(key);
    }
}
