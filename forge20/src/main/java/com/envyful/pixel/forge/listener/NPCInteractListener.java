package com.envyful.pixel.forge.listener;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.api.forge.world.UtilWorld;
import com.envyful.pixel.forge.PixelSafariForge;
import com.envyful.pixel.forge.config.PixelSafariLocale;
import com.envyful.pixel.forge.player.PixelSafariAttribute;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NPCInteractListener extends LazyListener {

    private final PixelSafariForge mod;

    public NPCInteractListener(PixelSafariForge mod) {
        super();

        this.mod = mod;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(NPCEvent.Interact event) {
        if (!this.isNPC(event.npc)) {
            return;
        }

        event.setCanceled(true);

        var world = UtilWorld.findWorld(this.mod.getConfig().getWorldName());

        var player = (ServerPlayer) event.player;
        PixelSafariAttribute attribute = this.mod.getPlayerManager().getPlayer(player).getAttribute(PixelSafariAttribute.class);

        if (world == null) {
            for (String s : this.mod.getLocale().getCurrentlyClosed()) {
                player.sendSystemMessage(UtilChatColour.colour(s));
            }
            return;
        }

        if (attribute == null || attribute.inSafari()) {
            return;
        }

        if (!attribute.hasEnoughMoney()) {
            for (String s : this.mod.getLocale().getNotEnoughMoney()) {
                player.sendSystemMessage(UtilChatColour.colour(s));
            }
            return;
        }

        PixelSafariLocale.NPCDialogue npcDialogue = this.mod.getLocale().getNpcDialogue();

        new Dialogue.DialogueBuilder()
                .setName(npcDialogue.getTitle())
                .setText(npcDialogue.getTextBody())
                .addChoice(new Choice(npcDialogue.getYesOption(), dialogueChoiceEvent -> {
                    dialogueChoiceEvent.setAction(DialogueNextActionPacket.DialogueGuiAction.CLOSE);
                    attribute.startSafari();
                }))
                .addChoice(new Choice(npcDialogue.getNoOption(), dialogueChoiceEvent ->
                        dialogueChoiceEvent.setAction(DialogueNextActionPacket.DialogueGuiAction.CLOSE)))
                .build().open(player);
    }

    private boolean isNPC(Entity entity) {
        return entity.getTags().contains(PixelSafariForge.NPC_NBT);
    }
}
