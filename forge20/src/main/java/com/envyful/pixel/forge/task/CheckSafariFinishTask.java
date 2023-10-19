package com.envyful.pixel.forge.task;

import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;

public class CheckSafariFinishTask implements Runnable {

    private final PixelSafariForge mod;

    public CheckSafariFinishTask(PixelSafariForge mod) {
        this.mod = mod;
    }

    @Override
    public void run() {
        for (ForgeEnvyPlayer onlinePlayer : this.mod.getPlayerManager().getOnlinePlayers()) {
            if (onlinePlayer == null) {
                continue;
            }

            PixelSafariAttribute attribute = onlinePlayer.getAttribute(PixelSafariAttribute.class);

            if (attribute == null || !attribute.inSafari()) {
                continue;
            }

            if (attribute.shouldSafariFinish()) {
                UtilForgeConcurrency.runSync(attribute::finishSafari);
            }
        }
    }
}
