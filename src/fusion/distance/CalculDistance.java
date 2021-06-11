package fusion.distance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import fusion.Models;
import net.sf.jargsemsat.jargsemsat.datastructures.DungAF;

public class CalculDistance {

	public static void calculDistance(DungAF af, Models mod, Distance distance, String semantic) {
		float dist = 0;
		Object min = null;
		Vector<Float> vec_distance = new Vector<Float>();
		Vector<Float> Min_distance = new Vector<Float>();
		
		// browse the models
		for (Collection<String> current_mod : mod.getModels()) {
			
			HashSet<HashSet<String>> ext = null;
			
			switch (semantic) {
				case "CO":
					ext = af.getCompleteExts();
					break;
				case "ST":
					ext = af.getStableExts();
					break;
				case "GR":
					ext = new HashSet<HashSet<String>>();
					ext.add(af.getGroundedExt());
					break;
				case "PR":
					ext = af.getPreferredExts();
					break;
			}
			
			for (Collection<String> current_ext : ext) {
				dist = distance.computeDistance(current_ext, current_mod);
				// vector to find the minimum distance of a model
				Min_distance.addElement(dist);
			}
			
			min = Collections.min(Min_distance);
			vec_distance.addElement((Float) min);
			Min_distance.clear();
		}
		
		mod.setDistance(vec_distance);
	}

}
