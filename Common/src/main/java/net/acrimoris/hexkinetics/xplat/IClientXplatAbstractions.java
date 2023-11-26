package net.acrimoris.hexkinetics.xplat;

import at.petrak.hexcasting.common.network.IMessage;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.acrimoris.hexkinetics.api.HexKineticsAPI;

import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IClientXplatAbstractions {
	void sendPacketToServer(IMessage packet);
	
	void initPlatformSpecific();
	
	<T extends Entity> void registerEntityRenderer(EntityType<? extends T> type, EntityRendererProvider<T> renderer);
	
	<T extends ParticleOptions> void registerParticleType(ParticleType<T> type,
														  Function<SpriteSet, ParticleProvider<T>> factory);
	
	void registerItemProperty(Item item, ResourceLocation id, ItemPropertyFunction func);
	
	// On Forge, these are already exposed; on Fabric we do a mixin
	void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap);
	
	void restoreLastFilter(AbstractTexture texture);
	
	IClientXplatAbstractions INSTANCE = find();
	
	private static IClientXplatAbstractions find() {
		var providers = ServiceLoader.load(IClientXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(
							Collectors.joining(",", "[", "]"));
			throw new IllegalStateException(
							"There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			HexKineticsAPI.LOGGER.debug("Instantiating client xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
