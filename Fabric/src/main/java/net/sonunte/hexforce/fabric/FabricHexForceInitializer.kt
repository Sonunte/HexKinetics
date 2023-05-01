package net.sonunte.hexforce.fabric

import net.fabricmc.api.ModInitializer
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.sonunte.hexforce.api.HexForceAPI
import net.sonunte.hexforce.common.casting.Patterns
import java.util.function.BiConsumer

object FabricHexForceInitializer : ModInitializer {

    override fun onInitialize() {
        HexForceAPI.LOGGER.info("Hello Fabric World!")

        FabricHexForceConfig.setup()

        initListeners()

        initRegistries()

        Patterns.registerPatterns()
    }

    private fun initListeners() {}

    private fun initRegistries() {}


    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}