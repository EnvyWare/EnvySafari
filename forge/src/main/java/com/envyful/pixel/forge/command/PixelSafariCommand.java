package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
        value = "pixelsafari",
        description = "PixelSafari main command",
        aliases = {
                "safari"
        }
)
@SubCommands(
        SpawnNPCCommand.class
)
public class PixelSafariCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        //TODO: help command
    }
}
