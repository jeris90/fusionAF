package fusion;

import java.util.Collections;
import java.util.Vector;

public class AggregateLexiMax extends AggregationFunction {

	/***
	 * Lexicographically compares two vectors (starting from the end to apply the
	 * leximax) containing the same number of elements. For instance, [3,2,1] >_lex
	 * [2,2,0] [4,3,1] >_lex [4,3,0] [3,3,1] =_lex [3,3,1]
	 * 
	 * @param vec1 a vector containing n positive real numbers
	 * @param vec2 another vector containing n positive real numbers
	 * @return 1 if vec1 >_lex vec2 -1 if vec2 >_lex vec1 0 if vec1 = vec2
	 * @throws IllegalArgumentException if the vectors have different sizes
	 */
	public static int compareLex(Vector<Float> vec1, Vector<Float> vec2) {
		if (vec1.size() != vec2.size())
			throw new IllegalArgumentException("LexiMax: Vectors must have the same number of elements.");
		for (int i = vec1.size() - 1; i > 0; i--) {
			if (vec1.get(i) > vec2.get(i))
				return 1;
			if (vec1.get(i) < vec2.get(i))
				return -1;
		}
		return 0;
	}

	@Override
	public Vector<Float> aggregate(Models mod) {

		float ranking = 0f;

		int res = 0;

		float current_rank = 0f;

		Vector<Float> result = new Vector<>();

		// We compute the transpose of the calculated distances in order to have, for
		// each candidate,
		// a vector containing the distance to each of the AFs in the profile.
		Vector<Vector<Float>> vTrans = Models.transpose(mod.getDistance());

		// Each of these vectors is then sorted in order to apply the leximax
		for (int i = 0; i < vTrans.size(); i++) {
			Collections.sort(vTrans.get(i));
		}

		result.add((float) 0.0);

		for (int i = 1; i < vTrans.size(); i++) {
			ranking = 0f;
			for (int j = 0; j < i; j++) {

				res = compareLex(vTrans.get(i), vTrans.get(j));

				if (res == 0) {
					ranking = result.get(j);
					break;
				} else {
					if (res == 1) {
						ranking++;
					} else {
						current_rank = result.get(j);
						result.set(j, current_rank + 1);
					}
				}
			}
			result.add(ranking);
		}

		// System.out.println(result);
		return result;
	}
}
