package quaternary.vanillabreak.core.patch;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Collection;

public class PatchUseSameChestBlockingBehaviorWithVanillaAndModded extends AbstractPatch {
	@Override
	protected Collection<String> computeAffectedClassNames() {
		return ImmutableList.of("net.minecraft.block.Block");
	}
	
	@Override
	protected String getLogMessage(String transformedClassName) {
		return "Changing behavior of chest opening under modded blocks to match vanilla's...";
	}
	
	@Override
	protected void doPatch(ClassNode node, String transformedClassName) {
		for(MethodNode method : node.methods) {
			//Forge added method so no Obf here
			if(method.name.equals("doesSideBlockChestOpening")) {
				//Basically just blank out this method and hardcode the vanilla path.
				//MASSIVE TODO: Actually test this since I don't have any other mods in this inst to try it with.
				method.instructions.clear();
				
				LabelNode lbl = new LabelNode();
				method.instructions.add(new LineNumberNode(9999, lbl));
				method.instructions.add(lbl);
				
				method.instructions.add(new VarInsnNode(ALOAD, 0));
				method.instructions.add(new VarInsnNode(ALOAD, 1));
				method.instructions.add(new VarInsnNode(ALOAD, 2));
				method.instructions.add(new VarInsnNode(ALOAD, 3));
				
				//also a forge added method, oddly enough
				MethodInsnNode isNormalCube = new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "isNormalCube", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z", false);
				method.instructions.add(isNormalCube);
				
				method.instructions.add(new InsnNode(IRETURN));
			}
		}
	}
	
	@Override
	public String getPatchConfigName() {
		return "UseSameChestBlockingBehaviorWithVanillaAndModdedBlocks";
	}
	
	@Override
	public String getPatchConfigComment() {
		return "If enabled, modded and vanilla blocks will use the same logic to determine if they are able to block a chest from opening or not.";
	}
}
