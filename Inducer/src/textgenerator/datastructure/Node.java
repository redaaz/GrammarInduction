/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

/**
 *
 * @author reda
 */
public class Node {
    static int counter;
    Type type;
    int nodeID;
    boolean terminal;
    String value;
    ObjectArrayList<Node> children;
    
    public Node(String v){
        this.nodeID=counter++;
        children=new ObjectArrayList();
        this.value=v;
        this.terminal=false;
    }
    
    public void setTerminal(){
        this.terminal=true;
    }
    
    public boolean isLeaf(){
        return terminal;
    }
    
    public void setChildren(ObjectArrayList<Node> cc){
        this.children=cc;
    }
    
    public String getValue(){
        return this.value;
    }

   @Override
    public boolean equals(Object that){
        
        if(((Node)that).nodeID!=this.nodeID)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
     
    
    public String traverse(){
        //do nothing
        return "ERROR NODE traverse";
    }
    
     public void build(String str,ObjectObjectOpenHashMap<String, String> allNodes,IntObjectOpenHashMap<Node> nodes){
        //do nothing
        System.out.println("ERROR NODE build");
    }
}
