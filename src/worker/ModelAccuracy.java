/**
 * 
 */
package worker;

import helper.IO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import constants.AccuracyType;
import constants.Consts;
import constants.ModelType;
import domain.BestProbableWord;

/**
 * @author harinder
 *
 */
public class ModelAccuracy {
	
	public static double getModelAccuracy(String imagesPath, String wordsPath, ModelType modelType, AccuracyType accuracyType) throws IOException {
		List<List<String>> images = IO.readImagesDat(imagesPath);
		List<String> goldWords = IO.readWordsDat(wordsPath);
		
		if(accuracyType==AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD){
			return getAverageDatasetLoglikelihood(images, goldWords, modelType);
		}
		
		List<String> mostProbableWords = getMostProbableWords(images, modelType);
		return getProbableWordsAccuracy(goldWords, mostProbableWords, accuracyType);
	}
	
	private static double getProbableWordsAccuracy(List<String> goldWords, List<String> probableWords, AccuracyType accuracyType) throws IOException {
		double accuracy = 0.0;
		
		switch(accuracyType){
		case CHARACTER_WISE:
			accuracy = getCharacterWiseAccuracy(goldWords, probableWords);
			break;
		case WORD_WISE:
			accuracy = getWordWiseAccuracy(goldWords, probableWords);
			break;
		case AVERAGE_DATASET_LOGLIKELIHOOD:
			//implemented there itself...no need to coming to this function
			break;
		}
		
		return accuracy;
	}
	
	private static double getAverageDatasetLoglikelihood(List<List<String>> images, List<String> goldWords, ModelType modelType) {
		double loglikelihood = 0.0;

		int count = images.size();
		for(int i=0;i<count;i++)
			loglikelihood += Math.log(ProbCalc.CalculateWordProbability(goldWords.get(i), images.get(i), modelType));
		
		loglikelihood /= count;
		return loglikelihood;
	}
	
	private static double getCharacterWiseAccuracy(List<String> goldWords, List<String> probableWords) {
		int similarCharsCount = 0;
		int totalCharsCount = 0;
		
		for(int i=0;i<probableWords.size();i++){
			similarCharsCount += getSimilarCharCount(goldWords.get(i), probableWords.get(i));
			totalCharsCount += goldWords.get(i).length();
		}
		
		double accuracy = ((double)similarCharsCount/totalCharsCount)*100;
		return accuracy;
	}
	
	private static double getWordWiseAccuracy(List<String> goldWords, List<String> probableWords) {
		int similarWordsCount = 0;
		int totalWordsCount = goldWords.size();
		
		for(int i=0;i<probableWords.size();i++){
			if(goldWords.get(i).equals(probableWords.get(i))) similarWordsCount++;
		}
		
		double accuracy = ((double)similarWordsCount/totalWordsCount)*100;
		return accuracy;
	}
	
	public static int getSimilarCharCount(String word1, String word2) {
		int count = 0;
		
		for(int i=0;i<word1.length();i++){
			if(word1.charAt(i) == word2.charAt(i)) count++;
		}
		
		return count;
	}
	
	public static List<String> getMostProbableWords(String imagesPath, ModelType modelType) throws IOException {
		List<List<String>> images = IO.readImagesDat(imagesPath);
		return getMostProbableWords(images, modelType);
	}
	
	private static List<String> getMostProbableWords(List<List<String>> images, ModelType modelType) {
		List<String> mostProbWords = new ArrayList<String>();
		
		for(List<String> imageIDs : images){
			int len = imageIDs.size();
				
			char[] wordArray = new char[len];
			BestProbableWord bestProbableWord = new BestProbableWord();
			
			getMostProbableWord(modelType, len, 0, imageIDs, wordArray, bestProbableWord);
			mostProbWords.add(bestProbableWord.wordString);
		}
		 
		 return mostProbWords;
	}
	
	private static String getMostProbableWord(List<String> imageIDs, ModelType modelType) {
		int len = imageIDs.size();
		
		char[] wordArray = new char[len];
		BestProbableWord bestProbableWord = new BestProbableWord();
		
		getMostProbableWord(modelType, len, 0, imageIDs, wordArray, bestProbableWord);
		return bestProbableWord.wordString;
	}
	
	
	private static void getMostProbableWord(ModelType modelType, int len, int idx,
			List<String> imageIDs, char[] wordArray, BestProbableWord bestProbWord) {
		if (idx == len) {
			String wordString = String.valueOf(wordArray);
			double prob = ProbCalc.getWordProbabilityNumerator(wordString, imageIDs, modelType, len);
			if(prob>bestProbWord.bestProb) {bestProbWord.wordString=wordString; bestProbWord.bestProb=prob;}
			return;
		}
 
		for (int i = 0; i < Consts.characters.length; i++) {
			wordArray[idx] = Consts.characters[i];
			getMostProbableWord(modelType, len, idx + 1, imageIDs, wordArray, bestProbWord);
		}
	}
}
