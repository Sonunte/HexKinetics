package net.sonunte.hexkinetics.common.casting.actions.math

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.phys.Vec3

object OpVectorReflection : ConstMediaAction {

	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val d = args.getVec3(0, argc)
		val n = args.getVec3(1, argc)

		val dotProduct = d.dot(n)
		val reflected = d.subtract(Vec3(n.x * dotProduct * 2.0, n.y * dotProduct * 2.0, n.z * dotProduct * 2.0))
		return reflected.asActionResult
	}
}