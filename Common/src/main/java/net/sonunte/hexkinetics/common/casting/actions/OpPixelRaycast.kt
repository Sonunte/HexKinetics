package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

object OpPixelRaycast : ConstMediaAction {
	override val argc = 2
	override val mediaCost = MediaConstants.DUST_UNIT / 100

	override fun execute(args: List<Iota>, env: CastingContext): List<Iota> {
		val origin = args.getVec3(0, argc)
		val look = args.getVec3(1, argc)

		env.assertVecInRange(origin)

		val rayHitResult = env.world.clip(
			ClipContext(
				origin,
				Action.raycastEnd(origin, look),
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.ANY,
				env.caster
			)
		)

		return if (rayHitResult.type == HitResult.Type.ENTITY && env.isVecInRange(rayHitResult.location)) {
			rayHitResult.location.asActionResult
		} else {
			if (rayHitResult.type == HitResult.Type.BLOCK && env.isVecInRange(rayHitResult.location)) {
				// casting OpBreakBlock at this position will not break the block we're looking at
				rayHitResult.location.asActionResult
			} else {
				listOf(NullIota())
			}
		}
	}
}