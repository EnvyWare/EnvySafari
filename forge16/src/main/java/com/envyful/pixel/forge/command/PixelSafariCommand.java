package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.pixel.forge.PixelSafariForge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

@Command(
        value = "pixelsafari",
        description = "PixelSafari main command",
        aliases = {
                "safari"
        }
)
@SubCommands({
        SpawnNPCCommand.class,
        LeaveCommand.class,
        SafariJoinCommand.class,
        WorldInfoCommand.class,
        ReloadCommand.class
})
public class PixelSafariCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        for (String s : PixelSafariForge.getInstance().getLocale().getRootCommandHelp()) {
            player.sendMessage(UtilChatColour.colour(s), Util.NIL_UUID);
        }
    }
}
