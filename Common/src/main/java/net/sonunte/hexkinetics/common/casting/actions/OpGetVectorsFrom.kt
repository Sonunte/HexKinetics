package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import net.minecraft.world.phys.Vec3

object OpGetVectorsFrom : ConstMediaAction {

	override val argc = 2


    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val pos = args.getVec3(0, argc).add(Vec3(0.0, -1.0, 0.0))
        val pos2 = args.getVec3(1, argc).add(Vec3(0.0, -1.0, 0.0))
        ctx.assertVecInRange(pos)
        ctx.assertVecInRange(pos2)


        val blockPositions = getBlockPositionsBetween(pos, pos2)

        return blockPositions.map(::Vec3Iota).asActionResult
    }
    private fun getBlockPositionsBetween(pos1: Vec3, pos2: Vec3): List<Vec3> {
        val minX = pos1.x.coerceAtMost(pos2.x).toInt()
        val minY = pos1.y.coerceAtMost(pos2.y).toInt()
        val minZ = pos1.z.coerceAtMost(pos2.z).toInt()

        val maxX = pos1.x.coerceAtLeast(pos2.x).toInt()
        val maxY = pos1.y.coerceAtLeast(pos2.y).toInt()
        val maxZ = pos1.z.coerceAtLeast(pos2.z).toInt()

        val blockPositions = mutableListOf<Vec3>()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    blockPositions.add(Vec3(x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        return blockPositions
    }

}