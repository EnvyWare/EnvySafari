package com.envyful.pixel.forge.listener;

import com.envyful.pixel.forge.PixelSafariForge;
import com.google.common.eventbus.Subscribe;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafariCatchListener {

    private final PixelSafariForge mod;

    public SafariCatchListener(PixelSafariForge mod) {
        this.mod = mod;

        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event) {

        if (!this.mod.getConfig().getWorldName()
                .equalsIgnoreCase(event.participant1[0].getWorld().getWorldInfo().getWorldName())) {
            return;
        }

        if (this.isPlayer(event.participant1) && this.isPlayer(event.participant2)) {
            if (!this.mod.getConfig().getSafariSettings().isAllowPVP()) {
                event.setCanceled(true);
            }

            return;
        }

        if (!this.mod.getConfig().getSafariSettings().isAllowPVE()) {
            event.setCanceled(true);
        }
    }

    private boolean isPlayer(BattleParticipant... participants) {
        for (BattleParticipant participant : participants) {
            if (participant != null && participant instanceof PlayerParticipant) {
                return true;
            }
        }

        return false;
    }
}
