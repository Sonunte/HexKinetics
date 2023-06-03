package net.sonunte.hexkinetics.fabric;

import at.petrak.hexcasting.api.misc.MediaConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.resources.ResourceLocation;
import net.sonunte.hexkinetics.api.HexKineticsAPI;
import net.sonunte.hexkinetics.api.config.HexKineticsConfig;
import net.sonunte.hexkinetics.xplat.IXplatAbstractions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Config(name = HexKineticsAPI.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/calcite.png")
public class FabricYourModConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public final Common common = new Common();
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public final Client client = new Client();
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public final Server server = new Server();

    public static FabricYourModConfig setup() {
        AutoConfig.register(FabricYourModConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        var instance = AutoConfig.getConfigHolder(FabricYourModConfig.class).getConfig();

        HexKineticsConfig.setCommon(instance.common);
        // We care about the client only on the *physical* client ...
        if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
            HexKineticsConfig.setClient(instance.client);
        }
        // but we care about the server on the *logical* server
        // i believe this should Just Work without a guard? assuming we don't access it from the client ever
        HexKineticsConfig.setServer(instance.server);

        return instance;
    }


    @Config(name = "common")
    private static class Common implements ConfigData, HexKineticsConfig.CommonConfigAccess { }

    @Config(name = "client")
    private static class Client implements ConfigData, HexKineticsConfig.ClientConfigAccess { }


    @Config(name = "server")
    private static class Server implements ConfigData, HexKineticsConfig.ServerConfigAccess {

        @ConfigEntry.Gui.CollapsibleObject
        private SettingsTranslocation settingsTranslocation = new SettingsTranslocation();

        @Override
        public boolean getMoveTileEntities() {
            return false;
        }

        @Override
        public boolean isTranslocationAllowed(@NotNull ResourceLocation blockId) {
            return false;
        }

        static class SettingsTranslocation {
            // costs of misc spells
            double exampleConstActionCost = 0.0;
            boolean moveTileEntities = DEFAULT_MOVE_TILE_ENTITIES;

            @ConfigEntry.Gui.Tooltip
            private List<String> translocationDenyList = HexKineticsConfig.ServerConfigAccess.Companion.getDEFAULT_TRANSLOCATION_DENY_LIST();
        }

        @Override
        public void validatePostLoad() throws ValidationException {
            // costs of misc spells
            this.settingsTranslocation.exampleConstActionCost = bound(this.settingsTranslocation.exampleConstActionCost, DEF_MIN_COST, DEF_MAX_COST);
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
            return (int) (settingsTranslocation.exampleConstActionCost * MediaConstants.DUST_UNIT);
        }
        //endregion
    }
}
