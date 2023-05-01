package net.sonunte.hexforce.forge;


import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraftforge.common.ForgeConfigSpec;
import net.sonunte.hexforce.api.config.HexForceConfig;

public class ForgeHexForceConfig implements HexForceConfig.CommonConfigAccess {
    public ForgeHexForceConfig(ForgeConfigSpec.Builder builder) {

    }

    public static class Client implements HexForceConfig.ClientConfigAccess {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Server implements HexForceConfig.ServerConfigAccess {
        // costs of example spells
        private static ForgeConfigSpec.DoubleValue exampleSpellActionCost;
        private static ForgeConfigSpec.DoubleValue exampleConstActionCost;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.translation("text.autoconfig.hexforce.option.server.exampleSpells").push("exampleSpells");

            exampleSpellActionCost = builder.translation("text.autoconfig.hexforce.option.server.exampleSpells.exampleSpellActionCost")
                    .defineInRange("exampleSpellActionCost", DEFAULT_EXAMPLE_SPELL_ACTION_COST, DEF_MIN_COST, DEF_MAX_COST);
            exampleConstActionCost = builder.translation("text.autoconfig.hexforce.option.server.exampleSpells.exampleConstActionCost")
                    .defineInRange("exampleConstActionCost", DEFAULT_EXAMPLE_CONST_ACTION_COST, DEF_MIN_COST, DEF_MAX_COST);

            builder.pop();
        }

        //region getters
        @Override
        public int getExampleSpellActionCost() {
            return (int) (exampleSpellActionCost.get() * MediaConstants.DUST_UNIT);
        }

        @Override
        public int getExampleConstActionCost() {
            return (int) (exampleConstActionCost.get() * MediaConstants.DUST_UNIT);
        }

        //endregion
    }
}
