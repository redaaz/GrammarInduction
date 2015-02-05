/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import datastructure.WordsDictionary;

/**
 *
 * @author reda
 */
public class Postprocessing {
    
    public static String toStringFromCM_SPAMResult(String x){
        if(x==null || x.isEmpty())
            return null;
        String[] Xs=x.split("SUP: ");
        if(Xs.length!=2)
            return null;
        
        String[] Indexes= Xs[0].split(" -1 ");
        
        String res="";
        
        for(String str: Indexes){
            if((!str.isEmpty()) && (tryParseInt(str))){
                res+=WordsDictionary.getWord(Integer.parseInt(str))+" ";
            }
        }
        
        return res;
    }
 
    
    private static boolean tryParseInt(String value)  
    {  
        try  
        {  
            Integer.parseInt(value);  
            return true;  
        } catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
    }
}
