/*
---------------------------------------------------------------------------------
File Name : TestCommand

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottontestplugin.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.furryplayplace.cottonframework.api.command.AbstractCommand;
import net.furryplayplace.cottontestplugin.Cottontestplugin;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.HashMap;

public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test-command", 0);
    }

    @Override
    public <T> HashMap<String, ArgumentType<T>> arguments() {
        return new HashMap<>();
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity sender, String[] args) {

        context.getSource().sendFeedback(() -> Text.of("Test command executed!"), false);
        context.getSource().sendFeedback(() -> Text.of( "Test config: " + Cottontestplugin.getInstance().getConfig().getString("test.login", "vakea")), false);

        return 1;
    }
}