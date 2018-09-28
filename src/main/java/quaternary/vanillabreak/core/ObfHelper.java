package quaternary.vanillabreak.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObfHelper {
	private static final Map<String, List<Pair<String, String>>> methods = new HashMap<>();
	
	static {
		putMapping("net.minecraft.tileentity.TileEntityChest", "checkForAdjacentChests", "func_145979_i()V");
		putMapping("net.minecraft.village.Village", "removeDeadAndOutOfRangeDoors", "func_75557_k()V");
		putMapping("net.minecraft.block.BlockChest", "getContainer", "func_189418_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)Lnet/minecraft/world/ILockableContainer;"); //WHY IS IT SO LONG
	}
	
	static void putMapping(String className, String deobfName, String obfName) {
		methods.computeIfAbsent(className, g -> new ArrayList<>()).add(Pair.of(deobfName, obfName));
	}
	
	public static boolean methodEquals(String possiblyObfName, String deobfName, String className) {
		if(!methods.containsKey(className)) return false;
		
		//TODO this is fucking spaghetti LMAO let's use vanillabreakcore.isDeobf like a big boy.
		for(Pair<String, String> m : methods.get(className)) {
			if((m.getLeft().equals(possiblyObfName) || m.getRight().equals(possiblyObfName)) && m.getLeft().equals(deobfName)) {
				return true;
			}
		}
		
		return false;
	}
}
