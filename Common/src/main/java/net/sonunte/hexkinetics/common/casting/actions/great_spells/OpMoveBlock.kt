package net.sonunte.hexkinetics.common.casting.actions.great_spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import org.apache.logging.log4j.LogManager

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

			val destination = Vec3(block.x + offset.x, block.y + offset.y, block.z + offset.z)
			val blockPosDestination = convertToBlockPos(destination)
			val blockPos = convertToBlockPos(block)

			if (isAir(blockPosDestination, ctx)) { placeBlock(ctx.world, blockPos, blockPosDestination) }

        }
	}

	fun convertToBlockPos(vec: Vec3): BlockPos {
		return BlockPos(vec.x, vec.y, vec.z)
	}

	fun isAir(blockPos: BlockPos, ctx: CastingContext): Boolean {
		val world = ctx.world
		val blockState = world.getBlockState(blockPos)
		val block = blockState.block

		return block == Blocks.AIR
	}
	fun placeBlock(world: ServerLevel, pos: BlockPos, destination: BlockPos, nbtData: CompoundTag? = null) {
		val blockState = world.getBlockState(pos)

		world.setBlockAndUpdate(destination, blockState)
	}


}