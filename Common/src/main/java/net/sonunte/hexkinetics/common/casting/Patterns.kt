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
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpAcceleration
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpMoveBlock
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpZeroG
import net.sonunte.hexkinetics.common.casting.actions.math.*
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
	val IS_VISIBLE = make(HexPattern.fromAngles("aqadwawaw", HexDir.NORTH_WEST), modLoc("visibility/const"), OpVisibilityDistillation)
	@JvmField
	val IS_GRAVITY = make(HexPattern.fromAngles("daad", HexDir.EAST), modLoc("is_gravity/const"), OpGravityPurification)
	@JvmField
	val VECTORS_MULTI = make(HexPattern.fromAngles("awaqawa", HexDir.WEST), modLoc("hadamard/const"), OpVectorComponentMultiplication)
	@JvmField
	val PIXEL_RAY = make(HexPattern.fromAngles("weqaqded", HexDir.EAST), modLoc("pixel/raycast"), OpPixelRaycast)
	@JvmField
	val SECRET = make(HexPattern.fromAngles("qqdeewee", HexDir.SOUTH_EAST), modLoc("get_vehicle/const"), OpSecret)
	@JvmField
	val SECRET_TWO = make(HexPattern.fromAngles("qqdeeaedeaee", HexDir.SOUTH_EAST), modLoc("get_rider/const"), OpSecretTwo)
	@JvmField
	val SECRET_THREE = make(HexPattern.fromAngles("aadedade", HexDir.EAST), modLoc("get_shooter/const"), OpSecretThree)
	@JvmField
	val ROUND_NUM = make(HexPattern.fromAngles("aadeeaa", HexDir.SOUTH_EAST), modLoc("round/const"), OpRoundNumber)
	@JvmField
	val VECTOR_REFLECTION = make(HexPattern.fromAngles("qqqqqdqqqqq", HexDir.SOUTH_EAST), modLoc("reflection/const"), OpVectorReflection)
	@JvmField
	val GET_VECTORS_BY = make(HexPattern.fromAngles("qqqqqeddedq", HexDir.SOUTH_EAST), modLoc("get_vec/const"), OpGetVectorsBy)
	@JvmField
	val GET_VECTORS_FROM = make(HexPattern.fromAngles("qaqeeqaq", HexDir.WEST), modLoc("get_vec_from/const"), OpGetVectorsFrom)
	@JvmField
	val ROTATE_SPELL = make(HexPattern.fromAngles("qqqadeeed", HexDir.EAST), modLoc("rotate/spell"), OpRotateSpell)
	@JvmField
	val ROTATE_TWO_SPELL = make(HexPattern.fromAngles("eeedaqqqa", HexDir.WEST), modLoc("rotate_two/spell"), OpRotateTwoSpell)
	@JvmField
	val ROTATE_BLOCK_SPELL = make(HexPattern.fromAngles("qqqqqaqqqwadeeed", HexDir.SOUTH_EAST), modLoc("rotate_block/spell"), OpRotateBlockSpell)
	@JvmField
	val MOTION_SWAP = make(HexPattern.fromAngles("adaadaqedaddad", HexDir.SOUTH_WEST), modLoc("swap/spell"), OpMomentumSwap)
	@JvmField
	val LESSER_TELEPORT = make(HexPattern.fromAngles("edqdewqaeaq", HexDir.NORTH_EAST), modLoc("lesser_teleport/spell"), OpLesserTeleport)
	@JvmField
	val PLACE_PROJECTILE = make(HexPattern.fromAngles("weeeweede", HexDir.SOUTH_EAST), modLoc("projectile/spell"), OpPlaceProjectile)
	@JvmField
	val ZERO_G = make(HexPattern.fromAngles("wwqqqwadaadawqqqww", HexDir.SOUTH_WEST), modLoc("zero_g/spell"), OpZeroG, true)
	@JvmField
	val ACCELERATION = make(HexPattern.fromAngles("wqeqaaeeeweeeaaqeqqaaq", HexDir.SOUTH_WEST), modLoc("fast/spell"), OpAcceleration, true)
	@JvmField
	val MOVE_BLOCK = make(HexPattern.fromAngles("eeqeeqeeeqeeqdeeqeqqwqqqeeqeqqwqq", HexDir.SOUTH_EAST), modLoc("move_block/spell"), OpMoveBlock, true)

	private fun make (pattern: HexPattern, location: ResourceLocation, operator: Action, isPerWorld: Boolean = false): PatternIota {
		val triple = Triple(pattern, location, operator)
		if (isPerWorld)
			PER_WORLD_PATTERNS.add(triple)
		else
			PATTERNS.add(triple)
		return PatternIota(pattern)
	}
}