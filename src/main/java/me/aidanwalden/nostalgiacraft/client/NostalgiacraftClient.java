package me.aidanwalden.nostalgiacraft.client;

import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class NostalgiacraftClient implements ClientModInitializer {

    public static File configFile;

    @Override
    public void onInitializeClient() {
        System.out.println("Hello World from NostalgiaCraft!");
        configFile = new File(getConfigDir().toFile(), "nostalgiacraft.properties");
        try {
            NostalgiacraftCore.loadConfig(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }


}
