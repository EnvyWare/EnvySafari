package com.envyful.pixel.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.pixel.forge.command.PixelSafariCommand;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.envyful.pixel.forge.listener.NPCInteractListener;
import com.envyful.pixel.forge.listener.SafariCatchListener;
import com.envyful.pixel.forge.listener.SafariCommandListener;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.envyful.pixel.forge.task.CheckSafariFinishTask;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;

@Mod(
        modid = "envysafari",
        name = "EnvySafari",
        version = PixelSafariForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class PixelSafariForge {

    public static final String NPC_NBT = "PIXEL_SAFARI_NPC";
    public static final String VERSION = "1.1.0";

    private static PixelSafariForge instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private PixelSafariConfig config;

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        instance = this;

        this.loadConfig();

        playerManager.registerAttribute(this, PixelSafariAttribute.class);
    }

    public void loadConfig() {
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
