/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author reda
 */
public class GeneralTest {
    
    public GeneralTest() {
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
     * Test of tryParseInt method, of class General.
     */
    @Test
    public void testTryParseInt() {
        System.out.println("tryParseInt");
        String value = "";
        
        boolean result = General.tryParseInt("0");
        assertEquals(true, result);
        
    }

    /**
     * Test of intersect method, of class General.
     */
    @Test
    public void testIntersect() {
        System.out.println("intersect");
        List<List<Integer>> input = new ArrayList<>();
        
        List<Integer> l1=new ArrayList<>();
        List<Integer> l2=new ArrayList<>();
        List<Integer> l3=new ArrayList<>();
        List<Integer> l4=new ArrayList<>();
        List<Integer> l5=new ArrayList<>();
        
        
        l1.add(1);l1.add(2);l1.add(3);l1.add(4);l1.add(5);l1.add(6);l1.add(8);
        l2.add(1);l2.add(2);l2.add(3);l2.add(6);l2.add(7);l2.add(8);l2.add(10);l2.add(11);l2.add(12);l2.add(13);
        l3.add(2);l3.add(3);l3.add(4);l3.add(6);
        l4.add(3);l4.add(4);l4.add(5);l4.add(6);l4.add(7);l4.add(8);
        l5.add(1);l5.add(2);l5.add(3);l5.add(6);
        input.add(l1);
        input.add(l2);
        input.add(l3);
        input.add(l4);
        input.add(l5);
        
        List<Integer> res=new ArrayList<>();
        res.add(3);res.add(6);
        //assertEquals(res, General.intersect(input));
        }

    /**
     * Test of ContainsSequence method, of class General.
     */
    @Test
    public void testContainsSequence() {
        System.out.println("ContainsSequence");
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        
        
        
        boolean result = General.ContainsSequence(l1, l2);
        assertEquals(true, result);
        
        l1=Arrays.asList(2,3);
        l2=Arrays.asList(1,2,3,5);
        
        result = General.ContainsSequence(l1, l2);
        assertEquals(true, result);
        
        List<Integer> l3 = Arrays.asList(3,2);
        result = General.ContainsSequence(l3, l2);
        assertEquals(false, result);
        
        List<Integer> l4 = Arrays.asList(3);
        result = General.ContainsSequence(l4, l2);
        assertEquals(true, result);
        
        List<Integer> l5 = Arrays.asList(1,5,7);
        l2=Arrays.asList(1,2,3,5,7);
        result = General.ContainsSequence(l5, l2);
        assertEquals(true, result);
        
        List<Integer> l6 = Arrays.asList(1,7,5);
        result = General.ContainsSequence(l6, l2);
        assertEquals(false, result);
    }
    
}
