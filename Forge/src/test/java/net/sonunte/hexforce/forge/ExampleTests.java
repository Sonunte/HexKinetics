package net.sonunte.hexforce.forge;

import net.minecraft.gametest.framework.*;
import net.sonunte.hexforce.api.HexForceAPI;

public class ExampleTests {
	@GameTest(templateNamespace = HexForceAPI.MOD_ID, template = "basic")
	public static void exampleTest(GameTestHelper helper) {
		HexForceAPI.LOGGER.debug("running example test");
		
		helper.onEachTick(() -> {
			HexForceAPI.LOGGER.debug("current tick: " + helper.getTick());
		});
	}
}
