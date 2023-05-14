package net.sonunte.hexkinetics.api.casting

import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import com.mojang.datafixers.util.Either
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3


fun List<Iota>.getVecOrEnti(idx: Int, argc: Int = 0): Either<Vec3, Entity> {
    val datum = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (datum) {
        is Vec3Iota -> Either.left(datum.vec3)
        is EntityIota -> Either.right(datum.entity)
        else -> throw MishapInvalidIota.of(
            datum,
            if (argc == 0) idx else argc - (idx + 1),
            "vecenti"
        )
    }
}