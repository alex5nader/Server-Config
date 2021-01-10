package dev.alexnader.server_config.mixin.server;

import dev.alexnader.server_config.api.Config;
import dev.alexnader.server_config.api.ConfigKey;
import dev.alexnader.server_config.api.ServerConfigEntrypoint;
import dev.alexnader.server_config.api.ServerConfigProvider;
import dev.alexnader.server_config.internal.ServerConfig;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements ServerConfigProvider {
    @Unique
    private final Map<ConfigKey<?>, Config> configs = new Reference2ReferenceOpenHashMap<>();

    @Inject(method = "<init>*", at = @At("TAIL"))
    void loadConfigs(CallbackInfo ci) {
        for (ServerConfigEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("server_config", ServerConfigEntrypoint.class)) {
            entrypoint.registerConfigs(key -> {
                Config config = key.create();
                config.initFromPath(key.path());
                configs.put(key, config);
            });
        }
    }

    @Override
    public <C extends Config> C config(ConfigKey<C> key) {
        //noinspection unchecked // ConfigKey<T> always maps to T
        return (C) configs.get(key);
    }

    @Override
    public <C extends Config> void save(ConfigKey<C> key) {
        configs.get(key).saveToPath(key.path());
    }

    @Override
    public void saveAll() {
        configs.keySet().forEach(this::save);
    }

    @Override
    public <C extends Config> void syncTo(ConfigKey<C> key, ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(key.id());
        configs.get(key).writeToPacket(buf);
        ServerPlayNetworking.send(player, ServerConfig.SINGLE_PACKET, buf);
    }

    @Override
    public <C extends Config> void syncToAll(ConfigKey<C> key) {
        PlayerLookup.all((MinecraftServer) (Object) this).forEach(player -> syncTo(key, player));
    }

    @Override
    public void syncAllTo(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(configs.size());

        for (ConfigKey<?> key : configs.keySet()) {
            buf.writeIdentifier(key.id());
            configs.get(key).writeToPacket(buf);
        }

        ServerPlayNetworking.send(player, ServerConfig.MANY_PACKET, buf);
    }

    @Override
    public void syncAllToAll() {
        PlayerLookup.all((MinecraftServer) (Object) this).forEach(this::syncAllTo);
    }
}
