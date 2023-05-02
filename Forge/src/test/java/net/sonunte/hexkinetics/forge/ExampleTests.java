package net.sonunte.hexkinetics.forge;

import net.minecraft.gametest.framework.*;
import net.sonunte.hexkinetics.api.HexKineticsAPI;

public class ExampleTests {
	@GameTest(templateNamespace = HexKineticsAPI.MOD_ID, template = "basic")
	public static void exampleTest(GameTestHelper helper) {
		HexKineticsAPI.LOGGER.debug("running example test");
		
		helper.onEachTick(() -> {
			HexKineticsAPI.LOGGER.debug("current tick: " + helper.getTick());
		});
	}
}
