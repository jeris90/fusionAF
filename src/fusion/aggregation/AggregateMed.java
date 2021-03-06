package fusion.aggregation;

import java.util.Collections;
import java.util.Vector;

import fusion.Models;
//Median aggregation function
public class AggregateMed extends AggregationFunction {

	public static double findMedian(Vector<Float> vector) {
		Collections.sort(vector);
		int m = vector.size();
		if (m % 2 != 0)
			return vector.get(m / 2);
		return (vector.get((m - 1) / 2) + vector.get(m / 2)) / 2.0;
	}

	@Override
	public Vector<Float> aggregate(Models mod) {
		Vector<Float> vecMedaine = new Vector<>();
		for (Vector<Float> model : Models.transpose(mod.getDistance())) {
			float med = (float) findMedian(model);
			vecMedaine.add(med);
		}
		return vecMedaine;
	}
}
