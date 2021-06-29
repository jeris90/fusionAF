package fusion.distance;

import java.util.Collection;
import java.util.HashSet;

/***
 * Class allowing the instantiation of a Jaccard distance (d(X,Y) = |X intersection Y| / |X union Y|).
 * As a reminder, this distance returns 1 if the two sets are similar, so to be consistent with the pseudo-distance criteria, 
 * we subtract this index from 1 : d(X,Y) = 1 - (|X intersection Y| / |X union Y|)
 */
public class DistanceJaccard extends Distance {

	@Override
	public float computeDistance(Collection<String> ext, Collection<String> mod) {
		
		Collection<String> union = new HashSet<String>(ext);
		Collection<String> intersection = new HashSet<String>(ext);
		
		union.addAll(mod);
		intersection.retainAll(mod);
		
		if(union.size() == 0)
			return 0;
		
		
		return (float) (1.0 - (intersection.size() / union.size()));
	}

}
