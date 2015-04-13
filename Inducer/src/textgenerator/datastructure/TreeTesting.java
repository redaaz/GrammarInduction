/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import text.General;


/**
 *
 * @author reda
 */
public class TreeTesting {
    
    public static void main(String[] args) throws IOException {
        
        String path="/Users/reda/Documents/NewAlgoTests/Twitter/(tweets_(50000)_) Fri Mar 13 13:42:13 CET 2015/";
        String fileName="tweets_(50000)__rules";
        int generatedSentences=100;
        
        OrNode head=new OrNode("<S>");
        AndOrTree tree=new AndOrTree(head);
        tree.buildTree(path,fileName);
        
        ObjectArrayList<IntArrayList> paths = tree.generatePathes(generatedSentences);
        for(ObjectCursor<IntArrayList> i: paths)
            System.out.println(i.value.toString());
        List<String> gen=new ArrayList<>();
        System.out.println("Start generating ...");
        for(int i=0;i<paths.size();i++){
            String str=tree.traverse(paths.get(i));
            gen.add(str);
            if(i % 2000000 == 0){
                System.out.println(" --> "+i);
                if(i == 10000000){
                    General.write(gen, path, "("+generatedSentences+")_Generated_"+fileName);
                    gen=new ArrayList<>();
                    System.gc();
                }
                else if(i % 10000000 ==0){
                    General.append(gen, path, "("+generatedSentences+")_Generated_"+fileName);
                    gen=new ArrayList<>();
                    System.gc();
                }
                
            }
            
        }
        General.append(gen, path, "("+generatedSentences+")_Generated_"+fileName);
        //List<String> gen=tree.generate(generatedSentences);
        
        //General.write(gen, path, "("+generatedSentences+")_Generated_"+fileName);
        
    }
    
}
