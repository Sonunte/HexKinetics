@file:Suppress("unused")

package net.sonunte.hexforce.common.casting

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import net.minecraft.resources.ResourceLocation
import net.sonunte.hexforce.api.HexForceAPI.modLoc
import net.sonunte.hexforce.common.casting.actions.OpExampleConstMediaAction
import net.sonunte.hexforce.common.casting.actions.spells.OpExampleSpellAction

object Patterns {

	@JvmField
	var PATTERNS: MutableList<Triple<HexPattern, ResourceLocation, Action>> = ArrayList()
	@JvmField
	var PER_WORLD_PATTERNS: MutableList<Triple<HexPattern, ResourceLocation, Action>> = ArrayList()

	@JvmStatic
	fun registerPatterns() {
		try {
			for ((pattern, location, action) in PATTERNS) {
				PatternRegistry.mapPattern(pattern, location, action)
			}
			for ((pattern, location, action) in PER_WORLD_PATTERNS) {
				PatternRegistry.mapPattern(pattern, location, action, true)
			}
		} catch (e: PatternRegistry.RegisterPatternException) {
			e.printStackTrace()
		}
	}

	// ========================== Examples =================================
	@JvmField
	val EXAMPLE_CONST = make(HexPattern.fromAngles("qawde", HexDir.SOUTH_WEST), modLoc("example/const"), OpExampleConstMediaAction)
	@JvmField
	val EXAMPLE_SPELL = make(HexPattern.fromAngles("awd", HexDir.SOUTH_WEST), modLoc("example/spell"), OpExampleSpellAction)

	private fun make (pattern: HexPattern, location: ResourceLocation, operator: Action, isPerWorld: Boolean = false): PatternIota {
		val triple = Triple(pattern, location, operator)
		if (isPerWorld)
			PER_WORLD_PATTERNS.add(triple)
		else
			PATTERNS.add(triple)
		return PatternIota(pattern)
	}
}