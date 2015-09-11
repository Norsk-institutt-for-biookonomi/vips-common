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

import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class NonLinearCurvesTest extends TestCase {
    
    public NonLinearCurvesTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of calculateEnkegaard method, of class NonLinearCurves.
     * Test numbers are from Ranjbar et al (2011), table 3
     */
    public void testCalculateEnkegaard() {
        System.out.println("calculateEnkegaard");
        Double T = null;
        Double a = 6.0947;
        Double b = -0.1663;
        Double c = 6.5921;
        Double d = -0.171;
        NonLinearCurves instance = new NonLinearCurves();
        Double expResult = null;
        
        // Preliminary testing
        /*for(Double Ti = 5.0; Ti< 39.0; Ti++)
        {
            System.out.println("r(" + Ti + ") = " + instance.calculateEnkegaard(Ti, a, b, c, d));
        }*/
        
        Double result = instance.calculateEnkegaard(30.0, a, b, c, d);
        // Expect optimum to be 30.81
        assertEquals(0.25624496911766853, result);
        result = instance.calculateEnkegaard(30.81, a, b, c, d);
        assertEquals(0.2584580449102842, result);
        result = instance.calculateEnkegaard(31.0, a, b, c, d);
        assertEquals(0.25830508171273814, result);
        
    }

    /**
     * Test of calculateBieri_1 method, of class NonLinearCurves.
     */
    public void testCalculateBieri_1() {
        System.out.println("calculateBieri_1");
        Double T = null;
        Double a = 0.0137;
        Double b = 1.6976;
        Double xMin = 38.0970;
        Double xMax = 10.6727;
        NonLinearCurves instance = new NonLinearCurves();
        
        
        // Preliminary testing
        /*for(Double Ti = 5.0; Ti< 39.0; Ti++)
        {
            System.out.println("r(" + Ti + ") = " + instance.calculateBieri_1(Ti, a, b, xMin, xMax));
        }*/
        Double expResult = 0.25510326186604604;
        
        Double result = instance.calculateBieri_1(31.0, a, b, xMin, xMax);
        assertEquals(0.25510326186604604, result);
        
        // Optimum
        result = instance.calculateBieri_1(31.19, a, b, xMin, xMax);
        assertEquals(0.2552330469855961, result);
        //tMax
        result = instance.calculateBieri_1(36.10, a, b, xMin, xMax);
        assertEquals(0.0008028254964528858, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of calculateBieri_2 method, of class NonLinearCurves.
     */
    public void testCalculateBieri_2() {
        System.out.println("calculateBieri_2");
        Double T = null;
        Double a = -0.1203;
        Double b = 0.8428;
        Double xMin = 36.6526;
        NonLinearCurves instance = new NonLinearCurves();
        
        /*for(Double Ti = 5.0; Ti< 39.0; Ti++)
        {
            System.out.println("r(" + Ti + ") = " + instance.calculateBieri_2(Ti, a, b, xMin));
        }*/
        Double expResult = null;
        Double result = instance.calculateBieri_2(31.0, a, b, xMin);
        assertEquals(0.25862133099558676, result);
        result = instance.calculateBieri_2(36.6526, a, b, xMin);
        assertEquals(-0.0, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of calculateLogan method, of class NonLinearCurves.
     * The estimated parameter values (p1,p2,p3,tMin,tMax) are from Berardinis et al (1992):
     * A phenological forecasting model for the apple and pear leafroller
     * Argyrotaenia Pulchellana
     */
    public void testCalculateLogan() {
        System.out.println("calculateLogan");
        Double T = null;
        Double p1 = 0.13289;
        Double p2 = 0.16517;
        Double p3 = 0.17339;
        Double tMin = 8.7;
        Double tMax = 34.5;
        NonLinearCurves instance = new NonLinearCurves();
        
        /*for(Double Ti = 5.0; Ti< 39.0; Ti++)
        {
            System.out.println("r(" + Ti + ") = " + instance.calculateLogan(Ti, p1, p2, p3,tMin, tMax));
        }*/
        
        Double expResult = 0.0;
        Double result = instance.calculateLogan(34.5, p1, p2, p3, tMin, tMax);
        assertEquals(expResult, result);
        
    }
    
}
