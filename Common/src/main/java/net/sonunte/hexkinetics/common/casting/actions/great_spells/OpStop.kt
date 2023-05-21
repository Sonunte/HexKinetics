package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3

object OpStop : SpellAction {

	override val argc = 1
	private const val COST = MediaConstants.DUST_UNIT
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)

		return Triple(
			Spell(entity),
			COST,
			listOf(ParticleSpray.burst(entity.position(), 2.0, 100))
		)
	}

	private data class Spell(val entity: Entity) : RenderedSpell {
        override fun cast(ctx: CastingContext) {

        }
	}

}