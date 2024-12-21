/*
---------------------------------------------------------------------------------
File Name : PermissionCommand

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 21.12.2024
Last Modified : 21.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static net.furryplayplace.cottonframework.CottonFramework.MOD_AUTHOR;
import static net.furryplayplace.cottonframework.CottonFramework.MOD_VERSION;

public class AboutCommand implements com.mojang.brigadier.Command<ServerCommandSource>, Predicate<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

    public LiteralCommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, String label) {
        return dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal(label).requires(this).executes(this)
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return builder.buildFuture();
    }

    @Override
    public boolean test(ServerCommandSource t) {
        return true;
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(() -> Text.literal(Formatting.GRAY + "CottonFramework v" + MOD_VERSION), false);
        context.getSource().sendFeedback(() -> Text.literal(Formatting.GRAY + "Developed by " + MOD_AUTHOR), false);
        context.getSource().sendFeedback(() -> Text.literal(Formatting.GRAY + "Visit https://github.com/FurryPlayPlace/CottonFramework for more information"), false);
        return 0;
    }
}