package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import com.sun.tools.javac.tree.TreeInfo.args
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import kotlin.math.*


object OpRotateBlockSpell : SpellAction {

	override val argc = 2
	private const val COST = (MediaConstants.DUST_UNIT * 0.125).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getVec3(0, argc)
		val rotation = args.getVec3(1, argc)
		ctx.assertVecInRange(target)

		return Triple(
			Spell(target, rotation),
			COST,
			listOf(ParticleSpray.burst(target, 2.0, 100))
		)
	}

	private data class Spell(val target: Vec3, val rotation: Vec3) : RenderedSpell {

		override fun cast(ctx: CastingContext) {
			val blockPos = BlockPos(target)
			val blockState = ctx.world.getBlockState(blockPos)

		}
	}

}