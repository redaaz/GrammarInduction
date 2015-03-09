/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reda
 */
public class TwitterProcessing {
    public static void main(String[] args) throws IOException {
        String folderPath="/Users/reda/Documents/NewAlgoTests/Twitter/";
        String fileName="training.1600000.processed.noemoticon";
        int maxi=200000;
        
        List<String> input=General.read(folderPath, fileName);
        List<String> pos=new ArrayList<>();
        List<String> neg=new ArrayList<>();
        List<String> neu=new ArrayList<>();
        
        int score=Integer.MAX_VALUE;
        int counter=0;
        for(String str:input){
            counter++;
            if(counter%10000==0)
                System.out.println(" -> "+counter);
            String[] ll=str.split(",");
            
            score=getScore(ll[0]);
            if(ll[5].length()<4)
                continue;
            if(score==-1){
                neg.add(ll[5].substring(1,ll[5].length()-1));
            }else if(score==0){
                neu.add(ll[5].substring(1,ll[5].length()-1));
            }else if(score==1){
                pos.add(ll[5].substring(1,ll[5].length()-1));
            }
            if(pos.size()>=maxi && neg.size()>=maxi && neu.size()>=maxi)
                break;
            
        }
        
        
        
        List<String> nn=new ArrayList<>();
        List<List<String>> ll=new ArrayList<>();
        ll.add(pos);ll.add(neg);ll.add(neu);
        nn.add("pos");nn.add("neg");nn.add("neu");
        for(int i=0;i<3;i++){
            if(ll.get(i).size()<=maxi){
                General.write(ll.get(i), folderPath, fileName+"_("+ll.get(i).size()+")_"+nn.get(i));
            }
            else{
                List<String> sub=ll.get(i).subList(0, maxi);
                General.write(sub, folderPath, fileName+"_("+sub.size()+")_"+nn.get(i));
            }
        }
        
        
    }
    
    
    public static int getScore(String str){
        if(str.contains("0"))
            return -1;
        if(str.contains("2"))
            return 0;
        return 1;
    }
}
