package location;

public class Location {
    private final double _lat;
    private final double _lon;
    private final String _name;

    public Location(final String name, final double lat, final double lon) {
        _name = name;
        _lat = lat;
        _lon = lon;
    }

    public double calcDistance(final double lat, final double lon) {
        final double earthRadius = 6371; //kilometers
        final double dLat = Math.toRadians(lat - _lat);
        final double dLng = Math.toRadians(lon - _lon);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(_lat)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final float dist = (float) (earthRadius * c);

        return dist;
    }

    @Override
    public String toString() {
        return String.format("%s (%f, %f)", _name, _lat, _lon);
    }
}