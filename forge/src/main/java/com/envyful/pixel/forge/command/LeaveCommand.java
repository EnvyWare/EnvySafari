package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.stream.Collectors;

@Command(
        value = "leave",
        description = "The player leaves the safari if currently in the safari",
        aliases = {
                "l"
        }
)
@Permissible("pixel.safari.command.leave")
public class LeaveCommand {

    @CommandProcessor()
    public void onCommand(@Sender EnvyPlayer<?> player, String[] args) {
        PixelSafariAttribute attribute = player.getAttribute(PixelSafariForge.class);

        if (!attribute.inSafari()) {
            player.message(UtilChatColour.translateColourCodes('&',
                    "&c&l(!) &cYou are not currently in the safari zone."));
            return;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("confirm")) {
            player.message(UtilChatColour.translateColourCodes('&',
                    "&c&l(!) &cType &7/safari l confirm&c to confirm you wish to leave the safari zone early."));
            return;
        }

        UtilForgeConcurrency.runSync(() -> {
            attribute.finishSafari();
            player.message(UtilChatColour.translateColourCodes('&', "&e&l(!) &eSpawned NPC."));
        });
    }
}
