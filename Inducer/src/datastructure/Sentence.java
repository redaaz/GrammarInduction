/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.cursors.IntCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    
    //only in case of sub rules
    private int originalReference=-1;
    
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
    
    public void setSentenceCode(IntArrayList in){
        this.sentenceCode=new ArrayList<>();
        for(IntCursor i:in){
            this.sentenceCode.add(i.value);
        }
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
        res = sentenceCode.stream().map((i) -> ""+i+delimiter).reduce(res, String::concat);
        return res;
    }
    
    public String toStringCM_SPAM(){
        String res="";
        res = sentenceCode.stream().map((i) -> ""+i+" -1 ").reduce(res, String::concat);
        return res+"-2";
    }
    
    @Override
    public boolean equals(Object aThat) {
        Sentence that=(Sentence)aThat;
        if(this.sentenceCode.size()!=that.sentenceCode.size())
            return false;
        for(int i=0;i<this.sentenceCode.size();i++){
            if(!Objects.equals(this.sentenceCode.get(i), that.sentenceCode.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    public void println(){
        System.out.println(this.toString());
    }
    
    public void setOriginalReference(int in){
        this.originalReference=in;
    }
    
    public int getOriginalReference(){
        return this.originalReference;
    }
    
    public String[] getSentenceCodeForCMSPADE(){
        if(this.sentenceCode==null || this.sentenceCode.isEmpty())
            return null;
        String[] res=new String[this.sentenceCode.size()*2+1];
        int counter=0;
        for(Integer in:this.sentenceCode){
            res[counter]=""+in;
            counter++;
            res[counter]="-1";
            counter++;
        }
        res[this.sentenceCode.size()*2]="-2";
        return res;
    }
    
    public String[] getSentenceCodeForCMClaSP(){
        if(this.sentenceCode==null || this.sentenceCode.isEmpty())
            return null;
        String[] res=new String[this.sentenceCode.size()*2+1];
        int counter=0;
        for(Integer in:this.sentenceCode){
            res[counter]=""+in;
            counter++;
            res[counter]="-1";
            counter++;
        }
        res[this.sentenceCode.size()*2]="-2";
        return res;
    }
}
