package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.phys.Vec3
import kotlin.math.round

object OpRoundedEntityLook : ConstMediaAction {


	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val e = args.getEntity(0, argc)
		ctx.assertEntityInRange(e)
		val roundedVector = Vec3(round(e.lookAngle.x),round(e.lookAngle.y),round(e.lookAngle.z))
		return roundedVector.asActionResult
	}
}