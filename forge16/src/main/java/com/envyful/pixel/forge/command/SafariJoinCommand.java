package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.config.PixelSafariConfig;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextActionPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

@Command(
        value = "join",
        description = "Join the safari via command",
        aliases = {
                "j"
        }
)
@Permissible("pixel.safari.command.join")
@Child
public class SafariJoinCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        EnvyPlayer<?> envyPlayer = PixelSafariForge.getInstance().getPlayerManager().getPlayer(player);
        PixelSafariAttribute attribute = envyPlayer.getAttribute(PixelSafariForge.class);

        if (attribute == null || attribute.inSafari()) {
            return;
        }

        World world = UtilWorld.findWorld(PixelSafariForge.getInstance().getConfig().getWorldName());

        if (world == null) {
            envyPlayer.message(PixelSafariForge.getInstance().getLocale().getCurrentlyClosed());
            return;
        }

        if (!attribute.hasEnoughMoney()) {
            envyPlayer.message(PixelSafariForge.getInstance().getLocale().getNotEnoughMoney());
            return;
        }

        String zone = PixelSafariForge.getInstance().getConfig().getMainZone();

        if (args.length > 0) {
            zone = args[0];
        }

        PixelSafariConfig.ZoneInfo zoneInfo = this.getZone(zone);

        if (zoneInfo == null) {
            envyPlayer.message(PixelSafariForge.getInstance().getLocale().getCannotFindSafariZone());
            return;
        }

        player.closeContainer();
        new Dialogue.DialogueBuilder()
                .setName("Safari")
                .setText("Are you sure you want to pay $" + PixelSafariForge.getInstance().getConfig().getCost() + " to enter the safari zone?")
                .addChoice(new Choice("Yes", dialogueChoiceEvent -> {
                    dialogueChoiceEvent.setAction(DialogueNextActionPacket.DialogueGuiAction.CLOSE);
                    attribute.startSafari();
                }))
                .addChoice(new Choice("No", dialogueChoiceEvent ->
                        dialogueChoiceEvent.setAction(DialogueNextActionPacket.DialogueGuiAction.CLOSE)))
                .build().open(player);
    }

    private PixelSafariConfig.ZoneInfo getZone(String zone) {
        for (PixelSafariConfig.ZoneInfo zoneInfo : PixelSafariForge.getInstance().getConfig().getZones()) {
            if (zoneInfo.getName().equalsIgnoreCase(zone)) {
                return zoneInfo;
            }
        }

        return null;
    }

}
