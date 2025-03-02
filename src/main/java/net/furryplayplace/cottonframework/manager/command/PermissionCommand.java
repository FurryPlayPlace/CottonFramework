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
import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.permissions.v1.CottonPermissions;
import net.furryplayplace.cottonframework.api.permissions.v1.Permissible;
import net.furryplayplace.cottonframework.configuration.PermissionConfiguration;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class PermissionCommand implements com.mojang.brigadier.Command<ServerCommandSource>, Predicate<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

    public LiteralCommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, String label) {
        return dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal(label).requires(this).executes(this)
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

        List<String> results = new ArrayList<>();

        String input = builder.getInput();
        int spaces = input.length() - input.replaceAll(" ", "").length();
        String[] cmds = input.split(" ");
        if (cmds.length <= 1) {
            results.add("creategroup");
            results.add("listgroups");
            results.add("user");
            results.add("group");
        } else {
            if (cmds[1].equalsIgnoreCase("user")) {
                if (spaces == 2) {
                    for (String plr : context.getSource().getServer().getPlayerManager().getPlayerNames())
                        results.add(plr);
                } else if (spaces == 3){
                    results.add("info");
                    results.add("group");
                    results.add("permissions");
                } else if (cmds.length >= 4) {
                    if (cmds[3].equalsIgnoreCase("group")) {
                        if (spaces == 4) {
                            results.add("add");
                            results.add("remove");
                        } else if (cmds.length <= 5) {
                            results.addAll(CottonFramework.groups.keySet());
                        }
                    } else if (cmds[3].equalsIgnoreCase("permissions")) {
                        if (spaces == 4) {
                            results.add("set");
                        } else {
                            if (spaces == 5) {
                                results.add("PERMISSION");
                            } else {
                                results.add("true");
                                results.add("false");
                            }
                        }
                    }
                }
            } else if (cmds[1].equalsIgnoreCase("group")) {
                if (spaces == 2) {
                    results.addAll(CottonFramework.groups.keySet());
                } else if (spaces == 3){
                    results.add("info");
                    results.add("parentgroups");
                    results.add("permissions");
                } else if (cmds.length >= 4){
                    if (cmds[3].equalsIgnoreCase("parentgroups")) {
                        if (cmds.length <= 4) {
                            results.add("add");
                            results.add("remove");
                        } else if (cmds.length <= 5) {
                            results.addAll(CottonFramework.groups.keySet());
                        }
                    } else if (cmds[3].equalsIgnoreCase("permissions")) {
                        if (cmds.length <= 4) {
                            results.add("set");
                        } else {
                            if (cmds.length <= 5) {
                                results.add("PERMISSION");
                            } else {
                                results.add("true");
                                results.add("false");
                            }
                        }
                    }
                }
            }
        }

        boolean should = true;
        for (String s : results) if (input.endsWith(" " + s)) should = false;
        if (should) for (String s : results)
            builder.suggest(s);

        return builder.buildFuture();
    }

    @Override
    public boolean test(ServerCommandSource t) {
        return t.hasPermissionLevel(3); // Player is operator
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource cs = context.getSource();

        try {
            Permissible permissible = CottonPermissions.getPermissible(cs);
            if (!permissible.h5$isHighLevelOperator()) {
                sendMessage(cs, Formatting.RED, "This command requires operator permission.");
                return 0;
            }
            String[] args = context.getInput().split(" ");
            if (args.length <= 1) {
                sendMessage(cs, null, "Invalid arguments!");
                return 0;
            }

            if (args[1].equalsIgnoreCase("creategroup")) {
                if (!CottonFramework.groups.containsKey(args[2])) {
                    try {
                        PermissionConfiguration conf = new PermissionConfiguration(args[2]);
                        CottonFramework.groups.put(conf.name, conf);
                        sendMessage(cs, null, "Created group: \"" + args[2] + "\"!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (args[1].equalsIgnoreCase("listgroups")) {
                sendMessage(cs, Formatting.BLUE, "List of Groups:");
                for (String group : CottonFramework.groups.keySet())
                    sendMessage(cs, null, group);
            }

            if (args[1].equalsIgnoreCase("user")) {
                String[] argz0 = context.getInput().split(args[0] + " " + args[1] + " ");
                if (argz0.length <= 1) {
                    sendMessage(cs, null, "Invalid arguments!");
                    return 0;
                }
                String[] argz = argz0[1].split(" ");
                PermissionConfiguration e = CottonFramework.getUser(CottonFramework.findGameProfile(cs, argz[0]));
                if (argz.length <= 0) {
                    sendMessage(cs, null, "Usage: /perms user <user> <info|permissions|group|has>");
                    return 0;
                }

                if (argz[1].equalsIgnoreCase("group")) {
                    if (argz[2].equalsIgnoreCase("add")) {
                        e.parentGroups.add(argz[3]);
                        sendMessage(cs, null, "Group added!");
                    }
                    if (argz[2].equalsIgnoreCase("remove")) {
                        e.parentGroups.remove(argz[3]);
                        sendMessage(cs, null, "Group removed!");
                    }
                    e.save();
                }
                if (argz[1].equalsIgnoreCase("info")) {
                    sendMessage(cs, Formatting.GREEN, "Info for user \"" + e.name + "\":");
                    sendMessage(cs, Formatting.GREEN, "UUID: " + e.uuid);
                    sendMessage(cs, Formatting.DARK_GREEN, "parentGroups:");
                    for (String s : e.parentGroups)
                        sendMessage(cs, Formatting.GREEN, "- " + s);

                    sendMessage(cs, Formatting.DARK_GREEN, "User permissions:");
                    for (String s : e.permissionsList)
                        sendMessage(cs, Formatting.GREEN, "- " + s);
                    for (String s : e.negPermissions)
                        sendMessage(cs, Formatting.GREEN, "- -" + s);
                    return 0;
                }
                if (argz[1].equalsIgnoreCase("permissions")) {
                    if (argz[2].equalsIgnoreCase("set")) {
                        String perm = argz[3];
                        String value = argz[4];
                        e.setPermission(perm, Boolean.valueOf(value));
                        sendMessage(cs, null, "Permission \"" + perm + "\" set to " + value + " for user " + argz[0]);
                    }
                }
            }

            if (args[1].equalsIgnoreCase("group")) {
                String[] argz0 = context.getInput().split(args[0] + " " + args[1] + " ");
                if (argz0.length <= 1) {
                    sendMessage(cs, null, "Invalid arguments!");
                    return 0;
                }
                String[] argz = argz0[1].split(" ");
                PermissionConfiguration e = CottonFramework.groups.get(argz[0]);
                if (argz.length <= 0) {
                    sendMessage(cs, null, "Usage: /perms group <group> <info|permissions|parentgroups>");
                    return 0;
                }
                if (argz[1].equalsIgnoreCase("info")) {
                    sendMessage(cs, Formatting.GREEN, "Info for group \"" + e.name + "\":");
                    sendMessage(cs, Formatting.GREEN, "Name: " + e.name);
                    sendMessage(cs, Formatting.DARK_GREEN, "parentGroups:");
                    for (String s : e.parentGroups)
                        sendMessage(cs, Formatting.GREEN, "- " + s);

                    sendMessage(cs, Formatting.DARK_GREEN, "Group permissions:");
                    for (String s : e.permissionsList)
                        sendMessage(cs, Formatting.GREEN, "- " + s);
                    for (String s : e.negPermissions)
                        sendMessage(cs, Formatting.GREEN, "- -" + s);
                    return 0;
                }
                if (argz[1].equalsIgnoreCase("permissions")) {
                    if (argz[2].equalsIgnoreCase("set")) {
                        String perm = argz[3];
                        String value = argz[4];
                        e.setPermission(perm, Boolean.valueOf(value));
                        sendMessage(cs, null, "Permission \"" + perm + "\" set to " + value + " for group " + argz[0]);
                    }
                }

                if (argz[1].equalsIgnoreCase("parentgroups")) {
                    if (argz[2].equalsIgnoreCase("add")) {
                        e.parentGroups.add(argz[3]);
                        sendMessage(cs, null, "Group added!");
                    }
                    if (argz[2].equalsIgnoreCase("remove")) {
                        e.parentGroups.remove(argz[3]);
                        sendMessage(cs, null, "Group removed!");
                    }
                    e.save();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Text colored_literal(String txt, Formatting color) {
       return Text.of(color + txt);
    }

    public void sendMessage(ServerCommandSource cs, Formatting color, String message) {
        cs.getPlayer().sendMessage(colored_literal(message, color), false);
    }

}