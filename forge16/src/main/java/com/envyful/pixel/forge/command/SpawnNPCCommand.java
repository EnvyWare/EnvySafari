package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.pixel.forge.PixelSafariForge;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

@Command(
        value = "spawnnpc",
        description = "Spawns the Safari NPC on your location",
        aliases = {
                "snpc"
        }
)
@Permissible("pixel.safari.command.spawn.npc")
public class SpawnNPCCommand {

    @CommandProcessor()
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        UtilForgeConcurrency.runSync(() -> {
            NPCChatting npc = new NPCChatting(player.getLevel());

            npc.setPos(player.getX(), player.getY(), player.getZ());
            npc.setAIMode(EnumTrainerAI.StandStill);
            npc.addTag(PixelSafariForge.NPC_NBT);
            npc.setPersistenceRequired();
            npc.initAI();
            npc.init(PixelSafariForge.getInstance().getConfig().getNpcSkin());

            player.getLevel().addFreshEntity(npc);
            npc.addTag(PixelSafariForge.NPC_NBT);

            player.sendMessage(UtilChatColour.colour("&e&l(!) &eSpawned NPC."), Util.NIL_UUID);
        });
    }
}
