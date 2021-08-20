package com.envyful.pixel.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.pixel.forge.command.PixelSafariCommand;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.envyful.pixel.forge.listener.NPCInteractListener;
import com.envyful.pixel.forge.listener.SafariCatchListener;
import com.envyful.pixel.forge.listener.SafariCommandListener;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.envyful.pixel.forge.task.CheckSafariFinishTask;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.bstats.forge.Metrics;

import java.io.IOException;
import java.nio.file.Paths;

@Mod(
        modid = "pixelsafari",
        name = "PixelSafari Forge",
        version = PixelSafariForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class PixelSafariForge {

    public static final String NPC_NBT = "PIXEL_SAFARI_NPC";
    public static final String VERSION = "0.3.0";

    private static PixelSafariForge instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private PixelSafariConfig config;

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        instance = this;

        this.loadConfig();

        Metrics metrics = new Metrics(
                Loader.instance().activeModContainer(),
                event.getModLog(),
                Paths.get("config/"),
                12199 //TODO
        );

        playerManager.registerAttribute(this, PixelSafariAttribute.class);

        ForgeUpdateBuilder.instance()
                .name("ForgePixelSafari")
                .requiredPermission("pixelsafari.update.notify")
                .owner("Pixelmon-Development")
                .repo("ForgePixelSafari")
                .version(VERSION)
                .start();
    }

    private void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(PixelSafariConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        new NPCInteractListener(this);
        new SafariCatchListener(this);
        new SafariCommandListener(this);

        this.commandFactory.registerCommand(event.getServer(), new PixelSafariCommand());

        new ForgeTaskBuilder()
                .task(new CheckSafariFinishTask(this))
                .delay(10L)
                .interval(10L)
                .async(true)
                .start();
    }

    public static PixelSafariForge getInstance() {
        return instance;
    }

    public PixelSafariConfig getConfig() {
        return this.config;
    }

    public ForgePlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
