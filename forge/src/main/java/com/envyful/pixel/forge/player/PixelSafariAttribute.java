package com.envyful.pixel.forge.player;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.forge.player.util.UtilTeleport;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.TimeUnit;

public class PixelSafariAttribute extends AbstractForgeAttribute<PixelSafariForge> {

    private long safariEnd = -1;
    private IPixelmonBankAccount bankAccount = null;

    public PixelSafariAttribute(PixelSafariForge manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public boolean inSafari() {
        return this.parent.getParent().getEntityWorld().getWorldInfo().getWorldName()
                .equalsIgnoreCase(PixelSafariForge.getInstance().getConfig().getWorldName());
    }

    public boolean shouldSafariFinish() {
        return System.currentTimeMillis() > this.safariEnd;
    }

    public void startSafari() {
        this.startSafari(this.manager.getConfig().getMainZone());
    }

    public void startSafari(String zone) {
        PixelSafariConfig.ZoneInfo zoneInfo = this.getZone(zone);

        if (zoneInfo == null) {
            return;
        }

        this.getBankAccount().changeMoney((int) -this.manager.getConfig().getCost());
        this.safariEnd = System.currentTimeMillis() +
                TimeUnit.SECONDS.toMillis(this.manager.getConfig().getTimeInSeconds());

        for (String startCommmand : this.manager.getConfig().getStartCommmands()) {
            UtilForgeServer.executeCommand(startCommmand.replace("%player%", this.parent.getParent().getName()));
        }

        UtilTeleport.teleportPlayer(this.parent.getParent(), UtilWorld.findWorld(this.manager.getConfig().getWorldName()),
                new Vec3d(zoneInfo.getX() + 0.5, zoneInfo.getY(), zoneInfo.getZ() + 0.5),
                zoneInfo.getPitch(), zoneInfo.getYaw());
        this.parent.message("&c&l- &c$" + this.manager.getConfig().getCost());
    }

    private PixelSafariConfig.ZoneInfo getZone(String zone) {
        for (PixelSafariConfig.ZoneInfo zoneInfo : this.manager.getConfig().getZones()) {
            if (zoneInfo.getName().equalsIgnoreCase(zone)) {
                return zoneInfo;
            }
        }

        return null;
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

    public void finishSafari() {
        PixelSafariConfig.ZoneInfo zoneInfo = this.manager.getConfig().getSpawnPosition();

        this.safariEnd = -1;
        this.parent.message("&e&l(!) &eTeleporting you to spawn");
        UtilTeleport.teleportPlayer(this.parent.getParent(), UtilWorld.findWorld(this.manager.getConfig().getSpawnWorldName()),
                new Vec3d(zoneInfo.getX() + 0.5, zoneInfo.getY(), zoneInfo.getZ() + 0.5),
                zoneInfo.getPitch(), zoneInfo.getYaw());
    }

    @Override
    public void load() {}

    @Override
    public void save() {}
}
