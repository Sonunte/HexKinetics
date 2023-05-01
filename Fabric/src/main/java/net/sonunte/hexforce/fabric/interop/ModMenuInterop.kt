package net.sonunte.hexforce.fabric.interop

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import net.sonunte.hexforce.fabric.FabricHexForceConfig

@Environment(EnvType.CLIENT)
class ModMenuInterop : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen -> AutoConfig.getConfigScreen(FabricHexForceConfig::class.java, parent).get() }
    }
}