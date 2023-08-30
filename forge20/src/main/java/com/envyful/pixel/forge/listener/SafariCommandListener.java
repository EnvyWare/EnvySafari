package com.envyful.pixel.forge.listener;

import com.envyful.api.forge.listener.LazyListener;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SafariCommandListener extends LazyListener {

    private final PixelSafariForge mod;

    public SafariCommandListener(PixelSafariForge mod) {
        this.mod = mod;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onBattleStart(CommandEvent event) {
        if (!(event.getParseResults().getContext().getSource().getEntity() instanceof ServerPlayer)) {
            return;
        }

        String command = event.getParseResults().getReader().getString();

        if (command.startsWith("/")) {
            return;
        }

        EnvyPlayer<?> player = PixelSafariForge.getInstance().getPlayerManager().getPlayer((ServerPlayer) event.getParseResults().getContext().getSource().getEntity());
        PixelSafariAttribute attribute = player.getAttribute(PixelSafariForge.class);

        if (attribute == null || !attribute.inSafari()) {
            return;
        }

        if (this.mod.getConfig().getSafariSettings().isCommandBlocked(command)) {
            event.setCanceled(true);
            player.message(this.mod.getLocale().getBlockedSafariCommand());
        }
    }
}
