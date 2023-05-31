package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

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
				(time * 2).toInt() * MediaConstants.DUST_UNIT
			},
			listOf(ParticleSpray.burst(target.position().add(0.0, target.eyeHeight / 2.0, 0.0),1.0)),
		)
	}

	private data class Spell(val target: Entity, val time: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			if (!target.isNoGravity || entityTicks.containsKey(target)) {
				ticks = time.toInt() * 20
				entityTicks[target] = ticks
				target.isNoGravity = true
				target.hurtMarked = true //Why!?
				tickDownNoGravity(target, ticks)
			}
		}
	}

	@JvmStatic
	fun tickZeroGEntities() {
		for ((entity, ticks) in entityTicks) {
			if (!entity.isRemoved) {
				tickDownNoGravity(entity, ticks)
			} else {
				entityTicks.remove(entity)
			}
		}
	}

	@JvmStatic
	fun unloadZeroGEntity(entity: Entity) {
		if (entityTicks.remove(entity) != null) {
			entity.isNoGravity = false;
		}
	}

	@JvmStatic
	fun tickDownNoGravity(target: Entity, ticks: Int) {
		if (ticks > 0) {
			if(target is Player && target.isFallFlying)
			{
				target.resetFallDistance()
				entityTicks[target] = ticks - 1
			}else
			{
				target.resetFallDistance()
				target.push(
					target.deltaMovement.x * 0.1,
					target.deltaMovement.y * 0.01,
					target.deltaMovement.z * 0.1
				)
				target.hurtMarked = true
				entityTicks[target] = ticks - 1
			}
		} else {
			entityTicks.remove(target)
			target.isNoGravity = false
		}
	}

}