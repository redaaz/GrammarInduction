/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package sentimentestcase;


import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import java.io.*;
import java.util.*;
import text.General;



/**
 *
 * @author reda
 */
public class SentimentTesting {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String fileName="50K_pos_markov_50000";
        String folderPath="/Users/reda/Downloads/markov-text-master/";

        List<String> input=General.read(folderPath,fileName);
        
        experiment ex=new experiment(input);
        
        ex.apply();
        
        ex.println();
        ex.writeExperiment(folderPath, fileName);
    }
    
    
}
