package com.panosnikolakakis.coordmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeleteLocationCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("locationdelete")
                        .then(CommandManager.argument("LocationName", StringArgumentType.greedyString())
                                .suggests(getLocationSuggestions())
                                .executes(context -> {
                                    String locationName = StringArgumentType.getString(context, "LocationName");

                                    if (deleteLocationFromFile(locationName)) {
                                        Text message = Text.literal("Location '")
                                                .append(Text.literal(locationName).setStyle(Style.EMPTY.withFormatting(Formatting.GOLD)))
                                                .append("' deleted.");

                                        context.getSource().sendFeedback(() -> message, false);
                                        return 1;
                                    } else {
                                        Text message = Text.literal("Location '")
                                                .append(Text.literal(locationName).setStyle(Style.EMPTY.withFormatting(Formatting.GOLD)))
                                                .append("' not found.");

                                        context.getSource().sendFeedback(() -> message, false);
                                        return 0;
                                    }
                                })
                        )
        );
    }

    private static boolean deleteLocationFromFile(String locationName) {
        try {
            String filePath = "locations.txt";

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Check if the location exists and remove it
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(locationName + ":")) {
                    lines.remove(i);
                    // Write the updated content back to the file
                    Files.write(Paths.get(filePath), lines);
                    return true; // Location found and deleted
                }
            }

            return false; // Location not found
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static SuggestionProvider<ServerCommandSource> getLocationSuggestions() {
        return (context, builder) -> {
            List<String> suggestions = getLocationNames();
            return CommandSource.suggestMatching(suggestions, builder);
        };
    }

    private static List<String> getLocationNames() {
        List<String> names = new ArrayList<>();
        try {
            String filePath = "locations.txt";
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length > 0) {
                    names.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
}