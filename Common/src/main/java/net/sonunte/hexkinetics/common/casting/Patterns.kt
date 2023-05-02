@file:Suppress("unused")

package net.sonunte.hexkinetics.common.casting

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import net.minecraft.resources.ResourceLocation
import net.sonunte.hexkinetics.api.HexKineticsAPI.modLoc
import net.sonunte.hexkinetics.common.casting.actions.OpPixelRaycast
import net.sonunte.hexkinetics.common.casting.actions.OpRoundedEntityLook
import net.sonunte.hexkinetics.common.casting.actions.OpVectorArgument
import net.sonunte.hexkinetics.common.casting.actions.OpVectorReflection
import net.sonunte.hexkinetics.common.casting.actions.spells.OpLesserTeleport
import net.sonunte.hexkinetics.common.casting.actions.spells.OpRotateSpell

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

	@JvmField
	val DIRECTION_LOOK = make(HexPattern.fromAngles("waa", HexDir.EAST), modLoc("direction/const"), OpRoundedEntityLook)
	@JvmField
	val VECTOR_ARGUMENT = make(HexPattern.fromAngles("eeeeeq", HexDir.SOUTH_WEST), modLoc("argument/const"), OpVectorArgument)
	@JvmField
	val PIXEL_RAY = make(HexPattern.fromAngles("weqaqded", HexDir.EAST), modLoc("pixel_cast/ray"), OpPixelRaycast)
	@JvmField
	val VECTOR_REFLECTION = make(HexPattern.fromAngles("qqqqqdqqqqq", HexDir.SOUTH_EAST), modLoc("reflection/const"), OpVectorReflection)
	@JvmField
	val ROTATE_SPELL = make(HexPattern.fromAngles("qqqadeeed", HexDir.EAST), modLoc("rotate/spell"), OpRotateSpell)
	@JvmField
	val LESSER_TELEPORT = make(HexPattern.fromAngles("edqdewqaeaq", HexDir.NORTH_EAST), modLoc("lesser_teleport/spell"), OpLesserTeleport)

	private fun make (pattern: HexPattern, location: ResourceLocation, operator: Action, isPerWorld: Boolean = false): PatternIota {
		val triple = Triple(pattern, location, operator)
		if (isPerWorld)
			PER_WORLD_PATTERNS.add(triple)
		else
			PATTERNS.add(triple)
		return PatternIota(pattern)
	}
}