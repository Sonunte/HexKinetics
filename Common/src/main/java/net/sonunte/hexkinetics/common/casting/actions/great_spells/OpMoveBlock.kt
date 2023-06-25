package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapLocationTooFarAway
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import net.sonunte.hexkinetics.api.config.HexKineticsConfig

object OpMoveBlock : SpellAction {

	override val argc = 2
	override val isGreat = true

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val block = args.getVec3(0, argc)
		val destinationOffset = args.getVec3(1, argc)
		val targetPos = block.add(destinationOffset)

		ctx.assertVecInRange(block)

		if (!ctx.isVecInWorld(targetPos))
			throw MishapLocationTooFarAway(targetPos, "too_close_to_out")

		val cost = when {
			destinationOffset.length() <= 1.0 -> MediaConstants.SHARD_UNIT
			destinationOffset.length() > 1.0 && destinationOffset.length() < 100.0 -> MediaConstants.CRYSTAL_UNIT * 5
			destinationOffset.length() in 100.0..10000.0 -> MediaConstants.CRYSTAL_UNIT * 10
			destinationOffset.length() > 10000.0 && destinationOffset.length() < 30000.0 -> (MediaConstants.CRYSTAL_UNIT * 10 + (destinationOffset.length() - 10000) * MediaConstants.SHARD_UNIT).toInt()
			else -> 0
		}

		return Triple(
			Spell(block, destinationOffset),
			cost,
			listOf(
				ParticleSpray.burst(block, 1.0, 50),
				ParticleSpray.burst(block.add(destinationOffset), 1.0, 50)
			)
		)
	}

	private data class Spell(val block: Vec3, val offset: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val destination = block.add(offset)
			val blockPosDestination = BlockPos(destination)
			val blockPos = BlockPos(block)

			val blockStateDestination = ctx.world.getBlockState(blockPosDestination)
			val blockState = ctx.world.getBlockState(blockPos)

			if (!HexKineticsConfig.server.isTranslocationAllowed(Registry.BLOCK.getKey(blockState.block)))
				return
			if (!ctx.isVecInWorld(destination) || offset.length() > 30000)
				return
			else if (!IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, blockPosDestination, blockStateDestination, ctx.caster) ||
				!ctx.canEditBlockAt(blockPos) || !IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, blockPos, blockState, ctx.caster))
				return
			else if (block == Blocks.AIR) {
				ctx.world.setBlockAndUpdate(blockPosDestination, ctx.world.getBlockState(blockPos))
				ctx.world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())
			} else {
				switchBlocks(ctx.world, ctx, blockPos, blockPosDestination)
			}
		}
	}
	private fun switchBlocks(world: ServerLevel, ctx: CastingContext, pos: BlockPos, destination: BlockPos) {
		val blockState = world.getBlockState(pos)
		val blockStDes = world.getBlockState(destination)
		val blockEntity = world.getBlockEntity(pos)

		if (blockEntity != null) {
			// The block is a tile entity
			if (HexKineticsConfig.server.moveTileEntities)
			{
				if (blockState.getDestroySpeed(world, pos) == blockStDes.getDestroySpeed(world, destination)) {
					// if equal hardness between moving block and block at destination coordinates
					return
				} else if ((blockState.getDestroySpeed(world, pos) > blockStDes.getDestroySpeed(world, destination) && blockStDes.getDestroySpeed(world, destination) >= 0) || blockState.getDestroySpeed(world, pos) < 0) {
					// if harder than destination
					world.destroyBlock(destination, true, ctx.caster)
					world.setBlockAndUpdate(destination, blockState)
					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
				} else if (blockState.getDestroySpeed(world, pos) < blockStDes.getDestroySpeed(world, destination) || blockStDes.getDestroySpeed(world, destination) < 0) {
					// if softer than destination
					world.removeBlockEntity(pos)
					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
				}
			}else{
				return
			}
		} else {
			// The block is not a tile entity
			if (blockState.getDestroySpeed(world, pos) == blockStDes.getDestroySpeed(world, destination)) {
				// if equal hardness between moving block and block at destination coordinates
				return
			} else if ((blockState.getDestroySpeed(world, pos) > blockStDes.getDestroySpeed(world, destination) && blockStDes.getDestroySpeed(world, destination) >= 0) || blockState.getDestroySpeed(world, pos) < 0) {
				// if harder than destination
				world.destroyBlock(destination, true, ctx.caster)
				world.setBlockAndUpdate(destination, blockState)
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
			} else if (blockState.getDestroySpeed(world, pos) < blockStDes.getDestroySpeed(world, destination) || blockStDes.getDestroySpeed(world, destination) < 0) {
				// if softer than destination
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
			}
		}
	}
}