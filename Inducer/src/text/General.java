/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import datastructure.CommonSlots;
import heuristic.Heuristic;
import heuristic.LongestMostFrequent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.AlgoCMSPAM_HPPC;
import spm.spam.SPMiningAlgorithm;


/**
 *
 * @author reda
 */
public class General {
    public static boolean tryParseInt(String value)  
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
    
    
    public static boolean ContainsSequence( List<Integer> pattern, List<Integer> toTest)
    {
        if(pattern.isEmpty())
            return true;
        if(pattern.size()==1)
            return toTest.contains(pattern.get(0));
        
        List<Boolean> test=new ArrayList<>(Collections.nCopies(pattern.size(), false));
                int currentPosition=0;
        for(int i=0;i<pattern.size();i++){
            
            for(int j=currentPosition;j<toTest.size();j++,currentPosition++){
                if(Objects.equals(pattern.get(i), toTest.get(j))){
                    test.set(i, Boolean.TRUE);
                    break;
                }        
            }
        }
        
        return !test.contains(Boolean.FALSE);
    }
    
    //convert from CMSPAM coded string to list of integer
    public static List<Integer> toIntegerList(String x){
        List<Integer> res=new ArrayList<>();
        String str=x.replace("-2", "");
        String replaced = str.replace(" -1 "," ");
        String[] tokens= replaced.split(" ");
        
        for(String s:tokens){
            if(tryParseInt(s)){
                res.add(Integer.parseInt(s));
            }
            else{
                return null;
            }
        }
        
        return res;
    }
    
    //to siplit an integer list by splitters -returning n+1 lists of slots between splitters (sometimes empty lists added)
    public static List<IntArrayList> split(List<Integer> toSplit,List<Integer> splitters){
        List<IntArrayList> res=new ArrayList<>();
        if(toSplit.isEmpty() || splitters.isEmpty())
            return null;
        int lastIndex=0;
        int index=0;
        
        for(Integer sp:splitters){    
            while(!Objects.equals(toSplit.get(index), sp)){
                index++;
            }
            if(lastIndex==index){
                res.add(new IntArrayList());
            }
            else{
                //res.add(toSplit.subList(lastIndex, index));    
                res.add(subList(toSplit,lastIndex, index));    
            }   
            index++;
            lastIndex=index;
        }
        //the last list
        if(index==toSplit.size()){
            res.add(new IntArrayList());
        }
        else{
           //res.add(toSplit.subList(index, toSplit.size()));     
            res.add(subList(toSplit,index, toSplit.size()));      
        }
        return res;
    }
    
    public static CommonSlots findCommonReferencesSlots(IntObjectOpenHashMap<IntArrayList> referenceToSlots,SPMiningAlgorithm algo,double minSup){
        ObjectArrayList<IntArrayList> in=new ObjectArrayList();
        IntIntOpenHashMap tempIndexsToinputIndexs=new IntIntOpenHashMap();
        //Iterator it = referenceToSlots.entrySet().iterator();
       /* Iterator it = referenceToSlots.iterator();
        while(it.hasNext()){
             HashMap.Entry pairs = (HashMap.Entry)it.next();
             in.add((List<Integer>)pairs.getValue());
             tempIndexsToinputIndexs.put(in.size()-1, (Integer)pairs.getKey());
        }
        */
         Iterator it=referenceToSlots.iterator();
         while(it.hasNext()){
              IntObjectCursor intobjectCursor=(IntObjectCursor)it.next();
              in.add((IntArrayList)intobjectCursor.value);
              tempIndexsToinputIndexs.put(in.size()-1,intobjectCursor.key);
                      
         }
        
        
        Heuristic heu=new LongestMostFrequent();
        //CommonSlots cs=intersect(in,algo,heu,minSup,tempIndexsToinputIndexs);
        CommonSlots cs=intersect(in,tempIndexsToinputIndexs);
        //minSup condition
        if(cs!=null && cs.commonReferences.size()>=minSup)
            return cs;
        return null;
    }
    
    //lists: list of slots for each input indexs
    //tempIndexsToinputIndexs: to convert from 0-based indexs sentences to global indexs
    //this function find the best intersection between slots to build new rules (longest - most frequent)
    @SuppressWarnings("empty-statement")
    static CommonSlots intersect(ObjectArrayList<IntArrayList> lists,IntIntOpenHashMap tempIndexsToinputIndexs){
        if(lists==null || lists.isEmpty())
            return null;
        
        ObjectArrayList<String> slotsStringforEachInput= new ObjectArrayList();
        /*
        lists.stream().forEach(li->{
           StringBuilder str=new StringBuilder();
           li.stream().forEach(elem-> str.append(elem));
           slotsStringforEachInput.add(str.toString());
        });
        */
        for(ObjectCursor<IntArrayList> li:lists){
           StringBuilder str=new StringBuilder();
           for(IntCursor elem: li.value){
               str.append(elem.value);
           }
           slotsStringforEachInput.add(str.toString());
        }
        
        ObjectIntOpenHashMap<String> Counter=new ObjectIntOpenHashMap();
        int MaxCount=0;
        ObjectArrayList<String> MaxString=new ObjectArrayList();
        ObjectObjectOpenHashMap<String,IntArrayList> refIds=new ObjectObjectOpenHashMap();
        int IdCounter=0;
        for(ObjectCursor<String> strC:slotsStringforEachInput) {
            String str=strC.value;
            if(!Counter.containsKey(str)){
                Counter.put(str,1);
            }
            else{
                Counter.put(str, Counter.get(str)+1);
            }
            
            if(Counter.get(str)>MaxCount){
                MaxString.clear();
                MaxString.add(str);
                MaxCount=Counter.get(str);
                
            }else if(Counter.get(str)==MaxCount){
                if(str.length()>MaxString.get(0).length())
                    MaxString.insert(0, str);
                else
                    MaxString.add(str);
                
            }
            if(refIds.get(str)==null)
                refIds.put(str, new IntArrayList());
            refIds.get(str).add(IdCounter);
            IdCounter++;
        };
        
        if(MaxString.isEmpty())
            return null;
        
        //if MaxString.size()>1
        CommonSlots res=new CommonSlots();
        //res.commonReferences=refIds.get(MaxString.get(0)).stream().map(x->tempIndexsToinputIndexs.get(x)).collect(Collectors.toList());
        IntArrayList tempo=new IntArrayList();
        for(IntCursor x:refIds.get(MaxString.get(0))){
            tempo.add(tempIndexsToinputIndexs.get(x.value));
        }
        res.commonReferences=tempo;
        res.slots=new IntArrayList();
        for(Character cc:MaxString.get(0).toCharArray()){
            res.slots.add(Integer.parseInt(""+cc));
        }
        return res;
        //else if ==0
        
    }
    
    public static String integerListToCMSPAMString(List<Integer> in){
        StringBuilder str=new StringBuilder();
        if(in==null || in.isEmpty())
            return "-2";
        in.stream().forEach(x->str.append(x).append(" -1 "));
        str.append("-2");
        return str.toString();
    }
    
    public static List<Integer> subList(IntArrayList li,int includedStart,int excludedEnd){
        List<Integer> res=new ArrayList<>();
        
        if(li==null || includedStart>excludedEnd || excludedEnd>li.size())
            return null;
        
        for(int i=includedStart;i<excludedEnd;i++)
            res.add(li.get(i));
        
        return res;
    }
    
    public static IntArrayList subList(List<Integer> li,int includedStart,int excludedEnd){
        IntArrayList res=new IntArrayList();
        
        if(li==null || includedStart>excludedEnd || excludedEnd>li.size())
            return null;
        
        for(int i=includedStart;i<excludedEnd;i++)
            res.add(li.get(i));
        
        return res;
    }
    
    public static IntArrayList toIntArrayList(int[] in){
        IntArrayList res=new IntArrayList();
        for(int i:in){
           res.add(i);
        }
        return res;
    }
    
    public static List<String> read(String folderPath,String fileName) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(folderPath+fileName+".txt"));
        String line;
        List<String> in = new ArrayList<>();
        
        while ((line = br.readLine()) != null) {
            in.add(line);
        }
        br.close();
        return in;
    }
    
    public static String getRoundedValue(double in){
        return ""+Math.round(in*100.0)/100.0;
    }
    
    public static void write(List<String> records,String outputPath,String filename) throws IOException {
        int bufSize=(int) Math.pow(1024, 2);
        File file;
        if(outputPath==null){
            file= new File("foo", ".txt");
        }
        else{
            file=new File(outputPath,filename+".txt");
        }
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

            System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
            write1(records, bufferedWriter);
        } finally {
            // comment this out if you want to inspect the files afterward
            //file.delete();
        }
    }
    
    public static void write1(List<String> records, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        for (String record: records) {
            writer.write(record+"\n");
        }
        writer.flush();
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000f + " seconds");
    }
}
