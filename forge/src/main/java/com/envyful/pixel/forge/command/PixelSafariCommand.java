package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

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
        WorldInfoCommand.class
})
public class PixelSafariCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                "&e&l(!) &eGo to the Safari NPC to enter the Safari Zone!")));
    }
}
