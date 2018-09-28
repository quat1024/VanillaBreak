package quaternary.vanillabreak.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1337)
@IFMLLoadingPlugin.TransformerExclusions("quaternary.vanillabreak.core")
@IFMLLoadingPlugin.Name("VanillaBreak - Don't you dare report bugs to Forge!")
public class VanillaBreakCore implements IFMLLoadingPlugin {
	
	public static boolean isDeobf = false;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"quaternary.vanillabreak.core.VanillaBreakTransformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		isDeobf = !((boolean) data.get("runtimeDeobfuscationEnabled"));
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
