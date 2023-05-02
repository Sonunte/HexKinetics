package net.sonunte.hexkinetics.common.casting.actions.spells

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import net.sonunte.hexkinetics.api.config.HexKineticsConfig
import net.sonunte.hexkinetics.common.casting.actions.OpExampleConstMediaAction.argc

object OpExampleSpellAction : SpellAction {
	/**
	 * The number of arguments from the stack that this action requires.
	 */
	override val argc = 1

	/**
	 * The method called when this Action is actually executed. Accepts the [args]
	 * that were on the stack (there will be [argc] of them), and the [ctx],
	 * which contains things like references to the caster, the ServerLevel,
	 * methods to determine whether locations and entities are in ambit, etc.
	 * Returns a triple of things. The [RenderedSpell] is responsible for the spell actually
	 * doing things in the world, the [Int] is how much media the spell should cost,
	 * and the [List] of [ParticleSpray] renders particle effects for the result of the SpellAction.
	 *
	 * The [execute] method should only contain code to find the targets of the spell and validate
	 * them. All the code that actually makes changes to the world (breaking blocks, teleporting things,
	 * etc.) should be in the private [Spell] data class below.
	 */
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val toAir = Vec3.atCenterOf(BlockPos(args.getVec3(0, argc)))

		// makes sure that the position at toAir is inside the range
		// the caster is allowed to affect.
		ctx.assertVecInRange(toAir)

		return Triple(
			Spell(toAir),
			HexKineticsConfig.server.exampleSpellActionCost,
			listOf(ParticleSpray.burst(toAir, 1.0))
		)
	}

	/**
	 * This class is responsible for actually making changes to the world. It accepts parameters to
	 * define where/what it should affect (for this example the parameter is [vec]), and the
	 * [cast] method within is responsible for using that data to alter the world.
	 */
	private data class Spell(val vec: Vec3) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val pos = BlockPos(vec)
			val blockState = ctx.world.getBlockState(pos)

			// makes sure the player has permission to break the block,
			// e.g. not in a claimed chunk or similar.
			if (!ctx.canEditBlockAt(pos) || !IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, pos, blockState, ctx.caster))
				return

			ctx.world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
		}
	}
}