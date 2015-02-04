/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reda
 */
public class PreprocessingTest {
    
    public PreprocessingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initialization method, of class Preprocessing.
     */
    @Test
    public void testInitialization() {
        System.out.println("initialization");
        Preprocessing.initialization();
        
    }

    /**
     * Test of removePunctuations method, of class Preprocessing.
     */
    @Test
    public void testRemovepunctuation() {
        System.out.println("removepunctuation");
        
        String x ="fgtg r)rfre:erf-67%hwe \"Hii\" { 1987";
        String x1="fgtg r rfre erf 67 hwe  Hii    1987";
        
        assertEquals(Preprocessing.removePunctuations(x), x1);
        
                
    }
    
     /**
     * Test of replacePunctuations method, of class Preprocessing.
     */
    @Test
    public void testReplacepunctuation() {
        System.out.println("replacePunctuations");
        
        Preprocessing.initialization();
        
        
        String x ="fgtg r)rfre:erf-67%hwe \"Hii\" { 1987";
        String x1="fgtg r __rightbrackets rfre __colon erf __dash 67 __percent hwe  __quotationmark Hii __quotationmark   __leftbrackets  1987";
        
        assertEquals(Preprocessing.repalcePunctuations(x), x1);
        
    }
    
    
   /**
     * Test of replaceNumbers method, of class Preprocessing.
     */
    @Test
    public void testreplaceNumbers() {
        System.out.println("replaceNumbers");
        
        String x1="fgtg r rfre erf 67 hwe  Hii    1987";
        String y="fgtg r rfre erf __number hwe  Hii    __number";
        
        assertEquals(Preprocessing.replaceNumbers(x1),y);
        
    } 
    
    
    /**
     * Test of removeLongWhiteSpaces method, of class Preprocessing.
     */
    @Test
    public void testremoveLongWhiteSpaces() {
        System.out.println("removeLongWhiteSpaces");
        
        String x1="fgtg r rfre erf 67 hwe  Hii    1987";
        String y1="fgtg r rfre erf 67 hwe Hii 1987";
        
        assertEquals(Preprocessing.removeLongPrePostWhiteSpaces(x1),y1);
        
        String x2="  fgtg r rf     ewe  Hii    1987    ";
        String y2="fgtg r rf ewe Hii 1987";
        
        assertEquals(Preprocessing.removeLongPrePostWhiteSpaces(x2),y2);
    }
    
}
