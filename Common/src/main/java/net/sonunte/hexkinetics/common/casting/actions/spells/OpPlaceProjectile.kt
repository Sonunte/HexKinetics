package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.world.entity.projectile.*
import net.minecraft.world.item.*
import net.minecraft.world.phys.Vec3

object OpPlaceProjectile : SpellAction {

	override val argc = 1
	private const val COST = MediaConstants.SHARD_UNIT

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val vec = args.getVec3(0, argc)
		ctx.assertVecInRange(vec)

		return Triple(
			Spell(vec),
			COST,
			listOf(ParticleSpray.burst(vec, 0.7))
		)
	}

	private data class Spell(val vec: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val placeeStack = ctx.getOperativeSlot { it.item is ArrowItem || it.item is EnderpearlItem || it.item is SpectralArrowItem || it.item is TippedArrowItem || it.item is SnowballItem || it.item is EggItem || it.item is TridentItem || it.item is EnderEyeItem || it.item is FireChargeItem }?.copy()

			when (placeeStack?.item) {
				Items.ARROW -> {
					val projectile = Arrow(ctx.world, vec.x, vec.y, vec.z)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.ENDER_PEARL -> {
					val projectile = ThrownEnderpearl(ctx.world, ctx.caster)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.SPECTRAL_ARROW -> {
					val projectile = SpectralArrow(ctx.world, vec.x, vec.y, vec.z)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.TIPPED_ARROW -> {
					val projectile = Arrow(ctx.world, vec.x, vec.y, vec.z)
					projectile.owner = ctx.caster
					projectile.setEffectsFromItem(placeeStack)
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.SNOWBALL -> {
					val projectile = Snowball(ctx.world, vec.x, vec.y, vec.z)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.EGG -> {
					val projectile = ThrownEgg(ctx.world, vec.x, vec.y, vec.z)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.TRIDENT -> {
					val projectile = ThrownTrident(ctx.world, ctx.caster, placeeStack)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.ENDER_EYE -> {
					val projectile = EyeOfEnder(ctx.world, vec.x, vec.y, vec.z)
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				Items.FIRE_CHARGE -> {
					val projectile = SmallFireball(ctx.world, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0)
					projectile.owner = ctx.caster
					projectile.setPos(vec)
					ctx.world.addFreshEntity(projectile)
				}
				else -> {
					return
				}
			}
			if (placeeStack != null) {
				ctx.withdrawItem(placeeStack, 1, true)
			}
		}
	}
}