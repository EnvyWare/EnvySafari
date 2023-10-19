package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.world.UtilWorld;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "world"
)
@Permissible("pixel.safari.command.world")
public class WorldInfoCommand {

    @CommandProcessor()
    public void onCommand(@Sender ServerPlayer player, String[] args) {
        player.sendSystemMessage(UtilChatColour.colour(UtilWorld.getName(player.level())));
    }
}
