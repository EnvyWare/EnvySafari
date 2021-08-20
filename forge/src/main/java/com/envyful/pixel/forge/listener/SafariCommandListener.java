package com.envyful.pixel.forge.listener;

import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.forge.PixelSafariForge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafariCommandListener extends LazyListener {

    private final PixelSafariForge mod;

    public SafariCommandListener(PixelSafariForge mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onBattleStart(CommandEvent event) {
        if (!(event.getSender() instanceof EntityPlayerMP)) {
            return;
        }

        if (!this.isAllowedCommand(event)) {
            event.setCanceled(true);
        }
    }

    private boolean isAllowedCommand(CommandEvent event) {
        return this.mod.getConfig().getSafariSettings().getAllowedCommands().contains(
                event.getCommand().getName() + " " + String.join(" ", event.getParameters())
        );
    }
}
