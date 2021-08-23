package com.envyful.pixel.forge.listener;

import com.envyful.api.forge.listener.LazyListener;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafariCommandListener extends LazyListener {

    private final PixelSafariForge mod;

    public SafariCommandListener(PixelSafariForge mod) {
        this.mod = mod;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onBattleStart(CommandEvent event) {
        if (!(event.getSender() instanceof EntityPlayerMP)) {
            return;
        }

        EnvyPlayer<?> player = PixelSafariForge.getInstance().getPlayerManager().getPlayer((EntityPlayerMP) event.getSender());
        PixelSafariAttribute attribute = player.getAttribute(PixelSafariForge.class);

        if (attribute == null || !attribute.inSafari()) {
            return;
        }

        if (!this.isAllowedCommand(event)) {
            event.setCanceled(true);
        }
    }

    private boolean isAllowedCommand(CommandEvent event) {
        for (String allowedCommand : this.mod.getConfig().getSafariSettings().getAllowedCommands()) {
            if (event.getCommand().getName().equalsIgnoreCase(allowedCommand)
                    || event.getCommand().getAliases().contains(allowedCommand.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
