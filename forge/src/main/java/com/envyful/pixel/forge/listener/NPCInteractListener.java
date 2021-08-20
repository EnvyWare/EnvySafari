package com.envyful.pixel.forge.listener;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiAcceptDeny;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NPCInteractListener extends LazyListener {

    private final PixelSafariForge mod;

    public NPCInteractListener(PixelSafariForge mod) {
        super();

        this.mod = mod;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!this.isNPC(event.getEntity())) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
        PixelSafariAttribute attribute = this.mod.getPlayerManager().getPlayer(player).getAttribute(PixelSafariForge.class);

        if (attribute == null || attribute.inSafari()) {
            return;
        }

        if (!attribute.hasEnoughMoney()) {
            player.sendMessage(new TextComponentString(
                    UtilChatColour.translateColourCodes('&', "&c&l(!) &cInsufficient funds!")));
            return;
        }

        new Dialogue.DialogueBuilder()
                .setName("Safari")
                .setText("Are you sure you want to pay $" + this.mod.getConfig().getCost() + " to enter the safari zone?")
                .addChoice(new Choice("Yes", dialogueChoiceEvent -> {
                    dialogueChoiceEvent.setAction(DialogueNextAction.DialogueGuiAction.CLOSE);
                    attribute.startSafari();
                }))
                .addChoice(new Choice("No", dialogueChoiceEvent ->
                        dialogueChoiceEvent.setAction(DialogueNextAction.DialogueGuiAction.CLOSE)))
                .build().open(player);
    }

    private boolean isNPC(Entity entity) {
        return entity.getTags().contains(PixelSafariForge.NPC_NBT);
    }
}
