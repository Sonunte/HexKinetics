package net.sonunte.hexforce.fabric;

import at.petrak.hexcasting.api.misc.MediaConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.sonunte.hexforce.api.HexForceAPI;
import net.sonunte.hexforce.api.config.HexForceConfig;
import net.sonunte.hexforce.xplat.IXplatAbstractions;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Config(name = HexForceAPI.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/calcite.png")
public class FabricHexForceConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public final Common common = new Common();
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public final Client client = new Client();
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public final Server server = new Server();

    public static FabricHexForceConfig setup() {
        AutoConfig.register(FabricHexForceConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        var instance = AutoConfig.getConfigHolder(FabricHexForceConfig.class).getConfig();

        HexForceConfig.setCommon(instance.common);
        // We care about the client only on the *physical* client ...
        if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
            HexForceConfig.setClient(instance.client);
        }
        // but we care about the server on the *logical* server
        // i believe this should Just Work without a guard? assuming we don't access it from the client ever
        HexForceConfig.setServer(instance.server);

        return instance;
    }


    @Config(name = "common")
    private static class Common implements ConfigData, HexForceConfig.CommonConfigAccess { }

    @Config(name = "client")
    private static class Client implements ConfigData, HexForceConfig.ClientConfigAccess { }


    @Config(name = "server")
    private static class Server implements ConfigData, HexForceConfig.ServerConfigAccess {

        @ConfigEntry.Gui.CollapsibleObject
        private ExampleSpells exampleSpells = new ExampleSpells();

        static class ExampleSpells {
            // costs of misc spells
            double exampleConstActionCost = DEFAULT_EXAMPLE_CONST_ACTION_COST;
            double exampleSpellActionCost = DEFAULT_EXAMPLE_SPELL_ACTION_COST;
        }

        @Override
        public void validatePostLoad() throws ValidationException {
            // costs of misc spells
            this.exampleSpells.exampleConstActionCost = bound(this.exampleSpells.exampleConstActionCost, DEF_MIN_COST, DEF_MAX_COST);
            this.exampleSpells.exampleSpellActionCost = bound(this.exampleSpells.exampleSpellActionCost, DEF_MIN_COST, DEF_MAX_COST);
        }

        private int bound(int toBind, int lower, int upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }
        private double bound(double toBind, double lower, double upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }


        //region getters
        @Override
        public int getExampleConstActionCost() {
            return (int) (exampleSpells.exampleConstActionCost * MediaConstants.DUST_UNIT);
        }

        @Override
        public int getExampleSpellActionCost() {
            return (int) (exampleSpells.exampleSpellActionCost * MediaConstants.DUST_UNIT);
        }
        //endregion
    }
}
