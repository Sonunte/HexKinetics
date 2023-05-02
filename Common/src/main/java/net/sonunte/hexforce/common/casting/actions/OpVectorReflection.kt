package net.sonunte.hexforce.common.casting.actions

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.phys.Vec3

object OpVectorReflection : ConstMediaAction {

	override val argc = 2

	/**
	 * The method called when this Action is actually executed. Accepts the [args]
	 * that were on the stack (there will be [argc] of them), and the [ctx],
	 * which contains things like references to the caster, the ServerLevel,
	 * methods to determine whether locations and entities are in ambit, etc.
	 * Returns the list of iotas that should be added back to the stack as the
	 * result of this action executing.
	 */
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val d = args.getVec3(0, argc)
		val n = args.getVec3(1, argc)

		val dotProduct = d.dot(n)
		val reflected = d.subtract(Vec3(n.x * dotProduct * 2.0, n.y * dotProduct * 2.0, n.z * dotProduct * 2.0))
		return reflected.asActionResult
	}
}