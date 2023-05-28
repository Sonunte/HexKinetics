package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

object OpRotateTwoSpell : SpellAction {

	// Together with Momentum Swap it is basically Absolute Dominion.  https://discord.com/channels/936370934292549712/1073666769551642624/1073981253340123196

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 0.75).toInt()

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
        override fun cast(ctx: CastingContext) {
            rotateEntityMotion(target, rotation.normalize(), ctx)
        }
	}

    private fun rotateEntityMotion(entity: Entity, rotation: Vec3, ctx: CastingContext) {
        // Get the current force of motion of the entity
        val motion = entity.deltaMovement.length() - 0.01

        // Rotate the motion
        val rotatedMotionX = motion * rotation.x
        val rotatedMotionY = motion * rotation.y
        val rotatedMotionZ = motion * rotation.z

        // Set the new rotated motion to the entity
		entity.lerpMotion(rotatedMotionX, rotatedMotionY, rotatedMotionZ)
		entity.hurtMarked = true
    }

}