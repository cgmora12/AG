package AG;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.swagger.codegen.SwaggerCodegen;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.standalone.ATLRunner;

import cs.ualberta.launcher.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
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
	public static String fileUrl = "";
	public static String defaultFileName = "data";
	public static String newfileName = "data";
	public static String desiredFileName = "data";
	public static String fileType = "csv";
	public static String alternativeFileType = "";
	public static String modelFileName = "table.xmi";
	public static String host = "server.com";
	public static String basePath = "/resource";
	public static String swaggerFileName = "swagger.json";
	public static String openAPIFileName = "openapi.json";
	public static String openAPIXMIFileName = "openapi.xmi";
	//public static String swaggerCodegeneFileName = "swagger-codegen-cli-2.2.1.jar";
	public static String apiCodeFolderName = "apiCode";
	public static String serverCodeFileName = "servercode.js";
	public static String resFolderName = "AG";
	public static String apiFolderName = "api";
	public static String tempFolderName = "generated";
	public static String mainFolderName = "AG_data";
	public static String fileSeparatorForResources = "/";
	public static String visualisationMainFolderName = "Visualisacion";
	public static String visualisationChartJS = "visualisation.html";
	public static String visualisationZipFileName = visualisationMainFolderName + ".zip";
	public static String visualisationProjectName = "ChartsDemo-macOS";
	public static String visualisationFolderName = "Demos";
	public static String visualisationSwiftFileName = "BarDemoViewController.swift";
	public static String visualisationSwiftFileName2 = "LineDemoViewController.swift";
	public static String visualisationSwiftFileName3 = "PieDemoViewController.swift";
	public static String visualisationSwiftFileName3Tab = "CustomPieTab.swift";

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
				default : System.out.println("No operation called: try csv2api");
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
			if(fileName.contains("/")) {
				fileUrl = fileName;
				fileName = fileName.split("/")[fileName.split("/").length-1];
			}
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
			desiredFileName = fileName;
		} 
		//args: csv2api http://url.to.data.csv filename
		else if(args.length == 3) {
			fileName = args[1];
			if(fileName.contains("/")) {
				fileUrl = fileName;
				fileName = fileName.split("/")[fileName.split("/").length-1];
			}
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
			desiredFileName = cleanString(args[2]);
		}
		//args: csv2api data table.xmi openapi.xmi 
		else if(args.length == 4) {
			fileName = args[1];
			if(fileName.contains("/")) {
				fileUrl = fileName;
				fileName = fileName.split("/")[fileName.split("/").length-1];
			}
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
			desiredFileName = fileName;
			modelFileName = args[2];
			openAPIXMIFileName = args[3];
		}

		mainFolderName = "AG_" + cleanString(fileName);

		if(!fileUrl.isEmpty()) {
			getCSVFromURL();
		}
		convertCSVIntoXMI();
		model2modelTransformation();
	    convertXMIintoJSON();
	    generateServer();
        addServerDependencies();
        generateApiCode();
        generateVisualisation();
        runApi();
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
        generateVisualisation();
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
        generateVisualisation();
        runApi();
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
        generateVisualisation();
        runApi();
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
			if(fileName.contains("/")) {
				fileUrl = fileName;
				fileName = fileName.split("/")[fileName.split("/").length-1];
			}
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
		} 
		//args: csv2openapi data openapi.json
		else if(args.length == 3) {
			fileName = args[1];
			if(fileName.contains("/")) {
				fileUrl = fileName;
				fileName = fileName.split("/")[fileName.split("/").length-1];
			}
			if(fileName.contains(".csv")) {
				fileName = fileName.replace(".csv", "");
			}
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
	
	private static void getCSVFromURL() {
		URL url;
		try {
			url = new URL(fileUrl);
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(desiredFileName + ".csv");
			fileName = desiredFileName;
			FileChannel fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


        FileInputStream fis = null;
		try {
			fis = new FileInputStream(csvFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
        FilterInputStream  filter = new BufferedInputStream(fis); 
    	BOMInputStream bomIn = new BOMInputStream(filter, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE,
    	        ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE);
    	
    	try {
			if (bomIn.hasBOM()) {
			    // has a UTF-8 BOM
				System.out.println("has a UTF-8 BOM");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	try {
			int firstNonBOMByte = bomIn.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		} // Skips BOM
    	
        
        /*try {
        	// Read first rows of data file
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            //br = new BufferedReader(new FileReader(csvFile));
            int limit = 2, counter = 0;            
            while ((line = br.readLine()) != null && counter < limit) {
                // use comma as separator
            	//TODO: replace all rare characters
                rows.add(cleanString(line));
                counter++;
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
        }*/
        
        // Rewrite CSV without rare characters
	    try {
			new File(mainFolderName + File.separator + apiCodeFolderName).mkdirs();
	        Path FROM = Paths.get(csvFile);
	        Path TO = Paths.get(mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType);
	        //overwrite the destination file if it exists, and copy
	        // the file attributes, including the rwx permissions
	        CopyOption[] options = new CopyOption[]{
	          StandardCopyOption.REPLACE_EXISTING,
	          StandardCopyOption.COPY_ATTRIBUTES
	        }; 
	        Files.copy(FROM, TO, options);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    /*Writer writer = null;
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
	       try {writer.close();} catch (Exception ex) {}
	    }*/
	}
	
	private static String cleanString(String s) {
		return StringUtils.stripAccents(s.replaceAll(" ", "").replaceAll("/", "_").replaceAll("\"", "").replaceAll("\'", "")
        		.replaceAll("\\?", "").replaceAll("\\+", "plus").replaceAll("\\-", "minus").replaceAll("\\(", "_").replaceAll("\\)", "_")
        		.replaceAll("\\[", "_").replaceAll("\\]", "_").replaceAll("\\{", "_").replaceAll("\\}", "_"))
        		.replaceAll("\\P{Print}", "").trim();
	}

	private static void convertCSVIntoXMI() {
	
		cleanCSV();
		
		// TODO: EMF code generation to parse CSV into data model (XMI)
		/*ResourceSet resourceSet = new ResourceSetImpl(); 
	    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new  XMIResourceFactoryImpl());
	    Resource tableMetaModel= resourceSet.getResource(URI.createFileURI(fileSeparatorForResources + resFolderName 
        		+ fileSeparatorForResources + "metamodels" + fileSeparatorForResources + "Table.ecore"), true);
	    EPackage tableEPackage = (EPackage) tableMetaModel.getContents().get(0);
	    //resourceSet.getPackageRegistry().put("http://gholizadeh.org", tableEPackage);
	    Resource tableModel = resourceSet.getResource(URI.createURI(mainFolderName + 
	    		fileSeparatorForResources + tempFolderName + fileSeparatorForResources + modelFileName), true);
	    
	    EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
	    EClass tableClass = tableEPackage.eClass();
	    
	    EFactory tableInstance = tableEPackage.getEFactoryInstance();
	    EObject tableObject = tableInstance.create(tableClass);
	    
	    EList tableAttributes = tableClass.getEAttributes();
		for (int iAttr = 0; iAttr < tableAttributes.size(); iAttr++) {
			EAttribute tableAttribute = ((EAttribute) tableAttributes.get(iAttr));
			System.out.println(tableAttribute.getName() + ": "+ tableObject.eGet(tableAttribute));
			// Set values from CSV
		}

	    EList<EObject> ModelObjects = new BasicEList<EObject>(); 
	    ModelObjects.add(tableObject);

	    tableModel.getContents().addAll(ModelObjects);
	    
	    try {
			tableModel.save(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	    
		
		String csvFile = mainFolderName + File.separator + apiCodeFolderName + File.separator + newfileName + "." + fileType;
		
		BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        //ArrayList<String[]> rows = new ArrayList<String[]>();


        System.out.println("create xmi");

        //Using StAX
        try {
        	
	        XMLOutputFactory xof = XMLOutputFactory.newInstance();
	        new File(mainFolderName + File.separator + tempFolderName).mkdirs();
	        XMLStreamWriter xMLStreamWriter = xof.createXMLStreamWriter(
	        		new FileWriter(mainFolderName + File.separator + tempFolderName + File.separator + modelFileName));
    		
			xMLStreamWriter.writeStartDocument();
	        xMLStreamWriter.writeStartElement("table:Table");
	        xMLStreamWriter.writeAttribute("xmi:version", "2.0");
	        xMLStreamWriter.writeAttribute("xmlns:xmi", "http://www.omg.org/XMI");
	        xMLStreamWriter.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	        xMLStreamWriter.writeAttribute("xmlns:table", "platform:/resource/TransformationRules/Table.ecore");
	        xMLStreamWriter.writeAttribute("filename", cleanString(fileName));
	        
	        try {
            	// Read first rows of data file
                //br = new BufferedReader(new FileReader(csvFile));

	        	/*try (OutputStream out = new FileOutputStream(csvFile)) {
	        		 
	        		// write a byte sequence
	        		out.write(0xEF);
	        		out.write(0xBB);
	        		out.write(0xBF);
	        	        
	        		out.close();
	        	} catch (IOException e) {
	        		e.printStackTrace();
	        	}*/
	        	
	            /*String[] charsetsToBeTested = 
	            	{"hol", "UTF8", "windows-1252", 
	            	"Windows-1252", "Windows-1251", "Windows-1250", "Windows-1253", 
	            	"ISO-8859-7", "ISO-646", "ISO-8859", 
	            	"UTF-16", "UTF-32", "UTF-8"
	            	};

	            CharsetDetector cd = new CharsetDetector();
	            File f = new File(csvFile);
	            Charset charset = cd.detectCharset(f, charsetsToBeTested);

	            String content = FileUtils.readFileToString(f, charset);
	            if(content.startsWith("\uFEFF")) {
	            	content = content.substring(1);
            		System.out.println("Remove char (UTF-8 BOM)");
            	} else {
            		System.out.println("First char " + content.charAt(0));
            	}
	            String content2 = FileUtils.readFileToString(f, "Windows-1252");
	            if(content2.startsWith("\uFEFF")) {
	            	content2 = content2.substring(1);
            		System.out.println("Remove char (UTF-8 BOM)");
            	} else {
            		System.out.println("First char " + content2.charAt(0));
            	}
	            
	            FileUtils.write(new File("prueba.csv"), content, "Windows-1252");
	            Charset charset2 = cd.detectCharset(new File("prueba.csv"), charsetsToBeTested);
	            */
	        	
	        	/*byte[] fileContent = Files.readAllBytes(new File(csvFile).toPath());
	        	//Convert byte[] to String
	        	String s = new String(fileContent);	        	 
	        	System.out.println("sub " + s.substring(0, 10));
	        	if ((fileContent[0] == (byte)0xFF) &&
	                    (fileContent[1] == (byte)0xFE) &&
	                    (fileContent[2] == (byte)0x00) &&
	                    (fileContent[3] == (byte)0x00))
	                {
		        	System.out.println("bomba");
	                }
	        	else {
		        	System.out.println(fileContent[0]);
	        	}

	            FileInputStream fis = new FileInputStream(csvFile); 
	            FilterInputStream  filter = new BufferedInputStream(fis); 
	        	BOMInputStream bomIn = new BOMInputStream(filter, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE,
	        	        ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE);
	        	
	        	if (bomIn.hasBOM()) {
	        	    // has a UTF-8 BOM
            		System.out.println("has a UTF-8 BOM");
	        	}
	        	int firstNonBOMByte = bomIn.read(); // Skips BOM
	        	 */
	        	
	        	br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
                int i = 0;
                //boolean firstLine = true;
                int limit = 2, counter = 0; 
                int firstLineCells = 0;
                while ((line = br.readLine()) != null && counter < limit) {
                    // use comma as separator
                	//TODO: replace all rare characters
                    //rows.add(line.split(csvSplitBy));
                	/*if(firstLine) {
	                	if(line.startsWith("\uFEFF")) {
	                		line = line.substring(1);
	                		System.out.println("Remove char (UTF-8 BOM)");
	                	} else {
	                		System.out.println("First char " + line.charAt(0));
	                	}
                	}
                	firstLine = false;*/
                	
                	// Cleaning
                	line = cleanString(line);
                	
                	String[] row = line.split(csvSplitBy);
                	if(i==0) {
                		firstLineCells = row.length;
                	}

        	        xMLStreamWriter.writeStartElement("rows");
        	        xMLStreamWriter.writeAttribute("position", i + "");
        	        
        			for (int j = 0; j < row.length; j++) {
        	    		// cells elements
            	        xMLStreamWriter.writeStartElement("cells");
            	        xMLStreamWriter.writeAttribute("value", row[j]);
                		String cellType = "";
                		try {
                    		if(row[j].equals("" + Integer.parseInt(row[j]))) {
                    			cellType = "integer";
                    		} else if(row[j].equals("" + Float.parseFloat(row[j]))){
                    			cellType = "number";
                    		} else {
                    			cellType = "string";
                    		}
                    	} catch (NumberFormatException e) {
                    		cellType = "string";
                    	}

            	        xMLStreamWriter.writeAttribute("type", cellType);
            	        xMLStreamWriter.writeEndElement();
        				
        			}
        			if(i>0 && row.length < firstLineCells) {
        				for(int k = row.length; k < firstLineCells; k++) {
                	        xMLStreamWriter.writeStartElement("cells");
                	        xMLStreamWriter.writeAttribute("value", "");
                    		String cellType = "string";

                	        xMLStreamWriter.writeAttribute("type", cellType);
                	        xMLStreamWriter.writeEndElement();
        				}
        			}
        			i++;
        	        xMLStreamWriter.writeEndElement();

                    counter++;
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
	        
	        xMLStreamWriter.writeEndElement();
	        xMLStreamWriter.writeEndDocument();
	        xMLStreamWriter.flush();
	        xMLStreamWriter.close();
	        
		} catch (XMLStreamException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //Using DOM
        /*try {
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

    		try {
            	// Read first rows of data file
                br = new BufferedReader(new FileReader(csvFile));
                int i = 0;
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                	//TODO: replace all rare characters
                    //rows.add(line.split(csvSplitBy));
                	String[] row = line.split(csvSplitBy);
                	
                	Element rowsElement = doc.createElement("rows");
            		Attr attrRow = doc.createAttribute("position");
            		attrRow.setValue(i + "");
            		rowsElement.setAttributeNode(attrRow);
            		rootElement.appendChild(rowsElement);
        			for (int j = 0; j < row.length; j++) {
        	    		// cells elements
                		Element cellsElement = doc.createElement("cells");
                		Attr attrCells = doc.createAttribute("value");
                		attrCells.setValue(row[j]);
                		cellsElement.setAttributeNode(attrCells);
                		String cellType = "";
                		try {
                    		if(row[j].equals("" + Integer.parseInt(row[j]))) {
                    			cellType = "integer";
                    		} else if(row[j].equals("" + Float.parseFloat(row[j]))){
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
        			i++;

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
    	}*/
    	
		
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
		
		// Use openapi model (xmi) to json parser (libs)
		/*ResourceSet resourceSetJson = new ResourceSetImpl();
		resourceSetJson.getResourceFactoryRegistry()
		                .getExtensionToFactoryMap()
		                .put("json", new JsonResourceFactory());
		 
		resourceSetJson.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new  XMIResourceFactoryImpl());

		Resource resourceJson = resourceSetJson.createResource
				  (URI.createFileURI(mainFolderName + File.separator + tempFolderName + File.separator + "jsonopenapi.json"));
		// Create empty resource with the given URI
		Resource load_resource = resourceSetJson.getResource(URI
				.createURI(mainFolderName + fileSeparatorForResources + tempFolderName + fileSeparatorForResources + openAPIXMIFileName), true);
	    EObject openapiObject = (EObject) load_resource.getContents().get(0);
	    
	    if(openapiObject != null) {
	    	try {
	    		resourceJson.getContents().add(openapiObject);
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    try {
	    	resourceJson.save(null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		ArrayList<HashMap<String,String>> fullParametersList = new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String,String>> parametersList = new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String,String>> fullPropertiesList = new ArrayList<HashMap<String,String>>();

		HashMap<String, String> apiHashMap = new HashMap<>();
		apiHashMap.put("fileName", fileName);

		HashMap<String, String> examplesHashMap = new HashMap<>();
		
		String jsonString = "", jsonStringFormatted = "", swaggerString = "", swaggerStringFormatted = "";
		
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(
            		FileUtils.readFileToString(new File(mainFolderName + File.separator + tempFolderName + File.separator + openAPIXMIFileName)));
            
            xmlJSONObj = xmlJSONObj.getJSONObject("openapi:API");
            xmlJSONObj.remove("openapi:API");
            xmlJSONObj.remove("xmi:version");
            xmlJSONObj.remove("xmlns:xmi");
            xmlJSONObj.remove("xmlns:openapi");
            
            //TODO: api description
            //xmlJSONObj = xmlJSONObj.getJSONObject("info").put("description", apiDescription);
            
            JSONArray pathsArray = xmlJSONObj.getJSONArray("paths");
            xmlJSONObj.remove("paths");

        	if (xmlJSONObj.get("servers") instanceof JSONObject) {
            	JSONObject servers = xmlJSONObj.getJSONObject("servers");
                xmlJSONObj.remove("servers");
                JSONArray serversArray = new JSONArray();
                serversArray.put(servers);
                xmlJSONObj.put("servers", serversArray);
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
             	
            	parametersList.clear();
            	try {
	        		HashMap<String, String> parameterHashMap = new HashMap<>();
	        		parameterHashMap.put("name", path);
	        		
	        		String example = "";
	        		try {
	        			example = jsonobj2.get("example") + "";
	        		} catch(Exception e) {
	            		System.out.println(e.getMessage());
	            	}
	        		
	        		parameterHashMap.put("example", example);

	        		examplesHashMap.put(path, example);
	        		
	        		String type = "";
	        		try {
	        			type = jsonobj2.getString("type");
	        		} catch(Exception e) {
	            		System.out.println(e.getMessage());
	            	}
	        		parameterHashMap.put("type", type);
	        		parameterHashMap.put("ptype", "property");
	        		parametersList.add(parameterHashMap);
	        		fullPropertiesList.add(parameterHashMap);
	
	               
            	} catch(Exception e) {
            		System.out.println(e.getMessage());
            	}
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
            	
            	JSONArray parametersArray = xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONArray("parameters");

                fullParametersList.clear();
            	for (int parameterIterator = 0; parameterIterator < parametersArray.length (); parameterIterator++) {
            	   
            	   parametersList.clear();
            	   
	               	try {
	               		String paramName = parametersArray.getJSONObject(parameterIterator).getString("name");
	   	        		HashMap<String, String> parameterHashMap = new HashMap<>();
	   	        		parameterHashMap.put("name", paramName);
	   	        		
	   	        		String example = "";
	   	        		
	   	        		if(!paramName.contentEquals("limit") && !paramName.contentEquals("offset") && !paramName.contentEquals("visualisation")) {

		   	        		try {
			   	        		example = examplesHashMap.get(paramName);
			   	        		if(example != null) {
				   	        		parameterHashMap.put("example", example);
			   	        		} else {
			   	        			parameterHashMap.put("example", "");
			   	        		}
		   	        		} catch(Exception e) {
		   	            		System.out.println(e.getMessage());
		   	        		}
		   	        		
		   	        		String type = "";
		   	        		try {
		   	        			type = parametersArray.getJSONObject(parameterIterator).getJSONObject("schema").getString("type");
		   	        		} catch(Exception e) {
		   	            		System.out.println(e.getMessage());
		   	            	}
		   	        		parameterHashMap.put("type", type);
		   	        		
		   	        		String in = "";
		   	        		try {
		   	        			in = parametersArray.getJSONObject(parameterIterator).getString("in");
		   	        		} catch(Exception e) {
		   	            		System.out.println(e.getMessage());
		   	            	}
		   	        		parameterHashMap.put("in", in);
		   	        		
		   	        		boolean required = false;
		   	        		try {
		   	        			required = parametersArray.getJSONObject(parameterIterator).getBoolean("required");
		   	        		} catch(Exception e) {
		   	            		System.out.println(e.getMessage());
		   	            		required = false;
		   	            	}
		   	        		parameterHashMap.put("required", String.valueOf(required));
		   	        		
		   	        		parameterHashMap.put("ptype", "parameter");
		   	        		parametersList.add(parameterHashMap);
		   	        		fullParametersList.add(parameterHashMap);

		   	        		if(!path.contentEquals("/")) {
		   	        			xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONArray("parameters")
		   	             			.getJSONObject(parameterIterator).put("example", example);
		   	        		}
		   	                
	   	        		}
	   	        		else {
	   	        			int exampleInt = parametersArray.getJSONObject(parameterIterator).getInt("example");
		   	                xmlJSONObj.getJSONObject("paths").getJSONObject(path).getJSONObject("get").getJSONArray("parameters")
		   	             		.getJSONObject(parameterIterator).put("example", exampleInt + "");
	   	        		}
	               	} catch(Exception e) {
	               		System.out.println(e.getMessage());
	               	}

            	}

            	String methodName = "";
            	try{
            		methodName = path.split("/")[1];
            	} catch(Exception e) {
            		methodName = "/";
            	}
        		apiHashMap.put("methodName", methodName);
        		
                fullParametersList.clear();
            	
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
            
            jsonString = xmlJSONObj.toString();
            ObjectMapper jsonFormatter = new ObjectMapper();
            Object json = jsonFormatter.readValue(jsonString, Object.class);
            jsonStringFormatted = jsonFormatter.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            
            JSONObject xmlSwaggerObj = xmlJSONObj;
            xmlSwaggerObj.remove("openapi");
            xmlSwaggerObj.put("swagger", "2.0");
            String urlServer = xmlSwaggerObj.getJSONArray("servers").getJSONObject(0).getString("url");
            String definitionScheme = ((urlServer.contains("https") ? "https" : "http"));
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

    	            	try {
	    	            	if(!parameters.getJSONObject(j).getString("example").isEmpty()) {
		            			parameters.getJSONObject(j).put("default", parameters.getJSONObject(j).getString("example"));
		    	            	parameters.getJSONObject(j).remove("example");
	    	            	}
    	            	} catch (JSONException e) {
    	        			System.out.println(e.getMessage());
    	            	}
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
            definitionsSchemes.put(definitionScheme);
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
				if(lineFunctionName.contains("getvisualisation")) {
					servercode += lineFunctionName + "\t" + "var obj = new Object();obj.value = \"visualisation\";args.visualisation = obj;\n"
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

	private static void generateVisualisation() {
	
		
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
		
		String visualisationCode = "";
		BufferedReader brVisualisation = null;
	    String lineVisualisation = "";
		try {
			brVisualisation = new BufferedReader(new InputStreamReader
					(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
		            		+ fileSeparatorForResources + "visualisation" 
		            		+ fileSeparatorForResources + visualisationChartJS)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while ((lineVisualisation = brVisualisation.readLine()) != null) {
				//TODO: arreglar pie chart (con botones como en Charts de iOS)
				if(lineVisualisation.contains("datasetsLineChart") || lineVisualisation.contains("datasetsBarChart")) {
					
					String datasets = "";
					
					if(lineVisualisation.contains("datasetsLineChart")) {
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
						
						visualisationCode += lineVisualisation.replace("datasetsLineChart", datasets) + "\n";
					}
					else if(lineVisualisation.contains("datasetsBarChart")) {
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
						
						visualisationCode += lineVisualisation.replace("datasetsBarChart", datasets) + "\n";
					}
				} 
				else if(lineVisualisation.contains("configPie0")) {
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
					visualisationCode += lineVisualisation.replace("var configPie0;", pies) + "\n";
				}
				else {
					visualisationCode += lineVisualisation + "\n";
				}
			}
			System.out.println("Visualisation.html generated");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
	    	brVisualisation.close();
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
					"controllers" + File.separator + visualisationChartJS, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.println(visualisationCode);
			writer.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println("Visualisation.html saved");
		
		/*try {
	        Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
	        		+ fileSeparatorForResources + "visualisation" 
	        		+ fileSeparatorForResources + visualisationMainFolderName + ".zip"), 
	        		new File(mainFolderName + File.separator + visualisationZipFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
			System.out.println(e.getMessage());
	    }
		
		try {
			FileUtils.deleteDirectory(new File(mainFolderName + File.separator + visualisationMainFolderName));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			ZipFile zipFile = new ZipFile(mainFolderName + File.separator + visualisationZipFileName);
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
		
		File file = new File(mainFolderName + File.separator + visualisationZipFileName);
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
		String swiftFilePath = mainFolderName + File.separator + visualisationMainFolderName + File.separator + visualisationProjectName 
				+ File.separator + visualisationProjectName + File.separator + visualisationFolderName 
				+ File.separator + visualisationSwiftFileName;
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
		    System.err.println("Visualisation created");
		    //System.out.println(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String swiftFilePath2 = mainFolderName + File.separator + visualisationMainFolderName + File.separator + visualisationProjectName 
				+ File.separator + visualisationProjectName + File.separator + visualisationFolderName 
				+ File.separator + visualisationSwiftFileName2;
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
		    System.err.println("Visualisation created");
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
			String swiftFilePath3 = mainFolderName + File.separator + visualisationMainFolderName + File.separator + visualisationProjectName 
					+ File.separator + visualisationProjectName + File.separator + visualisationFolderName 
					+ File.separator + visualisationSwiftFileName3Tab;
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
			    System.err.println("Visualisation created");
			    //System.out.println(content);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		// Build and open visualisation generated
		System.out.println("Waiting for building and opening visualisation...");
		try {
	        Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
	        		+ fileSeparatorForResources + "visualisation" 
	        		+ fileSeparatorForResources + "buildVisualisation"), 
	        		new File(mainFolderName + File.separator + "buildVisualisation").toPath(), StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
			System.out.println(e.getMessage());
	    }
	
		try {
	        Files.copy(AG.class.getResourceAsStream(fileSeparatorForResources + resFolderName 
	        		+ fileSeparatorForResources + "visualisation" 
	        		+ fileSeparatorForResources + "openVisualisation"), 
	        		new File(mainFolderName + File.separator + "openVisualisation").toPath(), StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
			System.out.println(e.getMessage());
	    }
		
		File execFile1 = new File(mainFolderName + File.separator + "buildVisualisation");
		execFile1.setExecutable(true);
		File execFile2 = new File(mainFolderName + File.separator + "openVisualisation");
		execFile2.setExecutable(true);
	    
	    try {
	    	if(System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
	    		System.out.println("The build visualisation process is only for MacOS");
	    	} else {
	    		Process p = new ProcessBuilder("./" + mainFolderName + File.separator + "buildVisualisation", "").start();
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
	    		System.out.println("The visualisation is built only for MacOS");
	    	} else {
		        Process p = new ProcessBuilder("./" + mainFolderName + File.separator +  "openVisualisation", "").start();
	    	}
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }*/
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
	
}

/*class CharsetDetector {

    public Charset detectCharset(File f, String[] charsets) {

        Charset charset = null;

        for (String charsetName : charsets) {
        	try {
        		charset = detectCharset(f, Charset.forName(charsetName));
        	} catch(Exception e) {
        		
        	}
            if (charset != null) {
                break;
            }
        }

        return charset;
    }

    private Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
            	System.out.println("Charset: " + charset.displayName());
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        File f = new File("example.txt");

        String[] charsetsToBeTested = {"UTF-8", "windows-1253", "ISO-8859-7"};

        CharsetDetector cd = new CharsetDetector();
        Charset charset = cd.detectCharset(f, charsetsToBeTested);

        if (charset != null) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(f), charset);
                int c = 0;
                while ((c = reader.read()) != -1) {
                    System.out.print((char)c);
                }
                reader.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }

        }else{
            System.out.println("Unrecognised charset.");
        }
    }
}*/

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
