package net.sonunte.hexkinetics.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPositiveDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import net.minecraft.world.phys.Vec3

object OpGetVectors : ConstMediaAction {

	override val argc = 2


    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val pos = args.getVec3(0, argc)
        val radius = args.getPositiveDouble(1, argc)
        ctx.assertVecInRange(pos)

        val blockPositions = getBlockPositionsInSphere(pos, radius)

        return blockPositions.map(::Vec3Iota).asActionResult
    }
    fun getBlockPositionsInSphere(pos: Vec3, radius: Double): List<Vec3> {
        val blockPositions = ArrayList<Vec3>()
        val radiusInt = radius.toInt()

        for (x in -radiusInt..radiusInt) {
            for (y in -radiusInt..radiusInt) {
                for (z in -radiusInt..radiusInt) {
                    val distanceSq = x * x + y * y + z * z
                    if (distanceSq <= radius * radius) {
                        blockPositions.add(pos.add(x.toDouble(), y.toDouble(), z.toDouble()))
                    }
                }
            }
        }

        return blockPositions
    }
}