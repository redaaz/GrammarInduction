/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import datastructure.WordsDictionary;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author reda
 */
public class Postprocessing {
    
    public static void main(String[] args) throws IOException{
        String folderPath="/Users/reda/Documents/NewAlgoTests/(100k) Mon Mar 09 22:57:22 CET 2015 (#2)/";
        String fileName="(250000)_Generated_100k_rules";
        englishGeneratedPostprocessing(folderPath,fileName);
    }
    
    public static String toStringFromCM_SPAMResult(String x){
        if(x==null || x.isEmpty())
            return null;
        String[] Xs=x.split("SUP: ");
        if(Xs.length!=2)
            return null;
        
        String[] Indexes= Xs[0].split(" -1 ");
        
        String res="";
        
        for(String str: Indexes){
            if((!str.isEmpty()) && (General.tryParseInt(str))){
                res+=WordsDictionary.getWord(Integer.parseInt(str))+" ";
            }
        }
        
        return res;
    }
 
    public static void englishGeneratedPostprocessing(String folderPath,String fileName) throws IOException{
        
        List<String> input=General.read(folderPath, fileName);
        
        initialization();
        
        for(int i=0;i<input.size();i++){
            for(ObjectObjectCursor<String, String> str: post){
                if(input.get(i).contains(str.key))
                    input.set(i, input.get(i).replace(str.key, str.value));
            }
        }
        General.write(input, folderPath, fileName+"_fixed");
    }
    
    public static void initialization(){
        post =new ObjectObjectOpenHashMap();
        post.put(" re ", "'re ");
        post.put(" ve ", " have ");
        post.put(" ll ", "'ll ");
        post.put(" d ", "'d ");
        post.put(" s ", "'s ");
        post.put(" t ", "'t ");
        post.put(" i ", " I ");
        
    }
    
    static ObjectObjectOpenHashMap<String,String> post;
}
