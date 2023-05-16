package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.casting.operators.spells.great.OpTeleport
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue
import kotlin.math.floor

object OpLesserTeleport : SpellAction {

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 0.2).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		val number = args.getNumOrVec(1, argc)
		val position = entity.position()
		val fractionx = Mth.clamp(number.map({ num -> num.absoluteValue }, { vec -> vec.x }), 0.0001, 99.99999999)
		val fractiony = Mth.clamp(number.map({ num -> num.absoluteValue }, { vec -> vec.y }), 0.0001, 99.99999999)
		val fractionz = Mth.clamp(number.map({ num -> num.absoluteValue }, { vec -> vec.z }), 0.0001, 99.99999999)
		ctx.assertEntityInRange(entity)

		return Triple(
			Spell(entity, fractionx, fractiony, fractionz),
			COST,
			listOf(ParticleSpray.burst(position, 1.0))
		)
	}

	private data class Spell(val entity: Entity, val fractionx: Double, val fractiony: Double, val fractionz: Double) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val pos = entity.position()

			OpTeleport.teleportRespectSticky(entity, Vec3(floor(pos.x) - pos.x, 0.0, floor(pos.z) - pos.z), ctx.world)


			if (pos.x < 0){
				OpTeleport.teleportRespectSticky(entity, Vec3((floor(pos.x) + (1.0 - fractionx / 100)) - floor(pos.x), 0.0, 0.0), ctx.world)
			} else{
				OpTeleport.teleportRespectSticky(entity, Vec3((floor(pos.x) + fractionx / 100) - floor(pos.x), 0.0, 0.0), ctx.world)
			}
			if (pos.y < 0){
				OpTeleport.teleportRespectSticky(entity, Vec3(0.0, (floor(pos.y) + (1.0 - fractiony / 100)) - floor(pos.y), 0.0), ctx.world)
			} else{
				OpTeleport.teleportRespectSticky(entity, Vec3(0.0, (floor(pos.y) + fractiony / 100) - floor(pos.y), 0.0), ctx.world)
			}
			if (pos.z < 0){
				OpTeleport.teleportRespectSticky(entity, Vec3(0.0, 0.0, (floor(pos.z) + (1.0 - fractionz / 100)) - floor(pos.z)), ctx.world)
			} else{
				OpTeleport.teleportRespectSticky(entity, Vec3(0.0, 0.0, (floor(pos.z) + fractionz / 100) - floor(pos.z)), ctx.world)
			}

		}
	}
}