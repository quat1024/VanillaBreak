package quaternary.vanillabreak.core.patch;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;

public abstract class AbstractPatch implements Opcodes {
	protected abstract Collection<String> computeAffectedClassNames();
	protected abstract String getLogMessage(String transformedClassName);
	protected abstract void doPatch(ClassNode node, String transformedClassName);
	
	public abstract String getPatchConfigName();
	public abstract String getPatchConfigComment();
	
	public String getConfigCategory() {
		return "patches";
	}
	
	public boolean isEnabledByDefault() {
		return true;
	}
	
	private Collection<String> affectedClassNames = computeAffectedClassNames();
	
	public Collection<String> getAffectedClassNames() {
		return affectedClassNames;
	}
	
	public void process(ClassNode node, String transformedClassName) {
		if(affectedClassNames.contains(transformedClassName)) {
			LogManager.getLogger("VanillaBreak ASM").info(getLogMessage(transformedClassName));
			doPatch(node, transformedClassName);
		}
	}
}
