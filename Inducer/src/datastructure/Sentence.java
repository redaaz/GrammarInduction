/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reda
 */
public class Sentence {
    
    static String delimiter= " ";
    
    List<Integer> sentenceCode;
    
    public Sentence(){
        sentenceCode=new ArrayList<>();
    }
    
    //to construct new instance- consider whitespaces as delimiter 
    public Sentence(String sen){
        sentenceCode=new ArrayList<>();
        
        if(sen.isEmpty())
            return;
        
        String[] senParts=sen.split(delimiter);
        
        for(String s:senParts){
            if(WordsDictionary.isExist(s)){
                sentenceCode.add(WordsDictionary.getWordIndex(s));
            }
            else{
                int wordIndex=WordsDictionary.addWord(s);
                sentenceCode.add(wordIndex);
            }
                
        }
        
    }
    
    public int getLength(){
        return sentenceCode.size();
    }
    
    //toString returns original words with additional whitespace at end
    @Override 
    public String toString(){
        String res="";
        res = sentenceCode.stream().map((i) -> WordsDictionary.getWord(i)+" ").reduce(res, String::concat);
        return res;
    }
    
    //returns coded words (words indexs) with additional whitespace at end
    public String toCodeString(){
        String res="";
        res = sentenceCode.stream().map((i) -> ""+i+" ").reduce(res, String::concat);
        return res;
    }
    
    public String toStringCM_SPAM(){
        String res="";
        res = sentenceCode.stream().map((i) -> ""+i+" -1 ").reduce(res, String::concat);
        return res+"-2";
    }
}
