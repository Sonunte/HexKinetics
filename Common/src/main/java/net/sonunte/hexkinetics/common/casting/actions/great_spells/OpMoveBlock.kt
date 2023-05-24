package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

object OpMoveBlock : SpellAction {

	override val argc = 2
	private const val COST = MediaConstants.CRYSTAL_UNIT * 10
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val block = args.getVec3(0, argc)
		val destinationOffset = args.getVec3(1, argc)

		return Triple(
			Spell(block, destinationOffset),
			COST,
			listOf(ParticleSpray.burst(block, 1.0, 50), ParticleSpray.burst(block.add(destinationOffset), 1.0, 50))
		)
	}

	private data class Spell(val block: Vec3, val offset: Vec3) : RenderedSpell {
        override fun cast(ctx: CastingContext) {

			val destination = Vec3(block.x + offset.x, block.y + offset.y, block.z + offset.z)
			val blockPosDestination = BlockPos(destination)
			val blockPos = BlockPos(block)

			if (!ctx.isVecInWorld(destination))
				return


			if (isAir(blockPosDestination, ctx)) { switchBlocks(ctx.world, blockPos, blockPosDestination) }

        }
	}

	fun isAir(blockPos: BlockPos, ctx: CastingContext): Boolean {
		val world = ctx.world
		val blockState = world.getBlockState(blockPos)
		val block = blockState.block

		return block == Blocks.AIR
	}
	private fun isTileEntity(blockPos: BlockPos, world: ServerLevel): Boolean {
		val blockEntity = world.getBlockEntity(blockPos)

		return blockEntity != null
	}
	fun switchBlocks(world: ServerLevel, pos: BlockPos, destination: BlockPos) {
		val blockState = world.getBlockState(pos)

		val isTileEntityBlock = isTileEntity(pos, world)

		if (isTileEntityBlock) {
			// The block is a tile entity
			return
		} else {
			// The block is not a tile entity
			world.setBlockAndUpdate(destination, blockState)
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
		}
	}


}