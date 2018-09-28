package quaternary.vanillabreak.core.patch;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import quaternary.vanillabreak.core.ObfHelper;

import java.util.Collection;

public class PatchReintroduceVillageloading extends AbstractPatch {
	@Override
	protected Collection<String> computeAffectedClassNames() {
		return ImmutableList.of("net.minecraft.village.Village");
	}
	
	@Override
	protected String getLogMessage(String transformedClassName) {
		return "Reintroducing village loading...";
	}
	
	@Override
	protected void doPatch(ClassNode node, String transformedClassName) {
		for(MethodNode method : node.methods) {
			if(ObfHelper.methodEquals(method.name, "removeDeadAndOutOfRangeDoors", transformedClassName)) {
				InsnList instructions = method.instructions;
				for(int i = 0; i < instructions.size() - 1; i++) {
					//It just so happens that the Forge check is the first place in the method
					//where two INVOKEVIRTUAL instructions are in a row. That's nice, it means
					//I don't have to check the parameters of them.
					AbstractInsnNode here = instructions.get(i);
					AbstractInsnNode next = instructions.get(i + 1);
					
					if(here.getOpcode() == INVOKEVIRTUAL && next.getOpcode() == INVOKEVIRTUAL) {
						//Look upwards 5 opcodes, since that's the start of this Forge block.
						//And it goes on for 8 opcodes.
						//Is this shit ASM practices? Yes.
						for(int j = 0; j < 8; j++) {
							instructions.remove(instructions.get(i - 5));
						}
						
						return;
					}
				}
			}
		}
	}
	
	@Override
	public String getPatchConfigName() {
		return "ReintroduceVillageloading";
	}
	
	@Override
	public String getConfigCategory() {
		return "chunkloading";
	}
	
	@Override
	public String getPatchConfigComment() {
		return "If enabled, villages will tend to chunkload themselves";
	}
}
