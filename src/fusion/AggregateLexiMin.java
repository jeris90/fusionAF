package fusion;

import java.util.Collections;
import java.util.Vector;
//LexiMin aggregation function
public class AggregateLexiMin extends Aggregate_Function {


	public static Vector<Vector<Float>> cmpareVec(Vector<Float> vec1, Vector<Vector<Float>> vec2) {
		for (int i = 0; i < vec2.size(); i++) {
			for (int j = 0; j < vec2.get(i).size(); j++) {
				if (vec1.get(j) > vec2.get(i).get(j)) {
					vec2.removeElement(vec1);
					break;
				}
			}
		}
		return vec2;
	}
	
	public static int compareLex(Vector<Float> vec1, Vector<Float> vec2) {
		for(int i=0 ; i<vec1.size() ; i++) {
			if(vec1.get(i) > vec2.get(i))
				return 1;
			if(vec1.get(i) < vec2.get(i))
				return -1;
		}
		return 0;
	}
	
	@Override
	public Vector<Float> choosenAggregate(Models mod) {
	
		float ranking = 0f;
		
		int res = 0;
		
		float current_rank = 0f;
		
		Vector<Float> result = new Vector<>();
		Vector<Vector<Float>> vTrans = Models.transpose(mod.getDistance());
		
		for (int i = 0; i < vTrans.size(); i++) {
			Collections.sort(vTrans.get(i)); 
		}
		
		result.add((float) 0.0);
		for(int i = 1 ; i<vTrans.size(); i++) {
			ranking = 0f;
			for(int j = 0; j < i ; j++) {
				
				res = compareLex(vTrans.get(i),vTrans.get(j));
				
				if( res == 0) {
					ranking = result.get(j);
					break;
				}
				else {
					if(res == 1) {
						ranking++;
					}
					else {
						current_rank = result.get(j);
						result.set(j, current_rank + 1);
					}
				}
			}
			result.add(ranking);
		}
		
		System.out.println(result);
		
		/*lex2.addAll(vTrans);
		
		for (int i = 0; i < vTrans.size(); i++) {
			lex2 = cmpareVec(vTrans.get(i), lex2);
		}
		
		for (int i = 0; i < vTrans.size(); i++) {
			for (int j = 0; j < lex2.size(); j++) {
				if (vTrans.get(i) == lex2.get(j)) {
					result.set(i, (float) 0.0);
				}
			}
		}*/
		return result;
	}
	
	/*
	@Override
	public Vector<Float> choosenAggregate(Models mod) {
		Vector<Float> lex = new Vector<>();
		Vector<Vector<Float>> lex2 = new Vector<>();
		Vector<Vector<Float>> vTrans = Models.transpose(mod.getDistance());
		
		
		for (int i = 0; i < vTrans.size(); i++) {
			lex.add((float) 1.0);
		}
		for (int j = 0; j < vTrans.size(); j++) {
			Collections.sort(vTrans.get(j));
		}
		lex2.addAll(vTrans);
		for (int i = 0; i < vTrans.size(); i++) {
			lex2 = cmpareVec(vTrans.get(i), lex2);
		}
		for (int i = 0; i < vTrans.size(); i++) {
			for (int j = 0; j < lex2.size(); j++) {
				if (vTrans.get(i) == lex2.get(j)) {
					lex.set(i, (float) 0.0);
				}
			}
		}
		return lex;
	}
	*/
}
