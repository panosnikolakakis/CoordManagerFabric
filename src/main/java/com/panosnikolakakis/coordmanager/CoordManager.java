package com.panosnikolakakis.coordmanager;

import com.panosnikolakakis.coordmanager.commands.DeleteLocationCommand;
import com.panosnikolakakis.coordmanager.commands.FindLocationCommand;
import com.panosnikolakakis.coordmanager.commands.ListLocationsCommand;
import com.panosnikolakakis.coordmanager.commands.SaveLocationCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordManager implements ModInitializer {
	public static final String MOD_ID = "coordmanager";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("CoordManager loaded.");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SaveLocationCommand.register(dispatcher);
			DeleteLocationCommand.register(dispatcher);
			FindLocationCommand.register(dispatcher);
			ListLocationsCommand.register(dispatcher);
		});
	}
}