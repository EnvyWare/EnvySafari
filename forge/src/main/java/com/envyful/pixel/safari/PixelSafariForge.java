package com.envyful.pixel.safari;

import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.bstats.forge.Metrics;

import java.nio.file.Paths;

@Mod(
        modid = "pixelsafari",
        name = "PixelSafari Forge",
        version = PixelSafariForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class PixelSafariForge {

    public static final String VERSION = "0.3.0";

    private static PixelSafariForge instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        instance = this;

        Metrics metrics = new Metrics(
                Loader.instance().activeModContainer(),
                event.getModLog(),
                Paths.get("config/"),
                12199
        );

        ForgeUpdateBuilder.instance()
                .name("AdvancedHolograms")
                .requiredPermission("advancedholograms.update.notify")
                .owner("Pixelmon-Development")
                .repo("AdvancedHolograms")
                .version(VERSION)
                .start();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    public static PixelSafariForge getInstance() {
        return instance;
    }
}
