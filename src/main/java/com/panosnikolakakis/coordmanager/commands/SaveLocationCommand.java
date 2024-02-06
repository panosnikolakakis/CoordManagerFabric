package com.panosnikolakakis.coordmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SaveLocationCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("locationsave")
                        .then(CommandManager.argument("LocationName", StringArgumentType.greedyString())
                            .executes(context -> {
                                String locationName = StringArgumentType.getString(context, "LocationName");

                                BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
                                Identifier dimensionId = context.getSource().getPlayer().getEntityWorld().getRegistryKey().getValue();

                                saveLocationToFile(locationName, playerPos, dimensionId);

                                Text message = Text.literal("Location '")
                                        .append(Text.literal(locationName).setStyle(Style.EMPTY.withFormatting(Formatting.GOLD)))
                                        .append("' saved at " + getDimensionName(dimensionId) + " coordinates: X " + playerPos.getX() + ", Y " + playerPos.getY() + ", Z " + playerPos.getZ());

                                context.getSource().sendFeedback(() -> message, false);
                                return 1;
                            })
                        )
        );
    }

    private static void saveLocationToFile(String locationName, BlockPos playerPos, Identifier dimensionId) {
        try {
            String filePath = "locations.txt";

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Update the existing location if it already exists
            boolean locationExists = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(locationName + ":")) {
                    lines.set(i, locationName + ": " + getDimensionName(dimensionId) + ": " + playerPos.getX() + " " + playerPos.getY() + " " + playerPos.getZ());
                    locationExists = true;
                    break;
                }
            }

            // If the location doesn't exist, add a new line
            if (!locationExists) {
                lines.add(locationName + ": " + getDimensionName(dimensionId) + ": " + playerPos.getX() + " " + playerPos.getY() + " " + playerPos.getZ());
            }

            // Write the updated content back to the file
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDimensionName(Identifier dimensionId) {
        String dimensionName = dimensionId.getPath();

        switch (dimensionName) {
            case "overworld":
                return Formatting.GREEN + "Overworld" + Formatting.RESET;
            case "the_nether":
                return Formatting.RED + "Nether" + Formatting.RESET;
            case "the_end":
                return Formatting.DARK_PURPLE + "The End" + Formatting.RESET;
            default:
                return dimensionName;
        }
    }
}