package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.pixel.forge.PixelSafariForge;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = "reload",
        description = "Reloads the config"
)
@Permissible("pixel.safari.command.reload")
@Child
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayer player, String[] args) {
        PixelSafariForge.getInstance().loadConfig();
        player.sendSystemMessage(UtilChatColour.colour("Reloaded config"));
    }
}
