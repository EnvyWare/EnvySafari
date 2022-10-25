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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

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
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        UtilForgeConcurrency.runSync(() -> {
            NPCChatting npc = new NPCChatting(player.world);

            npc.setPosition(player.posX, player.posY, player.posZ);
            npc.setNoAI(true);
            npc.setAIMode(EnumTrainerAI.StandStill);
            npc.setAIMoveSpeed(0.0f);
            npc.addTag(PixelSafariForge.NPC_NBT);
            npc.enablePersistence();
            npc.initAI();
            npc.init(PixelSafariForge.getInstance().getConfig().getNpcSkin());

            player.world.spawnEntity(npc);
            npc.getEntityData().setBoolean(PixelSafariForge.NPC_NBT, true);

            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                    "&e&l(!) &eSpawned NPC.")));
        });
    }
}
