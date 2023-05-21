@file:Suppress("unused")

package net.sonunte.hexkinetics.common.casting

import at.petrak.hexcasting.api.PatternRegistry
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import net.minecraft.resources.ResourceLocation
import net.sonunte.hexkinetics.api.HexKineticsAPI.modLoc
import net.sonunte.hexkinetics.common.casting.actions.*
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpGreaterImpulse
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpMoveBlock
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpStop
import net.sonunte.hexkinetics.common.casting.actions.spells.*

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
	val IS_GRAVITY = make(HexPattern.fromAngles("daad", HexDir.EAST), modLoc("is_gravity/const"), OpGravityPurification)
	@JvmField
	val VECTOR_ARGUMENT = make(HexPattern.fromAngles("eeeeeq", HexDir.SOUTH_WEST), modLoc("argument/const"), OpVectorArgument)
	@JvmField
	val VECTORS_MULTI = make(HexPattern.fromAngles("awaqawa", HexDir.WEST), modLoc("hadamard/const"), OpVectorComponentMultiplication)
	@JvmField
	val PIXEL_RAY = make(HexPattern.fromAngles("weqaqded", HexDir.EAST), modLoc("pixel/raycast"), OpPixelRaycast)
	@JvmField
	val ROUND_NUM = make(HexPattern.fromAngles("aadeeaa", HexDir.SOUTH_EAST), modLoc("round/const"), OpRoundNumber)
	@JvmField
	val VECTOR_REFLECTION = make(HexPattern.fromAngles("qqqqqdqqqqq", HexDir.SOUTH_EAST), modLoc("reflection/const"), OpVectorReflection)
	@JvmField
	val ROTATE_SPELL = make(HexPattern.fromAngles("qqqadeeed", HexDir.EAST), modLoc("rotate/spell"), OpRotateSpell)
	@JvmField
	val ROTATE_TWO_SPELL = make(HexPattern.fromAngles("eeedaqqqa", HexDir.WEST), modLoc("rotate_two/spell"), OpRotateTwoSpell)
	@JvmField
	val MOTION_SWAP = make(HexPattern.fromAngles("adaadaqedaddad", HexDir.SOUTH_WEST), modLoc("swap/spell"), OpMomentumSwap)
	@JvmField
	val LESSER_TELEPORT = make(HexPattern.fromAngles("edqdewqaeaq", HexDir.NORTH_EAST), modLoc("lesser_teleport/spell"), OpLesserTeleport)
	@JvmField
	val ADD_GRAVITY = make(HexPattern.fromAngles("aadedade", HexDir.WEST), modLoc("add_gravity/spell"), OpAddGravity)
	@JvmField
	val GREATER_IMPULSE = make(HexPattern.fromAngles("wqeqaaeeeweeeaaqeqqaaq", HexDir.SOUTH_WEST), modLoc("greater_impulse/spell"), OpGreaterImpulse, true)
	@JvmField
	val STOP = make(HexPattern.fromAngles("wwawawwwawawwdwdwwdw", HexDir.SOUTH_EAST), modLoc("stop/spell"), OpStop, true)
	@JvmField
	val MOVE_BLOCK = make(HexPattern.fromAngles("eeqeeqeeeqeeqdeeqeqqwqqqeeqeqqwqq", HexDir.SOUTH_EAST), modLoc("greater_translocation/spell"), OpMoveBlock, true)
	@JvmField
	val SECRET = make(HexPattern.fromAngles("wadadwwdwwdqadwwed", HexDir.NORTH_EAST), modLoc("secret"), OpSecret)

	private fun make (pattern: HexPattern, location: ResourceLocation, operator: Action, isPerWorld: Boolean = false): PatternIota {
		val triple = Triple(pattern, location, operator)
		if (isPerWorld)
			PER_WORLD_PATTERNS.add(triple)
		else
			PATTERNS.add(triple)
		return PatternIota(pattern)
	}
}