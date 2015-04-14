/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;


import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.sizeof.RamUsageEstimator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.Bitmap;
import text.General;

/**
 *
 * @author reda
 */
public class FrequentPattern {
    List<Integer> patterns;
    int sup;
    List<Integer> inputReferences;
    double cohesion; // calculated by Jaccard index (law)
    
    public FrequentPattern(){
        patterns = new ArrayList<>();
        inputReferences=new ArrayList<>();
        sup=0;
        this.cohesion=-1;
    }
    
    public FrequentPattern(List<Integer> in,int supp){
        this.sup=supp;
        inputReferences=new ArrayList<>();
        patterns = new ArrayList<>();
        patterns = in;
        this.cohesion=-1;
    }
    //take string from CMSPAM format "number -1 number -1 .... -1 -2"
    public FrequentPattern(String x){
        //check for null or empty string
        if(x==null || x.isEmpty()){
            patterns = null;
            sup=-1;
            return;
        }   
        String[] parts=x.split("SUP: ");
        //check for parsing error
        if(parts.length!=2){
            patterns = null;
            sup=-1;
            return;
        }
        
        //set sup value
        if(!General.tryParseInt(parts[1])){
            patterns=null;
            sup=-1;
            return;
        }
        else{
            sup=Integer.parseInt(parts[1]);
        }
        this.cohesion=-1;
        patterns = new ArrayList<>();
        String[] Indexes= parts[0].split(" -1 ");
        
        for(String str: Indexes){
            boolean valid=General.tryParseInt(str);
            if(!str.isEmpty() && valid){
                patterns.add(Integer.parseInt(str));        
            }
            if(!valid){
                patterns=null;
                sup=-1;
                return;
            }
                
        }
        
    }
    
    @Override
    public String toString(){
        if(patterns==null)
            return null;
        if(patterns.isEmpty())
            return "";
        
        String res="";
        
        res = patterns.stream().filter((index) -> (WordsDictionary.isExist((int)index))).map((index) -> WordsDictionary.getWord(index)+" ").reduce(res, String::concat);
        
        return res;
    }
    
    public String toStringWithSup(){
        return toString() + "SUP: "+sup;
    }
    
    public String toStringCode(){
        String res="";
        if(patterns==null)
            return null;
        if(patterns.isEmpty())
            return "";
        res= patterns.stream().map((i) -> ""+i+" ").reduce(res, String::concat);
        return res;
    }
    
    public String toStringCodeWithSup(){
        return toStringCode()+ "SUP: "+sup;
    }
    
    //@param input is a list of repetitions for each token in the frequent pattern - repetition means: [inputSentenceID & tokenPosition in such input sentence ]
    public void setInputReferences(List<List<Repetition>> input,List<Sentence> in ){
        this.inputReferences=intersect(input,in);
    }
    
    public void setInputReferences2(List<List<Repetition>> input,List<String> in ){
        this.inputReferences=intersect2(input,in);
    }
    //to calc cohesion by Jaccard index law i.e.: intersection(11)/union (10||01||11)
    //@param vdb is the vertical data base used in CMSPAM the algorithm
    //required: sup value 
    public void setCohesion(Map<Integer,Bitmap> vdb){
        /*PERFORMANCE TEST*/long acc1=0,acc2=0,acc3=0,acc4=0;
        
        if (this.patterns.size()<=1){
            this.cohesion=0;//to be discussed
            return;
        }
        
        IntOpenHashSet set=new IntOpenHashSet();
        for(Integer pp:this.patterns){
            //((Bitmap)vdb.get(pp)).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList());
            for(Repetition rp:((Bitmap)vdb.get(pp)).inputReferences){
                set.add(rp.inputSentenceID);
            }
        }
        if(set.isEmpty())
            this.cohesion=-1;
        
        this.cohesion=this.sup/(double)set.size();
        
    }
    
    //repList List<Repetition> for each item in the pattern
    public void setCohesion(List<List<Repetition>> repList){
        /*PERFORMANCE TEST*/long acc1=0,acc2=0,acc3=0,acc4=0;
        
        if (this.patterns.size()<=1){
            this.cohesion=0;//to be discussed
            return;
        }
        
        IntOpenHashSet set=new IntOpenHashSet();
        for(List<Repetition> pp:repList){
            //((Bitmap)vdb.get(pp)).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList());
            for(Repetition rp:pp){
                set.add(rp.inputSentenceID);
            }
        }
        if(set.isEmpty())
            this.cohesion=-1;
        
        this.cohesion=this.sup/(double)set.size();
        
    }
    
    public double getCohesion(){
        return this.cohesion;
    }
    
    public List<Integer> getPattern(){
        return this.patterns;
    }
    
    public void printReferencesList(){
        this.inputReferences.stream().forEach((i) -> {
            System.out.print(i+" ");
        });
         System.out.println();
    }
    
    public List<Integer> getReferencesList(){
        return this.inputReferences;
    }
    
    public String toStringReferencesList(){
        String res="";
        res = this.inputReferences.stream().map((i) -> (""+i+" ")).reduce(res, String::concat);
        return res;
    }
    //check that all pattern items appear in the refreces sentences in the correst order
    //this is not efficient solution for this issue 
    public List<Integer> patternOrderCorrection(List<Integer> references){
        List<Integer> res=new ArrayList<>();
        
        
                
        return res;
    }
    //returns list of input related sentences indexs 
    public  List<Integer> intersect(List<List<Repetition>> input,List<Sentence> in){
        if(input.isEmpty())
            return null;
        if(input.size()==1)
            return input.get(0).stream().map(x->x.inputSentenceID).collect(Collectors.toList());
        /*PERFORMANCE TEST*/long acc1=0,acc2=0,acc3=0,acc4=0;
        /*PERFORMANCE TEST*/
        //choose smallest set to optimaize the intersection
        /*PERFORMANCE TEST*/long tt1=System.currentTimeMillis();
        int minListSizeIndex=-1;
        int minListSize=Integer.MAX_VALUE;
        for(int i=0;i<input.size();i++){
            if(!input.get(i).isEmpty()&& input.get(i).size()<minListSize){
                minListSizeIndex=i;
                minListSize=input.get(i).size();
            }        
        }
        /*PERFORMANCE TEST*/long tt2=System.currentTimeMillis();
        /*PERFORMANCE TEST*/acc1+=tt2-tt1;
        //Set<Integer> uniqueNums = new HashSet<>();
        //uniqueNums.addAll(input.get(minListSizeIndex).stream()
        //.map(x->x.inputSentenceID).collect(Collectors.toList()));
        
        IntOpenHashSet uniqueNums = new IntOpenHashSet();
        for(Repetition x: input.get(minListSizeIndex))
            uniqueNums.add(x.inputSentenceID);
        
        /*PERFORMANCE TEST*/long tt3=System.currentTimeMillis();
        /*PERFORMANCE TEST*/acc2+=tt3-tt2;
        for(int i=0;i<input.size();i++){
            if(i!=minListSizeIndex){
                IntOpenHashSet temp=new IntOpenHashSet();
                for(Repetition hh:input.get(i)){
                    temp.add(hh.inputSentenceID);
                }
                uniqueNums.retainAll(temp);
            }
        }
        
        IntArrayList res1=new IntArrayList(uniqueNums);
        List<Integer> res=new ArrayList<>();
        /*PERFORMANCE TEST*/long tt4=System.currentTimeMillis();
        /*PERFORMANCE TEST*/acc3+=tt4-tt3;
        for(IntCursor i: res1){
            //if (General.ContainsSequence(this.patterns,General.toIntegerList(in.get(i).toStringCM_SPAM())))
            
            if (General.ContainsSequence(this.patterns,in.get(i.value).sentenceCode))
               res.add(i.value);       
        }
        /*PERFORMANCE TEST*/long tt5=System.currentTimeMillis();
        /*PERFORMANCE TEST*/acc4+=tt5-tt4;
        //*PERFORMANCE TEST*/System.out.println("tt1 : "+acc1 );
        //*PERFORMANCE TEST*/System.out.println("tt2 : "+acc2 );
        //*PERFORMANCE TEST*/System.out.println("tt3 : "+acc3 );
        //*PERFORMANCE TEST*/System.out.println("tt4 : "+acc4 +" # "+res1.size());
        
        return res;
    }
    
    public List<Integer> intersect2(List<List<Repetition>> input,List<String> in1){
        //choose smallest set to optimaize the intersection
        int minListSizeIndex=-1;
        int minListSize=Integer.MAX_VALUE;
        for(int i=0;i<input.size();i++){
            if(!input.get(i).isEmpty()&& input.get(i).size()<minListSize){
                minListSizeIndex=i;
                minListSize=input.get(i).size();
            }        
        }
        
        Set<Integer> uniqueNums = new HashSet<>(input.get(minListSizeIndex).stream()
        .map(x->x.inputSentenceID).collect(Collectors.toList()));
        
        for(int i=0;i<input.size();i++){
            if(i!=minListSizeIndex)
                uniqueNums.retainAll(new HashSet<>(input.get(i).stream()
        .map(x->x.inputSentenceID).collect(Collectors.toList())));
        }
        
        List<Integer> res1=new ArrayList<>(uniqueNums);
        List<Integer> res=new ArrayList<>();
        
        for(Integer i: res1){
            if (General.ContainsSequence(this.patterns,General.toIntegerList(in1.get(i))))
               res.add(i);       
        }
        return res;
    }
    
    public void println(){
        System.out.println(this.toString()+" ("+this.sup+") ");
    }
    
    public void printlnWithReferencesList(){
        System.out.println(this.toString()+" ("+this.sup+") "+this.toStringReferencesList());
    }

    public int getSup(){
        return this.sup;
    }
}
