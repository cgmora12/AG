package AG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
	public static String defaultFileName = "data";
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
	public static String resFolderName = "AG";
	public static String apiFolderName = "api";
	public static String tempFolderName = "generated";
	public static String mainFolderName = "AG_data";
	public static String fileSeparatorForResources = "/";
	public static String visualizationMainFolderName = "Visualizacion";
	public static String visualizationChartJS = "visualization.html";
	public static String visualizationZipFileName = visualizationMainFolderName + ".zip";
	public static String visualizationProjectName = "ChartsDemo-macOS";
	public static String visualizationFolderName = "Demos";
	public static String visualizationSwiftFileName = "BarDemoViewController.swift";
	public static String visualizationSwiftFileName2 = "LineDemoViewController.swift";
	public static String visualizationSwiftFileName3 = "PieDemoViewController.swift";
	public static String visualizationSwiftFileName3Tab = "CustomPieTab.swift";

	public static boolean m2mTransformation = false;
	public static boolean openapi2api = false;
	public static boolean xmi2json = false;
	public static boolean xmi2api = false;
	public static boolean csv2api = false;
	public static boolean csv2openapi = false;
	
	public static void main(String[] args) {
		
		if(args.length > 0) {
			switch (args[0]) {
				case "csv2api" : csv2api(args);
					break;
				/*case "openapi2api" : openapi2api(args);
					break;*/
				case "xmi2api" : xmi2api(args);
					break;
				case "xmi2json" : xmi2json(args);
					break;
				case "xml2api" : xml2api(args);
					break;
				case "m2m" : m2m(args);
					break;
				case "csv2openapi" : csv2openapi(args);
					break;
				default : System.out.println("No operation called");
					break;
			}
		} else {
			//Default operation without parameters
			csv2api(args);
		}
                
	}
	
	private static void csv2api(String[] args) {
		System.out.println("csv2api");

		//args: csv2api
		csv2api = true;
		
		//args: csv2api data
		if(args.length == 2) {
			fileName = args[1];
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
		} 
		//args: csv2api data table.xmi openapi.xmi 
		else if(args.length == 4) {
			fileName = args[1];
			modelFileName = args[2];
			openAPIXMIFileName = args[3];
		}

		mainFolderName = "AG_" + cleanString(fileName);

		convertCSVIntoXMI();
		model2modelTransformation();
	    convertXMIintoJSON();
	    generateServer();
        addServerDependencies();
        generateApiCode();
        runApi();
        generateVisualization();
        System.out.println("Automatic API Generation finished!");
		
	}
	
	/*private static void openapi2api(String[] args) {
		System.out.println("openapi2api");

		//args: openapi2api
		openapi2api = true;
		
		//args: openapi2api data openapi.json 
		if(args.length == 3) {
			fileName = args[1];
			swaggerFileName = args[2];
		}
		
		File source = new File(swaggerFileName);
		File dest = new File(tempFolderName + File.separator + swaggerFileName);
		try {
		    FileUtils.copyFile(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
	    generateServer();
        addServerDependencies();
        generateApiCode();
        runApi();
        generateVisualization();
        System.out.println("Automatic API Generation finished!");
	}*/
	
	private static void xmi2api(String[] args) {
		System.out.println("xmi2api");

		//args: xmi2api
		xmi2api = true;
		
		//args: xmi2api data
		if(args.length == 2) {
			fileName = args[1];
		}
		//args: xmi2api data openapi.xmi
		else if(args.length == 3) {
			fileName = args[1];
			openAPIXMIFileName = args[2];
		}
		//args: xmi2api data openapi.xmi openapi.json 
		else if(args.length == 4) {
			fileName = args[1];
			openAPIXMIFileName = args[2];
			openAPIFileName = args[3];
		}

		//mainFolderName = "AG_" + cleanString(fileName);

		File source = new File(openAPIXMIFileName);
		File dest = new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName);
		try {
		    FileUtils.copyFile(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		cleanCSV();

		// Move source files to temp folder
	    convertXMIintoJSON();
	    generateServer();
        addServerDependencies();
        generateApiCode();
        runApi();
        generateVisualization();
        System.out.println("Automatic API Generation finished!");
	}
	
	private static void xmi2json(String[] args) {
		System.out.println("xmi2json");

		//args: xmi2json
		xmi2json = true;
		
		//args: xmi2json openapi.xmi
		if(args.length == 2) {
			openAPIXMIFileName = args[1];
		}
		//args: xmi2json openapi.xmi openapi.json 
		if(args.length == 3) {
			openAPIXMIFileName = args[1];
			openAPIFileName = args[2];
		}

		//mainFolderName = "AG_" + cleanString(fileName);

		// Move source files to temp folder
		File source = new File(openAPIXMIFileName);
		File dest = new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName);
		try {
		    FileUtils.copyFile(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
				
		convertXMIintoJSON();
        System.out.println("JSON API definition generated!");
	}
	
	private static void xml2api(String[] args) {
		System.out.println("xml2api");
		
		//args: xml2api ficheroDatos xml
		if(args.length == 5) {
			fileName = args[1];
			alternativeFileType = args[2];
		}

		mainFolderName = "AG_" + cleanString(fileName);
		
	    convertDataFileIntoCSV();
		convertCSVIntoXMI();
		model2modelTransformation();
	    convertXMIintoJSON();
	    generateServer();
        addServerDependencies();
        generateApiCode();
        runApi();
        generateVisualization();
        System.out.println("Automatic API Generation finished!");
	}
	
	private static void m2m(String[] args) {
		System.out.println("m2m");
		
		//args: m2m 
		m2mTransformation = true;
		
		//args: m2m table.xmi
		if(args.length == 2) {
			modelFileName = args[1];
		}
		//args: m2m table.xmi openapi.xmi
		if(args.length == 3) {
			modelFileName = args[1];
			openAPIXMIFileName = args[2];
		}

		//mainFolderName = "AG_" + cleanString(fileName);

		// Move source files to temp folder
		File source = new File(modelFileName);
		File dest = new File(mainFolderName + File.separator + tempFolderName + File.separator + modelFileName);
		try {
		    FileUtils.copyFile(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		model2modelTransformation();
        System.out.println("Converted datafile model into OpenAPI model!");
	}
	
	private static void csv2openapi(String[] args) {
		System.out.println("csv2openapi");

		//args: csv2openapi
		csv2openapi = true;
		
		//args: csv2openapi data
		if(args.length == 2) {
			fileName = args[1];
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
		} 
		//args: csv2openapi data openapi.json
		else if(args.length == 3) {
			fileName = args[1];
			openAPIFileName = args[2];
		}

		mainFolderName = "AG_" + cleanString(fileName);

		convertCSVIntoXMI();
		model2modelTransformation();
	    convertXMIintoJSON();
        System.out.println("OpenAPI Generation finished!");
		
	}

	@Override
	public String toString() {
		return "AG [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	/* Returns true if url is valid */
    public static boolean URLisValid(String url) 
    { 
        /* Try creating a valid URL */
        try { 
            new URL(url).toURI(); 
            return true; 
        } 
          
        // If there was an Exception 
        // while creating URL object 
        catch (Exception e) { 
            return false; 
        } 
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
	
	private static void cleanCSV() {
		
		/*if(URLisValid(fileName)) {
			URL url = null;
			try {
				url = new URL(fileName);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				FileUtils.copyURLToFile(url, new File(defaultFileName + "." + fileType));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fileName = defaultFileName;
		}*/
		
		String csvFile = fileName + "." + fileType;
		
		BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        ArrayList<String> rows = new ArrayList<String>();

        try {
        	// Read first rows of data file
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	//TODO: replace all rare characters
                rows.add(cleanString(line));

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
			new File(mainFolderName + File.separator + apiCodeFolderName).mkdirs();
	    	File file = new File(mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType);
	    	file.delete();
	        writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType), "utf-8"));
	        for(int i = 0; i < rows.size(); i++) {
	        	if(i > 0) {
		        	writer.write("\n");
	        	}
			    writer.write(rows.get(i));
	        }
	    } catch (IOException ex) {
	        // Report
	    } finally {
	       try {writer.close();} catch (Exception ex) {/*ignore*/}
	    }
	}
	
	private static String cleanString(String s) {
		return StringUtils.stripAccents(s.replaceAll(" ", "").replaceAll("/", "_").replaceAll("\"", "").replaceAll("\'", "")
        		.replaceAll("\\?", "").replaceAll("\\+", "plus").replaceAll("\\-", "minus").replaceAll("\\(", "_").replaceAll("\\)", "_")
        		.replaceAll("\\[", "_").replaceAll("\\]", "_").replaceAll("\\{", "_").replaceAll("\\}", "_"))
        		.replaceAll("\\P{Print}", "").trim();
	}

	private static void convertCSVIntoXMI() {
	
		cleanCSV();
		
		String csvFile = mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType;
		
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
                rows.add(line.split(csvSplitBy));

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
    		attr.setValue(cleanString(fileName));
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
    		new File(mainFolderName + File.separator + tempFolderName).mkdirs();
    		StreamResult result = new StreamResult(new File(mainFolderName + File.separator + tempFolderName + File.separator + modelFileName));

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
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
            		+ fileSeparatorForResources + "metamodels" + fileSeparatorForResources + "Table.ecore"), 
            		new File(mainFolderName + File.separator + tempFolderName + File.separator + "Table.ecore").toPath(), 
            		StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName + 
            		fileSeparatorForResources + "metamodels" + fileSeparatorForResources + "Openapi.ecore"), 
            		new File(mainFolderName + File.separator + tempFolderName + File.separator + "Openapi.ecore").toPath(), 
            		StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName + 
            		fileSeparatorForResources + "transformator" + fileSeparatorForResources + "Table2Openapi.atl"), 
            		new File(mainFolderName + File.separator + tempFolderName + File.separator + "Table2Openapi.atl").toPath(), 
            		StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName + 
            		fileSeparatorForResources + "transformator" + fileSeparatorForResources
            		+ "Table2Openapi.emftvm"), 
            		new File(mainFolderName + File.separator + tempFolderName + File.separator + "Table2Openapi.emftvm").toPath(), 
            		StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }
		
		String tableEcore = mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources + "Table.ecore";
		String tableModel = mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources + modelFileName;
		String openapiEcore = mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources + "Openapi.ecore";
		String openapiModel = mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources + openAPIXMIFileName;
		String folder = mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources;

		System.out.println("tableEcore" + tableEcore);
		System.out.println("tableModel" + tableModel);
		System.out.println("openapiEcore" + openapiEcore);
		System.out.println("openapiModel" + openapiModel);
		System.out.println("folder" + folder);
		
		Launcher launcher = new Launcher();
		launcher.runATL(tableEcore, "Table", 
				tableModel, openapiEcore, "Openapi", 
				openapiModel, "Table2Openapi", folder);
		
	}

	private static void convertXMIintoJSON() {
		String jsonString = "", jsonStringFormatted = "", swaggerString = "", swaggerStringFormatted = "";
		
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(
            		FileUtils.readFileToString(new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName)));
            
            xmlJSONObj = xmlJSONObj.getJSONObject("openapi:API");
            xmlJSONObj.remove("openapi:API");
            xmlJSONObj.remove("xmi:version");
            xmlJSONObj.remove("xmlns:xmi");
            xmlJSONObj.remove("xmlns:openapi");
            
            JSONArray pathsArray = xmlJSONObj.getJSONArray("paths");
            xmlJSONObj.remove("paths");

        	if (xmlJSONObj.get("servers") instanceof JSONObject) {
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
            	
            	if (jsonobjAux.get("content") instanceof JSONArray)
                {
	            	JSONArray jsonobjAuxContentArray = jsonobjAux.getJSONArray("content");
	            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
            			.getJSONObject("responses").getJSONObject("200").remove("content");
	                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
                		.getJSONObject("responses").getJSONObject("200").put("content", new JSONObject());
	                
	            	for (int j = 0; j < jsonobjAuxContentArray.length(); j++) {
	            		String contentType = jsonobjAuxContentArray.getJSONObject(j).getString("contentTypeName");
		            	JSONObject jsonobjAuxContent = jsonobjAuxContentArray.getJSONObject(j).getJSONObject("contentType");

		            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
		            		.getJSONObject("responses").getJSONObject("200").getJSONObject("content").put(contentType, jsonobjAuxContent);
		            	
		            	if(contentType.contains("json")) {
			            	String ref = jsonobjAuxContent.getJSONObject("schema").getJSONObject("items").getString("ref");
			            	jsonobjAuxContent.getJSONObject("schema").remove("items");
			            	jsonobjAuxContent.getJSONObject("schema").put("items", new JSONObject());
			            	jsonobjAuxContent.getJSONObject("schema").getJSONObject("items").put("$ref", ref);
		            	}
		            	
	            	}
                } else {
	            	JSONObject jsonobjAuxContentObject = jsonobjAux.getJSONObject("content");
                	String contentType = jsonobjAuxContentObject.getString("contentTypeName");
	            	JSONObject jsonobjAuxContent = jsonobjAuxContentObject.getJSONObject("contentType");
	            	
	            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
	            		.getJSONObject("responses").getJSONObject("200").remove("content");
	                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
	                	.getJSONObject("responses").getJSONObject("200").put("content", new JSONObject());
	            	xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get")
	            		.getJSONObject("responses").getJSONObject("200").getJSONObject("content").put(contentType, jsonobjAuxContent);
            	
	            	if(contentType.contains("json")) {
		            	String ref = jsonobjAuxContent.getJSONObject("schema").getJSONObject("items").getString("ref");
		            	jsonobjAuxContent.getJSONObject("schema").remove("items");
		            	jsonobjAuxContent.getJSONObject("schema").put("items", new JSONObject());
		            	jsonobjAuxContent.getJSONObject("schema").getJSONObject("items").put("$ref", ref);
	            	}
                }
            	
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
            	JSONObject getProduces = xmlSwaggerObj.getJSONObject("paths").getJSONObject(pathSwagger).getJSONObject("get").getJSONObject("responses").getJSONObject("200")
            			.getJSONObject("content");
            	try {
                	if(getProduces.getJSONObject("application/json") != null) {
                		produces.put("application/json");
                	}
            	} catch(JSONException e) {
            		e.printStackTrace();
            	}
            	try {
	            	if(getProduces.getJSONObject("text/html") != null) {
	            		produces.put("text/html");
	            	}
            	} catch(JSONException e) {
            		e.printStackTrace();
            	}
            	
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
            	
            	JSONObject contentObject = jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("content");
            	
            	try {
	            	if(contentObject.getJSONObject("application/json") != null) {
	            		JSONObject jsonobjAuxSwagger = contentObject.getJSONObject("application/json").getJSONObject("schema");
	                	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").remove("content");
	                	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").put("schema", jsonobjAuxSwagger);
	                	
	                	String ref = jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").getString("$ref");
	                	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").remove("$ref");
	                	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getJSONObject("items").put("$ref", "#/definitions" + ref.substring(ref.lastIndexOf("/")));
	            	}
            	} catch(JSONException e) {
            		e.printStackTrace();
            	}

            	try {
                	if(contentObject.getJSONObject("text/html") != null) {
                		JSONObject jsonobjAuxSwagger = contentObject.getJSONObject("text/html").getJSONObject("schema");
                    	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").remove("content");
                    	jsonobj.getJSONObject("get").getJSONObject("responses").getJSONObject("200").put("schema", jsonobjAuxSwagger);	
                    }
            	} catch(JSONException e) {
            		e.printStackTrace();
            	}
            	
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
		
		try (PrintWriter out = new PrintWriter(mainFolderName + File.separator + tempFolderName + File.separator + openAPIFileName)) {
		    out.println(jsonStringFormatted);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try (PrintWriter out = new PrintWriter(mainFolderName + File.separator + tempFolderName + File.separator + swaggerFileName)) {
		    out.println(swaggerStringFormatted);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*private static void generateApiDefinition() {
		
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
            apiDefinition += "{ \"swagger\" : \"2.0\", \"info\" : { \"version\" : \"1.0.0\", \"title\" : \"" + fileName + "\", \"description\" : \"Obtaining the " + fileNameNoWhiteSpaces  
            			+ "\" }, \"host\" : \"" + host + "\", \"basePath\" : \"" + basePath + "\",";
            apiDefinition += "\"paths\" : { \"/\" : { \"get\" : { \"summary\" : \"GET " + fileName + "\", \"operationId\" : \"get" 
            			+ fileNameNoWhiteSpaces + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\", \"produces\" : [ \"application/json\" ],";
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
            	apiDefinition += "\"/" + columns[i] + "/{" + columns [i] + "}\" : { \"get\" : { \"summary\" : \"" + columnsDescriptions[i] + "\", \"operationId\" : \"" 
            			+ columnsDescriptions[i].replaceAll(" ", "").replaceAll("-", "") + "\", \"description\": \"Use value 'all' in a parameter for non-empty values\", \"produces\" : [ \"application/json\" ],";
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
		
	}*/

	private static void generateServer() {

		// TODO: Swagger 2.0 to OpenAPI
		if(openapi2api || xmi2api || csv2api) {
			String sourcePath = mainFolderName + File.separator + tempFolderName + File.separator + swaggerFileName;
			new File(mainFolderName + File.separator + apiCodeFolderName).mkdirs();
			File source = new File(sourcePath);
			File dest = new File(mainFolderName + File.separator + apiCodeFolderName + File.separator + swaggerFileName);
			try {
			    FileUtils.copyFile(source, dest);
			} catch (IOException e) {
			    e.printStackTrace();
			}

			String sourcePath2 = mainFolderName + File.separator + tempFolderName + File.separator + openAPIFileName;
			File source2 = new File(sourcePath2);
			File dest2 = new File(mainFolderName + File.separator + apiCodeFolderName + File.separator + openAPIFileName);
			try {
			    FileUtils.copyFile(source2, dest2);
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		
		String[] args = new String [7];
		args[0] = "generate";
		args[1] = "--lang";
		args[2] = "nodejs-server";
		args[3] = "--input-spec";
		args[4] = mainFolderName + File.separator + apiCodeFolderName + File.separator + swaggerFileName;
		args[5] = "--output";
		args[6] = mainFolderName + File.separator + apiCodeFolderName;
		SwaggerCodegen.main(args);
		
		/*Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("java -jar " + File.separator + resFolderName + File.separator + swaggerCodegeneFileName 
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
			br = new BufferedReader(new FileReader(mainFolderName + File.separator + apiCodeFolderName + "/package.json"));
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
			writer = new PrintWriter(mainFolderName + File.separator + apiCodeFolderName + "/package.json", "UTF-8");
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
			br = new BufferedReader(new FileReader(mainFolderName + File.separator + apiCodeFolderName + "/controllers/DefaultService.js"));
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
					(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
							+ fileSeparatorForResources + apiFolderName 
							+ fileSeparatorForResources + serverCodeFileName)));
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
				if(lineFunctionName.contains("getvisualization")) {
					servercode += lineFunctionName + "\t" + "var obj = new Object();obj.value = \"visualization\";args.visualization = obj;\n"
							+ "\t" + "exports.getOperation(args, res, next); \n}\n";
				} else {
					servercode += lineFunctionName + "\t" + "exports.getOperation(args, res, next); \n}\n";
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
			writer = new PrintWriter(mainFolderName + File.separator + apiCodeFolderName + "/controllers/DefaultService.js", "UTF-8");
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

	private static void runApi() {

		System.out.println("Launching API to localhost...");
		try {
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
            		+ fileSeparatorForResources + "api" + fileSeparatorForResources + "runApi.bat"), 
            		new File(mainFolderName + File.separator + "runApi.bat").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName + 
            		fileSeparatorForResources + "api" + fileSeparatorForResources + "runApi2.bat"), 
            		new File(mainFolderName + File.separator + "runApi2.bat").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }
		File execFile = new File(mainFolderName + File.separator + "runApi.bat");
		execFile.setExecutable(true);
		
	    try {
	    	String executable = "./" + mainFolderName + File.separator + "runApi.bat";
	    	if(System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
	    		executable = mainFolderName + File.separator + "runApi.bat";
	    		Runtime.getRuntime().exec("cmd.exe /C start " + executable);
	    	}
	    	else {
	    		Process p = new ProcessBuilder(executable, "").start();
	        
		        //Runtime rt = Runtime.getRuntime();
	            //Process p = rt.exec("runApi");
		        /*BufferedReader reader = 
		                new BufferedReader(new InputStreamReader(p.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ( (line = reader.readLine()) != null) {
				   builder.append(line);
				   builder.append(System.getProperty("line.separator"));
				}
				String result = builder.toString();
				System.out.println(result);*/
				//p.waitFor(5000, TimeUnit.MILLISECONDS);
	    	}
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }/* catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		System.out.println("Server listening in http://localhost:8080/v1/");
		
	}

	private static void generateVisualization() {

		
		// ChartJS
		
		//Determine the columns to generate the graph
		JSONObject xmlJSONObj;
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<String> allColumnNames = new ArrayList<String>();
		String columnNameId = "";
		try {
			xmlJSONObj = XML.toJSONObject(FileUtils.readFileToString(new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName)));
			JSONArray xmlJSONObjAux = xmlJSONObj.getJSONObject("openapi:API").getJSONObject("components")
					.getJSONObject("schemas").getJSONObject("mainComponent").getJSONArray("properties");
			
			for(int i = 0; i < xmlJSONObjAux.length(); i++) {
				if(i == 0) {
					columnNameId = xmlJSONObjAux.getJSONObject(i).get("name").toString();
				}
				else if(xmlJSONObjAux.getJSONObject(i).getJSONObject("content").get("type").equals("integer")) {
					columnNames.add(xmlJSONObjAux.getJSONObject(i).get("name").toString());
				}
				allColumnNames.add(xmlJSONObjAux.getJSONObject(i).get("name").toString());
			}
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String visualizationCode = "";
		BufferedReader brVisualization = null;
        String lineVisualization = "";
		try {
			brVisualization = new BufferedReader(new InputStreamReader
					(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
		            		+ fileSeparatorForResources + "visualization" 
		            		+ fileSeparatorForResources + visualizationChartJS)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while ((lineVisualization = brVisualization.readLine()) != null) {
				//TODO: arreglar pie chart (con botones como en Charts de iOS)
				if(lineVisualization.contains("datasetsLineChart") || lineVisualization.contains("datasetsBarChart")) {
					
					String datasets = "";
					
					if(lineVisualization.contains("datasetsLineChart")) {
						for(int i = 0; i < columnNames.size(); i ++) {
							if (i > 0) {
								datasets += ",";
							}
							String dataset = "{\n" + 
									"	label: '"+ columnNames.get(i) + "',\n" + 
									"	backgroundColor: color(colorsArray[" + i + "% colorsArray.length]).alpha(0.5).rgbString(),\n" + 
									"	borderColor: colorsArray[" + i + "% colorsArray.length],\n" + 
									"	borderWidth: 3,\n" + 
									"	fill: false,\n" + 
									"	data: [\n" + 
									"		dataLineChart" + //columnNames.get(i) +
									"	]\n" + 
									"}";
							datasets += dataset;
						}
						
						visualizationCode += lineVisualization.replace("datasetsLineChart", datasets) + "\n";
					}
					else if(lineVisualization.contains("datasetsBarChart")) {
						for(int i = 0; i < columnNames.size(); i ++) {
							if (i > 0) {
								datasets += ",";
							}
							String dataset = "{\n" + 
									"	label: '"+ columnNames.get(i) + "',\n" + 
									"	backgroundColor: color(colorsArray[" + i + "% colorsArray.length]).alpha(0.5).rgbString(),\n" + 
									"	borderColor: colorsArray[" + i + "% colorsArray.length],\n" + 
									"	borderWidth: 3,\n" + 
									"	data: [\n" + 
									"		dataBarChart" + //columnNames.get(i) +
									"	]\n" + 
									"}";
							datasets += dataset;
						}
						
						visualizationCode += lineVisualization.replace("datasetsBarChart", datasets) + "\n";
					}
				} 
				else if(lineVisualization.contains("configPie0")) {
					String pies = "";
					for(int i = 0; i < allColumnNames.size(); i ++) {
						String pie = "var configPie" + i + " = {\n" + 
						"			type: 'pie',\n" + 
						"			data: {\n" + 
						"				datasets: [{\n" + 
						"					data: [ " +
						"						dataPieChart\n" + 
						"					],\n" + 
						"					backgroundColor: [\n" + 
						"						window.chartColors.red,\n" + 
						"						window.chartColors.orange,\n" + 
						"						window.chartColors.yellow,\n" + 
						"						window.chartColors.green,\n" + 
						"						window.chartColors.blue,\n" + 
						"					],\n" + 
						"					label: '" + allColumnNames.get(i) + "'\n" + 
						"				}"+
						"				],\n" + 
						"				labels: [\n" + 
						"					labelsPieChart\n" + 
						"				]\n" + 
						"			},\n" + 
						"			options: {\n" + 
						"				responsive: true,\n" + 
						"				title: {\n" + 
						"					display: true,\n" + 
						"					text: 'Chart.js Pie Chart (" + allColumnNames.get(i) + ")'\n" + 
						"				}" +
						"			}\n" + 
						"		};\n\n";
						pie += "</script>\n\n<button id=\"pieChartButton" + i + "\">PieChart " + allColumnNames.get(i) + "</button>\n\n<script>\n\n";
						pie += "		document.getElementById('pieChartButton" + i + "').addEventListener('click', function() {\n" + 
								"			var pie = document.getElementById('chart-area').getContext('2d');\n" + 
								"			window.myPie = new Chart(pie, configPie" + i + ");\n" + 
								//"			document.getElementById('pieChartLabel').innerHTML = '" + allColumnNames.get(i) + "';\n" +
								"		});\n\n";
						pies += pie;
					}
					visualizationCode += lineVisualization.replace("var configPie0;", pies) + "\n";
				}
				else {
					visualizationCode += lineVisualization + "\n";
				}
			}
			System.out.println("Visualization.html generated");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	brVisualization.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(mainFolderName + File.separator + apiCodeFolderName + File.separator + 
    				"controllers" + File.separator + visualizationChartJS, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.println(visualizationCode);
			writer.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println("Visualization.html saved");
		
		/*try {
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
            		+ fileSeparatorForResources + "visualization" 
            		+ fileSeparatorForResources + visualizationMainFolderName + ".zip"), 
            		new File(mainFolderName + File.separator + visualizationZipFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }
		
		try {
			FileUtils.deleteDirectory(new File(mainFolderName + File.separator + visualizationMainFolderName));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			ZipFile zipFile = new ZipFile(mainFolderName + File.separator + visualizationZipFileName);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();

				File file = new File(mainFolderName + File.separator + name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File(mainFolderName + File.separator + visualizationZipFileName);
		if (file.exists()) {
		    file.delete();
		} else {
		    System.err.println("Error deleting zip file");
		}
		
		//Determine the columns to generate the graph
		JSONObject xmlJSONObj;
		ArrayList<String> columnNames = new ArrayList<String>();
		String columnNameId = "";
		try {
			xmlJSONObj = XML.toJSONObject(FileUtils.readFileToString(new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName)));
			JSONArray xmlJSONObjAux = xmlJSONObj.getJSONObject("openapi:API").getJSONObject("components")
					.getJSONObject("schemas").getJSONObject("mainComponent").getJSONArray("properties");
			
			for(int i = 0; i < xmlJSONObjAux.length(); i++) {
				if(i == 0) {
					columnNameId = xmlJSONObjAux.getJSONObject(i).get("name").toString();
				}
				else if(xmlJSONObjAux.getJSONObject(i).getJSONObject("content").get("type").equals("integer")) {
					columnNames.add(xmlJSONObjAux.getJSONObject(i).get("name").toString());
				}
			}
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Create graphs in swift
		String swiftFilePath = mainFolderName + File.separator + visualizationMainFolderName + File.separator + visualizationProjectName 
				+ File.separator + visualizationProjectName + File.separator + visualizationFolderName 
				+ File.separator + visualizationSwiftFileName;
		try(BufferedReader br = new BufferedReader(new FileReader(swiftFilePath))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String content = sb.toString();
		    content = content.replaceFirst("columnNameId", columnNameId);
		    if(!columnNames.isEmpty()) {
		    	for(int i = 0; i < columnNames.size(); i++) {
		    		if(i == 0) {
				    	content = content.replaceFirst("\"columnNames\"", "\"" + columnNames.get(i) +  "\"");
		    		} else {
				    	content = content.replaceFirst("\"" + columnNames.get(i-1) + "\"", 
				    			"\"" + columnNames.get(i-1) + "\"" + ", " + "\"" + columnNames.get(i) + "\"");
		    		}
		    	}
		    }
		    
		    File swiftFile = new File(swiftFilePath);
		    FileUtils.writeStringToFile(swiftFile, content);
		    System.err.println("Visualization created");
		    //System.out.println(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String swiftFilePath2 = mainFolderName + File.separator + visualizationMainFolderName + File.separator + visualizationProjectName 
				+ File.separator + visualizationProjectName + File.separator + visualizationFolderName 
				+ File.separator + visualizationSwiftFileName2;
		try(BufferedReader br = new BufferedReader(new FileReader(swiftFilePath2))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String content = sb.toString();
		    content = content.replaceFirst("columnNameId", columnNameId);
		    if(!columnNames.isEmpty()) {
		    	for(int i = 0; i < columnNames.size(); i++) {
		    		if(i == 0) {
				    	content = content.replaceFirst("\"columnNames\"", "\"" + columnNames.get(i) +  "\"");
		    		} else {
				    	content = content.replaceFirst("\"" + columnNames.get(i-1) + "\"", 
				    			"\"" + columnNames.get(i-1) + "\"" + ", " + "\"" + columnNames.get(i) + "\"");
		    		}
		    	}
		    }
		    
		    File swiftFile = new File(swiftFilePath2);
		    FileUtils.writeStringToFile(swiftFile, content);
		    System.err.println("Visualization created");
		    //System.out.println(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Pie chart

		String csvFile = mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType;
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
                rows.add(line.split(csvSplitBy));

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
        
        ArrayList<String> classifiedValues = new ArrayList<String>();
        ArrayList<String> classifiedColumnNames = new ArrayList<String>();
        boolean classification = true;
        boolean distinct_value = true;

    	for(int n_cell = 0; n_cell < rows.get(0).length; n_cell++) {
    		for(int n_row = 1; n_row < rows.size() && classification; n_row++) {
    			if(n_cell < rows.get(n_row).length) {
	        		for(int n_value = 0; n_value < classifiedValues.size() && distinct_value; n_value++) {
	        			String cell = rows.get(n_row)[n_cell];
	        			if(classifiedValues.get(n_value).equals(cell) || StringUtils.isBlank(cell)) {
	        				distinct_value = false;
	        			}
	        		}
	        		if(distinct_value && !StringUtils.isBlank(rows.get(n_row)[n_cell])) {
	        			classifiedValues.add(rows.get(n_row)[n_cell]);
	        		}
	        		distinct_value = true;
	        		if(classifiedValues.size() > 5) {
	        			classification = false;
	        		}
    			}
        	}
    		if(classifiedValues.size() <= 1) {
    			classification = false;
    		}
    		if(classification) {
    			System.out.println("Classified values at " + rows.get(0)[n_cell]);
    			classifiedColumnNames.add(rows.get(0)[n_cell]);
    		}
    		classifiedValues.clear();
			classification = true;
        }
    	
    	// TODO: create pie chart for each classification column
    	if(classifiedColumnNames.size() > 0) {
    		String swiftFilePath3 = mainFolderName + File.separator + visualizationMainFolderName + File.separator + visualizationProjectName 
    				+ File.separator + visualizationProjectName + File.separator + visualizationFolderName 
    				+ File.separator + visualizationSwiftFileName3Tab;
    		try(BufferedReader br3= new BufferedReader(new FileReader(swiftFilePath3))) {
    		    StringBuilder sb = new StringBuilder();
    		    line = br3.readLine();

    		    while (line != null) {
    		        sb.append(line);
    		        sb.append(System.lineSeparator());
    		        line = br3.readLine();
    		    }
    		    String content = sb.toString();
    		    if(!classifiedColumnNames.isEmpty()) {
    		    	for(int i = 0; i < classifiedColumnNames.size(); i++) {
    		    		if(i == 0) {
    				    	content = content.replaceFirst("\"columnNames\"", "\"" + classifiedColumnNames.get(i) +  "\"");
    		    		} else {
    				    	content = content.replaceFirst("\"" + classifiedColumnNames.get(i-1) + "\"", 
    				    			"\"" + classifiedColumnNames.get(i-1) + "\"" + ", " + "\"" 
    				    					+ classifiedColumnNames.get(i) + "\"");
    		    		}
    		    	}
    		    	content = content.replaceFirst("filename", fileName);
    		    }
    		    
    		    File swiftFile = new File(swiftFilePath3);
    		    FileUtils.writeStringToFile(swiftFile, content);
    		    System.err.println("Visualization created");
    		    //System.out.println(content);
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}

		// Build and open visualization generated
		System.out.println("Waiting for building and opening visualization...");
		try {
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
            		+ fileSeparatorForResources + "visualization" 
            		+ fileSeparatorForResources + "buildVisualization"), 
            		new File(mainFolderName + File.separator + "buildVisualization").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }

		try {
            Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
            		+ fileSeparatorForResources + "visualization" 
            		+ fileSeparatorForResources + "openVisualization"), 
            		new File(mainFolderName + File.separator + "openVisualization").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
			System.out.println(e.getMessage());
        }
		
		File execFile1 = new File(mainFolderName + File.separator + "buildVisualization");
		execFile1.setExecutable(true);
		File execFile2 = new File(mainFolderName + File.separator + "openVisualization");
		execFile2.setExecutable(true);
        
	    try {
	    	if(System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
	    		System.out.println("The build visualization process is only for MacOS");
	    	} else {
	    		Process p = new ProcessBuilder("./" + mainFolderName + File.separator + "buildVisualization", "").start();
		        BufferedReader reader = 
		                new BufferedReader(new InputStreamReader(p.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String builder_line = null;
				while ((builder_line = reader.readLine()) != null) {
				   builder.append(builder_line);
				   builder.append(System.getProperty("line.separator"));
				}
				String result = builder.toString();
				System.out.println(result);
		        p.waitFor();
	    	}
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    try {
	    	if(System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
	    		System.out.println("The visualization is built only for MacOS");
	    	} else {
		        Process p = new ProcessBuilder("./" + mainFolderName + File.separator +  "openVisualization", "").start();
	    	}
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }*/
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
