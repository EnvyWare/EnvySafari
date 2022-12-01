package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.world.UtilWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "world",
        description = "Gets the world name"
)
@Permissible("pixel.safari.command.world")
@Child
public class WorldInfoCommand {

    @CommandProcessor()
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        player.sendMessage(new StringTextComponent(UtilWorld.getName(player.getLevel())), Util.NIL_UUID);
    }
}
