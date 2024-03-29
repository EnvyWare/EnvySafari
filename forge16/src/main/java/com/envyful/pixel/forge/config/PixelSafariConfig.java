package com.envyful.pixel.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@ConfigPath("config/EnvySafari/config.yml")
@ConfigSerializable
public class PixelSafariConfig extends AbstractYamlConfig {

    private String worldName = "SafariWorld";
    private String mainZone = "Main";
    private String npcSkin = "Gardener";
    private double cost = 200.0;
    private long timeInSeconds = 300;
    private String spawnWorldName = "spawn";
    private Settings safariSettings = new Settings();
    private ZoneInfo spawnPosition = new ZoneInfo("Spawn", 0, 0, 0, 0f, 0f);
    private Map<String, ZoneInfo> zones = ImmutableMap.of(
            "one", new ZoneInfo("Main", 0, 100, 0, 0f, 0f),
            "two", new ZoneInfo("SomeBiome", 10, 100, 10, 0f, 0f)
    );
    private List<String> startCommmands = Lists.newArrayList(
            "give %player% pixelmon:safari_ball 10"
    );

    public PixelSafariConfig() {
        super();
    }

    public String getWorldName() {
        return this.worldName;
    }

    public double getCost() {
        return this.cost;
    }

    public long getTimeInSeconds() {
        return this.timeInSeconds;
    }

    public String getMainZone() {
        return this.mainZone;
    }

    public String getSpawnWorldName() {
        return this.spawnWorldName;
    }

    public ZoneInfo getSpawnPosition() {
        return this.spawnPosition;
    }

    public Settings getSafariSettings() {
        return this.safariSettings;
    }

    public String getNpcSkin() {
        return this.npcSkin;
    }

    public List<ZoneInfo> getZones() {
        return Lists.newArrayList(this.zones.values());
    }

    public List<String> getStartCommmands() {
        return this.startCommmands;
    }

    @ConfigSerializable
    public static class ZoneInfo {

        private String name;
        private int x;
        private int y;
        private int z;
        private double yaw;
        private double pitch;

        public ZoneInfo() {
        }

        public ZoneInfo(String name, int x, int y, int z, double pitch, double yaw) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.yaw = yaw;
        }

        public String getName() {
            return this.name;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }

        public float getPitch() {
            return (float) this.pitch;
        }

        public float getYaw() {
            return (float) this.yaw;
        }
    }

    @ConfigSerializable
    public static class Settings {

        private boolean allowPVE = true;
        private boolean allowPVP = false;
        private boolean cacheInventory = false;
        private Map<String, ConfigItem> tempItems = ImmutableMap.of("example", ConfigItem.builder()
                        .type("pixelmon:poke_ball")
                        .amount(16)
                        .name("Safari Poke Ball")
                .build());
        private List<String> allowedCommands = Lists.newArrayList(
                "safari l",
                "safari leave",
                "pixelsafari leave",
                "pixelsafari l"
        );
        private transient List<Pattern> allowedCommandPatterns = null;

        public Settings() {
        }

        public boolean isCacheInventory() {
            return this.cacheInventory;
        }

        public boolean isAllowPVE() {
            return this.allowPVE;
        }

        public List<ConfigItem> getTemporaryItems() {
            return Lists.newArrayList(this.tempItems.values());
        }

        public boolean isAllowPVP() {
            return this.allowPVP;
        }

        public boolean isCommandBlocked(String command) {
            if (this.allowedCommandPatterns == null) {
                this.allowedCommandPatterns = Lists.newArrayList();

                for (String allowedCommand : this.allowedCommands) {
                    this.allowedCommandPatterns.add(Pattern.compile(allowedCommand));
                }
            }

            for (Pattern allowedCommandPattern : this.allowedCommandPatterns) {
                if (allowedCommandPattern.matcher(command).matches()) {
                    return false;
                }
            }

            return true;
        }
    }
}
