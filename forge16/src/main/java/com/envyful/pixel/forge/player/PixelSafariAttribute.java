package com.envyful.pixel.forge.player;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.forge.player.util.UtilTeleport;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.concurrent.TimeUnit;

public class PixelSafariAttribute extends AbstractForgeAttribute<PixelSafariForge> {

    private long safariEnd = -1;
    private BankAccount bankAccount = null;

    public PixelSafariAttribute(PixelSafariForge manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public boolean inSafari() {
        return UtilWorld.getName(this.getParent().getParent().getLevel())
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

        this.getBankAccount().take(this.manager.getConfig().getCost());
        this.safariEnd = System.currentTimeMillis() +
                TimeUnit.SECONDS.toMillis(this.manager.getConfig().getTimeInSeconds());

        for (String startCommmand : this.manager.getConfig().getStartCommmands()) {
            UtilForgeServer.executeCommand(startCommmand.replace("%player%", this.parent.getParent().getName().getString()));
        }

        World world = UtilWorld.findWorld(this.manager.getConfig().getWorldName());
        UtilTeleport.teleportPlayer(this.parent.getParent(), world,
                new Vector3d(zoneInfo.getX() + 0.5, zoneInfo.getY(), zoneInfo.getZ() + 0.5),
                zoneInfo.getPitch(), zoneInfo.getYaw());
        this.parent.message(UtilChatColour.translateColourCodes('&',"&c&l- &c$" + this.manager.getConfig().getCost()));
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
        return this.getBankAccount().getBalance().doubleValue() >= this.manager.getConfig().getCost();
    }

    private BankAccount getBankAccount() {
        if (this.bankAccount == null) {
            this.bankAccount = BankAccountProxy.getBankAccountUnsafe(this.parent.getUuid());
        }

        return this.bankAccount;
    }

    public void finishSafari() {
        PixelSafariConfig.ZoneInfo zoneInfo = this.manager.getConfig().getSpawnPosition();

        this.safariEnd = -1;
        this.parent.message(this.manager.getLocale().getReturningToSpawn());
        UtilTeleport.teleportPlayer(this.parent.getParent(), UtilWorld.findWorld(this.manager.getConfig().getSpawnWorldName()),
                new Vector3d(zoneInfo.getX() + 0.5, zoneInfo.getY(), zoneInfo.getZ() + 0.5),
                zoneInfo.getPitch(), zoneInfo.getYaw());
    }

    @Override
    public void load() {}

    @Override
    public void save() {}
}
