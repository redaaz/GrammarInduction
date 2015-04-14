/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentestcase;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author reda
 */
public class stanfordWrapper {
     public static SentimentType findSentiment(String line) {

        if(line == null || line.isEmpty()) {
          throw new IllegalArgumentException("The line must not be null or empty.");
        }

        Annotation annotation = processLine(line);

        int mainSentiment = findMainSentiment(annotation);

        if(mainSentiment < 0 || mainSentiment == 2 || mainSentiment > 4) { //You should avoid magic numbers like 2 or 4 try to create a constant that will provide a description why 2
           return SentimentType.Neutral; //You shold avoid null returns 
        }
        
        if(mainSentiment==1 || mainSentiment==0)
            return SentimentType.Negative;
        
        return SentimentType.Positive;

     }
     
     public static List<SentimentType> findSentiments(List<String> lines) {
         List<SentimentType> res=new ArrayList<>();
         
        if(lines == null || lines.isEmpty()) {
          throw new IllegalArgumentException("The line must not be null or empty.");
        }
            
        List<Annotation> annotations = processLines(lines);
        
        List<Integer> mainSentiments = findMainSentiments(annotations);
        
        for(Integer mainSentiment:mainSentiments){        
            if(mainSentiment==null){
                res.add(SentimentType.Null);
                continue;
            }
            if(mainSentiment < 0 || mainSentiment == 2 || mainSentiment > 4) { //You should avoid magic numbers like 2 or 4 try to create a constant that will provide a description why 2
               res.add(SentimentType.Neutral); 
               continue;
            }

            if(mainSentiment==1 || mainSentiment==0){
                res.add(SentimentType.Negative);
                continue;
            }

            res.add(SentimentType.Positive);
        }
        return res;
     }

     private static int findMainSentiment(Annotation annotation) {

        int mainSentiment = Integer.MIN_VALUE;
        int longest = Integer.MIN_VALUE;


        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

           int sentenceLength = String.valueOf(sentence).length();

           if(sentenceLength > longest) {

             Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);

             mainSentiment = RNNCoreAnnotations.getPredictedClass(tree);

             longest = sentenceLength ;

            }
        }

        return mainSentiment;

     }

     private static List<Integer> findMainSentiments(List<Annotation> annotations) {
        List<Integer> mainSentiments= new ArrayList<>();
        //for(int i=0;i<annotations.size();i++)
        //    mainSentiments.add(Integer.MAX_VALUE);
        
        int mainSentiment = Integer.MIN_VALUE;
        
        int counter=annotations.size();
        
        for(Annotation annotation: annotations){
            
            if(annotation==null){
                mainSentiments.add(null);
                continue;
            }
            //System.out.println("find Main Sentiment: "+counter--);
            counter--;
            int longest = Integer.MIN_VALUE;
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

               int sentenceLength = String.valueOf(sentence).length();

               if(sentenceLength > longest) {

                 Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                 
                 if(mainSentiments.size()+counter==annotations.size())
                    mainSentiments.set(mainSentiments.size()-1,RNNCoreAnnotations.getPredictedClass(tree));
                 else
                    mainSentiments.add(RNNCoreAnnotations.getPredictedClass(tree));

                 longest = sentenceLength ;

                }
            }
        }
        return mainSentiments;

     }

     private static Annotation processLine(String line) {
        
        StanfordCoreNLP pipeline = createPieline();
        
        
        return pipeline.process(line);

     }

     private static List<Annotation> processLines(List<String> lines) {
        
        StanfordCoreNLP pipeline = createPieline();
        
        List<Annotation> res =new ArrayList<>();
        int counter=lines.size();
        for(String line:lines)
        {
            counter--;
            if(counter%10==0)
                System.out.println("Process Line: "+counter);
                
            if(line==null || line.isEmpty() ){
                res.add(null);
                continue;
            }
            res.add(pipeline.process(line));
        }
        return res;

     }
     
     private static Properties createPipelineProperties() {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        return props;

     }
     
     private static StanfordCoreNLP createPieline() {

        Properties props = createPipelineProperties();

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        return pipeline;

     }
}
