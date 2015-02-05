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
public class WordsDictionaryTest {
    
    public WordsDictionaryTest() {
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
     * Test of addNewWord method, of class WordsDictionary.
     */
    @Test
    public void testAddNewWord() throws Exception {
        System.out.println("addNewWord");
        WordsDictionary.resetWordsDictionary();
        
        int value1=WordsDictionary.uniqueWordsList.size();
        WordsDictionary.addNewWord("");
        int value2=WordsDictionary.uniqueWordsList.size();
        assertTrue("bug when add empty string",value1==value2);
        
        
        WordsDictionary.addNewWord(null);
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.isEmpty());
        
        WordsDictionary.addNewWord("hi");
        assertTrue("bug when add new word string 1",WordsDictionary.uniqueWordsList.size()==1);
        assertTrue("bug when add new word string 2",WordsDictionary.wordsToIndexDictionary.get('h').get("hi")==0);
        assertNull("bug when add new word string 3",WordsDictionary.wordsToIndexDictionary.get('a'));
        
        WordsDictionary.addNewWord("h");
        assertTrue("bug when add new single-letter word string",WordsDictionary.uniqueWordsList.size()==2);
        assertTrue("bug when add new word string",WordsDictionary.wordsToIndexDictionary.get('h').get("h")==1);
        
    }
    
    

    /**
     * Test of addWord method, of class WordsDictionary.
     */
    @Test
    public void testAddWord() throws Exception {
        System.out.println("addWord");
        
        WordsDictionary.resetWordsDictionary();
        
        WordsDictionary.addWord("");
        assertTrue("bug when add empty string",WordsDictionary.uniqueWordsList.isEmpty());
        
        WordsDictionary.addWord(null);
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.isEmpty());
        
        WordsDictionary.addWord("hi");
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.size()==1);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('h')!=null);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a')==null);
        
        WordsDictionary.addWord("hi");
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.size()==1);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('h')!=null);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a')==null);
        
        WordsDictionary.addWord("abc");
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.size()==2);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a')!=null);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a').size()==1);
        
        
        WordsDictionary.addWord("aabc");
        assertTrue("bug when add null string",WordsDictionary.uniqueWordsList.size()==3);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a')!=null);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('a').size()==2);
        assertTrue("bug when add null string",WordsDictionary.wordsToIndexDictionary.get('h').size()==1);
        
        
        
        
    }

    /**
     * Test of getWordIndex method, of class WordsDictionary.
     */
    @Test
    public void testGetWordIndex() throws Exception {
        System.out.println("getWordIndex");
        WordsDictionary.resetWordsDictionary();
        
        WordsDictionary.addWord(null);
        WordsDictionary.addWord("aaaa");
        WordsDictionary.addWord("cccc");
        WordsDictionary.addWord("dddd");
        WordsDictionary.addWord("eeee");
        WordsDictionary.addWord("bbbb");
        WordsDictionary.addWord("cccc2");
        
        assertEquals("testGetWordIndex 1",WordsDictionary.getWordIndex("dddd"), (Integer)2);
        assertEquals("testGetWordIndex 2",WordsDictionary.getWordIndex("cccc2"), (Integer)5);
        
    }

    /**
     * Test of getWord method, of class WordsDictionary.
     */
    @Test
    public void testGetWord() {
        System.out.println("getWord");
        WordsDictionary.resetWordsDictionary();
        
        WordsDictionary.addWord(null);
        WordsDictionary.addWord("aaaa");
        WordsDictionary.addWord("cccc");
        WordsDictionary.addWord("dddd");
        WordsDictionary.addWord("eeee");
        WordsDictionary.addWord("");
        WordsDictionary.addWord("bbbb");
        WordsDictionary.addWord("cccc2");
        
        assertEquals("testGetWord 1",WordsDictionary.getWord(1), "cccc");
        assertEquals("testGetWord 2",WordsDictionary.getWord(4), "bbbb");
        assertEquals("testGetWord 3",WordsDictionary.getWord(5), "cccc2");
        assertNull("testGetWord 3",WordsDictionary.getWord(-6));
        assertNull("testGetWord 3",WordsDictionary.getWord(100));
    }

    /**
     * Test of isExist method, of class WordsDictionary.
     */
    @Test
    public void testIsExist() throws Exception {
        System.out.println("isExist");
        
        WordsDictionary.resetWordsDictionary();
        
        WordsDictionary.addWord(null);
        WordsDictionary.addWord("aaaa");
        WordsDictionary.addWord("cccc");
        WordsDictionary.addWord("dddd");
        WordsDictionary.addWord("eeee");
        WordsDictionary.addWord("");
        WordsDictionary.addWord("bbbb");
        WordsDictionary.addWord("cccc2");
        
        
        assertTrue(WordsDictionary.isExist("aaaa"));
        assertTrue(WordsDictionary.isExist("cccc2"));
        assertTrue(WordsDictionary.isExist("cccc"));
        
        assertFalse(WordsDictionary.isExist((String)null));
        assertFalse(WordsDictionary.isExist("2222"));
        assertFalse(WordsDictionary.isExist(""));
        
    }
    
}
