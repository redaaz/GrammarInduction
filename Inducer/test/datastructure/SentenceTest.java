/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

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
public class SentenceTest {
    
    public SentenceTest() {
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
     * Test of getLength method, of class Sentence.
     */
    @Test
    public void testGetLength() {
        System.out.println("getLength");
        WordsDictionary.resetWordsDictionary();
        
        Sentence instance1 = new Sentence();
        assertEquals(0, instance1.getLength());
        
        Sentence instance2 = new Sentence("");
        assertEquals(0, instance2.getLength());
        
        
        
        WordsDictionary.addWord("hi0");
        WordsDictionary.addWord("hi1");
        WordsDictionary.addWord("hi2");
        WordsDictionary.addWord("hi3");
        WordsDictionary.addWord("hi4");
        WordsDictionary.addWord("hi5");
        WordsDictionary.addWord("hi6");
        
        Sentence instance3 = new Sentence("hi1 hi4 hi5 hi1 hi0");
        assertEquals(5, instance3.getLength());
        assertEquals("hi1 hi4 hi5 hi1 hi0 ", instance3.toString());
        assertEquals("1 4 5 1 0 ", instance3.toCodeString());
        
        Sentence instance4 = new Sentence("hi1 hi7 hi5 hi1 hi0");
        assertEquals("1 7 5 1 0 ", instance4.toCodeString());
        assertEquals("hi1 hi7 hi5 hi1 hi0 ", instance4.toString());
        assertEquals(5,instance4.getLength());
        
        Sentence instance5 = new Sentence("hi8");
        assertEquals(1, instance5.getLength());
        
    }

    /**
     * Test of toString method, of class Sentence.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        WordsDictionary.resetWordsDictionary();
        
        WordsDictionary.addWord("hi0");
        WordsDictionary.addWord("hi1");
        WordsDictionary.addWord("hi2");
        WordsDictionary.addWord("hi3");
        WordsDictionary.addWord("hi4");
        WordsDictionary.addWord("hi5");
        WordsDictionary.addWord("hi6");
        
        Sentence instance = new Sentence("hi1 hi4 hi3 hi1");
        
        String result = instance.toCodeString();
        assertEquals("1 4 3 1 ", result);
        
        
    }
    
}
