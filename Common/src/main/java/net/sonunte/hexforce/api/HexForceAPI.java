package net.sonunte.hexforce.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface HexForceAPI
{
	String MOD_ID = "hexforce";
	Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	Supplier<HexForceAPI> INSTANCE = Suppliers.memoize(() -> {
		try {
			return (HexForceAPI) Class.forName("net.sonunte.hexforce.common.impl.HexForceAPIImpl")
								 .getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find HexForceAPIImpl, using a dummy");
			return new HexForceAPI() {
			};
		}
	});
	
	static HexForceAPI instance() {
		return INSTANCE.get();
	}
	
	static ResourceLocation modLoc(String s) {
		return new ResourceLocation(MOD_ID, s);
	}
}
