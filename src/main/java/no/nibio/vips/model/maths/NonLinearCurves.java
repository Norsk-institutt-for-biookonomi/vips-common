/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with VIPSCommon.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.model.maths;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class NonLinearCurves {
    
    /**
     * Based on Enkegaard (1993). Source: Ranjbar et al. (2011)
     * @param T temperature (degrees Celcius)
     * @param a empirical constant
     * @param b empirical constant
     * @param c empirical constant
     * @param d empirical constant
     * @return developmental rate
     */
    public Double calculateEnkegaard(Double T, Double a, Double b, Double c, Double d)
    {
        return (a + b * T) * (Math.exp(-1 * (c + d * T)));
    }
    
    /**
     * Based on Bieri et.al (1983). Source: Ranjbar et al. (2011)
     * @param T temperature (degrees Celcius)
     * @param a empirical constant
     * @param b empirical constant
     * @param xMin empirical constant: upper temperature threshold
     * @param xMax empirical constant: lower temperature threshold
     * @return developmental rate
     */
    public Double calculateBieri_1(Double T, Double a, Double b, Double xMin, Double xMax)
    {
        return (a * (T - xMax)) - (Math.pow(b, T - xMin));
    }
    
    /**
     * Based on Bieri et.al (1983). Source: Ranjbar et al. (2011)
     * @param T temperature (degrees Celcius)
     * @param a empirical constant
     * @param b empirical constant
     * @param xMin empirical constant: lower temperature threshold
     * @return developmental rate
     */
    public Double calculateBieri_2(Double T, Double a, Double b, Double xMin)
    {
        return a * ((T - xMin)/(Math.pow(b, T - xMin)));
    }
    
    /**
     * Based on Logan et al (1976): An Analytic Model for Description of Temperature
     * Dependent Rate Phenomena in Arthropods. This is function #6 from this publication.
     * 
     * @param T temperature (degrees Celcius)
     * @param p1 empirical constant
     * @param p2 empirical constant
     * @param p3 empirical constant
     * @param tMin the lower temperature threshold
     * @param tMax the upper temperature threshold
     * @return 
     */
    public Double calculateLogan(Double T, Double p1, Double p2, Double p3, Double tMin, Double tMax)
    {
        return p1 * (Math.exp(p2 * (T-tMin)) - Math.exp((p2 * (tMax - tMin)) - p3 * (tMax-T)));
    }
    
    /**
     * <p>Based on Baskerville et al (1969): Rapid estimation of heat accumulation
     * from maximum and minimum temperatures. Ref Figure 1</p>
     * <p>Formulas from Zalom et al 1983: Degree-days: The calculation and use
     * of heat units in pest managemeng. Division of Agriculture and Natural resources,
     * University of California. Leaflet 21373</p>
     * @param Tmin minimum value of sine wave
     * @param Tmax maximum value of sine wave
     * @param Tl lower threshold of accumulation
     * @param Tu upper threshold of accumulation
     * @return 
     */
    public Double calculateSingleSineWaveWithCutoff(Double Tmin, Double Tmax, Double Tl, Double Tu)
    {
        // Case 1: Tmax and Tmin are both above Tl and Tu
        if(Tu != null && Tmin >= Tu)
        {
            return Tu - Tl;
        }
        // Case 2: Tmax and Tmin are both below Tl and Tu
        if(Tmax <= Tl)
        {
            return 0.0;
        }
        
        Double m = (Tmax+Tmin)/2;
        
        // Case 3: Tmax and Tmin are between Tl and Tu
        if(Tmin >= Tl && (Tu == null || Tmax <= Tu))
        {
            return m - Tl;
        }
        
        Double alpha = (Tmax-Tmin)/2;
        
        Double theta_1 = Math.asin((Tl - m) / alpha);
        
        // Case 4: Tmin below Tl, Tmax below Tu
        if(Tmin <= Tl && (Tu == null || Tmax <= Tu))
        {
            return (1/Math.PI) * (
                    (m-Tl) * (Math.PI/2 - theta_1) 
                    + alpha * Math.cos(theta_1)
                    );
        }
        
        Double theta_2 = Math.asin((Tu - m) / alpha);
        
        // Case 5: Tmin above Tl and Tmax above Tu
        if(Tmin > Tl && Tu != null && Tmax > Tu)
        {
            return (1/Math.PI) * ( 
                    (m-Tl)*(theta_2 + Math.PI/2) 
                    + (Tu-Tl)*(Math.PI/2-theta_2) 
                    - Math.acos(theta_2) 
                    );
        }
        
        // Case 6: Tmin below Tl, Tmax above Tu
        if(Tmin <= Tl && Tu != null && Tmax >= Tu)
        {
            return (1/Math.PI) * ( 
                      (m-Tl) * (theta_2 - theta_1)
                      + alpha * (Math.cos(theta_1) - Math.cos(theta_2))
                      + (Tu-Tl) * (Math.PI/2 - theta_2)
                    );
        }
        
        return null;
        
    }

}
