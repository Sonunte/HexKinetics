package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

object OpGreaterImpulse : SpellAction {

	override val argc = 3
	private const val COST = (MediaConstants.DUST_UNIT * 0.125).toInt()
	private var ticks = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val time = args.getDouble(1, argc)
		val force = args.getVec3(2, argc)
		ctx.assertEntityInRange(target)

		return Triple(
			Spell(target, time, force),
			COST,
			listOf(ParticleSpray.burst(target.position(), 2.0, 100))
		)
	}

	private data class Spell(val target: Entity, val time: Double, val force: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.isNoGravity = true
			ticks = time.toInt()
			tickDownNoGravity(target)

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

		if (target.isNoGravity) {
			target.resetFallDistance()
			target.deltaMovement = target.deltaMovement.multiply(1.098, 1.0 / 0.98, 1.098)
		}
		var delayCount = ticks

		if (delayCount > 0) {
			delayCount--
			ticks--
		}
		if (delayCount.toInt() <= 0) {
			target.isNoGravity = false

		}
	}
}