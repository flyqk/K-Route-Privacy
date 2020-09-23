package privacy599;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConvertPoints {

	public static void main(String[] args) {
		convertPoints("data\\route5\\usc_route_5.gpx", "data\\routes.json");
	}
	
	public static void convertPoints(String inputFile, String outputFile){
		StringBuilder xmlStringBuilder = new StringBuilder();
		DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new File(inputFile));
					Element root = doc.getDocumentElement();
					NodeList trkseg = root.getElementsByTagName("trkseg");
					int trkLength = trkseg.getLength();
					int index = 0;
					StringBuilder routesOutput = new StringBuilder();
					System.out.println("{");
					System.out.println("points:{");
					routesOutput.append("{");
					routesOutput.append("\"points\":{");
					long timeStamp = System.currentTimeMillis();
					for(int i = 0; i < trkLength; i++){
						Node node = trkseg.item(i);
						if(node.getNodeType() == Node.ELEMENT_NODE){
							NodeList nodes = node.getChildNodes();
							int trkptLength = nodes.getLength();
							for(int l = 0; l < trkptLength; l++){
								Node trkptNode = nodes.item(l);
								if(trkptNode.getNodeType() == Node.ELEMENT_NODE){
									index++;
									Element trkptElement = (Element)trkptNode;
//									System.out.println(trkptElement.getAttribute("lat"));
//									System.out.println(trkptElement.getAttribute("lon"));
									System.out.print(index+":"+"{lat:"+trkptElement.getAttribute("lat")+", "+"lon:"+trkptElement.getAttribute("lon")+"}");
									Random rand = new Random();
									long timeSpan = (rand.nextInt(2) + 1)*1000;
									timeStamp += timeSpan;
									routesOutput.append("\""+index+"\":"+"{\"lat\":"+trkptElement.getAttribute("lat")+", "+"\"lon\":"+trkptElement.getAttribute("lon")+",\"timeStamp\":"+timeStamp+"}");
									if(l < trkptLength -1){
										System.out.print(",");
										routesOutput.append(",");
									}
									System.out.println("");
									routesOutput.append("");
								}
								
							}
						}
					}
					System.out.println("}");
					System.out.println("routes:[");
					routesOutput.append("},");
					routesOutput.append("\"routes\":[");
					for(int m=1; m < index+1; m++){
						for(int n=m; n < index+1; n++){
							int routes = 1;
							if(Math.abs(m-n) > 100){
								Random rand = new Random();
								routes = rand.nextInt(5) + 1;
							}
							System.out.println("["+m+","+n+","+routes+"]");
//							routesOutput.append("[\""+m+"\",\""+n+"\",\""+routes+"\"]");
							routesOutput.append("["+m+","+n+","+routes+"]");
							if(m == index && n == index){
							
							}
							else {
								System.out.println(",");
								routesOutput.append(",");
							}
						}
					}
					System.out.println("]");
					routesOutput.append("]");
					System.out.println("}");
					routesOutput.append("}");
					
					PrintWriter out = new PrintWriter(outputFile);
					out.println(routesOutput.toString());
					out.close();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

}
