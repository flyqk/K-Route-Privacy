package privacy599;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ExperimentalSimulation {

	public static void main(String[] args) {
		StringBuilder analysisResults = new StringBuilder();
		analysisResults.append("route, K, T, F\n");
		for(int i=1; i<6; i++){
			String inputFileName = "data\\route"+i+"\\usc_route_"+i+".gpx";
			String outputFileName = "data\\route"+i+"\\routes.json";
			ConvertPoints.convertPoints(inputFileName, outputFileName);
			for(int k=1; k<16; k++){
				int f = KRoutePrivacy.KRoutePrivacy(k, 720, outputFileName, "data\\route"+i+"\\route_"+k+".gdx");
				analysisResults.append("route_"+i+","+k+",15,"+f+"\n");
			}
		}
		
		
		try {
			PrintWriter out = new PrintWriter("data\\analysisResults.csv");
			out.println(analysisResults.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	}

}
