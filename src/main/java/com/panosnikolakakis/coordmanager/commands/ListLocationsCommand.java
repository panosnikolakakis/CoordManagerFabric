package com.panosnikolakakis.coordmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ListLocationsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("locationlist")
                        .executes(context -> {
                            List<String> lines = getAllLocationLines();

                            if (!lines.isEmpty()) {
                                Text message = Text.literal("Saved locations:");
                                context.getSource().sendFeedback(() -> message, false);

                                for (String line : lines) {
                                    String[] parts = line.split(":", 3);
                                    if (parts.length == 3) {
                                        String locationName = parts[0].trim();
                                        String dimension = parts[1].trim();
                                        String coordinates = parts[2].trim().replace(" ", ", ");

                                        Text locationMessage = Text.literal("- ")
                                                .append(Text.literal(locationName).setStyle(Style.EMPTY.withFormatting(Formatting.GOLD)))
                                                .append(": " + dimension + ": " + coordinates);

                                        context.getSource().sendFeedback(() -> locationMessage, false);
                                    }
                                }
                            } else {
                                Text message = Text.literal("No locations saved yet.");
                                context.getSource().sendFeedback(() -> message, false);
                            }

                            return 1;
                        })
        );
    }

    private static List<String> getAllLocationLines() {
        try {
            String filePath = "locations.txt";
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}