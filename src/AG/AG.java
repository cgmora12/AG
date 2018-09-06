package AG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import io.swagger.codegen.SwaggerCodegen;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.m2m.atl.emftvm.standalone.ATLRunner;
import cs.ualberta.launcher.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class AG {

	// Variables
	public static String fileName = "data";
	public static String newfileName = "data";
	public static String fileType = "csv";
	public static String alternativeFileType = "";
	public static String modelFileName = "table.xmi";
	public static String host = "server.com";
	public static String basePath = "/resource";
	public static String swaggerFileName = "swagger.json";
	public static String openAPIFileName = "openapi.json";
	public static String openAPIXMIFileName = "openapi.xmi";
	public static String swaggerCodegeneFileName = "swagger-codegen-cli-2.2.1.jar";
	public static String apiCodeFolderName = "apiCode";
	public static String serverCodeFileName = "servercode.js";
	public static String resFolderName = "/AG/";

	public static boolean m2mTransformation = false;
	public static boolean openapi2api = false;
	public static boolean xmi2json = false;
	public static boolean xmi2api = false;
	public static boolean csv2api = false;
	
	public static void main(String[] args) {
		// TODO: Swagger 2.0 to OpenAPI
		
		//args: ficheroDatos
		if(args.length == 1) {
			fileName = args[0];
		}
		//args: openapi.json openapi2api
		else if(args.length == 2 && args[1].contentEquals("openapi2api")) {
			openapi2api = true;
			swaggerFileName = args[0];
		}
		//args: ficheroDatos xml
		else if(args.length == 2) {
			fileName = args[0];
			alternativeFileType = args[1];
		}
		//args: openapi.xmi openapi.json xmi2api
		else if(args.length == 3 && args[2].contentEquals("xmi2api")) {
			xmi2api = true;
			openAPIXMIFileName = args[0];
			openAPIFileName = args[1];
		}
		//args: openapi.xmi openapi.json xmi2json
		else if(args.length == 3 && args[2].contentEquals("xmi2json")) {
			xmi2json = true;
			openAPIXMIFileName = args[0];
			openAPIFileName = args[1];
		}
		//args: ficheroDatos metro.com /madrid
		else if(args.length == 3) {
			fileName = args[0];
			host = args[1];
			basePath = args[2];
		}
		//args: data table.xmi openapi.xmi csv2api
		else if(args.length == 4 && args[3].contentEquals("csv2api")) {
			csv2api = true;
			fileName = args[0];
			modelFileName = args[1];
			openAPIXMIFileName = args[2];
		}
		//args: ficheroDatos xml metro.com /madrid
		else if(args.length == 4) {
			fileName = args[0];
			alternativeFileType = args[1];
			host = args[2];
			basePath = args[3];
		}
		//args: ficheroDatos table.xmi metro.com /madrid m2m
		else if(args.length == 5) {
			fileName = args[0];
			modelFileName = args[1];
			host = args[2];
			basePath = args[3];
			if(args[4].contentEquals("m2m")) {
				m2mTransformation = true;
			}
		}
		
		//Automatic API Generation process
		if(m2mTransformation) {
	        System.out.println("convertCSVIntoXMI");
			convertCSVIntoXMI();
		} 
		else if(openapi2api) {
		    System.out.println("convertOpenapiToAPI");
		    generateServer();
		} 
		else if(xmi2json) {
		    System.out.println("convertXMIintoJSON");
		    convertXMIintoJSON();
		}  
		else if(xmi2api) {
		    System.out.println("convertXMIintoAPI");
		    convertXMIintoJSON();
		    generateServer();
	        addServerDependencies();
	        generateApiCode();
	        System.out.println("Automatic API Generation finished!");
		}   
		else if(csv2api) {
		    System.out.println("convertCSVintoAPI");
			convertCSVIntoXMI();
			model2modelTransformation();
		    convertXMIintoJSON();
		    generateServer();
	        addServerDependencies();
	        generateApiCode();
	        System.out.println("Automatic API Generation finished!");
		} 
		else {
			if(!alternativeFileType.isEmpty() && alternativeFileType != fileType) { 
			    convertDataFileIntoCSV();
			}
			String file = fileName + "." + fileType;
			File f = new File(file);
			if(f.exists() && !f.isDirectory()) { 
				generateApiDefinition();
		        generateServer();
		        addServerDependencies();
		        generateApiCode();
		        System.out.println("Automatic API Generation finished!");
			} else {
				System.out.println("The file " + fileName + "." + fileType + " does NOT exist...");
			}
			
		}
                
	}

	@Override
	public String toString() {
		return "AG [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	private static void convertDataFileIntoCSV() {
		
		String xmlFile = fileName + "." + alternativeFileType;
		
		File f = new File(xmlFile);
		
		if(f.exists() && !f.isDirectory()) { 
						
	        //File stylesheet = new File("style.xsl");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = null;
		    Document document = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				document = builder.parse(xmlFile);
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			List<XmlObject> xmlObjectList = new ArrayList<XmlObject>();
			List<Line> lineList = new ArrayList<Line>();
			List<SetOfLines> setOfLinesList = new ArrayList<SetOfLines>();
	
		    for(int i = 0; i < document.getElementsByTagName("rdf:Description").getLength(); i++) {
		    	Node n = document.getElementsByTagName("rdf:Description").item(i);
		    	Element e = (Element)n;
		    	
		    	if(n.getAttributes().getNamedItem("rdf:about") != null) {
			    	if(e.getElementsByTagName("mao:ofLine").getLength() > 0) {
				    	XmlObject xmlObject = new XmlObject();
				    	xmlObject.stationName = e.getElementsByTagName("foaf:name").item(0).getFirstChild().getNodeValue();
				    	xmlObject.about = n.getAttributes().getNamedItem("rdf:about").getNodeValue();
				    	xmlObject.hasEscalator = (e.getElementsByTagName("mao:hasEscalator").getLength() > 0 
			    				&& e.getElementsByTagName("mao:hasEscalator").item(0).hasChildNodes() 
			    				? e.getElementsByTagName("mao:hasEscalator").item(0).getFirstChild().getNodeValue() 
			    				: "FALSE");
				    	xmlObject.hasLift = (e.getElementsByTagName("mao:hasLift").getLength() > 0 
			    				&& e.getElementsByTagName("mao:hasLift").item(0).hasChildNodes() 
			    				? e.getElementsByTagName("mao:hasLift").item(0).getFirstChild().getNodeValue() 
			    				: "FALSE");
				    	xmlObject.hasTravelator = (e.getElementsByTagName("mao:hasTravelator").getLength() > 0 
			    				&& e.getElementsByTagName("mao:hasTravelator").item(0).hasChildNodes() 
			    				? e.getElementsByTagName("mao:hasTravelator").item(0).getFirstChild().getNodeValue() 
			    				: "FALSE");
				    	xmlObject.transfer = (e.getElementsByTagName("mao:Transfer").getLength() > 0 
			    				&& e.getElementsByTagName("mao:Transfer").item(0).hasChildNodes() 
			    				? e.getElementsByTagName("mao:Transfer").item(0).getFirstChild().getNodeValue() 
			    				: "");
				    	/*xmlObject.type = (e.getElementsByTagName("rdf:type").getLength() > 0
				    			? e.getElementsByTagName("rdf:type").item(0).getAttributes().getNamedItem("rdf:resource").getNodeValue()
				    			: "");*/
	
				    	if(e.getElementsByTagName("mao:ofLine").item(0).getAttributes().getNamedItem("rdf:resource") != null) {
				    		xmlObject.lineAbout = e.getElementsByTagName("mao:ofLine").item(0).getAttributes().getNamedItem("rdf:resource").getNodeValue();
				    	} else if(e.getElementsByTagName("mao:ofLine").item(0).getAttributes().getNamedItem("rdf:nodeID") != null) {
				    		xmlObject.lineListID = e.getElementsByTagName("mao:ofLine").item(0).getAttributes().getNamedItem("rdf:nodeID").getNodeValue();
				    	}
				    	
				    	xmlObjectList.add(xmlObject);
			    	} else {
				    	Line line = new Line();
				    	line.lineName = e.getElementsByTagName("foaf:name").item(0).getFirstChild().getNodeValue();
				    	line.routeService = (e.getElementsByTagName("mao:routeService").getLength() > 0 
				    			? e.getElementsByTagName("mao:routeService").item(0).getAttributes().getNamedItem("rdf:resource").getNodeValue()
				    			: " ");
				    	line.lineAbout = (n.getAttributes().getNamedItem("rdf:about") != null
				    			? n.getAttributes().getNamedItem("rdf:about").getNodeValue()
				    			: " ");
				    	line.lineID = (e.getElementsByTagName("mao:Setof").getLength() > 0 
				    			? e.getElementsByTagName("mao:Setof").item(0).getAttributes().getNamedItem("rdf:nodeID").getNodeValue()
				    			: " ");
				    	lineList.add(line);
			    	}
			    	
		    	} else {
		    		SetOfLines setOfLines = new SetOfLines();
		    		setOfLines.linesID = n.getAttributes().getNamedItem("rdf:nodeID").getNodeValue();
		    		List<String> abouts = new ArrayList<String>();
		    		for(int j = 0; j < e.getChildNodes().getLength(); j++) {
		    			if(e.getChildNodes().item(j).getAttributes() != null && e.getChildNodes().item(j).getAttributes().getLength() > 0) {
		    				if(e.getChildNodes().item(j).getNodeName() != "rdf:type") {
		    					abouts.add(e.getChildNodes().item(j).getAttributes().getNamedItem("rdf:resource").getNodeValue());
		    				}
		    			}
		    		}
		    		setOfLines.abouts = abouts;
		    		setOfLinesList.add(setOfLines);
		    	}		    	
		    	
		    }
		    
		    for(int i = 0; i < xmlObjectList.size(); i++) {
		    	XmlObject xmlObject = xmlObjectList.get(i);
		    	if(!xmlObject.lineListID.replaceAll(" ", "").isEmpty() 
		    			&& xmlObject.lineAbout.replaceAll(" ", "").isEmpty()) {
		    		for(SetOfLines setOfLines : setOfLinesList) {
		    			if(xmlObject.lineListID.equals(setOfLines.linesID)){
	    					boolean initial = true;
		    				for(String about : setOfLines.abouts) {
		    					if(initial) {
			    					initial = false;
			    					xmlObject.lineAbout = about;
			    					xmlObject.lineListID = " ";
			    					XmlObject xmlObjectAux = new XmlObject(xmlObject);
			    					xmlObjectList.set(i, xmlObjectAux);
			    				}else{
			    					XmlObject xmlObjectAux = new XmlObject(xmlObject);
			    					xmlObjectAux.lineAbout = about;
			    					xmlObjectAux.lineListID = " ";
			    					xmlObjectList.add(i+1, xmlObjectAux);
			    				}
		    				}
		    			}
		    		}
		    	}
		    	if(!xmlObject.lineAbout.replaceAll(" ", "").isEmpty()) {
		    		for(Line line: lineList) {
		    			if(xmlObject.lineAbout.equals(line.lineAbout)) {
		    				xmlObject.lineName = line.lineName;
		    				xmlObject.lineID = line.lineID;
		    				xmlObject.routeService = line.routeService;
		    				xmlObjectList.set(i, xmlObject);
		    			}
		    		}
		    	}
		    }
		    
		    Writer writer = null;
		    try {
		    	File file = new File(fileName + "." + fileType);
		    	file.delete();
		        writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(fileName + "." + fileType), "utf-8"));
		        writer.write("stationName,about,hasEscalator,hasLift,hasTravelator,transfer,"+/*type,*/"lineAbout,lineID,lineName,routeService");
		        for(XmlObject xmlObject : xmlObjectList) {
		        	writer.write("\n" + xmlObject.toString());
		        }
		    } catch (IOException ex) {
		        // Report
		    } finally {
		       try {writer.close();} catch (Exception ex) {/*ignore*/}
		    }
		    
	        /*StreamSource stylesource = new StreamSource(stylesheet);
	        Transformer transformer = null;
			try {
				transformer = TransformerFactory.newInstance()
				        .newTransformer(stylesource);
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Source source = new DOMSource(document);
	        Result outputTarget = new StreamResult(new File("x.csv"));
	        try {
				transformer.transform(source, outputTarget);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	        
			System.out.println("Parsed XML into CSV!");
		}
		else {
	        System.out.println("Error: data file not found or type not supported!");
		}
		
	}

	private static void convertCSVIntoXMI() {
	
		String csvFile = fileName + "." + fileType;
		
		BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        ArrayList<String[]> rows = new ArrayList<String[]>();

        try {
        	// Read first rows of data file
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	//TODO: replace all rare characters
                rows.add(line.replaceAll(" ", "").replaceAll("\"", "").replaceAll("\'", "").split(csvSplitBy));

            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // Rewrite CSV without rare characters
	    Writer writer = null;
	    try {
			new File(apiCodeFolderName).mkdirs();
	    	File file = new File(apiCodeFolderName + File.separator + newfileName + "." + fileType);
	    	file.delete();
	        writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(apiCodeFolderName + File.separator + newfileName + "." + fileType), "utf-8"));
	        for(int i = 0; i < rows.size(); i++) {
	        	if(i > 0) {
		        	writer.write("\n");
	        	}
	        	for (int j = 0; j < rows.get(i).length; j++) {
	        		if(j == 0) {
			        	writer.write(rows.get(i)[j]);
	        		} else {
			        	writer.write(csvSplitBy + rows.get(i)[j]);
	        		}
	        	}
	        }
	    } catch (IOException ex) {
	        // Report
	    } finally {
	       try {writer.close();} catch (Exception ex) {/*ignore*/}
	    }
        
        try {
	        System.out.println("create xmi");

        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    		// root elements
    		Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("table:Table");

    		// set attributes to root element
    		Attr attrsXmi = doc.createAttribute("xmi:version");
    		attrsXmi.setValue("2.0");
    		rootElement.setAttributeNode(attrsXmi);
    		Attr attrsXmi2 = doc.createAttribute("xmlns:xmi");
    		attrsXmi2.setValue("http://www.omg.org/XMI");
    		rootElement.setAttributeNode(attrsXmi2);
    		Attr attrsXmi3 = doc.createAttribute("xmlns:xsi");
    		attrsXmi3.setValue("http://www.w3.org/2001/XMLSchema-instance");
    		rootElement.setAttributeNode(attrsXmi3);
    		Attr attrsXmi4 = doc.createAttribute("xmlns:table");
    		attrsXmi4.setValue("platform:/resource/TransformationRules/Table.ecore");
    		rootElement.setAttributeNode(attrsXmi4);

    		Attr attr = doc.createAttribute("filename");
    		attr.setValue(fileName);
    		rootElement.setAttributeNode(attr);
    		
    		doc.appendChild(rootElement);

    		for(int i = 0; i < rows.size(); i++) {
        		// rows elements
        		Element rowsElement = doc.createElement("rows");
        		Attr attrRow = doc.createAttribute("position");
        		attrRow.setValue(i + "");
        		rowsElement.setAttributeNode(attrRow);
        		rootElement.appendChild(rowsElement);
    			for (int j = 0; j < rows.get(i).length; j++) {
    	    		// cells elements
            		Element cellsElement = doc.createElement("cells");
            		Attr attrCells = doc.createAttribute("value");
            		attrCells.setValue(rows.get(i)[j]);
            		cellsElement.setAttributeNode(attrCells);
            		String cellType = "";
            		try {
                		if(rows.get(i)[j].equals("" + Integer.parseInt(rows.get(i)[j]))) {
                			cellType = "integer";
                		} else if(rows.get(i)[j].equals("" + Float.parseFloat(rows.get(i)[j]))){
                			cellType = "number";
                		} else {
                			cellType = "string";
                		}
                	} catch (NumberFormatException e) {
                		cellType = "string";
                	}

            		Attr attrCells2 = doc.createAttribute("type");
            		attrCells2.setValue(cellType);
            		cellsElement.setAttributeNode(attrCells2);
            		rowsElement.appendChild(cellsElement);
    				
    			}
    		}

    		// write the content into xml file
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		Transformer transformer = transformerFactory.newTransformer();
    		DOMSource source = new DOMSource(doc);
    		StreamResult result = new StreamResult(new File("." + File.separator + modelFileName));

    		// Output to console for testing
    		// StreamResult result = new StreamResult(System.out);

    		transformer.transform(source, result);

    		System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
    		tfe.printStackTrace();
    	}
    	
		
	}
	
	private static void model2modelTransformation() {
		
		/*String[] args = new String [10];
		args[0] = "-f";
		args[1] = "Table2Openapi.atl";
		args[2] = "-s";
		args[3] = "Table.ecore";
		args[4] = "-t";
		args[5] = "Openapi.ecore";
		args[6] = "-i";
		args[7] = modelFileName;
		args[8] = "-o";
		args[9] = openAPIXMIFileName;
		try {
			ATLRunner.main(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		try {
            Files.copy(AG.class.getResourceAsStream(resFolderName + "metamodels" + File.separator + "Table.ecore"), 
            		new File("Table.ecore").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(resFolderName + "metamodels" + File.separator + "Openapi.ecore"), 
            		new File("Openapi.ecore").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(resFolderName + "transformator" + File.separator + "Table2Openapi.atl"), 
            		new File("Table2Openapi.atl").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(resFolderName + "transformator" + File.separator + "Table2Openapi.emftvm"), 
            		new File("Table2Openapi.emftvm").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }
		
		Launcher launcher = new Launcher();
		launcher.runATL("Table.ecore", "Table", modelFileName, "Openapi.ecore", "Openapi", openAPIXMIFileName, "Table2Openapi", "");
		
	}

	private static void convertXMIintoJSON() {
		String jsonString = "", jsonStringFormatted = "", swaggerString = "", swaggerStringFormatted = "";
		
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(FileUtils.readFileToString(new File(openAPIXMIFileName)));
            
            xmlJSONObj = xmlJSONObj.getJSONObject("openapi:API");
            xmlJSONObj.remove("openapi:API");
            xmlJSONObj.remove("xmi:version");
            xmlJSONObj.remove("xmlns:xmi");
            xmlJSONObj.remove("xmlns:openapi");
            
            JSONArray pathsArray = xmlJSONObj.getJSONArray("paths");
            xmlJSONObj.remove("paths");
            
            try {
            	JSONArray servers = xmlJSONObj.getJSONArray("servers");
            } catch (JSONException e) {
            	JSONObject servers = xmlJSONObj.getJSONObject("servers");
                xmlJSONObj.remove("servers");
                JSONArray serversArray = new JSONArray();
                serversArray.put(servers);
                xmlJSONObj.put("servers", serversArray);
            }
            
            for(int i = 0; i < pathsArray.length(); i++) {
            	JSONObject jsonobj = pathsArray.getJSONObject(i);
            	String path = jsonobj.getString("pattern");
            	jsonobj.remove("pattern");
            	if(i == 0) {
                	xmlJSONObj.put("paths", new JSONObject());
            	}
            	JSONObject jsonobj2 = pathsArray.getJSONObject(i).getJSONObject("get");
            	xmlJSONObj.getJSONObject("paths").put(path, new JSONObject());
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).put("get", jsonobj2);
            	
            	try {
	            	JSONObject parameters = xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("parameters");
	            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").put("parameters", new JSONArray());
	            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONArray("parameters").put(parameters);
            	} catch (JSONException e) {
        			System.out.println(e.getMessage());
            	}
            	
            	JSONObject jsonobjAux = pathsArray.getJSONObject(i).getJSONObject("get").getJSONObject("responses").getJSONObject("responseCode");
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").remove("responses");
                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").put("responses", new JSONObject());
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").put("200", jsonobjAux);
            	
            	JSONObject jsonobjAuxContent = jsonobjAux.getJSONObject("content").getJSONObject("contentType");
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").remove("content");
                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").put("content", new JSONObject());
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content").put("application/json", jsonobjAuxContent);
            	
            	String ref = jsonobjAuxContent.getJSONObject("schema").getJSONObject("items").getString("ref");
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("application/json").getJSONObject("schema").remove("items");
                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("application/json").getJSONObject("schema").put("items", new JSONObject());
            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("application/json").getJSONObject("schema").getJSONObject("items").put("$ref", ref);
            	
            }
            
            JSONArray propertiesArray = xmlJSONObj.getJSONObject("components").getJSONObject("schemas").getJSONObject("mainComponent").getJSONArray("properties");
            xmlJSONObj.getJSONObject("components").getJSONObject("schemas").getJSONObject("mainComponent").remove("properties");
            
            for(int i = 0; i < propertiesArray.length(); i++) {
            	JSONObject jsonobj = propertiesArray.getJSONObject(i);
            	String path = jsonobj.getString("name");
            	jsonobj.remove("name");
            	if(i == 0) {
                	xmlJSONObj.getJSONObject("components").getJSONObject("schemas").getJSONObject("mainComponent").put("properties", new JSONObject());
            	} 
            	JSONObject jsonobj2 = propertiesArray.getJSONObject(i).getJSONObject("content");
            	xmlJSONObj.getJSONObject("components").getJSONObject("schemas").getJSONObject("mainComponent").getJSONObject("properties").put(path, jsonobj2);
            	
            }
            
            jsonString = xmlJSONObj.toString();
            ObjectMapper jsonFormatter = new ObjectMapper();
            Object json = jsonFormatter.readValue(jsonString, Object.class);
            jsonStringFormatted = jsonFormatter.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            

            JSONObject xmlSwaggerObj = xmlJSONObj;
            xmlSwaggerObj.remove("openapi");
            xmlSwaggerObj.put("swagger", "2.0");
            String urlServer = xmlSwaggerObj.getJSONArray("servers").getJSONObject(0).getString("url");
            urlServer = urlServer.replaceAll("http://", "");
            urlServer = urlServer.replaceAll("https://", "");
            if(StringUtils.countMatches(urlServer,"/") > 0) {
                xmlSwaggerObj.put("host", urlServer.substring(0, urlServer.indexOf("/")));
                xmlSwaggerObj.put("basePath", urlServer.substring(urlServer.indexOf("/")));
            } else {
                xmlSwaggerObj.put("host", urlServer);
                xmlSwaggerObj.put("basePath", "/");
            }
            xmlSwaggerObj.remove("servers");

            
            for(int i = 0; i < xmlSwaggerObj.getJSONObject("paths").length(); i++) {
            	String pathSwagger = xmlSwaggerObj.getJSONObject("paths").names().getString(i);
            	JSONObject jsonobj = xmlSwaggerObj.getJSONObject("paths").getJSONObject(pathSwagger);
            	JSONArray produces = new JSONArray();
            	produces.put("application/json");
            	jsonobj.getJSONObject("get").put("produces", produces);
            	
            	
            	try {
            		JSONArray parameters = jsonobj.getJSONObject("get").getJSONArray("parameters");
            		for(int j = 0; j < parameters.length(); j++) {
            			parameters.getJSONObject(j).put("type", parameters.getJSONObject(j).getJSONObject("schema").getString("type"));
    	            	parameters.getJSONObject(j).remove("schema");
            		}
	            	
            	} catch (JSONException e) {
        			System.out.println(e.getMessage());
            	}
            	
            	JSONObject jsonobjAuxSwagger = jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content").getJSONObject("application/json").getJSONObject("schema");
            	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").remove("content");
            	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").put("schema", jsonobjAuxSwagger);
            	
            	String ref = jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").getString("$ref");
            	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").remove("$ref");
            	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").put("$ref", "#/definitions" + ref.substring(ref.lastIndexOf("/")));
            	
            }
            
            JSONObject componentsSchemas = xmlSwaggerObj.getJSONObject("components").getJSONObject("schemas");
            xmlSwaggerObj.remove("components");
            xmlSwaggerObj.put("definitions", componentsSchemas);
            JSONArray definitionsSchemes = new JSONArray();
            definitionsSchemes.put("https");
            xmlSwaggerObj.put("schemes", definitionsSchemes);
            
            swaggerString = xmlSwaggerObj.toString();
            ObjectMapper swaggerFormatter = new ObjectMapper();
            Object jsonSwagger = swaggerFormatter.readValue(swaggerString, Object.class);
            swaggerStringFormatted = swaggerFormatter.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSwagger);
            
        } catch (JSONException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
        } catch (JsonParseException e) {
			System.out.println(e.getMessage());
		} catch (JsonMappingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		try (PrintWriter out = new PrintWriter(openAPIFileName)) {
		    out.println(jsonStringFormatted);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try (PrintWriter out = new PrintWriter(swaggerFileName)) {
		    out.println(swaggerStringFormatted);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void generateApiDefinition() {
		
		String csvFile = fileName + "." + fileType;

		String apiDefinition = "";
		String fileNameNoWhiteSpaces = fileName.replaceAll(" ", "").replaceAll("-", "");
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] columns = null;
        String[] columnsDescriptions = null;
        String[] exampleData = null;
        String[] dataTypes = null;

        try {
        	// Read first rows of data file
            br = new BufferedReader(new FileReader(csvFile));
            if ((line = br.readLine()) != null) {
                // use comma as separator
            	//TODO: replace all rare characters
                columnsDescriptions = line.replaceAll("\"", "").replaceAll("\'", "").split(cvsSplitBy);
                columns = line.replaceAll(" ", "_").replaceAll("\"", "").replaceAll("\'", "").split(cvsSplitBy);

            }
            if ((line = br.readLine()) != null) {
            	//TODO: replace all rare characters
                exampleData = line.replaceAll("\"", "").replaceAll("\'", "").split(cvsSplitBy);
                dataTypes = new String[exampleData.length];
                for(int i = 0; i < exampleData.length; i++) {
                	try {
                		if(exampleData[i].equals("" + Integer.parseInt(exampleData[i]))) {
                    		dataTypes[i] = "integer";
                		} else if(exampleData[i].equals("" + Float.parseFloat(exampleData[i]))){
                    		dataTypes[i] = "number";
                		} else {
                    		dataTypes[i] = "string";
                		}
                	} catch (NumberFormatException e) {
                		dataTypes[i] = "string";
                	}
                }

            }
            br.close();

    		// TODO: Swagger 2.0 to OpenAPI
            
            // Generate OpenAPI definition and doc
            apiDefinition = "";
            apiDefinition += "{ \"openapi\" : \"3.0.0\", \"info\" : { \"version\" : \"1.0.0\", \"title\" : \"" + fileName + "\", \"description\" : \"Obtaining the " + fileNameNoWhiteSpaces  + "\" }, \"servers\" : [ { \"url\" : \"" + host+basePath + "\" } ] ,";
            apiDefinition += "\"paths\" : { \"/\" : { \"get\" : { \"summary\" : \"GET " + fileName + "\", \"operationId\" : \"get" + fileNameNoWhiteSpaces + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\",";
            apiDefinition += "\"parameters\" : [ ";
            		
            // Query parameters
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
            	}
            	apiDefinition += "{ \"in\" : \"query\", \"name\" : \"" + columns[i] + "\", \"schema\" : { \"type\" : \"" + dataTypes[i] + "\" }, \"description\" : \"" + columnsDescriptions[i] + "\" }";
                if(i == columns.length-1) {
                	apiDefinition += " ],";
                }
            }
            
            apiDefinition += "\"responses\" : { \"200\" : { \"description\" : \"successful operation\", \"content\" : { \"application/json\" : { \"schema\" : { \"type\" : \"array\", \"items\" : { \"$ref\" : \"#/components/schemas/COLUMNS\" } } } } } } } },";
            
            // Path parameters
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
            	}
            	apiDefinition += "\"/" + columns[i] + "/{" + columns [i] + "}\" : { \"get\" : { \"summary\" : \"" + columnsDescriptions[i] + "\", \"operationId\" : \"" + columnsDescriptions[i].replaceAll(" ", "").replaceAll("-", "") + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\",";
            	apiDefinition += "\"parameters\" : [ { \"name\" : \"" + columns[i] + "\", \"in\" : \"path\", \"description\" : \"" + columnsDescriptions[i] + "\", \"required\" : true, \"schema\" : { \"type\" : \"" + dataTypes[i] + "\" } } ],";
            	apiDefinition += "\"responses\" : { \"200\" : { \"description\" : \"successful operation\", \"content\" : { \"application/json\" : { \"schema\" : { \"type\" : \"array\", \"items\" : { \"$ref\" : \"#/components/schemas/COLUMNS\" } } } } } } } } ";
            }
            apiDefinition += "} ,";

            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
        		} else {
        			apiDefinition += "\"components\" : { \"schemas\" : { \"COLUMNS\" : { \"type\" : \"object\", \"required\" : [ \"" + columns[i] + "\" ], \"properties\" : {";
               	}
            	apiDefinition += "\"" + columns[i] + "\" : { \"type\" : \"" + dataTypes[i] + "\", \"example\" : \"" + exampleData[i] + "\" }";
                if(i == columns.length-1) {
                	apiDefinition += "}, \"xml\" : { \"name\" : \"COLUMNS\" } } } } }";
                }
            }
            
			// Create file
            ObjectMapper jsonFormatter = new ObjectMapper();
            Object json = jsonFormatter.readValue(apiDefinition, Object.class);
            String apiDefinitionFormatted = jsonFormatter.writerWithDefaultPrettyPrinter()
                                           .writeValueAsString(json);
            new File(apiCodeFolderName).mkdirs();
            PrintWriter writer2 = new PrintWriter(apiCodeFolderName + "/" + openAPIFileName, "UTF-8");
            writer2.println(apiDefinitionFormatted);
            writer2.close();
            
            // Generate Swagger 2.0 API definition and doc
            apiDefinition = "";
            apiDefinition += "{ \"swagger\" : \"2.0\", \"info\" : { \"version\" : \"1.0.0\", \"title\" : \"" + fileName + "\", \"description\" : \"Obtaining the " + fileNameNoWhiteSpaces  + "\" }, \"host\" : \"" + host + "\", \"basePath\" : \"" + basePath + "\",";
            apiDefinition += "\"paths\" : { \"/\" : { \"get\" : { \"summary\" : \"GET " + fileName + "\", \"operationId\" : \"get" + fileNameNoWhiteSpaces + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\", \"produces\" : [ \"application/json\" ],";
            apiDefinition += "\"parameters\" : [ ";
            		
            // Query parameters
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
            	}
            	apiDefinition += "{ \"in\" : \"query\", \"name\" : \"" + columns[i] + "\", \"type\" : \"" + dataTypes[i] + "\", \"description\" : \"" + columnsDescriptions[i] + "\" }";
                if(i == columns.length-1) {
                	apiDefinition += " ],";
                }
            }
            
            apiDefinition += "\"responses\" : { \"200\" : { \"description\" : \"successful operation\", \"schema\" : { \"type\" : \"array\", \"items\" : { \"$ref\" : \"#/definitions/COLUMNS\" } } } } } } ,";
        	
            // Path parameters
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
            	}
            	apiDefinition += "\"/" + columns[i] + "/{" + columns [i] + "}\" : { \"get\" : { \"summary\" : \"" + columnsDescriptions[i] + "\", \"operationId\" : \"" + columnsDescriptions[i].replaceAll(" ", "").replaceAll("-", "") + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\", \"produces\" : [ \"application/json\" ],";
            	apiDefinition += "\"parameters\" : [ { \"name\" : \"" + columns[i] + "\", \"in\" : \"path\", \"description\" : \"" + columnsDescriptions[i] + "\", \"required\" : true, \"type\" : \"" + dataTypes[i] + "\" } ],";
            	apiDefinition += "\"responses\" : { \"200\" : { \"description\" : \"successful operation\", \"schema\" : { \"type\" : \"array\", \"items\" : { \"$ref\" : \"#/definitions/COLUMNS\" } } } } } }";
            }
            apiDefinition += "} ,";
            
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
        		} else {
        			apiDefinition += "\"definitions\" : { \"COLUMNS\" : { \"type\" : \"object\", \"required\" : [ \"" + columns[i] + "\" ], \"properties\" : {";
               	}
            	apiDefinition += "\"" + columns[i] + "\" : { \"type\" : \"" + dataTypes[i] + "\", \"example\" : \"" + exampleData[i] + "\" }";
                if(i == columns.length-1) {
                	apiDefinition += "}, \"xml\" : { \"name\" : \"COLUMNS\" } } }, \"schemes\" : [ \"https\" ] }";
                }
            }
            
			// Create file
            ObjectMapper jsonFormatter2 = new ObjectMapper();
            Object json2 = jsonFormatter2.readValue(apiDefinition, Object.class);
            String apiDefinitionFormatted2 = jsonFormatter2.writerWithDefaultPrettyPrinter()
                                           .writeValueAsString(json2);
            new File(apiCodeFolderName).mkdirs();
            PrintWriter writer = new PrintWriter(apiCodeFolderName + "/" + swaggerFileName, "UTF-8");
            writer.println(apiDefinitionFormatted2);
            writer.close();
               
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
	}

	private static void generateServer() {

		if(openapi2api || xmi2api || csv2api) {
			new File(apiCodeFolderName).mkdirs();
			File source = new File(swaggerFileName);
			File dest = new File(apiCodeFolderName + File.separator + swaggerFileName);
			try {
			    FileUtils.copyFile(source, dest);
			} catch (IOException e) {
			    e.printStackTrace();
			}
			/*File sourceData = new File(fileName + "." + fileType);
			File destData = new File(apiCodeFolderName + File.separator + fileName + "." + fileType);
			try {
			    FileUtils.copyFile(sourceData, destData);
			} catch (IOException e) {
			    e.printStackTrace();
			}*/
		}
		
		String[] args = new String [7];
		args[0] = "generate";
		args[1] = "--lang";
		args[2] = "nodejs-server";
		args[3] = "--input-spec";
		args[4] = apiCodeFolderName + File.separator + swaggerFileName;
		args[5] = "--output";
		args[6] = apiCodeFolderName;
		SwaggerCodegen.main(args);
		
		/*Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("java -jar " + resFolderName + swaggerCodegeneFileName 
					+ " generate -i " + apiCodeFolderName + "/" + swaggerFileName + " -o " + apiCodeFolderName + " -l nodejs-server");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			proc.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/		
	}

	private static void addServerDependencies() {
		/*String npm = System.getProperty("os.name").toLowerCase().contains("win") ? "npm.cmd" : "npm";
        Process proc2 = new ProcessBuilder("cmd", "npm install allto-json")
                .directory(new File(apiCodeFolderName))
                .start();*/
        String lines = "";
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(apiCodeFolderName + "/package.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String line = "";
        try {
			while ((line = br.readLine()) != null) {
				lines += line + "\n";
				if(line.contains("\"dependencies\": {")) {
					lines += "\t" + "\"papaparse\": \"^4.3.7\"," + "\n";
				} 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
        try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(apiCodeFolderName + "/package.json", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        writer.println(lines);
        writer.close();
		
	}


	private static void generateApiCode() {

		/*String csvFile = fileName + "." + fileType;
		
        File source = new File(csvFile);
        File dest = new File(apiCodeFolderName + "/" + csvFile);
        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

		// Edit DefaultService.js of nodejs server
		String servercode = "";
		
		BufferedReader br = null;
		String line = "";
        ArrayList<String> lineFunctionNames = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(apiCodeFolderName + "/controllers/DefaultService.js"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while ((line = br.readLine()) != null) {
				if(line.contains("function(args, res, next) {")) {
					lineFunctionNames.add(line + "\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
        try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
        
        BufferedReader brServer = null;
        String lineServer = "";
		try {
			brServer = new BufferedReader(new InputStreamReader
					(AG.class.getResourceAsStream(resFolderName + serverCodeFileName)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while ((lineServer = brServer.readLine()) != null) {
				if(lineServer.contains("var fileName =")) {
					//servercode += "\tvar fileName =\"./" + fileName + "." + fileType + "\";" + "\n";
					servercode += lineServer + "\n";
				} 
				else {
					servercode += lineServer + "\n";
				}
			}
			for(String lineFunctionName: lineFunctionNames) {
				servercode += lineFunctionName + "\t" + "exports.getOperation(args, res, next); \n}\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	brServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(apiCodeFolderName + "/controllers/DefaultService.js", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.println(servercode);
			writer.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// Edit Default.js of nodejs server
		/*
		String defaultCode = "";
		String servercodeCall = "(req.swagger.params, req.query, res, next);";
        
        BufferedReader brServer2 = null;
		String lineServer2 = "";
		try {
			brServer2 = new BufferedReader(new FileReader
					(apiCodeFolderName + "/controllers/Default.js"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while ((lineServer2 = brServer2.readLine()) != null) {
				if(lineServer2.contains("Default.getdata")) {
					defaultCode += lineServer2.substring(0, lineServer2.indexOf('(')) + servercodeCall + "\n";
				}
				else {
					defaultCode += lineServer2 + "\n";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	brServer2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PrintWriter writer2 = null;
		try {
			writer2 = new PrintWriter(apiCodeFolderName + "/controllers/Default.js", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer2.println(defaultCode);
			writer2.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		*/
		
	}
	
}

// Auxiliar classes to parse specific xml file
class XmlObject {
	public String stationName = " ";
	public String about = " ";
	public String hasEscalator = " ";
	public String hasLift = " ";
	public String hasTravelator = " ";
	public String transfer = " ";
	//public String type = " ";
	public String lineAbout = " ";
	public String lineID = " ";
	public String lineListID = " ";
	public String lineName = " ";
	public String routeService = " ";

    public XmlObject(XmlObject xmlObject) {
    	this.stationName = xmlObject.stationName;
    	this.about = xmlObject.about;
    	this.hasEscalator = xmlObject.hasEscalator;
    	this.hasLift = xmlObject.hasLift;
    	this.hasTravelator = xmlObject.hasTravelator;
    	this.transfer = xmlObject.transfer;
    	this.lineAbout = xmlObject.lineAbout;
    	this.lineID = xmlObject.lineID;
    	this.lineListID = xmlObject.lineListID;
    	this.lineName = xmlObject.lineName;
    	this.routeService = xmlObject.routeService;
	}

	public XmlObject() {
		// TODO Auto-generated constructor stub
		stationName = "";
		about = "";
		hasEscalator = "";
		hasLift = "";
		hasTravelator = "";
		transfer = "";
		lineAbout = "";
		lineID = "";
		lineName = "";
		routeService = "";
	}

	@Override
    public String toString() {
        return this.stationName + "," + this.about + "," + this.hasEscalator 
        		 + "," + this.hasLift + "," + this.hasTravelator 
        		 + "," + this.transfer 
        		 //+ "," + this.type
        		 + "," + this.lineAbout + "," + this.lineID
        		 + "," + this.lineName + "," + this.routeService;
    }
}

class Line {
	public String lineAbout = " ";
	public String lineID = " ";
	public String lineName = " ";
	public String routeService = " ";
}
class SetOfLines {
	public String linesID = " ";
	public List<String> abouts = new ArrayList<String>();
}
