package com.envyful.pixel.forge.listener;

import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerQuitListener {

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        EnvyPlayer<?> envyPlayer = PixelSafariForge.getInstance().getPlayerManager().getPlayer((ServerPlayer) event.getEntity());

        if (envyPlayer == null) {
            return;
        }

        PixelSafariAttribute attribute = envyPlayer.getAttribute(PixelSafariForge.class);

        if (!attribute.inSafari()) {
            return;
        }

        attribute.finishSafari();
    }
}
