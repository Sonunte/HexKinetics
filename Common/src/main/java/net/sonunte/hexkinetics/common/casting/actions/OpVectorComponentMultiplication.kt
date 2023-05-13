package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.phys.Vec3

object OpVectorComponentMultiplication : ConstMediaAction {

	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val f = args.getVec3(0, argc)
		val s = args.getVec3(1, argc)

		val multi = Vec3(f.x * s.x, f.y * s.y, f.z * s.z)
		return multi.asActionResult
	}
}