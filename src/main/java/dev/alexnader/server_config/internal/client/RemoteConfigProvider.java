package dev.alexnader.server_config.internal.client;

import dev.alexnader.server_config.api.ConfigProvider;
import net.minecraft.network.PacketByteBuf;

public interface RemoteConfigProvider extends ConfigProvider {
    void initConfig(PacketByteBuf buf);
    void updateConfig(PacketByteBuf buf);
}
