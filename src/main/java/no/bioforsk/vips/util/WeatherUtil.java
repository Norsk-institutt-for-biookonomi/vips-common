package no.bioforsk.vips.util;

/**
 * Weather related utility methods
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherUtil {
    
    /**
     * Calculates Water Vapor Deficiency in Pa
     * @param temperature
     * @param relative humidity
     * @return Water Vapor Deficiency in Pa
     */
    public double getWVD(double temperature, double relativeHumidity) {
        double saturationPressure = this.getSaturationPressure(temperature);
        double partialPressure = this.getPartialPressure(saturationPressure, relativeHumidity);
        /*
        if(this.DEBUG)
        System.out.println("[BlightModelActivity] DEBUG: saturationPressure=" + saturationPressure + ", partialPressure=" + partialPressure + ", WVD=" + ((saturationPressure - partialPressure) * 1000));
         * */
        return (saturationPressure - partialPressure) * 1000;
    }

    /**
     * Calculates the partial pressure
     * @param saturationPressure in kPa
     * @param RH
     * @return partial pressure in kPa
     */
    public double getPartialPressure(double saturationPressure, double relativeHumidity) {
        return relativeHumidity * saturationPressure / 100;
    }

    /**
     * Calculates the saturation pressure, based on temperature
     * @param temperature
     * @return saturation pressure in kPa
     */
    public double getSaturationPressure(double temperature) {
        return 0.61078 * Math.exp(17.269 * temperature / (temperature + 237.3));
    }
    
    
}
