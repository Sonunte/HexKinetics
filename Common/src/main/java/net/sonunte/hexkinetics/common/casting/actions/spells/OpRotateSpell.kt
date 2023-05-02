package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

object OpRotateSpell : SpellAction {

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 0.8).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val rotation = args.getVec3(1, argc)
		val position = target.position()
		ctx.assertEntityInRange(target)
		val dvec = target.lookAngle.scale(1.0)

		return Triple(
			Spell(target, rotation),
			COST,
			listOf(ParticleSpray.burst(position.add(dvec), 1.9, 100))
		)
	}

	private data class Spell(val target: Entity, val rotation: Vec3) : RenderedSpell {

		val targetLookAt = target.position().add(rotation)
		val anchor = EntityAnchorArgument.Anchor.FEET
		override fun cast(ctx: CastingContext) {
			target.lookAt(anchor, targetLookAt)

		}
	}
}