package com.envyful.pixel.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.pixel.forge.command.PixelSafariCommand;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.envyful.pixel.forge.config.PixelSafariLocale;
import com.envyful.pixel.forge.listener.NPCInteractListener;
import com.envyful.pixel.forge.listener.SafariCatchListener;
import com.envyful.pixel.forge.listener.SafariCommandListener;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.envyful.pixel.forge.task.CheckSafariFinishTask;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.IOException;

@Mod("pixelsafari")
public class PixelSafariForge {

    public static final String NPC_NBT = "PIXEL_SAFARI_NPC";
    public static final String VERSION = "1.0.2";

    private static PixelSafariForge instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private PixelSafariConfig config;
    private PixelSafariLocale locale;

    public PixelSafariForge() {
        MinecraftForge.EVENT_BUS.register(this);

        instance = this;

        this.loadConfig();

        playerManager.registerAttribute(this, PixelSafariAttribute.class);
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(PixelSafariConfig.class);
            this.locale = YamlConfigFactory.getInstance(PixelSafariLocale.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        new NPCInteractListener(this);
        new SafariCatchListener(this);
        new SafariCommandListener(this);

        new ForgeTaskBuilder()
                .task(new CheckSafariFinishTask(this))
                .delay(10L)
                .interval(10L)
                .async(true)
                .start();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), new PixelSafariCommand());
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

    public PixelSafariLocale getLocale() {
        return this.locale;
    }
}
