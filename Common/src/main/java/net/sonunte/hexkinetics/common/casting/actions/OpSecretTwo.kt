package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import net.minecraft.world.entity.TamableAnimal

object OpSecretTwo : ConstMediaAction {

	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val vehicle = args.getEntity(0, argc)
		val target = vehicle.firstPassenger

		ctx.assertEntityInRange(vehicle)


		return target.asActionResult
	}
}