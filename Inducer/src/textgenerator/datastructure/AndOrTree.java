/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textgenerator.datastructure;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectOpenHashSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.LongCursor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import text.General;

public class AndOrTree {
 
    IntObjectOpenHashMap<Node> nodes;
    Node head;
    public static boolean VeryBig=false;
    public AndOrTree(Node h){
        this.head=h;
        this.nodes=new IntObjectOpenHashMap();
        this.nodes.put(h.nodeID, h);
    }
    
    public String traverse(){
        String in="";
        if(head.terminal==true)
            in+= head.value+" ";
        else
            in+=head.traverse();
        return in;
        
    }
    
    public static ObjectObjectOpenHashMap<String,String> readFromFile(String path,String filename) throws IOException
    {
        BufferedReader br2 = new BufferedReader(new FileReader(path+filename+".txt"));
        String line2;
        ObjectObjectOpenHashMap res=new ObjectObjectOpenHashMap();
        while ((line2 = br2.readLine()) != null) {
            
            String[] ll=line2.split(" -> ");
            res.put(ll[0], ll[1]);
            
        }
        return res;
    
    }
    
    public void buildTree(String path,String filename) throws IOException{
        ObjectObjectOpenHashMap<String, String> leftSideToRightSide = readFromFile(path,filename);
        this.head.build(head.getValue(),leftSideToRightSide, this.nodes);
        this.head.setRange();
    }
    
    static public IntArrayList path;
    public ObjectArrayList<IntArrayList> pathes;
    public List<String> generate(int count){
        List<String> res=new ArrayList<>();
        ObjectObjectOpenHashMap<Character,ObjectObjectOpenHashMap<Character,ObjectOpenHashSet<String>>> dic=new ObjectObjectOpenHashMap();
        int ExsitCounter=0;
        pathes = new ObjectArrayList();
        
        for(int i=0;i<count;i++){
            path=new IntArrayList();
            String str=this.traverse();
            pathes.add(path.clone());
            if(!isExist(str,dic)){
                res.add(str);
                if(i % 1000000 == 0)
                    System.out.println(" --> "+i);
            }
            else{
                i--;
                ExsitCounter++;
                if(ExsitCounter % 1000000 == 0)
                    System.out.println("ExsitCounter = "+ExsitCounter);
            }
        }
        
        return res;
    }
    
    public boolean isExist(String str,ObjectObjectOpenHashMap<Character,ObjectObjectOpenHashMap<Character,ObjectOpenHashSet<String>>> dic){
        if(str==null || str.isEmpty() || dic==null || str.length()<2)
            return false;
        Character st=str.charAt(0);
        Character nd=str.charAt(str.length()-1)==' '?  str.charAt(str.length()-2):str.charAt(str.length()-1);
        
        if(!dic.containsKey(st)){//st doesnt exist
            dic.put(st, new ObjectObjectOpenHashMap());
            dic.get(st).put(nd, new ObjectOpenHashSet());
            dic.get(st).get(nd).add(str);
            return false;
        }else{
            if(!dic.get(st).containsKey(nd)){//nd doesnt exist
                dic.get(st).put(nd, new ObjectOpenHashSet());
                dic.get(st).get(nd).add(str);
                return false;
            }
            else{//st and nd are exist
                if(!dic.get(st).get(nd).contains(str)){
                    dic.get(st).get(nd).add(str);
                    return false;
                }
                else{
                    return true;
                }
            }   
        }
    }
    
    public ObjectArrayList<IntArrayList> generatePathes(long count){
        
        ObjectArrayList<IntArrayList> paths=new ObjectArrayList();
        for(int i=0;i<count;i++)
            paths.add(new IntArrayList());
        
        this.head.getGeneratingDistribution(paths, count);
        
        return paths;
    }
   
    
    public String traverse(IntArrayList choices){
        String in="";
        if(head.terminal==true)
            in+= head.value+" ";
        else
            in+=head.traverse( choices);
        return in;
        
    }
}
