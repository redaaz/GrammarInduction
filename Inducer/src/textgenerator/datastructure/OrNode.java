/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.util.List;
import java.util.Random;
import text.General;

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
            in += this.children.get(chooseOneChild()).traverse();
        }
        return in;
    }
    
    public int chooseOneChild(){
        Random r=new Random();
        int randomNum = r.nextInt(this.children.size());
        
        return randomNum;
    }
    
    public int chooseOneChild2(){
        Random r=new Random(System.currentTimeMillis());
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
    
    @Override
    public void setRange(){
        int mini = Integer.MAX_VALUE;
        int maxi = Integer.MIN_VALUE;
        for(ObjectCursor<Node> n: this.children){
            if(!n.value.rangeIni){
                n.value.setRange();
            }
            if(mini > n.value.range.min)
                mini = n.value.range.min;
            if(maxi < n.value.range.max)
                maxi = n.value.range.max;
        }
        //all are set
        
        this.range.min = mini;
        this.range.max = maxi;
        this.rangeIni = true;
    }
    
    @Override
    public String getSentence(String in,IntArrayList path){
        String out=in;
        for(ObjectCursor<Node> i:this.children){
            out += i.value.getSentence(out, path);
        }
        return out;
    }
    
    @Override
    public long getGeneratingPower(){
        long res=1;
        if(this.isLeaf())
            return 1;
        else{
            for(ObjectCursor<Node> n:this.children){
                res += n.value.getGeneratingPower();
            }
        }
        return res;    
    }
    
    @Override
    public void getGeneratingDistribution(ObjectArrayList<IntArrayList> paths,long count){
        if(this.isLeaf())
            return;
        IntArrayList children1=this.getNonTerminalChildren();
        LongArrayList gp=new LongArrayList();
        for(IntCursor i:children1){
            gp.add(this.children.get(i.value).getGeneratingPower());
        }
        List<Long> ratios= General.getRatios(gp, count);
        
        int counter=0;
        for(int i=0;i<ratios.size();i++){
            for(int j=0;j<ratios.get(i);j++){
                paths.get(counter).add(i);
                counter++;
            }
        }
        
        for(int i=0;i<children1.size();i++)
            this.children.get(children1.get(i)).getGeneratingDistribution(paths, ratios.get(i));
        
    }
    
    @Override
    public String traverse(IntArrayList choices){
        String in="";
        if(this.terminal==true){
            in += this.value+" ";
        }
        else{
            int choice=choices.remove(0);
            in += this.children.get(choice).traverse(choices);
        }
        return in;
    }
}
