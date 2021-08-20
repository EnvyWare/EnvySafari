package com.envyful.pixel.forge.player;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.pixel.forge.PixelSafariForge;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PixelSafariAttribute extends AbstractForgeAttribute<PixelSafariForge> {

    private long safariEnd = -1;
    private IPixelmonBankAccount bankAccount = null;

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
                TimeUnit.SECONDS.toMillis(this.manager.getConfig().getTimeInSeconds());
    }

    public boolean hasEnoughMoney() {
        return this.getBankAccount().getMoney() >= this.manager.getConfig().getCost();
    }

    private IPixelmonBankAccount getBankAccount() {
        if (this.bankAccount == null) {
            this.bankAccount = Pixelmon.moneyManager.getBankAccount(this.parent.getUuid()).get();
        }

        return this.bankAccount;
    }

    @Override
    public void load() {}

    @Override
    public void save() {}
}
