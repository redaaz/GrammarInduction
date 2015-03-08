/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

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
        
        String path="/Users/reda/Downloads/rt-polaritydata/rt-polaritydata/(neg) Thu Mar 05 01:33:03 CET 2015/";
        String fileName="neg_rules";
        
        OrNode head=new OrNode("<S>");
        AndOrTree tree=new AndOrTree(head);
        tree.buildTree(path,fileName);
        
        List<String> gen=tree.generate(50000);
        
        General.write(gen, path, "("+gen.size()+")_Generated_"+fileName);
        
    }
    
}
