package fusion.distance;

import java.util.Collection;
//Abstract Distance function
public abstract class Distance {
	public abstract float computeDistance(Collection<String> Ext, Collection<String> Mod);
}
