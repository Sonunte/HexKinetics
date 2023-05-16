package net.sonunte.hexkinetics.common.casting.actions.spells


import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.entity.Entity
import kotlin.math.abs


object OpAddGravity : SpellAction {

	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		val blocks = args.getDouble(1, argc)
		ctx.assertEntityInRange(entity)

		val cost = abs((MediaConstants.DUST_UNIT * 2 * blocks + (entity.fallDistance / 5))).toInt()

		return Triple(
			Spell(entity, blocks),
			cost,
			listOf(ParticleSpray.burst(entity.position(), 1.0))
		)
	}
	private data class Spell(val entity: Entity, val blocks: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			entity.fallDistance = entity.fallDistance + blocks.toFloat()
		}
	}
}