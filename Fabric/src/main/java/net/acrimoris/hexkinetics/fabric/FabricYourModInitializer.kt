package net.acrimoris.hexkinetics.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.acrimoris.hexkinetics.api.HexKineticsAPI
import net.acrimoris.hexkinetics.common.casting.Patterns
import net.acrimoris.hexkinetics.common.casting.actions.great_spells.OpAcceleration
import net.acrimoris.hexkinetics.common.casting.actions.great_spells.OpZeroG
import java.util.function.BiConsumer

object FabricYourModInitializer : ModInitializer {

    override fun onInitialize() {
        HexKineticsAPI.LOGGER.info("Hello Fabric World!")

        FabricYourModConfig.setup()

        initListeners()

        initRegistries()

        Patterns.registerPatterns()

        ServerTickEvents.END_SERVER_TICK.register {
            OpZeroG.tickZeroGEntities()
            OpAcceleration.tickAcceleratedEntities()
        }

        ServerEntityEvents.ENTITY_UNLOAD.register { entity, world ->
            OpZeroG.unloadZeroGEntity(entity)
        }
    }

    private fun initListeners() {}

    private fun initRegistries() {}


    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}