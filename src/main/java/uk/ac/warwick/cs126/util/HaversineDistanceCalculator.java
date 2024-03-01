package uk.ac.warwick.cs126.util;

/** Class for implementing HaversineDisatanceCalculator and its operations. */
public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    /** Calculates distance in Kilometres
     * 
     * @param lat1                  Latitude coordinate of first Restaurant.
     * @param lon1                  Longitude coordinate of first Restaurant.
     * @param lat2                  Latitude coordinate of second Restaurant.
     * @param lon2                  Longitude coordinate of second Restaurant. 
     * 
     * Uses java.lang.Math commands which are imported by default.
     * Conversion to double to not lose accuracy in calculations.
     * 
     * @return Value for distance in km to 1 decimal place. 
    */
    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        lat1 = (float)Math.toRadians(lat1);
        lon1 = (float)Math.toRadians(lon1);
        lat2 = (float)Math.toRadians(lat2);
        lon2 = (float)Math.toRadians(lon2);
        float x = (lat2-lat1)/2;
        float y = (lon1-lon2)/2;
        float x1 = (float)Math.pow(x,2);
        float y1 = (float)Math.pow(y,2);
        float x2 = (float)Math.sin(x1);
        float y2 = (float)Math.sin(y1);
        float z1 = (float)Math.cos(lat1);
        float z2 = (float)Math.cos(lat2);
        float b = z1*z2*y2;
        float a = x2 + b;
        float c1 = (float)Math.sqrt(a);
        float c2 = (float)Math.asin(c1);
        float c = 2*c2;
        double d = (double)R*c;
        d = Math.round(d*10)/10.0;
        return (float)d;
    }

    /** Calculates distance in Miles
     * 
     * @param lat1                  Latitude coordinate of first Restaurant.
     * @param lon1                  Longitude coordinate of first Restaurant.
     * @param lat2                  Latitude coordinate of second Restaurant.
     * @param lon2                  Longitude coordinate of second Restaurant. 
     * 
     * Uses java.lang.Math commands which are imported by default.
     * Conversion to double to not lose accuracy in calculations.
     * Divides by km to mi conversion value. 
     * 
     * @return Value for distance in mi to 1 decimal place. 
    */
    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        lat1 = (float)Math.toRadians(lat1);
        lon1 = (float)Math.toRadians(lon1);
        lat2 = (float)Math.toRadians(lat2);
        lon2 = (float)Math.toRadians(lon2);
        float x = (lat2-lat1)/2;
        float y = (lon1-lon2)/2;
        float x1 = (float)Math.pow(x,2);
        float y1 = (float)Math.pow(y,2);
        float x2 = (float)Math.sin(x1);
        float y2 = (float)Math.sin(y1);
        float z1 = (float)Math.cos(lat1);
        float z2 = (float)Math.cos(lat2);
        float b = z1*z2*y2;
        float a = x2 + b;
        float c1 = (float)Math.sqrt(a);
        float c2 = (float)Math.asin(c1);
        float c = 2*c2;
        double d = (double)R*c;
        d = (double)((float)d/kilometresInAMile);
        d = Math.round(d*10)/10.0;
        return (float)d;
    }

}