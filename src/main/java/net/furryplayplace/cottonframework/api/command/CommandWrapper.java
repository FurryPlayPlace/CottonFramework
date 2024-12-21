/*
---------------------------------------------------------------------------------
File Name : CommandWrapper

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 21.12.2024
Last Modified : 21.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.regex.Pattern;


@AllArgsConstructor
@Getter
public class CommandWrapper implements Command<ServerCommandSource>, Predicate<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    private final String name;
    private final AbstractCommand command;

    public String name() {
        return command.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    public LiteralCommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, String label) {
        return dispatcher.register(
                LiteralArgumentBuilder.<ServerCommandSource>literal(label).requires(this).executes(this)
                        .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        try {
            if (command.onlyPlayer && !(context.getSource().getEntity() instanceof ServerPlayerEntity)){
                context.getSource().getEntity().sendSystemMessage(new LiteralText("Player only command!"), UUID.randomUUID());
                return 0;
            }
            String[] args = PATTERN_ON_SPACE.split(context.getInput());
            String sentCommandLabel = args[0].toLowerCase();

            PlayerEntity player = commandContext.getSource().getPlayer();


            User user = context.getSource().getEntity() != null ? ex.getUserByName(context.getSource().getName()) : BssentialsMod.CONSOLE_USER;
            if (!ex.hasPerm(user, name)){
                user.sendMessage("&4No permission for command.");
                return 0;
            }

            return ex.onCommand(user, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length)) ? 1 : 0;
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
        return true;
    }
}