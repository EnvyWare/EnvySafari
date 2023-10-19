package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.pixel.forge.PixelSafariForge;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import net.minecraft.server.level.ServerPlayer;

@Command(
        value = {
                "spawnnpc",
                "snpc"
        }
)
@Permissible("pixel.safari.command.spawn.npc")
public class SpawnNPCCommand {

    @CommandProcessor()
    public void onCommand(@Sender ServerPlayer player, String[] args) {
        UtilForgeConcurrency.runSync(() -> {
            NPCChatting npc = new NPCChatting(player.level());

            npc.setPos(player.getX(), player.getY(), player.getZ());
            npc.setAIMode(EnumTrainerAI.StandStill);
            npc.addTag(PixelSafariForge.NPC_NBT);
            npc.setPersistenceRequired();
            npc.initAI();
            npc.init(PixelSafariForge.getInstance().getConfig().getNpcSkin());

            player.level().addFreshEntity(npc);
            npc.addTag(PixelSafariForge.NPC_NBT);

            player.sendSystemMessage(UtilChatColour.colour("&e&l(!) &eSpawned NPC."));
        });
    }
}
