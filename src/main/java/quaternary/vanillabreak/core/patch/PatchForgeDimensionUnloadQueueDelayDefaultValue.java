package quaternary.vanillabreak.core.patch;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Collection;

public class PatchForgeDimensionUnloadQueueDelayDefaultValue extends AbstractPatch {
	@Override
	protected Collection<String> computeAffectedClassNames() {
		return ImmutableList.of("net.minecraftforge.common.ForgeModContainer");
	}
	
	@Override
	protected String getLogMessage(String transformedClassName) {
		return "Fixing Forge's dimension unload timer config default value...";
	}
	
	@Override
	protected void doPatch(ClassNode node, String transformedClassName) {
		for(MethodNode method : node.methods) {
			//Forge method so no obf here
			if(method.name.equals("syncConfig")) {
				InsnList instructions = method.instructions;
				boolean foundIconsts = false;
				boolean foundString = false;
				
				for(int i = 0; i < instructions.size(); i++) {
					AbstractInsnNode instruction = instructions.get(i);
					if(!(instruction instanceof LdcInsnNode)) continue;
					LdcInsnNode ldc = (LdcInsnNode) instruction;
					if(!(ldc.cst instanceof String)) continue;
					String cst = (String) ldc.cst;
					
					if(cst.equals("dimensionUnloadQueueDelay")) {
						//Replace the next 2 occurrences of ICONST_0 with LDC 300
						int foundIconst0s = 0;
						for(int j = i; j < instructions.size() && foundIconst0s < 2; j++) {
							instruction = instructions.get(j);
							if(instruction.getOpcode() == ICONST_0) {
								instructions.set(instruction, new LdcInsnNode(300));
								foundIconst0s++;
							}
						}
						
						foundIconsts = true;
					} else if(cst.endsWith("a nether portal a few time per second.")) {
						ldc.cst = cst.concat("\n\n!!! Note that VanillaBreak patches this default value to 300, not Forge's 0!");
						foundString = true;
					}
					
					if(foundIconsts && foundString) break;
				}
			} else if(method.name.equals("<clinit>")) {
				//For good measure, let's replace the 0 set in the class initializer as well
				InsnList instructions = method.instructions;
				for(int i = 0; i < instructions.size(); i++) {
					AbstractInsnNode instruction = instructions.get(i);
					if(instruction instanceof FieldInsnNode && ((FieldInsnNode)instruction).name.equals("dimensionUnloadQueueDelay")) {
						//Replace the iconst_0 right before it
						instructions.set(instruction.getPrevious(), new LdcInsnNode(300));
						break;
					}
				}
			}
		}
	}
	
	@Override
	public String getPatchConfigName() {
		return "FixForgeDimensionUnloadQueueDelayDefaultValue";
	}
	
	@Override
	public String getPatchConfigComment() {
		return "If enabled, dimensions will unload 300 ticks after the last thing goes through a portal, compared to Forge's default of 0. This is actually configurable in the Forge config, all this patch does is change the *default* value of it. If you had already launched Forge without this mod, you will have to fix the default value manually.";
	}
}
