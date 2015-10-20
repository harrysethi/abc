/**
 * 
 */
package test;

import helper.IO;

import java.io.IOException;
import java.util.List;

import constants.ModelType;
import domain.CliqueTreeHelper;
import domain.InGraph;
import domain.InGraphHelper;
import domain.Pair_data;

/**
 * @author harinder
 *
 */


//TODO: vaishali said ki can we use 'set' as adjancencies in inGraph to have search time as O(1)
//TODO: if no pair-skip factors, graph disconnected
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("------started------");
		
		readPotentials();
		
		String dataTreePath = "OCRdataset-2/data/data-loopsWS.dat";
		List<Pair_data> dataPairs = IO.readDataTree(dataTreePath);
		
		List<InGraph> inGraphs = InGraphHelper.makeInGraph(dataPairs, ModelType.PAIR_SKIP_MODEL);
		//InGraphNode minFillNode = CliqueTreeHelper.getMinFillNode(inGraphs.get(0));
		
		CliqueTreeHelper.cliqueTree_msgPassing_calcBelief(inGraphs.get(1), ModelType.PAIR_SKIP_MODEL);
		
		System.out.println("..........here............");
		
		//String[] imageID_arr = { "3", "3" };
		//printProbabilities("aa", imageID_arr);

		//printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE);
		//printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE);
		/*String imagesPath1 = "OCRdataset/data/large/allimages1.dat";
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, imagesPath1); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE, imagesPath1); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath1); 
		
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.CHARACTER_WISE, imagesPath1); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE, imagesPath1);
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath1); 
		
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE, imagesPath1); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.WORD_WISE, imagesPath1);
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath1); 
		
		String imagesPath2 = "OCRdataset/data/large/allimages2.dat";
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, imagesPath2); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE, imagesPath2); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath2); 
		
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.CHARACTER_WISE, imagesPath2); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE, imagesPath2); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath2); 
		
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE, imagesPath2); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.WORD_WISE, imagesPath2); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath2); 
		
		String imagesPath3 = "OCRdataset/data/large/allimages3.dat";
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, imagesPath3); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE, imagesPath3); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath3); 
		
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.CHARACTER_WISE, imagesPath3); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE, imagesPath3); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath3); 
		
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE, imagesPath3); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.WORD_WISE, imagesPath3); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath3); 
		
		String imagesPath4 = "OCRdataset/data/large/allimages4.dat";
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, imagesPath4); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE, imagesPath4); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath4); 
		
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.CHARACTER_WISE, imagesPath4); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE, imagesPath4); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath4); 
		
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE, imagesPath4); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.WORD_WISE, imagesPath4); 
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath4); 
		
		String imagesPath5 = "OCRdataset/data/large/allimages5.dat";
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, imagesPath5); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.WORD_WISE, imagesPath5); 
		printModelAccuracy(ModelType.OCR_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath5); 
		
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.CHARACTER_WISE, imagesPath5); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.WORD_WISE, imagesPath5); 
		printModelAccuracy(ModelType.TRANSITION_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath5); 
		
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE, imagesPath5);
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.WORD_WISE, imagesPath5);
		printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.AVERAGE_DATASET_LOGLIKELIHOOD, imagesPath5); */
		
		
		//printModelAccuracy(ModelType.COMBINED_MODEL, AccuracyType.CHARACTER_WISE);

		// List<String> probWords =
		// ModelAccuracy.getMostProbableWords(imagesPath,
		// ModelType.COMBINED_MODEL);

		System.out.println("------completed------");
	}

	/*private static void printModelAccuracy(ModelType modelType,
			AccuracyType accuracyType, String imagesPath) throws IOException {
		String wordsPath = "OCRdataset/data/large/allwords.dat";

		double accuracy = ModelAccuracy.getModelAccuracy(imagesPath, wordsPath,
				modelType, accuracyType);
		System.out.println(accuracy);
	}*/

	private static void readPotentials() throws IOException {
		String ocrPotentialsPath = "OCRdataset-2/potentials/ocr.dat";
		String transPotentialsPath = "OCRdataset-2/potentials/trans.dat";

		helper.IO.readPotentials(ocrPotentialsPath, transPotentialsPath);
	}
	
	private static void readTree() throws IOException {
		String dataTreePath = "OCRdataset-2/data/data-tree.dat";
		String truthTreePath = "OCRdataset-2/data/truth-tree.dat";

		IO.readDataTree(dataTreePath);
		IO.readTruthTree(truthTreePath);
	}

	/*private static void printProbabilities(String word, String[] imageID_arr) {
		List<String> imageIDs = Arrays.asList(imageID_arr);

		System.out.println(ProbCalc.CalculateWordProbability(word, imageIDs,
				ModelType.OCR_MODEL));
		System.out.println(ProbCalc.CalculateWordProbability(word, imageIDs,
				ModelType.TRANSITION_MODEL));
		System.out.println(ProbCalc.CalculateWordProbability(word, imageIDs,
				ModelType.COMBINED_MODEL));
	}*/

}
