package net.sonunte.hexforce.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import net.sonunte.hexforce.api.config.HexForceConfig
import net.sonunte.hexforce.common.casting.actions.OpExampleConstMediaAction.argc
import kotlin.math.floor

object OpLesserTeleport : SpellAction {

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 0.5).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, OpLesserTeleport.argc)
		val fraction = args.getDouble(1, argc)
		val position = entity.position()
		ctx.assertEntityInRange(entity)

		return Triple(
			OpLesserTeleport.Spell(entity, fraction),
			COST,
			listOf(ParticleSpray.burst(position, 1.0))
		)
	}

	private data class Spell(val entity: Entity, val fraction: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val pos = entity.position()

			//entity.setPos(Vec3(floor(pos.x), floor(pos.y), floor(pos.z)))
			entity.setPos(Vec3(pos.x + fraction.toBigDecimal().toDouble(), pos.y + fraction.toBigDecimal().toDouble(), pos.z + fraction.toBigDecimal().toDouble()))

		}
	}
}