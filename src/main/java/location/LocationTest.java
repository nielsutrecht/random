package location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationTest {
    public static void main(final String[] args) {
        final List<Location> schools = new ArrayList<>();
        final double lat = 52.029871;
        final double lon = 5.104306;
        schools.add(new Location("Amsterdam", 52.361531, 4.870846));
        schools.add(new Location("Maastricht", 50.847766, 5.683835));
        schools.add(new Location("Utrecht", 52.089315, 5.111873));

        Collections.sort(schools, new LocationDistanceCompare(lat, lon));

        for (final Location s : schools) {
            System.out.print(s);
            System.out.print(": ");
            System.out.println(String.format("%.2f km", s.calcDistance(lat, lon)));
        }
    }
}