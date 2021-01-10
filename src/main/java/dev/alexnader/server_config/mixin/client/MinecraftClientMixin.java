package dev.alexnader.server_config.mixin.client;

import dev.alexnader.server_config.api.Config;
import dev.alexnader.server_config.api.ConfigKey;
import dev.alexnader.server_config.api.ServerConfigEntrypoint;
import dev.alexnader.server_config.internal.client.RemoteConfigProvider;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements RemoteConfigProvider {
    @Unique
    private final Map<Identifier, ConfigKey<?>> keyIds = new Object2ReferenceOpenHashMap<>();
    @Unique
    private final Map<ConfigKey<?>, Config> configs = new Reference2ReferenceOpenHashMap<>();

    @Inject(method = "<init>*", at = @At("TAIL"))
    void initConfigs(CallbackInfo ci) {
        for (ServerConfigEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("server_config", ServerConfigEntrypoint.class)) {
            entrypoint.registerConfigs(key -> keyIds.put(key.id(), key));
        }
    }

    @Override
    public <C extends Config> C config(ConfigKey<C> key) {
        //noinspection unchecked
        return (C) configs.get(key);
    }

    @Override
    public void initMany(PacketByteBuf buf) {
        int count = buf.readVarInt();

        for (int i = 0; i < count; i++) {
            initConfig(buf);
        }
    }

    @Override
    public void initConfig(PacketByteBuf buf) {
        ConfigKey<?> key = keyIds.get(buf.readIdentifier());
        Config config = key.create();
        config.initFromPacket(buf);
        configs.put(key, config);
    }
}
