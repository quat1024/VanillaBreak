package quaternary.vanillabreak.core.patch;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import quaternary.vanillabreak.core.ObfHelper;

import java.util.Collection;

public class PatchRestoreVanillaHopperInteractionsWithBlockedChest extends AbstractPatch {
	@Override
	protected Collection<String> computeAffectedClassNames() {
		return ImmutableList.of("net.minecraft.block.BlockChest");
	}
	
	@Override
	protected String getLogMessage(String transformedClassName) {
		return "Restoring vanilla behavior with hoppers pulling from blocked chests...";
	}
	
	@Override
	protected void doPatch(ClassNode node, String transformedClassName) {
		//This doesn't actually work xD
		/*
		for(MethodNode method : node.methods) {
			if(ObfHelper.methodEquals(method.name, "getContainer", transformedClassName)) {
				System.out.println("found getcontainer");
				InsnList instructions = method.instructions;
				
				int foundIload3s = 0;
				
				for(int i = 0; i < instructions.size(); i++) {
					AbstractInsnNode instruction = instructions.get(i);
					if(instruction instanceof VarInsnNode && instruction.getOpcode() == ILOAD && ((VarInsnNode)instruction).var == 3) { //"allowBlocking"
						System.out.println("found iload 3");
						foundIload3s++;
					}
					
					if(foundIload3s == 1) {
						//Forge adds a negated check for this.
						//This works out to "ILOAD 3, IFNE L5" and L5 is the "next iterator" routine.
						System.out.println("stomping");
						instructions.set(instruction, new InsnNode(ICONST_0));
						break;
					}
				}
			}
		}
		*/
	}
	
	@Override
	public String getPatchConfigName() {
		return "RestoreVanillaHopperInteractionsWithBlockedChest";
	}
	
	@Override
	public String getPatchConfigComment() {
		return "Restores vanilla behavior by reintroducing MC-99321, \"Hoppers cannot pull items from double chests if second chest is blocked\".";
	}
}
