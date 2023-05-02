package net.sonunte.hexforce.common.casting.actions

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota

object OpVectorArgument : ConstMediaAction {
	/**
	 * The number of arguments from the stack that this action requires.
	 */
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
		val vec = args.getVec3(0,argc)
		val num = args.getDouble(1, argc)
		// all types that can be converted to an iota will have .asActionResult
		// defined for them to make them easy to return as the result of an Action.

		return if (num <= 0) {
			return listOf(NullIota())
		} else {
			if (num == 1.0)
				return listOf(DoubleIota(vec.x)) else
					if (num == 2.0)
						return listOf(DoubleIota(vec.y)) else
							if (num == 3.0)
								return listOf(DoubleIota(vec.z)) else if (num > 3)
									return listOf(NullIota()) else TODO()
		}

	}
}