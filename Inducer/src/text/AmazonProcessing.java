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
public class AmazonProcessing {
    public static void main(String[] args) throws IOException {
        String folderPath="/Users/reda/Documents/NewAlgoTests/Amazon/Clothing&Accessories/";
        String fileName="Clothing&Accessories";
        int maxi=10000;
        
        List<String> input=General.read(folderPath, fileName);
        List<String> pos=new ArrayList<>();
        List<String> neg=new ArrayList<>();
        List<String> neu=new ArrayList<>();
        boolean newRev=true;
        int score=Integer.MAX_VALUE;
        for(String str:input){
            if(str.startsWith("review/score: ")){
                newRev=true;
                score=getScore(str);
            }
            else if(str.startsWith("review/text: ") && newRev){
                if(score==-1){
                    neg.add(str.substring(13));
                }else if(score==0){
                    neu.add(str.substring(13));
                }else if(score==1){
                    pos.add(str.substring(13));
                }
                newRev=false;
            }
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
        if(str.contains("1") || str.contains("2"))
            return -1;
        if(str.contains("3"))
            return 0;
        return 1;
    }
    
    
}
