/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentusecase;

import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import text.General;

/**
 *
 * @author reda
 */
public class experiment {
    
    private List<String> input;
    
    private List<SentimentType> sentimentInput;
    
    private int posCounter=0;
    private int negCounter=0;
    private int neuCounter=0;
    private int nullCounter=0;
    public experiment(List<String> in){
        this.input=in;
        this.sentimentInput=new ArrayList<>();
    }
    
    public double getPositiveRatio(){
        if(sentimentInput.isEmpty())
                return -1;
        return (100.0*this.posCounter)/((double)sentimentInput.size());
    }
    
    public double getNegativeRatio(){
        if(sentimentInput.isEmpty())
                return -1;
        return (100.0*this.negCounter)/((double)sentimentInput.size());
    }
    
    public double getNeutralRatio(){
        if(sentimentInput.isEmpty())
                return -1;
        return (100.0*this.neuCounter)/((double)sentimentInput.size());
    }
    
    public void apply(){
        System.out.print("Total sentences to analyse: "+this.input.size());
        
        this.sentimentInput=stanfordWrapper.findSentiments(this.input);
        this.sentimentInput.stream().forEach(x->this.addResult(x));
        /*
        int counter=this.input.size();
        
        
        for(String str: this.input){
            //if(counter%20==0)
            {    
                System.out.println(counter-- +" sens. remain to finish.");   
            }
            SentimentType st=stanfordWrapper.findSentiment(str);
            
            this.sentimentInput.add(st);
            this.addResult(st);
        }
        */
    }
    
    public int size(){
        return this.sentimentInput.size();
    }
    
    private void addResult(SentimentType st){
        if(st==SentimentType.Positive)
            this.posCounter++;
        if(st==SentimentType.Negative)
            this.negCounter++;
        if(st==SentimentType.Neutral)
            this.neuCounter++;
        if(st==SentimentType.Null)
            this.nullCounter++;
    }
    
    public String toString(SentimentType st){
        StringBuilder str=new StringBuilder();
        
        if(st==SentimentType.Positive)
            str.append("Positive Ratio: ").append(General.getRoundedValue( this.getPositiveRatio())).append("%");
        if(st==SentimentType.Negative)
            str.append("Negative Ratio: ").append(General.getRoundedValue(this.getNegativeRatio())).append("%");
        if(st==SentimentType.Neutral)
            str.append("Neutral Ratio: ").append(General.getRoundedValue(this.getNeutralRatio())).append("%");
        if(st==SentimentType.Null)
            str.append("Null Count: ").append(this.nullCounter).append("%");
        
        return str.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder str=new StringBuilder();
        str.append("Total Size: ").append(this.size()).append(" sentences.");
        return str.toString();
    }
    
    public void println(){
        System.out.println(this.toString());
        System.out.println(this.toString(SentimentType.Positive)+"  ("+this.posCounter +")");
        System.out.println(this.toString(SentimentType.Negative)+"  ("+this.negCounter+")");
        System.out.println(this.toString(SentimentType.Neutral)+"  ("+this.neuCounter+")");
        System.out.println(this.toString(SentimentType.Null));
    }
    
    public void writeExperiment(String folderPath,String inputFileName) throws IOException{
        String timeStamp=(new Date()).toString();
        
        
        String ExperimentReportName="("+inputFileName+") "+timeStamp;
        
        List<String> in=new ArrayList<>();
        
        in.add(this.toString());
        in.add(this.toString(SentimentType.Positive)+"  ("+this.posCounter +")");
        in.add(this.toString(SentimentType.Negative)+"  ("+this.negCounter+")");
        in.add(this.toString(SentimentType.Neutral)+"  ("+this.neuCounter+")");
        in.add(this.toString(SentimentType.Null));
        General.write(in, folderPath, ExperimentReportName);
        
            
        
    }
}
