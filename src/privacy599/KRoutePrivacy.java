package privacy599;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class KRoutePrivacy {

	/**
	 * K route privacy algorithm
	 * 
	 * A parameter K indicates the required number of routes among the location time series D [{x, y, t}, ..., {x_n, y_n, t_n}]
	 * The algorithm returns the minimum number of required frequency.
	 * Data structure is two  dimensional matrix R[][] with location points as coordinates to reflect the queried number of routes between the two points:
	 * 
	 * 	  p1 p2 p3 .... pn
	 * p1  1  3  3 .... 3	
	 * p2  3  0  1 .... 5
	 * p3  ..............
	 * .   ..............
	 * .   ..............
	 * pn  ..............
	 * 
	 * @param args
	 */
	
	public static int KRoutePrivacy(int k, long T, String dataPath, String outputPath){
//		int k = Integer.parseInt(args[0]);
//		int k = 10;
		int routes = 0;
		int f = 1;
		long t = T;
//		ArrayList<Point> points = new ArrayList<Point>();
//		ArrayList<Point> eliminatedDataPoints = new ArrayList<Point>();
//		Integer[][] R = null;
		readPointsandRoutesFromJSONFile(dataPath);
		Point[] eliminatedDataPoints = new Point[0];
		while(routes <= k){
			routes = 1;
			eliminatedDataPoints = eliminateDataByFrequency(f, points);
			int lastPointIndex = -1;
			
			for(int i = 0; i < eliminatedDataPoints.length; i++){
				Point point = eliminatedDataPoints[i];
				if(lastPointIndex != -1 && point != null){
					routes *= routesList[i][lastPointIndex];
//					System.out.println("x:"+i+" y:"+lastPointIndex+" "+routesList[i][lastPointIndex]);
				}
				if(eliminatedDataPoints[i] != null){
				lastPointIndex = i;
				}
			}
			f++;
		}
		
		System.out.println(k);
		System.out.println(f);
		
//		presentData(eliminatedDataPoints);
		try {
			writeEliminatedPoints(eliminatedDataPoints, k, outputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return f;
	}
	
	public static void main(String[] args) {
		KRoutePrivacy(2, 720, "data\\route2\\routes.json", "data\\routes_"+2+".gdx");
	}
	
	
	private static void writeEliminatedPoints(Point[] eliminatedDataPoints, int k, String outputPath) throws FileNotFoundException {
		String fileName = outputPath;
		StringBuilder builder = new StringBuilder();
		int totalPointsNumber = 0;
		for(Point point : eliminatedDataPoints){
			if(point == null){
				continue;
			}
			totalPointsNumber++;
			builder.append("<trkpt lat=\"");
			builder.append(point.lat+"\" lon=\"");
			builder.append(point.lon+"\"/>\n");
		}
		
		System.out.println(totalPointsNumber);
		PrintWriter out = new PrintWriter(fileName);
		out.println(builder.toString());
		out.close();
	}


	static int[][] routesList = null;
	static Point[] points = null;
	
	
	private static void readPointsandRoutesFromJSONFile(String filePath){
		  JSONParser jsonParser = new JSONParser();

	        try {     
	            Object obj = jsonParser.parse(new FileReader(filePath));

	            JSONObject jsonObject =  (JSONObject) obj;
	            
	            JSONObject pointsObject = (JSONObject) jsonObject.get("points");
	            points = new Point[pointsObject.size()];
	            Iterator<String> iterator = pointsObject.keySet().iterator();
	            while(iterator.hasNext()){
	            	String key = iterator.next();
//	            	String value = (String) pointsObject.get(key);
	            	JSONObject pointObject = (JSONObject) pointsObject.get(key);
	            	Point point = new Point();
	            	point.lat = Double.parseDouble(pointObject.get("lat").toString());
	            	point.lon = Double.parseDouble(pointObject.get("lon").toString());
	            	point.timestamp = Long.parseLong(pointObject.get("timeStamp").toString());
	            	int index = Integer.parseInt(key) -1;
	            	points[index]=point;
	            }
	            int pointsNumber = points.length;
	            routesList = new int[pointsNumber][pointsNumber];
	            JSONArray routesArray = (JSONArray) jsonObject.get("routes");
	            int pairNumber = routesArray.size();
	            for(int i= 0; i<pairNumber; i++){
	            	JSONArray route = (JSONArray) routesArray.get(i);
//	            	routesList[][Integer.parseInt(routes.get(0))-1];
	            	int xIndex = Integer.parseInt(route.get(0).toString())-1;
	            	int yIndex = Integer.parseInt(route.get(1).toString())-1;
	            	int routeNumber = Integer.parseInt(route.get(2).toString());
	            	routesList[xIndex][yIndex] = routeNumber;
	            	routesList[yIndex][xIndex] = routeNumber;
	            }
	            
//
//	            String name = (String) jsonObject.get("name");
//	            System.out.println(name);
//
//	            String city = (String) jsonObject.get("city");
//	            System.out.println(city);
//
//	            String job = (String) jsonObject.get("job");
//	            System.out.println(job);
//
//	            // loop array
//	            JSONArray cars = (JSONArray) jsonObject.get("cars");
//	            Iterator<String> iterator = cars.iterator();
//	            while (iterator.hasNext()) {
//	             System.out.println(iterator.next());
//	            }
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
	}
	

	private static Point[] eliminateDataByFrequency(int frequency, Point[] points){
		Point[] eliminatedPoints = new Point[points.length];
		long timeSpan = 0;
		long lastTimeStamp = -1;
		for(int i = 0; i<points.length; i++){
			Point point = points[i];
			if(lastTimeStamp != -1){
				timeSpan = point.timestamp - lastTimeStamp;
//				System.out.println("timeSpan"+timeSpan);
				if(timeSpan >= frequency*1000){
					eliminatedPoints[i] = point;
					lastTimeStamp = point.timestamp;
				}
			}
			else{
				eliminatedPoints[i] = point;
				lastTimeStamp = point.timestamp;
			}
			
		}
		
		return eliminatedPoints;
	}
	
	private static void presentData(ArrayList<Point> points){
		
	}
	
	private static class Point {
		double lat;
		double lon;
		int index;
		long timestamp;
	}

}
