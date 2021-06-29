package fusion.distance;

import java.util.Collection;


public abstract class Distance {
	
	/***
	 * Calculates a distance between two sets of arguments (an argument is represented by a String).
	 * IMPORTANT: The smaller the distance between the two sets, the more similar the sets are. 
	 * Thus a distance of 0 means that the two sets of arguments are identical (d(x,y)=0 iff x=y). 
	 * In addition, the distance must be symmetrical (d(x,y) = d(y,x)).
	 * @param ext a set of arguments (an extension)
	 * @param mod a set of arguments (a candidate)
	 * @return a real number representing the distance between ext and mod
	 */
	public abstract float computeDistance(Collection<String> ext, Collection<String> mod);
}
