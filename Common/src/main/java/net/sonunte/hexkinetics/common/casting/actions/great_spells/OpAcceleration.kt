package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

object OpAcceleration : SpellAction {

	override val argc = 3
	private val entityFastTicks = HashMap<Entity, Int>()
	private val entityWaitTicks = HashMap<Entity, Int>()

	private var supertime = 0
	override val isGreat = true

	private var speed = Vec3(0.0, 0.0, 0.0)

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = Mth.clamp(args.getDouble(1, argc), 0.0, 200.0)
		val force = args.getVec3(2, argc)

		val cost = if(force.length() >= 1)
		{
			(force.lengthSqr() * time * MediaConstants.DUST_UNIT).toInt()
		}else if (force.length() > 0)
		{
			MediaConstants.DUST_UNIT * time.toInt()
		}else
		{
			0
		}

		ctx.assertEntityInRange(target)


		return Triple(
			Spell(target, time, force),
			cost,
			listOf(ParticleSpray.burst(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),1.0)),
		)
	}

	private data class Spell(val target: Entity, val time: Double, val force: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			if (entityFastTicks.containsKey(target))
				return   // don't change the already set propulsion
			supertime = time.toInt() * 5 + 5
			entityFastTicks[target] = supertime
			entityWaitTicks[target] = 0
			speed = force
			tickAccelerate(target, force, supertime)

		}
	}

	@JvmStatic
	fun tickAcceleratedEntities() {
		for ((entity, ticks) in entityFastTicks) {
			if (!entity.isRemoved) {
				tickAccelerate(entity, speed, ticks)
			} else {
				entityFastTicks.remove(entity)
				entityWaitTicks.remove(entity)
			}
		}
	}

	@JvmStatic
	fun tickAccelerate(target: Entity, force: Vec3, ticks: Int) {
		if (ticks > 0) {
			val wait = entityWaitTicks[target] ?: 0

			if (wait >= 0) {
				if (wait == 5)
				{
					target.push(force.x, force.y, force.z)
					target.hurtMarked = true
				}
				entityWaitTicks[target] = wait + 1
			}
			entityFastTicks[target] = ticks - 1

			if (wait < 0 || wait > 5) {
				entityWaitTicks[target] = 0
			}
		} else {
			entityFastTicks.remove(target)
			entityWaitTicks.remove(target)
		}
	}


}