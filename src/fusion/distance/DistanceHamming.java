package fusion.distance;
import java.util.Collection;

public class DistanceHamming extends Distance {

    // Function to calculate the hamming distance 
    public float computeDistance(Collection<String> Ext, Collection<String> Mod) {
        float difference = 0f;
        for (String i : Ext) {
            if (!Mod.contains(i)) {
                difference++;
            }
        }
        for (String i : Mod) {
            if (!Ext.contains(i)) {
                difference++;
            }
        }

        return difference;
    }

 

}
