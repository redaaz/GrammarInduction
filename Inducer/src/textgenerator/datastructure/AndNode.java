/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.util.Arrays;
import java.util.List;
import text.General;

/**
 *
 * @author reda
 */
public class AndNode extends Node{
    
    int leafLength;
    
    public AndNode(String v){
        super(v);
        this.type=Type.AND;
        String [] ll=v.split(" ");
        this.leafLength=ll.length;
    }
    
    @Override
    public String traverse(){
        String in="";
        if(this.terminal==true){
            in += this.value+" ";
        }
        else{
            for(ObjectCursor<Node> n: this.children){
                in += n.value.traverse();
            }
        }
        return in;
    }
    
    @Override
    public void build(String in,ObjectObjectOpenHashMap<String, String> allNodes,IntObjectOpenHashMap<Node> nodes){
        
       if(this.children!=null && this.children.isEmpty() && !this.isLeaf())
        {
            System.out.println(this.value);
            String[] ll=allNodes.get(in).split(" ");

            //create my children
            ObjectArrayList<Node> child=new ObjectArrayList();
            for(String str:ll){
                if(str.contains("<R")&&str.contains(">")){

                    OrNode and=new OrNode(str);
                    child.add(and);
                }
                else{
                    AndNode term=new AndNode(str);
                    term.setTerminal();
                    term.range.max = term.range.min = term.getLeafLength();
                    term.rangeIni = true;
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
    
    public int getLeafLength(){
        return terminal ? leafLength : -1;
    }
    
    @Override
    public void setRange(){
        int mini = 0;
        int maxi = 0;
        for(ObjectCursor<Node> n: this.children){
            if(!n.value.rangeIni){
                n.value.setRange();
            }
            mini += n.value.range.min;
            maxi += n.value.range.max;
        }
        //all are set
        this.range.min = mini;
        this.range.max = maxi;
        this.rangeIni = true;
    }
    
    @Override
    public long getGeneratingPower(){
        long res=1;
        if(this.isLeaf())
            return 1;
        else{
            for(ObjectCursor<Node> n:this.children){
                long old=res;
                res *= n.value.getGeneratingPower();
                if(res<old){
                    AndOrTree.VeryBig = true;
                    return Long.MAX_VALUE;//to see
                }
            }
        }
        return res;    
    }
    
    @Override
    public void getGeneratingDistribution(ObjectArrayList<IntArrayList> paths,long count){
        if(this.isLeaf())
            return;
        for(ObjectCursor<Node> i:children)
            i.value.getGeneratingDistribution(paths, count);
    }
    
    @Override
    public String traverse(IntArrayList choices){
         String in="";
        if(this.terminal==true){
            in += this.value+" ";
        }
        else{
            for(ObjectCursor<Node> n: this.children){
                in += n.value.traverse(choices);
            }
        }
        return in;
    }
}
