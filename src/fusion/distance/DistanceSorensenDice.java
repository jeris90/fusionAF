package fusion.distance;

import java.util.Collection;
import java.util.HashSet;

/***
 * Class allowing the instantiation of a Sorensen-Dice distance (d(X,Y) = 2|X intersection Y| / (|X| + |Y|)).
 * As a reminder, this distance returns 1 if the two sets are similar, so to be consistent with the pseudo-distance criteria, 
 * we subtract this index from 1 : d(X,Y) = 1 - (2|X intersection Y| / (|X| + |Y|))
 */
public class DistanceSorensenDice extends Distance {

	@Override
	public float computeDistance(Collection<String> ext, Collection<String> mod) {
		Collection<String> intersection = new HashSet<String>(ext);
		
		intersection.retainAll(mod);
		
		double sumSize = (double)ext.size() + (double)mod.size();
		double intersection2 = 2F * (double)intersection.size();
		
		if(sumSize == 0)
			return 0;
		
		double res = 1.0 - (intersection2 / sumSize);
		
		return (float)res;
	}

}
