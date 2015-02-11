/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import datastructure.CommonSlots;
import datastructure.Slot;
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
        double minSup=0.4;
        System.out.println("intersect");
        List<List<Integer>> input = new ArrayList<>();
        
        List<Integer> slot1=Arrays.asList(6,7,8,9,10,11,12,13);
        List<Integer> slot2=Arrays.asList(1,2,3,4,5,10,11,12,13);
        List<Integer> slot3=Arrays.asList(1,2,3,4,5,6,7,8,9);
        
        List<List<Integer>> ll=Arrays.asList(slot1,slot2,slot3);
        
        CommonSlots cs= General.intersect(ll,minSup);
        
        assertEquals(5,cs.commonReferences.size());
        assertEquals(Arrays.asList(1,2,3,4,5),cs.commonReferences);
        assertEquals(2,cs.solts.size());
        assertEquals(Arrays.asList(1,2), cs.solts);
        
        List<Integer> slot4=Arrays.asList(6,7,8,9,10,11,12,13);
        List<Integer> slot5=Arrays.asList(1,2);
        List<Integer> slot6=Arrays.asList(3,4,5);
        
        List<List<Integer>> ll2=Arrays.asList(slot4,slot5,slot6);
        cs= General.intersect(ll2,minSup);
        
        assertEquals(0,cs.commonReferences.size());
        assertEquals(Arrays.asList(),cs.commonReferences);
        assertEquals(0,cs.solts.size());
        assertEquals(Arrays.asList(), cs.solts);
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

    /**
     * Test of toIntegerList method, of class General.
     */
    @Test
    public void testToIntegerList() {
        System.out.println("toIntegerList");
        String x = "";
        
    }

    /**
     * Test of split method, of class General.
     */
    @Test
    public void testSplit() {
        System.out.println("split");
        List<Integer> toSplit = Arrays.asList(1,2,3,4,5,6,7,8,9);
        List<Integer> splitters = Arrays.asList(3,6,8);
        
        List<List<Integer>> result = General.split(toSplit, splitters);
        assertEquals(4,result.size());
        assertEquals(Arrays.asList(1,2), result.get(0));
        assertEquals(Arrays.asList(4,5), result.get(1));
        assertEquals(Arrays.asList(7), result.get(2));
        assertEquals(Arrays.asList(9), result.get(3));
        
        
        
        List<Integer> splitters2 = Arrays.asList(2,3);
        List<List<Integer>> r2 = General.split(toSplit, splitters2);
        assertEquals(3,r2.size());
        assertEquals(Arrays.asList(1), r2.get(0));
        assertEquals(Arrays.asList(), r2.get(1));
        assertEquals(Arrays.asList(4,5,6,7,8,9), r2.get(2));
        
        
    }

    /**
     * Test of findCommonReferencesSlots method, of class General.
     */
    @Test
    public void testFindCommonReferencesSlots() {
        System.out.println("findCommonReferencesSlots");
        List<Slot> slots = null;
        CommonSlots expResult = null;
//        CommonSlots result = General.findCommonReferencesSlots(slots);
//       assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of integerListToCMSPAMString method, of class General.
     */
    @Test
    public void testIntegerListToCMSPAMString() {
        System.out.println("integerListToCMSPAMString");
        List<Integer> in = null;
        String expResult = "";
        String result = General.integerListToCMSPAMString(in);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
