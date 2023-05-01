package net.sonunte.hexforce.forge

import net.minecraftforge.event.RegisterGameTestsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.sonunte.hexforce.api.HexForceAPI

@EventBusSubscriber(modid = HexForceAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public object HexForceGameTests {
	@SubscribeEvent
	public fun registerTests(event: RegisterGameTestsEvent) {
		HexForceAPI.LOGGER.debug("registering tests")
		event.register(ExampleTests::class.java)
	}
}