package net.sonunte.hexkinetics.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import thedarkcolour.kotlinforforge.KotlinModLoadingContext;
import net.sonunte.hexkinetics.api.HexKineticsAPI;
import net.sonunte.hexkinetics.api.config.HexKineticsConfig;
import net.sonunte.hexkinetics.common.casting.Patterns;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(HexKineticsAPI.MOD_ID)
public class ForgeHexKineticsInitializer {
	
	public ForgeHexKineticsInitializer() {
		HexKineticsAPI.LOGGER.info("Hello Forge World!");
		initConfig();
		initRegistry();
		initListeners();
	}
	
	private static void initConfig () {
		Pair<ForgeHexKineticsConfig, ForgeConfigSpec> config = (new ForgeConfigSpec.Builder()).configure(ForgeHexKineticsConfig::new);
		Pair<ForgeHexKineticsConfig.Client, ForgeConfigSpec> clientConfig = (new ForgeConfigSpec.Builder()).configure(ForgeHexKineticsConfig.Client::new);
		Pair<ForgeHexKineticsConfig.Server, ForgeConfigSpec> serverConfig = (new ForgeConfigSpec.Builder()).configure(ForgeHexKineticsConfig.Server::new);
		HexKineticsConfig.setCommon(config.getLeft());
		HexKineticsConfig.setClient(clientConfig.getLeft());
		HexKineticsConfig.setServer(serverConfig.getLeft());
		ModLoadingContext mlc = ModLoadingContext.get();
		mlc.registerConfig(ModConfig.Type.COMMON, config.getRight());
		mlc.registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
		mlc.registerConfig(ModConfig.Type.SERVER, serverConfig.getRight());
	}
	
	private static void initRegistry () {
	}
	
	private static void initListeners () {
		IEventBus modBus = getModEventBus();
		IEventBus evBus = MinecraftForge.EVENT_BUS;
		
		modBus.register(ForgeHexKineticsClientInitializer.class);
		
		modBus.addListener((FMLCommonSetupEvent evt) -> evt.enqueueWork(Patterns::registerPatterns));
	}
	
	// https://github.com/VazkiiMods/Botania/blob/1.18.x/Forge/src/main/java/vazkii/botania/forge/ForgeCommonInitializer.java
	private static <T> void bind (ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		getModEventBus().addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}
	
	// This version of bind is used for BuiltinRegistries.
	private static <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		source.accept((t, id) -> Registry.register(registry, id, t));
	}
	
	private static IEventBus getModEventBus () {
		return KotlinModLoadingContext.Companion.get().getKEventBus();
	}
}