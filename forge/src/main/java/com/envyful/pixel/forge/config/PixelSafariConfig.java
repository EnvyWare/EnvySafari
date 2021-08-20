package com.envyful.pixel.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigPath("config/PixelSafari/config.yml")
public class PixelSafariConfig extends AbstractYamlConfig {

    private String worldName = "SafariWorld";
    private String mainZone = "Main";
    private double cost = 200.0;
    private long timeInSeconds = 300;
    private List<ZoneInfo> zones = Lists.newArrayList(
            new ZoneInfo("Main", 0, 100, 0),
            new ZoneInfo("SomeBiome", 10, 100, 10)
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

    public List<ZoneInfo> getZones() {
        return this.zones;
    }

    @ConfigSerializable
    private class ZoneInfo {

        private String name;
        private int x;
        private int y;
        private int z;
        private float yaw;
        private float pitch;

        public ZoneInfo(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
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
            return this.pitch;
        }

        public float getYaw() {
            return this.yaw;
        }
    }
}
