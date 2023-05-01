package net.sonunte.hexforce.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

object OpRotateSpell : SpellAction {

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 3).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val rotation = args.getVec3(1, argc)
		ctx.assertEntityInRange(target)

		return Triple(
			Spell(target, rotation),
			COST,
			listOf(ParticleSpray.burst(target.position(), 1.0))
		)
	}

	/**
	 * This class is responsible for actually making changes to the world. It accepts parameters to
	 * define where/what it should affect (for this example the parameter is [vec]), and the
	 * [cast] method within is responsible for using that data to alter the world.
	 */

	private data class Spell(val target: Entity, val rotation: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.yRot = (target.yRot + rotation.y).toFloat()
			target.xRot = (target.xRot + rotation.x).toFloat()

		}
	}
}