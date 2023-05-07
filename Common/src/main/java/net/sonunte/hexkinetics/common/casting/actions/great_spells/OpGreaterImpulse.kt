package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

object OpGreaterImpulse : SpellAction {

	override val argc = 3
	private var ticks = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = args.getDouble(1, argc)
		val force = args.getVec3(2, argc)
		ctx.assertEntityInRange(target)
		var cost = ((force.lengthSqr() + time.pow(2)) * MediaConstants.DUST_UNIT).toInt()

		return Triple(
			Spell(target, time, force),
			cost,
			listOf(ParticleSpray(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),	force.normalize(),0.0,0.1)),
		)
	}

	private data class Spell(val target: Entity, val time: Double, val force: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.isNoGravity = true
			ticks = time.toInt() * 10
			tickDownNoGravity(target)
			target.push(force.x, force.y, force.z)
			target.hurtMarked = true //Why!?

		}
	}

	@JvmStatic
	fun tickAllEntities(world: ServerLevel) {
		for (entity in world.allEntities) {
			tickDownNoGravity(entity)
		}
	}
	@JvmStatic
	fun tickDownNoGravity(target: Entity) {

		var delayCount = ticks

		if (delayCount > 0) {
			delayCount--
			ticks--
			target.resetFallDistance()
			target.push(target.deltaMovement.normalize().x * 0.1, target.deltaMovement.normalize().y * -0.05, target.deltaMovement.normalize().z * 0.1)
			target.hurtMarked = true
		}
		if (delayCount.toInt() <= 0) {
			target.isNoGravity = false

		}
	}
}