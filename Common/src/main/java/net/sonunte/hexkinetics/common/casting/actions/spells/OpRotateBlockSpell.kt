package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.RailBlock.SHAPE
import net.minecraft.world.level.block.state.properties.BlockStateProperties.*
import net.minecraft.world.level.block.state.properties.RailShape
import net.minecraft.world.phys.Vec3
import kotlin.math.abs


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
			val blockDirection = getDirectionFromVector(rotation.normalize())

			setBlockDirection(ctx.world, blockPos, blockDirection)

		}
	}
	fun setBlockDirection(world: ServerLevel, blockPos: BlockPos, newDirection: Direction) {
		val blockState = world.getBlockState(blockPos)
		val block = blockState.block
		if (blockState.properties.contains(SHAPE))
		{
			val modifiedState = blockState.setValue(SHAPE, getRailShapeFromDirection(newDirection))
			world.setBlockAndUpdate(blockPos, modifiedState)
			world.updateNeighborsAt(blockPos, block)
		}
		if (blockState.properties.contains(FACING))
		{
			val modifiedState = blockState.setValue(FACING, newDirection)
			world.setBlockAndUpdate(blockPos, modifiedState)
			world.updateNeighborsAt(blockPos, block)
		}
		if (blockState.properties.contains(FACING_HOPPER))
		{
			if (newDirection == Direction.UP)
			{
				val modifiedState = blockState.setValue(FACING_HOPPER, Direction.DOWN)
				world.setBlockAndUpdate(blockPos, modifiedState)
				world.updateNeighborsAt(blockPos, block)
			}else
			{
				val modifiedState = blockState.setValue(FACING_HOPPER, newDirection)
				world.setBlockAndUpdate(blockPos, modifiedState)
				world.updateNeighborsAt(blockPos, block)
			}
		}
		if (blockState.properties.contains(HORIZONTAL_FACING))
		{
			if (newDirection == Direction.UP || newDirection == Direction.DOWN)
			{
				val modifiedState = blockState.setValue(HORIZONTAL_FACING, changeDirectionToHorizontal(newDirection))
				world.setBlockAndUpdate(blockPos, modifiedState)
				world.updateNeighborsAt(blockPos, block)
			}else
			{
				val modifiedState = blockState.setValue(HORIZONTAL_FACING, newDirection)
				world.setBlockAndUpdate(blockPos, modifiedState)
				world.updateNeighborsAt(blockPos, block)
			}
		}
		if (blockState.properties.contains(VERTICAL_DIRECTION))
		{
			if (newDirection == Direction.EAST || newDirection == Direction.WEST || newDirection == Direction.NORTH || newDirection == Direction.SOUTH)
			{
				return
			}else
			{
				val modifiedState = blockState.setValue(VERTICAL_DIRECTION, newDirection)
				world.setBlockAndUpdate(blockPos, modifiedState)
				world.updateNeighborsAt(blockPos, block)
			}
		}
		if (!blockState.properties.contains(FACING) && !blockState.properties.contains(SHAPE) && !blockState.properties.contains(FACING_HOPPER) && !blockState.properties.contains(HORIZONTAL_FACING) && !blockState.properties.contains(VERTICAL_DIRECTION))
			return
	}

	private fun getDirectionFromVector(vector: Vec3): Direction {
		val xD = abs(vector.x)
		val yD = abs(vector.y)
		val zD = abs(vector.z)

		return when {
			xD > zD && xD >= yD -> {
				if (vector.x > 0) Direction.EAST else Direction.WEST
			}
			yD > xD && yD > zD -> {
				if (vector.y > 0) Direction.UP else Direction.DOWN
			}
			else -> {
				if (vector.z > 0) Direction.SOUTH else Direction.NORTH
			}
		}
	}
	private fun changeDirectionToHorizontal(dir: Direction): Direction {
		if (dir == Direction.WEST)
		{
			return Direction.WEST
		}else if (dir == Direction.EAST)
		{
			return Direction.EAST
		}else if (dir == Direction.NORTH)
		{
			return Direction.NORTH
		}else if (dir == Direction.SOUTH)
		{
			return Direction.SOUTH
		}else
		{
			return Direction.NORTH //Default to NORTH if direction is not recognized
		}
	}

	private fun getRailShapeFromDirection(direction: Direction): RailShape {
		return when (direction) {
			Direction.NORTH -> RailShape.NORTH_SOUTH
			Direction.EAST -> RailShape.EAST_WEST
			Direction.SOUTH -> RailShape.NORTH_SOUTH
			Direction.WEST -> RailShape.EAST_WEST
			else -> RailShape.NORTH_SOUTH // Default to NORTH_SOUTH if direction is not recognized
		}
	}

}