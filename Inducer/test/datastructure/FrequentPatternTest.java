/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
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
public class FrequentPatternTest {
    
    public FrequentPatternTest() {
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
     * Test of toString method, of class FrequentPattern.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        FrequentPattern instance = new FrequentPattern();
        assertEquals("", instance.toString());
        
        FrequentPattern instance2 = new FrequentPattern(null,-4);
        assertNull( instance2.toString());
        
        instance2 = new FrequentPattern(new ArrayList<>(),-4);
        assertEquals("", instance2.toString());
        
        instance2 = new FrequentPattern("");
        assertEquals(null, instance2.toString());
    }

    /**
     * Test of toStringWithSup method, of class FrequentPattern.
     */
    @Test
    public void testToStringWithSup() {
        System.out.println("toStringWithSup");
        WordsDictionary.resetWordsDictionary();
        WordsDictionary.addWord("h1");
        WordsDictionary.addWord("h2");
        WordsDictionary.addWord("h3");
        WordsDictionary.addWord("h4");
        
        FrequentPattern instance2 = new FrequentPattern(new ArrayList<>(),-4);
        assertEquals("SUP: -4", instance2.toStringWithSup());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP: 3");
        assertEquals("h2 h4 SUP: 3", instance2.toStringWithSup());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP:");
        assertEquals("nullSUP: -1", instance2.toStringWithSup());
        
    }

    /**
     * Test of toStringCode method, of class FrequentPattern.
     */
    @Test
    public void testToStringCode() {
        System.out.println("toStringCode");
        WordsDictionary.resetWordsDictionary();
        WordsDictionary.addWord("h1");
        WordsDictionary.addWord("h2");
        WordsDictionary.addWord("h3");
        WordsDictionary.addWord("h4");
        
        FrequentPattern instance2 = new FrequentPattern(new ArrayList<>(),-4);
        assertEquals("", instance2.toStringCode());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP: 3");
        assertEquals("1 3 ", instance2.toStringCode());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP:");
        assertEquals(null, instance2.toStringCode());
    }

    /**
     * Test of toStringCodeWithSup method, of class FrequentPattern.
     */
    @Test
    public void testToStringCodeWithSup() {
        System.out.println("toStringCodeWithSup");
        WordsDictionary.resetWordsDictionary();
        WordsDictionary.addWord("h1");
        WordsDictionary.addWord("h2");
        WordsDictionary.addWord("h3");
        WordsDictionary.addWord("h4");
        
        FrequentPattern instance2 = new FrequentPattern(new ArrayList<>(),-4);
        assertEquals("SUP: -4", instance2.toStringCodeWithSup());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP: 3");
        assertEquals("1 3 SUP: 3", instance2.toStringCodeWithSup());
        
        instance2 = new FrequentPattern("1 -1 3 -1 SUP:");
        assertEquals("nullSUP: -1", instance2.toStringCodeWithSup());
    }
    
}
