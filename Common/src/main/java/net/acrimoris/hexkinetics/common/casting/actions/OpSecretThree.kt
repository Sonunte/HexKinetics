package net.acrimoris.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import net.minecraft.world.entity.projectile.Projectile

object OpSecretThree : ConstMediaAction {

	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val projectile = args.getEntity(0, argc)

		ctx.assertEntityInRange(projectile)

		if (projectile is Projectile) {
			val target = projectile.owner

			return if (target != null) {
				if (ctx.isEntityInRange(target)) {
					target.asActionResult
				}else {
					listOf(NullIota())
				}
			}else {
				listOf(NullIota())
			}
		}else {
			return listOf(NullIota())
		}

	}
}