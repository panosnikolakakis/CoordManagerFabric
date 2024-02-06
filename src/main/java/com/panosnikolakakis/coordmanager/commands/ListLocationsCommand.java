package com.panosnikolakakis.coordmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ListLocationsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("locationlist")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("locationlist command triggered"), false);
                            return 1;
                        })
        );
    }
}
