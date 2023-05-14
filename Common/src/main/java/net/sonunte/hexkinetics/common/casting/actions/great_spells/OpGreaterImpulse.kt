package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.sonunte.hexkinetics.api.HexKineticsAPI
import kotlin.math.pow

object OpGreaterImpulse : SpellAction {

	override val argc = 3
	private val entityTicks = HashMap<Entity, Int>()
	private var ticks = 0
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = args.getDouble(1, argc)
		val force = args.getVec3(2, argc)
		ctx.assertEntityInRange(target)


		return Triple(
			Spell(target, time, force),
			if (time in 0.0..1.0)
			{
			((force.lengthSqr() + time) * MediaConstants.DUST_UNIT).toInt()
			}else{
				if (time < 0)
				{
					(force.lengthSqr() * MediaConstants.DUST_UNIT).toInt()
				}else
					((force.lengthSqr() + time * 2) * MediaConstants.DUST_UNIT).toInt()
			},
			listOf(ParticleSpray(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),	force.normalize(),0.0,0.1)),
		)
	}

	private data class Spell(val target: Entity, val time: Double, val force: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.isNoGravity = true
			ticks = time.toInt() * 10
			entityTicks[target] = ticks
			tickDownNoGravity(target)
			target.push(force.x, force.y, force.z)
			target.hurtMarked = true //Why!?

		}
	}

	@JvmStatic
	fun tickAllEntities(world: ServerLevel) {
		for (entity in world.allEntities) {
			entityTicks.computeIfAbsent(entity) { 0 }
			tickDownNoGravity(entity)
		}
	}

	@JvmStatic
	fun tickDownNoGravity(target: Entity) {
		val tick = entityTicks[target] ?: ticks

		if (tick > 0) {
			target.resetFallDistance()
			target.push(
				target.deltaMovement.x * 0.205,
				target.deltaMovement.y * -0.01,
				target.deltaMovement.z * 0.205
			)
			target.hurtMarked = true
			entityTicks[target] = tick - 1
		}

		if (tick <= 0) {
			if (tick < 0) {
				entityTicks[target] = 0
			}
			target.isNoGravity = false
		}
	}

}