package com.envyful.pixel.forge.player;

import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigItem;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PixelSafariAttribute extends AbstractForgeAttribute<PixelSafariForge> {

    private long safariEnd = -1;
    private BankAccount bankAccount = null;
    private boolean inSafari = false;
    private NonNullList<ItemStack> items = null;
    private NonNullList<ItemStack> armor = null;
    private ItemStack offhand = null;

    public PixelSafariAttribute(PixelSafariForge manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public PixelSafariAttribute(UUID uuid) {
        super(uuid);
    }

    public boolean inSafari() {
        return this.inSafari;
    }

    public boolean shouldSafariFinish() {
        return System.currentTimeMillis() > this.safariEnd;
    }

    public void startSafari() {
        this.inSafari = true;
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

        if (this.manager.getConfig().getSafariSettings().isCacheInventory()) {
            this.items = getFakeInv(this.getParent().getParent());
            this.offhand = this.getParent().getParent().inventory.offhand.get(0);
            this.getParent().getParent().inventory.items.clear();
            this.getParent().getParent().inventory.offhand.clear();

            List<ConfigItem> temporaryItems = this.manager.getConfig().getSafariSettings().getTemporaryItems();

            for (ConfigItem temporaryItem : temporaryItems) {
                this.getParent().getParent().inventory.add(UtilConfigItem.fromConfigItem(temporaryItem));
            }

            this.getParent().getParent().refreshContainer(this.getParent().getParent().inventoryMenu);
        }

        World world = UtilWorld.findWorld(this.manager.getConfig().getWorldName());
        UtilTeleport.teleportPlayer(this.parent.getParent(), world,
                new Vector3d(zoneInfo.getX() + 0.5, zoneInfo.getY(), zoneInfo.getZ() + 0.5),
                zoneInfo.getPitch(), zoneInfo.getYaw());
        this.parent.message(UtilChatColour.translateColourCodes('&',"&c&l- &c$" + this.manager.getConfig().getCost()));
    }

    public static NonNullList<ItemStack> getFakeInv(PlayerEntity player) {
        NonNullList<ItemStack> ret = NonNullList.withSize(36, ItemStack.EMPTY);
        for(int i = 0; i < player.inventory.items.size(); i++) {
            ret.set(i, player.inventory.items.get(i).copy());
        }

        return ret;
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

        if (this.manager.getConfig().getSafariSettings().isCacheInventory()) {
            this.getParent().getParent().inventory.items.clear();
            this.getParent().getParent().inventory.offhand.clear();

            for (int i = 0; i < this.items.size(); i++) {
                this.getParent().getParent().inventory.items.set(i, this.items.get(i));
            }

            this.getParent().getParent().inventory.offhand.set(0, this.offhand);
            this.getParent().getParent().refreshContainer(this.getParent().getParent().inventoryMenu);
        }

        this.inSafari = false;
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
