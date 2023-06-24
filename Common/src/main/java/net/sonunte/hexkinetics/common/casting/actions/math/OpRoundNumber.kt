package net.sonunte.hexkinetics.common.casting.actions.math

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getNumOrVec
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
					{ (lnum.roundToInt()).asActionResult }, { rvec -> Vec3(rvec.x.roundToInt().toDouble(), rvec.y.roundToInt().toDouble(), rvec.z.roundToInt().toDouble()).asActionResult }
				)
			}, { lvec ->
				num.map(
					{ rnum -> (rnum.roundToInt()).asActionResult }, { Vec3(lvec.x.roundToInt().toDouble(), lvec.y.roundToInt().toDouble(), lvec.z.roundToInt().toDouble()).asActionResult }
				)
			})
	}
}