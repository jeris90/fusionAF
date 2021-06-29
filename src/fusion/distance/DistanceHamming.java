package fusion.distance;
import java.util.Collection;

public class DistanceHamming extends Distance {

    // Function to calculate the hamming distance 
    public float computeDistance(Collection<String> ext, Collection<String> mod) {
        float difference = 0f;
        for (String i : ext) {
            if (!mod.contains(i)) {
                difference++;
            }
        }
        for (String i : mod) {
            if (!ext.contains(i)) {
                difference++;
            }
        }

        return difference;
    }
}
