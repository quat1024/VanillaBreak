package quaternary.vanillabreak.core.patch;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import quaternary.vanillabreak.core.ObfHelper;

import java.util.Collection;

public class PatchReintroduceChestloading extends AbstractPatch {
	@Override
	protected Collection<String> computeAffectedClassNames() {
		return ImmutableList.of("net.minecraft.tileentity.TileEntityChest");
	}
	
	@Override
	protected String getLogMessage(String transformedClassName) {
		return "Reintroducing chest chunkloading...";
	}
	
	@Override
	protected void doPatch(ClassNode node, String transformedClassName) {
		for(MethodNode method : node.methods) {
			if(ObfHelper.methodEquals(method.name, "checkForAdjacentChests", transformedClassName)) {
				
				//Overwrite the first RETURN (which is the one after the forge check) to a nop, effectively skipping the check
				InsnList instructions = method.instructions;
				for(int i = 0; i < instructions.size(); i++) {
					AbstractInsnNode instruction = instructions.get(i);
					if(instruction.getOpcode() == RETURN) {
						instructions.set(instruction, new InsnNode(NOP));
						return;
					}
				}
			}
		}
	}
	
	@Override
	public String getPatchConfigName() {
		return "ReintroduceChestloading";
	}
	
	@Override
	public String getConfigCategory() {
		return "chunkloading";
	}
	
	@Override
	public String getPatchConfigComment() {
		return "If enabled, chests are able to load chunks when they are along a chunk border and searching to see if they are double chests";
	}
}
