package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

object OpMoveBlock : SpellAction {

	override val argc = 2
	private const val COST = MediaConstants.CRYSTAL_UNIT * 10
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val block = args.getVec3(0, argc)
		val destinationOffset = args.getVec3(1, argc)

		val cost = if (destinationOffset.length() <= 1.0)
		{
			MediaConstants.SHARD_UNIT
		}
		else if (destinationOffset.length() > 1.0 && destinationOffset.length() < 100.0)
		{
			MediaConstants.CRYSTAL_UNIT * 5
		}
		else if (destinationOffset.length() in 100.0..10000.0)
		{
			MediaConstants.CRYSTAL_UNIT * 10
		}
		else if (destinationOffset.length() > 10000.0 && destinationOffset.length() < 30000.0)
		{
			(MediaConstants.CRYSTAL_UNIT * 10 + (destinationOffset.length() - 10000) * MediaConstants.SHARD_UNIT).toInt()
		}else {
			0
		}

		return Triple(
			Spell(block, destinationOffset),
			cost,
			listOf(ParticleSpray.burst(block, 1.0, 50), ParticleSpray.burst(block.add(destinationOffset), 1.0, 50))
		)
	}

	private data class Spell(val block: Vec3, val offset: Vec3) : RenderedSpell {
        override fun cast(ctx: CastingContext) {

			val destination = block.add(offset)
			val blockPosDestination = BlockPos(destination)
			val blockPos = BlockPos(block)

			val blockStateDestination = ctx.world.getBlockState(blockPosDestination)
			val blockState = ctx.world.getBlockState(blockPos)

			if (!ctx.isVecInWorld(destination))
				return

			if (offset.length() > 30000)
				return

			if (blockState.block == Blocks.END_PORTAL_FRAME || blockState.block == Blocks.END_PORTAL || blockState.block == Blocks.END_GATEWAY || blockState.block == Blocks.NETHER_PORTAL || blockState.block == Blocks.BARRIER)
				return


			if (!ctx.canEditBlockAt(blockPosDestination) || !IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, blockPosDestination, blockStateDestination, ctx.caster) || !ctx.canEditBlockAt(blockPos) || !IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, blockPos, blockState, ctx.caster))
				return


			if (isAir(blockPosDestination, ctx))
			{
				ctx.world.setBlockAndUpdate(blockPosDestination, ctx.world.getBlockState(blockPos))
				ctx.world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())
			}
			else {
				switchBlocks(ctx.world, ctx, blockPos, blockPosDestination)
			}

        }
	}

	fun isAir(blockPos: BlockPos, ctx: CastingContext): Boolean {
		val world = ctx.world
		val blockState = world.getBlockState(blockPos)
		val block = blockState.block

		return block == Blocks.AIR
	}
	fun isTileEntity(blockPos: BlockPos, world: ServerLevel): Boolean {
		val blockEntity = world.getBlockEntity(blockPos)

		return blockEntity != null
	}
	fun switchBlocks(world: ServerLevel, ctx: CastingContext, pos: BlockPos, destination: BlockPos) {
		val blockState = world.getBlockState(pos)
		val blockStDes = world.getBlockState(destination)

		val isTileEntityBlock = isTileEntity(pos, world)

		if (isTileEntityBlock) {
			// The block is a tile entity
			return
		} else {
			// The block is not a tile entity
			if (blockState.getDestroySpeed(world, pos) == blockStDes.getDestroySpeed(world, destination))
			{
				// if equal hardness between moving block and block at destination coordinates

				return
			}
			else if ((blockState.getDestroySpeed(world, pos) > blockStDes.getDestroySpeed(world, destination) && blockStDes.getDestroySpeed(world, destination) >= 0 ) || blockState.getDestroySpeed(world, pos) < 0)
			{
				// if harder than destination

				world.destroyBlock(destination, true, ctx.caster)
				world.setBlockAndUpdate(destination, blockState)
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
			}
			else if (blockState.getDestroySpeed(world, pos) < blockStDes.getDestroySpeed(world, destination) || blockStDes.getDestroySpeed(world, destination) < 0)
			{
				// if softer than destination

				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
			}
		}
	}


}