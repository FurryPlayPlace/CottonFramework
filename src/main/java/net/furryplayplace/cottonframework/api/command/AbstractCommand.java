/*
---------------------------------------------------------------------------------
File Name : AbstractCommand

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.furryplayplace.cottonframework.api.permissions.v1.CottonPermissions;
import net.furryplayplace.cottonframework.api.permissions.v1.Permissible;
import net.furryplayplace.cottonframework.api.permissions.v1.Permission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public abstract class AbstractCommand implements Command<ServerCommandSource>, Predicate<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {
    private final String name;
    private final boolean onlyPlayer;
    private final String permission;

    public abstract int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity sender, String[] args);

    public LiteralCommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, String label) {
        return dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal(label).requires(this).executes(this)
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            if (this.onlyPlayer && !(context.getSource().getEntity() instanceof ServerPlayerEntity)){
                return 0;
            }

            String[] args = context.getInput().substring(this.name.length()+1).split(" ");

            return this.execute(context, context.getSource().getPlayer(), args);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        return null;
    }

    @Override
    public boolean test(ServerCommandSource serverCommandSource) {
        Permissible permissible = CottonPermissions.getPermissible(serverCommandSource.getPlayer());
        if (permissible == null) return false;
        return permissible.hasPermission(Permission.of("cotton.permissions." + this.permission, "Cotton Framework Auto Generated Permissions"));
    }
}