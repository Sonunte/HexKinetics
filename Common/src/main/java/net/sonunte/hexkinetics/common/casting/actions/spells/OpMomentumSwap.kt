package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3

object OpMomentumSwap : SpellAction {

	override val argc = 2
	private const val COST = MediaConstants.DUST_UNIT

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		val target2 = args.getEntity(1, argc)
		val position = Vec3(target.position().x - target2.position().x, target.position().y - target2.position().y, target.position().z - target2.position().z)
		ctx.assertEntityInRange(target)
		ctx.assertEntityInRange(target2)

		return Triple(
			Spell(target, target2),
			COST,
			listOf(ParticleSpray.burst(position, 2.0, 100))
		)
	}

	private data class Spell(val target: Entity, val target2: Entity) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            swapEntityMotion(target, target2)
        }
	}

    private fun swapEntityMotion(entity: Entity, entity2: Entity) {
        // Get the current motions of the entities
        val motion = entity.deltaMovement
		val motion2 = entity2.deltaMovement


        // Set the new motions
		entity.deltaMovement = motion2
		entity.hurtMarked = true
		entity2.deltaMovement = motion
		entity2.hurtMarked = true
    }

}