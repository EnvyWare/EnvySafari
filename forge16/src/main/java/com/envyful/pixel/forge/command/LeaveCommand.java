package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import net.minecraft.entity.player.ServerPlayerEntity;

@Command(
        value = "leave",
        description = "The player leaves the safari if currently in the safari",
        aliases = {
                "l"
        }
)
@Permissible("pixel.safari.command.leave")
public class LeaveCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity sender, String[] args) {
        EnvyPlayer<?> player = PixelSafariForge.getInstance().getPlayerManager().getPlayer(sender);
        PixelSafariAttribute attribute = player.getAttribute(PixelSafariForge.class);

        if (!attribute.inSafari()) {
            player.message(PixelSafariForge.getInstance().getLocale().getCannotLeaveSafari());
            return;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("confirm")) {
            player.message(PixelSafariForge.getInstance().getLocale().getPleaseConfirmLeave());
            return;
        }

        UtilForgeConcurrency.runSync(attribute::finishSafari);
    }
}
