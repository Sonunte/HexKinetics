package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

object OpAcceleration : SpellAction {

	override val argc = 3
	private val entityTicks = HashMap<Entity, Int>()
	private var ticks = 0
	override val isGreat = true
	private val cost = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = args.getDouble(1, argc)
		val force = args.getVec3(2, argc)

		ctx.assertEntityInRange(target)


		return Triple(
			Spell(target, time),
			cost,
			listOf(ParticleSpray.burst(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),1.0)),
		)
	}

	private data class Spell(val target: Entity, val time: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ticks = time.toInt() * 10
			entityTicks[target] = ticks
			tickAccelerate(target)

		}
	}

	@JvmStatic
	fun tickDownAllEntities(world: ServerLevel) {
		for (entity in world.allEntities) {
			entityTicks.computeIfAbsent(entity) { 0 }
			tickAccelerate(entity)
		}
	}

	@JvmStatic
	fun tickAccelerate(target: Entity) {
		val tick = entityTicks[target] ?: ticks

		if (tick > 0) {
			target.resetFallDistance()
			target.push(
				target.deltaMovement.x * 0.2055,
				target.deltaMovement.y * 0.04,
				target.deltaMovement.z * 0.2055
			)
			target.hurtMarked = true
			entityTicks[target] = tick - 1
		}

		if (tick <= 0) {
			if (tick < 0) {
				entityTicks[target] = 0
			}
		}
	}

}