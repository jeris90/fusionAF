package fusion.distance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import fusion.Models;
import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;

public class CalculDistance {

	/***
	 * Return the set of extensions of a given AF with respect to a given extension.
	 * @param af
	 * @param semantics
	 * @return
	 */
	public static HashSet<HashSet<String>> computeExtensions(DungAF af, String semantics){
		HashSet<HashSet<String>> ext = null;
		
		switch (semantics) {
			case "co":
				ext = af.getCompleteExts();
				break;
			case "st":
				ext = af.getStableExts();
				break;
			case "gr":
				ext = new HashSet<HashSet<String>>();
				ext.add(af.getGroundedExt());
				break;
			case "pr":
				ext = af.getPreferredExts();
				break;
			case "sst":
				ext = af.getSemiStableExts();
				break;
			default:
				System.err.println("Error concerning an unknown extension of a file containing an AF.");
				System.exit(1);
		}
		
		return ext;
	}
	
	public static void calculDistance(DungAF af, Models mod, Distance distance, String semantic) {
		float dist = 0;
		Object min = null;
		Vector<Float> vec_distance = new Vector<Float>();
		Vector<Float> min_distance = new Vector<Float>(); // vector to find the minimum distance of a model
		
		HashSet<HashSet<String>> extensions = computeExtensions(af,semantic);
		
		System.out.println(extensions);
		
		// browse the models
		for (Collection<String> current_mod : mod.getModels()) {
			for (Collection<String> current_ext : extensions) {
				dist = distance.computeDistance(current_ext, current_mod);
				min_distance.addElement(dist);
			}
			
			min = Collections.min(min_distance);
			vec_distance.addElement((Float) min);
			min_distance.clear();
		}
		
		mod.addDistance(vec_distance);
	}

}
