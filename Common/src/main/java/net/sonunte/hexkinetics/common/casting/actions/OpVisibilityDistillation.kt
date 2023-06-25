package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

object OpVisibilityDistillation : ConstMediaAction {

	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val entity = args.getEntity(0, argc)
		val vector = args.getVec3(1, argc)
		ctx.assertEntityInRange(entity)

		return (isVectorInFieldOfView(entity, vector) && isVectorVisible(entity, vector, ctx)).asActionResult
	}
	private fun isVectorVisible(entity: Entity, vector: Vec3, ctx: CastingContext): Boolean {
		val playerEyePos = entity.eyePosition
		val direction = vector.subtract(playerEyePos).normalize()
		val step = 0.1
		var currentPosition = playerEyePos

		while (currentPosition.distanceTo(vector) > step) {
			currentPosition = currentPosition.add(direction.multiply(step, step, step))
			val blockPos = BlockPos(currentPosition)
			val block = ctx.world.getBlockState(blockPos).block

			// Check if the block at the current position obstructs the view
			if (block != Blocks.AIR) {
				return false
			}
		}

		return true
	}
	private fun isVectorInFieldOfView(entity: Entity, vector: Vec3): Boolean {
		val entityLook = entity.lookAngle
		val vectorTarget = (vector.subtract(entity.eyePosition)).normalize()

		val dotProduct = entityLook.dot(vectorTarget)
		val threshold = 0.1

		return dotProduct >= threshold
	}
}