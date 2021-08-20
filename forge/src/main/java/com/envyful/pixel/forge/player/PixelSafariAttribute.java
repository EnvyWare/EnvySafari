package com.envyful.pixel.forge.player;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.pixel.forge.PixelSafariForge;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PixelSafariAttribute extends AbstractForgeAttribute<PixelSafariForge> {

    private long safariEnd = -1;

    public PixelSafariAttribute(PixelSafariForge manager, ForgeEnvyPlayer parent) {
        super(manager, parent);
    }

    public boolean inSafari() {
        return this.parent.getParent().getEntityWorld().getWorldInfo().getWorldName()
                .equalsIgnoreCase(PixelSafariForge.getInstance().getConfig().getWorldName());
    }

    public boolean shouldSafariFinish() {
        return System.currentTimeMillis() > this.safariEnd;
    }

    public void startSafari() {
        this.safariEnd = System.currentTimeMillis() +
                TimeUnit.SECONDS.toMillis(PixelSafariForge.getInstance().getConfig().getTimeInSeconds());
    }

    @Override
    public void load() {}

    @Override
    public void save() {}
}
