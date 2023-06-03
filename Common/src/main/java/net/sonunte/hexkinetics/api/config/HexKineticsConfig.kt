package net.sonunte.hexkinetics.api.config

import at.petrak.hexcasting.api.HexAPI
import net.minecraft.resources.ResourceLocation

object HexKineticsConfig {
    interface CommonConfigAccess { }

    interface ClientConfigAccess { }

    interface ServerConfigAccess {
        val moveTileEntities: Boolean
        val exampleConstActionCost: Int

        fun isTranslocationAllowed(blockId: ResourceLocation): Boolean


        companion object {
            const val DEF_MIN_COST = 0.0001
            const val DEF_MAX_COST = 10_000.0

            // default settings for Greater Translocation
            const val DEFAULT_MOVE_TILE_ENTITIES = false
            val DEFAULT_TRANSLOCATION_DENY_LIST: List<String> = listOf("minecraft:end_portal", "minecraft:end_portal_frame", "minecraft:end_gateway", "minecraft:nether_portal", "minecraft:command_block", "minecraft:chain_command_block", "minecraft:repeating_command_block", "minecraft:barrier")
        }
    }

    // Simple extensions for resource location configs
    fun anyMatch(keys: List<String>, key: ResourceLocation): Boolean {
        for (s in keys) {
            if (ResourceLocation.isValidResourceLocation(s)) {
                val rl = ResourceLocation(s)
                if (rl == key) {
                    return true
                }
            }
        }
        return false
    }

    fun noneMatch(keys: List<String>, key: ResourceLocation): Boolean {
        return !anyMatch(keys, key)
    }

    private object DummyCommon : CommonConfigAccess {  }
    private object DummyClient : ClientConfigAccess {  }
    private object DummyServer : ServerConfigAccess {
        override val moveTileEntities: Boolean
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
        override val exampleConstActionCost: Int
            get() = throw IllegalStateException("Attempted to access property of Dummy Config Object")
        override fun isTranslocationAllowed(blockId: ResourceLocation): Boolean {
            throw IllegalStateException("Attempted to access property of Dummy Config Object")
        }
    }

    @JvmStatic
    var common: CommonConfigAccess = DummyCommon
        set(access) {
            if (field != DummyCommon) {
                HexAPI.LOGGER.warn("CommonConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }

    @JvmStatic
    var client: ClientConfigAccess = DummyClient
        set(access) {
            if (field != DummyClient) {
                HexAPI.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }

    @JvmStatic
    var server: ServerConfigAccess = DummyServer
        set(access) {
            if (field != DummyServer) {
                HexAPI.LOGGER.warn("ServerConfigAccess was replaced! Old {} New {}",
                        field.javaClass.name, access.javaClass.name)
            }
            field = access
        }
}