package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "world",
        description = "Gets the world name"
)
@Permissible("pixel.safari.command.world")
public class WorldInfoCommand {

    @CommandProcessor()
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        player.sendMessage(new TextComponentString(player.getServerWorld().getWorldInfo().getWorldName()));
    }
}
