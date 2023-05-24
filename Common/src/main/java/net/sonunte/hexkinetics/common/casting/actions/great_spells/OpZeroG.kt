package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

object OpZeroG : SpellAction {

	override val argc = 2
	private val entityTicks = HashMap<Entity, Int>()
	private var ticks = 0
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = args.getDouble(1, argc)

		ctx.assertEntityInRange(target)


		return Triple(
			Spell(target, time),
			if (time in 0.0..1.0){
				time.toInt() * MediaConstants.DUST_UNIT
			}else{
				if (time < 0)
				{
					0
				}else
					(time * 2).toInt() * MediaConstants.DUST_UNIT
			},
			listOf(ParticleSpray.burst(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),1.0)),
		)
	}

	private data class Spell(val target: Entity, val time: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			ticks = time.toInt() * 20
			entityTicks[target] = ticks
			target.isNoGravity = true
			target.hurtMarked = true //Why!?
			tickDownNoGravity(target)

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
			target.isNoGravity = false
		}
	}

}