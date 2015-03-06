/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.util.Random;

/**
 *
 * @author reda
 */
public class OrNode extends Node {
    
    public OrNode(String v){
        super(v);
        this.type=Type.OR;
    }
    
    @Override
    public String traverse(){
        String in="";
        if(this.terminal==true){
            in += this.value+" ";
        }
        else{
            in +=this.children.get(chooseOneChild()).traverse();
        }
        return in;
    }
    
    public int chooseOneChild(){
        Random r=new Random();
        int randomNum = r.nextInt(this.children.size());
        return randomNum;
    }
    
    @Override
    public void build(String in,ObjectObjectOpenHashMap<String, String> allNodes,IntObjectOpenHashMap<Node> nodes){
        
        if(this.children!=null && this.children.isEmpty() && !this.isLeaf())
        {
            System.out.println(this.value);
            String sss=allNodes.get(in);
            String[] ll=allNodes.get(in).split("  \\| ");

            //create my children
            ObjectArrayList<Node> child=new ObjectArrayList();
            for(String str:ll){
                str=str.trim();
                if(str.contains("<R")&&str.contains(">")){

                    AndNode and=new AndNode(str);
                    child.add(and);
                }
                else{
                    AndNode term=new AndNode(str);
                    term.setTerminal();
                    child.add(term);
                }
            }
            //add my children
            this.setChildren(child);

            //add me
            nodes.put(this.nodeID, this);

            //call for my cildren
            for(ObjectCursor<Node> n:this.children){
                if(!n.value.isLeaf())
                    n.value.build(n.value.value, allNodes, nodes);
            }
        }
    }
}
