package fusion.distance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import fusion.Models;
import fusion.Print;
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
			case "apx":
			case "tgf":
				ext = af.getPreferredExts();
				break;
			default:
				System.err.println("Error concerning an unknown extension of a file containing an AF : " + ext);
				System.exit(1);
		}
		return ext;
	}
	
	/***
	 * This method computes, for a given AF, its set of extensions w.r.t. a given semantics before calculating its distance to 
	 * each existing model (candidate). 
	 * @param af
	 * @param models
	 * @param distance
	 * @param semantic
	 */
	public static void computeDistance(DungAF af, Models models, Distance distance, String semantic, boolean print) {
		float dist = 0;
		Object min = null;
		Vector<Float> vec_distance = new Vector<Float>();
		Vector<Float> min_distance = new Vector<Float>(); // vector to find the minimum distance of a model
		
		HashSet<HashSet<String>> extensions = computeExtensions(af,semantic);
		
		Print.print(print, extensions.toString());
		
		// browse the models
		for (Collection<String> current_mod : models.getModels()) {
			for (Collection<String> current_ext : extensions) {
				dist = distance.computeDistance(current_ext, current_mod);
				min_distance.addElement(dist);
			}
			
			min = Collections.min(min_distance);
			vec_distance.addElement((Float) min);
			min_distance.clear();
		}
		
		models.addDistance(vec_distance);
	}

}
