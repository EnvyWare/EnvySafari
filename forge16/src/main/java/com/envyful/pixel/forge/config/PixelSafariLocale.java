package com.envyful.pixel.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigPath("config/PixelSafari/locale.yml")
@ConfigSerializable
public class PixelSafariLocale extends AbstractYamlConfig {

    private List<String> cannotLeaveSafari = Lists.newArrayList(
            "&c&l(!) &cYou are not currently in the safari zone."
    );

    private List<String> pleaseConfirmLeave = Lists.newArrayList(
            "&c&l(!) &cType &7/safari l confirm&c to confirm you wish to leave the safari zone early."
    );

    private List<String> returningToSpawn = Lists.newArrayList(
            "&e&l(!) &eTeleporting you to spawn"
    );

    private List<String> rootCommandHelp = Lists.newArrayList(
            "&e&l(!) &eGo to the Safari NPC to enter the Safari Zone!"
    );

    private List<String> currentlyClosed = Lists.newArrayList(
            "&c&l(!) &cThe safari is currently closed!"
    );

    private List<String> notEnoughMoney = Lists.newArrayList(
            "&c&l(!) &cInsufficient funds!"
    );

    private List<String> cannotFindSafariZone = Lists.newArrayList(
            "&c&l(!) &cCannot find that zone!"
    );

    public PixelSafariLocale() {
        super();
    }

    public List<String> getCannotLeaveSafari() {
        return this.cannotLeaveSafari;
    }

    public List<String> getPleaseConfirmLeave() {
        return this.pleaseConfirmLeave;
    }

    public List<String> getReturningToSpawn() {
        return this.returningToSpawn;
    }

    public List<String> getRootCommandHelp() {
        return this.rootCommandHelp;
    }

    public List<String> getCurrentlyClosed() {
        return this.currentlyClosed;
    }

    public List<String> getNotEnoughMoney() {
        return this.notEnoughMoney;
    }

    public List<String> getCannotFindSafariZone() {
        return this.cannotFindSafariZone;
    }
}
