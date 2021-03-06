/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import jdk.nashorn.internal.codegen.types.Type;


/**
 *
 * @author reda
 */
public class Preprocessing {
   
    static HashMap<Character,String> punctuationList =new HashMap<>();
    
    public static HashMap<PreTextOperation,Method> getOperation=new HashMap<>();
    
    public static void initialization() throws NoSuchMethodException{
        punctuationList.put('\'', "__apostrophe");
        punctuationList.put('’', "__apostrophe");                   
        punctuationList.put('[', "__leftbrackets");
        punctuationList.put(']', "__rightbrackets");
        punctuationList.put('(', "__leftbrackets");
        punctuationList.put(')', "__rightbrackets");
        punctuationList.put('{', "__leftbrackets");
        punctuationList.put('}', "__rightbrackets");
        punctuationList.put('⟨', "__leftbrackets");
        punctuationList.put('⟩', "__rightbrackets");
        punctuationList.put(':', "__colon");
        punctuationList.put(',', "__comma");
        punctuationList.put('،', "__comma");
        punctuationList.put('、', "__comma");
        punctuationList.put('-', "__dash");
        punctuationList.put('…', "__ellipsis");
        punctuationList.put('!', "__exclamationmark");
        punctuationList.put('.', "__fullstop");
        punctuationList.put('‐', "__hyphen");
        punctuationList.put('?', "__questionmark");
        punctuationList.put('؟', "__questionmark");
        punctuationList.put(';', "__semicolon");
        punctuationList.put('/', "__slash");
        punctuationList.put('⁄', "__slash");
        punctuationList.put('&', "__ampersand");
        punctuationList.put('*', "__asterisk");
        punctuationList.put('@', "__atsign");
        punctuationList.put('\\', "__backslash");
        punctuationList.put('•', "__bullet");
        punctuationList.put('^', "__caret");
        punctuationList.put('°', "__degree");
        punctuationList.put('″', "__dittomark");
        punctuationList.put('¡', "__invertedexclamationmark");
        punctuationList.put('¿', "__invertedquestionmark");
        punctuationList.put('#', "__hash");
        punctuationList.put('№', "__numerosign");
        punctuationList.put('÷', "__obelus");
        punctuationList.put('¶', "__pilcrow");
        punctuationList.put('′', "__prime");
        punctuationList.put('‴', "__prime");
        punctuationList.put('″', "__prime");
        punctuationList.put('§', "__sectionsign");
        punctuationList.put('~', "__tilde");
        punctuationList.put('_', "__underscore");
        punctuationList.put('|', "__verticalbar");
        punctuationList.put('¦', "__verticalbar");
        punctuationList.put('‖', "__verticalbar");
        punctuationList.put('©', "__copyright");
        punctuationList.put('℗', "__soundrecordingcopyright");
        punctuationList.put('®', "__registeredmark");
        punctuationList.put('℠', "__servicemark");
        punctuationList.put('™', "__trademark");
        punctuationList.put('¤', "__genericcurrencysymbol");
        punctuationList.put('%', "__percent");
        punctuationList.put('‰', "__percent");
        punctuationList.put('+', "__plus");
        punctuationList.put('º', "__ordinalindicator");
        punctuationList.put('ª', "__ordinalindicator");
        punctuationList.put('"', "__quotationmark");
        punctuationList.put('\'', "__quotationmark");
        punctuationList.put('”', "__quotationmark");
        punctuationList.put('“', "__quotationmark");
        punctuationList.put('’', "__quotationmark");
        punctuationList.put('‘', "__quotationmark");
        
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
        getOperation.put(PreTextOperation.RemoveNumbers,Preprocessing.class.getMethod("removeNumbers",parameterTypes));
        getOperation.put(PreTextOperation.ReplaceNumbers,Preprocessing.class.getMethod("replaceNumbers",parameterTypes));
        getOperation.put(PreTextOperation.RemovePunctuations,Preprocessing.class.getMethod("removePunctuations",parameterTypes));
        getOperation.put(PreTextOperation.ReplacePunctuations,Preprocessing.class.getMethod("repalcePunctuations",parameterTypes));
    } 
    //punc. -> " "+punc.name+" "
    public static String repalcePunctuations(String x){
        String res="";
        for(int i=0;i<x.length();i++){
            if(punctuationList.containsKey(x.charAt(i)))
                res+=" "+punctuationList.get(x.charAt(i))+" ";
            else
                res+=x.charAt(i);
        }
        
        return res;
    }
    //punc. -> " "
    public static String removePunctuations(String x) {
        return x.replaceAll("[^\\p{L} 0-9]", " ");
    }
    
    public static String replaceNumbers(String x){
        return x.replaceAll("[0-9]+", "__number");
    }
    //num. -> ""
    //all number (individual or included) 
    public static String removeNumbers(String x){
        return x.replaceAll("[0-9]+", "");
    }
    //to remove pre and post and long withspaces
    // included long spaces -> " "
    public static String removeLongPrePostWhiteSpaces(String x){
        return  x.replaceAll("[ ]+", " ").trim();
    }
    
    public static String toLowerCase(String x){
        return x.toLowerCase();
    }
}
