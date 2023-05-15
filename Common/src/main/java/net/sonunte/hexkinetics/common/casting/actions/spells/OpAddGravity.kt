package net.sonunte.hexkinetics.common.casting.actions.spells


import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.world.entity.Entity
import net.sonunte.hexkinetics.common.casting.actions.great_spells.OpGreaterImpulse


object OpAddGravity : SpellAction {

	override val argc = 1
	private var uses = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		ctx.assertEntityInRange(entity)

		val cost = MediaConstants.DUST_UNIT * 5  * uses

		return Triple(
			Spell(entity),
			cost,
			listOf(ParticleSpray.burst(entity.position(), 1.0))
		)
	}
	private data class Spell(val entity: Entity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			entity.fallDistance = entity.fallDistance + 1
		}
	}
}