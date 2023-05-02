package net.sonunte.hexkinetics.forge

import net.minecraftforge.event.RegisterGameTestsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.sonunte.hexkinetics.api.HexKineticsAPI

@EventBusSubscriber(modid = HexKineticsAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public object HexKineticsGameTests {
	@SubscribeEvent
	public fun registerTests(event: RegisterGameTestsEvent) {
		HexKineticsAPI.LOGGER.debug("registering tests")
		event.register(ExampleTests::class.java)
	}
}