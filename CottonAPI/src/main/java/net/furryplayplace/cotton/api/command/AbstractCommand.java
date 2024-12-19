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

package net.furryplayplace.cotton.api.command;

import com.mojang.brigadier.context.CommandContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.network.ServerPlayerEntity;

@Getter
@AllArgsConstructor
public abstract class AbstractCommand {
    private final String name;
    private final int permission;

    public abstract int execute(CommandContext<?> context, ServerPlayerEntity sender);
}