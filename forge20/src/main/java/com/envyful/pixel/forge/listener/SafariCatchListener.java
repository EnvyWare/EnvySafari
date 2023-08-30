package com.envyful.pixel.forge.listener;

import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SafariCatchListener {

    private final PixelSafariForge mod;

    public SafariCatchListener(PixelSafariForge mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event) {
        var playerOne = this.getPlayer(event.getTeamOne());
        var playerTwo = this.getPlayer(event.getTeamTwo());

        if (playerOne != null && playerTwo != null) {
            if (!this.mod.getConfig().getSafariSettings().isAllowPVP() && (this.inSafari(playerOne) || this.inSafari(playerTwo))) {
                event.setCanceled(true);
            }

            return;
        }

        if (!this.mod.getConfig().getSafariSettings().isAllowPVE()) {
            if ((this.inSafari(playerOne) || this.inSafari(playerTwo))) {
                event.setCanceled(true);
            }
        }
    }

    private ServerPlayer getPlayer(BattleParticipant... participants) {
        for (BattleParticipant participant : participants) {
            if (participant instanceof PlayerParticipant) {
                return ((PlayerParticipant)participant).player;
            }
        }

        return null;
    }

    private boolean inSafari(ServerPlayer player) {
        if (player == null) {
            return false;
        }

        EnvyPlayer<?> envyPlayer = PixelSafariForge.getInstance().getPlayerManager().getPlayer(player);
        PixelSafariAttribute attribute = envyPlayer.getAttribute(PixelSafariForge.class);

        return attribute != null && attribute.inSafari();
    }
}
