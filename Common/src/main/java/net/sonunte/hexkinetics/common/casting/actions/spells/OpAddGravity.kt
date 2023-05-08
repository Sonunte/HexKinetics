package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FallingBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3


object OpAddGravity : SpellAction {

	override val argc = 1
	private const val COST = (MediaConstants.DUST_UNIT * 0.2).toInt()

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val collapse = Vec3.atCenterOf(BlockPos(args.getVec3(0, argc)))
		ctx.assertVecInRange(collapse)

		return Triple(
			Spell(collapse),
			COST,
			listOf(ParticleSpray.burst(collapse, 1.0))
		)
	}
	private data class Spell(val vec: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val pos = BlockPos(vec)
			val blockState = ctx.world.getBlockState(pos)

			// makes sure the player has permission to break the block
			if (!ctx.canEditBlockAt(pos) || !IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, pos, blockState, ctx.caster))
				return

			ctx.world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())

			val fallingBlockEntityType = EntityType.FALLING_BLOCK.javaClass
			val constructor = fallingBlockEntityType.getDeclaredConstructor(CastingContext::class.java, Double::class.java, Double::class.java, Double::class.java, BlockState::class.java)
			constructor.isAccessible = true
			val fallingBlockEntity = constructor.newInstance(ctx.world, pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, blockState)


			ctx.world.addFreshEntity(fallingBlockEntity as Entity)
			fallingBlockEntity.setDeltaMovement(0.0, -0.1, 0.0)

		}
	}
}