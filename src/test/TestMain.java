/**
 * 
 */
package test;

import java.io.IOException;

import worker.ModelAccuracy;
import constants.AccuracyType;
import constants.InferenceType;
import constants.ModelType;

/**
 * @author harinder
 *
 */

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("------started------");
		
		readPotentials();
		
		String dataTreePath = "OCRdataset-2/data/data-loopsWS.dat";
		String truthTreePath = "OCRdataset-2/data/truth-loopsWS.dat";

		//List<Pair_data> dataPairs = IO.readDataPairs(dataTreePath);
		//List<InGraph> inGraphs = InGraphHelper.makeInGraph(dataPairs, ModelType.PAIR_SKIP_MODEL);
		//CliqueTreeHelper.cliqueTree_msgPassing_calcBelief(inGraphs.get(5), ModelType.PAIR_SKIP_MODEL);
		
		//System.out.println(ModelAccuracy.getModelAccuracy(dataTreePath, truthTreePath, ModelType.OCR_MODEL, AccuracyType.CHARACTER_WISE, InferenceType.JUNCTION_TREE_MP));
		
		System.out.println(ModelAccuracy.getModelAccuracy(dataTreePath, truthTreePath, ModelType.PAIR_SKIP_MODEL, AccuracyType.CHARACTER_WISE, InferenceType.LB));
		
		System.out.println("------completed------");
	}

	private static void readPotentials() throws IOException {
		String ocrPotentialsPath = "OCRdataset-2/potentials/ocr.dat";
		String transPotentialsPath = "OCRdataset-2/potentials/trans.dat";

		helper.IO.readPotentials(ocrPotentialsPath, transPotentialsPath);
	}

}
