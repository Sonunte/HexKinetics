package net.sonunte.hexkinetics.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface HexKineticsAPI
{
	String MOD_ID = "hexkinetics";
	Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	Supplier<HexKineticsAPI> INSTANCE = Suppliers.memoize(() -> {
		try {
			return (HexKineticsAPI) Class.forName("net.sonunte.hexkinetics.common.impl.HexKineticsAPIImpl")
								 .getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find HexKineticsAPIImpl, using a dummy");
			return new HexKineticsAPI() {
			};
		}
	});
	
	static HexKineticsAPI instance() {
		return INSTANCE.get();
	}
	
	static ResourceLocation modLoc(String s) {
		return new ResourceLocation(MOD_ID, s);
	}
}
