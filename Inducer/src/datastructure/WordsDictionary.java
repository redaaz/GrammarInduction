/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author reda
 */
public class  WordsDictionary {
    
    //zero-based list of unique words
    static List<String> uniqueWordsList=new ArrayList<>();
    
    //first-char based dictionary of word indexs (zero-based indexs)
    static HashMap<Character, HashMap<String, Integer> >  wordsToIndexDictionary=new HashMap<>();
    
    
    // to add new word to the structure: (1) list (2) dictionary 
    // @param word should be >=1 length
    static void addNewWord(String word) throws Exception{
        if(word.length()<1)
            throw new Exception(word + " can not be added! - (length<1)");
        
        uniqueWordsList.add(word);
        
        if(wordsToIndexDictionary.get(word.charAt(0))==null)
            wordsToIndexDictionary.put(word.charAt(0), new HashMap<>());
        
        wordsToIndexDictionary.get(word.charAt(0)).put(word, uniqueWordsList.size()-1);
       
    }
    
    
    public static void addWord(String word) throws Exception{
        if(word.length()<1)
            throw new Exception(word + " can not be added! - (length<1)");
               
        //check if exist
        if(wordsToIndexDictionary.get(word.charAt(0))==null){
            addNewWord(word);
        }
        else{
            if(wordsToIndexDictionary.get(word.charAt(0)).get(word)==null){
                addNewWord(word);
            }
        }
        
    }
    
    //return -1  if doesnt exist
    public static Integer getWordIndex(String word) throws Exception{
        if(word.length()<1)
            throw new Exception(word + " can not be added! - (length<1)");
        
        if(wordsToIndexDictionary.get(word.charAt(0))==null){
            return -1;
        }
        else{
            if(wordsToIndexDictionary.get(word.charAt(0)).get(word)==null){
                return -1;
            }
            return wordsToIndexDictionary.get(word.charAt(0)).get(word);
        }
    }
    
    //return null if doesnt exist
    public static String getWord(Integer index){
        if(index<0 || index>=uniqueWordsList.size())
            return null;
        return uniqueWordsList.get(index);
    }
    
    public static boolean isExist(String word) throws Exception{
        if(word.length()<1)
            throw new Exception(word + " can not be added! - (length<1)");
        
        if(wordsToIndexDictionary.get(word.charAt(0))==null){
            return false;
        }
        else{
            if(wordsToIndexDictionary.get(word.charAt(0)).get(word)==null){
                return false;
            }
            return true;
        }
    }
    
}
