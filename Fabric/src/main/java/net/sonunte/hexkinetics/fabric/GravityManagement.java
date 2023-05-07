package net.sonunte.hexkinetics.fabric;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.sonunte.hexkinetics.api.HexKineticsAPI;

import static net.sonunte.hexkinetics.common.casting.actions.great_spells.OpGreaterImpulse.tickAllEntities;

public class GravityManagement implements ModInitializer {

    @Override
    public void onInitialize() {
        HexKineticsAPI.LOGGER.info("GravityManagement On");
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickAllEntities((ServerLevel) server.getWorldData());
            HexKineticsAPI.LOGGER.info("Executing tickAllEntities() every tick");
        });
    }
}
