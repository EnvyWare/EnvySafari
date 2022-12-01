package com.envyful.pixel.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.pixel.forge.PixelSafariForge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "reload",
        description = "Reloads the config"
)
@Permissible("pixel.safari.command.reload")
@Child
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity player, String[] args) {
        PixelSafariForge.getInstance().loadConfig();
        player.sendMessage(new StringTextComponent("Reloaded config"), Util.NIL_UUID);
    }
}
