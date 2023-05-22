package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3

object OpMoveBlock : SpellAction {

	override val argc = 2
	private const val COST = MediaConstants.CRYSTAL_UNIT * 15
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val block = args.getVec3(0, argc)
		val destinationOffset = args.getVec3(1, argc)

		return Triple(
			Spell(block, destinationOffset),
			COST,
			listOf(ParticleSpray.burst(destinationOffset, 2.0, 100))
		)
	}

	private data class Spell(val block: Vec3, val offset: Vec3) : RenderedSpell {
        override fun cast(ctx: CastingContext) {

        }
	}

}