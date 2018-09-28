package quaternary.vanillabreak.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import quaternary.vanillabreak.core.patch.AbstractPatch;

import java.io.File;

public class VanillaBreakCoreConfig {
	private static int version = 1;
	
	private static Configuration config;
	
	public static void readConfigFromFile(File file) {
		config = new Configuration(file, Integer.toString(version));
	}
	
	public static boolean isPatchEnabled(AbstractPatch patch) {
		Property prop = config.get(patch.getConfigCategory(), patch.getPatchConfigName(), patch.isEnabledByDefault(), patch.getPatchConfigComment());
		prop.setRequiresMcRestart(true);
		
		return prop.getBoolean();
	}
	
	public static void save() {
		config.save();
	}
}
