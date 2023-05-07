package net.sonunte.hexkinetics.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.sonunte.hexkinetics.api.HexKineticsAPI
import net.sonunte.hexkinetics.common.casting.Patterns
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpGreaterImpulse
import java.util.function.BiConsumer

object FabricYourModInitializer : ModInitializer {

    override fun onInitialize() {
        HexKineticsAPI.LOGGER.info("Hello Fabric World!")

        FabricYourModConfig.setup()

        initListeners()

        initRegistries()

        Patterns.registerPatterns()

        var tickCounter = 0

        ServerTickEvents.END_SERVER_TICK.register { server ->
            if(tickCounter >= 2)
            {
                OpGreaterImpulse.tickAllEntities(server.overworld())
                tickCounter = 0
            }
            tickCounter++

        }
    }

    private fun initListeners() {}

    private fun initRegistries() {}


    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}