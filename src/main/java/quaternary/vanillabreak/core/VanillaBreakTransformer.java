package quaternary.vanillabreak.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import quaternary.vanillabreak.core.patch.AbstractPatch;
import quaternary.vanillabreak.core.patch.PatchForgeDimensionUnloadQueueDelayDefaultValue;
import quaternary.vanillabreak.core.patch.PatchReintroduceChestloading;
import quaternary.vanillabreak.core.patch.PatchReintroduceVillageloading;
import quaternary.vanillabreak.core.patch.PatchRestoreVanillaHopperInteractionsWithBlockedChest;
import quaternary.vanillabreak.core.patch.PatchUseSameChestBlockingBehaviorWithVanillaAndModded;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class VanillaBreakTransformer implements IClassTransformer {
	private static Collection<AbstractPatch> patches = new ArrayList<>();
	private static Collection<String> classNames = new HashSet<>();
	
	static {
		VanillaBreakCoreConfig.readConfigFromFile(new File(new File("config"), "vanillabreak_core.cfg"));
		
		patches.add(new PatchReintroduceChestloading());
		patches.add(new PatchReintroduceVillageloading());
		patches.add(new PatchForgeDimensionUnloadQueueDelayDefaultValue());
		patches.add(new PatchUseSameChestBlockingBehaviorWithVanillaAndModded());
		//patches.add(new PatchRestoreVanillaHopperInteractionsWithBlockedChest());
		
		//Remove patches that the user has disabled
		//(This also creates all the config entries)
		patches.removeIf(patch -> !VanillaBreakCoreConfig.isPatchEnabled(patch));
		VanillaBreakCoreConfig.save(); //flush config to disk
		
		//Compute a list of classes to check
		patches.forEach(patch -> classNames.addAll(patch.getAffectedClassNames()));
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		//Why null check? If another class transformer errors this will keep me from NPEing on it
		//and making fake errors ;)
		if(basicClass == null || !classNames.contains(transformedName)) return basicClass;
		
		//Create a classnode for easier class manipulation
		ClassReader reader = new ClassReader(basicClass);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		//Feed the patcher beast
		patches.forEach(patch -> patch.process(node, transformedName));
		
		//Write it back to a byte[]
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}
}
