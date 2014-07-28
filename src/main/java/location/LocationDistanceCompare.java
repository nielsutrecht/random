package location;

import java.util.Comparator;

public class LocationDistanceCompare implements Comparator<Location> {

    private final double _lat;
    private final double _lon;

    public LocationDistanceCompare(final double lat, final double lon) {
        _lat = lat;
        _lon = lon;
    }

    @Override
    public int compare(final Location school1, final Location school2) {
        final double dist1 = school1.calcDistance(_lat, _lon);
        final double dist2 = school2.calcDistance(_lat, _lon);

        return Double.compare(dist1, dist2);
    }

}