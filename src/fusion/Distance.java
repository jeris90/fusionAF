package fusion;

import java.util.Collection;
//Abstract Distance function
public abstract class Distance {
	public abstract int computeDistance(Collection<String> Ext, Collection<String> Mod);
}
