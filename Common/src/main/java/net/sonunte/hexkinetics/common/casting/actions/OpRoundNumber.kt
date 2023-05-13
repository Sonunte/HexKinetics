package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt

object OpRoundNumber : ConstMediaAction {

	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val num = args.getNumOrVec(0, argc)

		return num.map(
			{ lnum ->
				num.map(
					{ rnum -> (lnum.roundToInt()).asActionResult }, { rvec -> Vec3(rvec.x, rvec.y, rvec.z).asActionResult }
				)
			}, { lvec ->
				num.map(
					{ rnum -> (rnum.roundToInt()).asActionResult }, { Vec3(lvec.x, lvec.y, lvec.z).asActionResult }
				)
			})
	}
}