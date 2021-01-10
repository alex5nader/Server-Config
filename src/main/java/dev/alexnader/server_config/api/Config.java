package dev.alexnader.server_config.api;

import net.minecraft.network.PacketByteBuf;

import java.nio.file.Path;

public interface Config {
    void initFromPacket(PacketByteBuf buf);
    void initFromPath(Path path);

    void writeToPacket(PacketByteBuf buf);
}
