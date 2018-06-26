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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import io.swagger.codegen.SwaggerCodegen;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class AG {

	// Variables
	public static String fileName = "data";
	public static String fileType = "csv";
	public static String alternativeFileType = "";
	public static String host = "server.com";
	public static String basePath = "/resource";
	public static String swaggerFileName = "swagger.json";
	public static String openAPIFileName = "openapi.json";
	public static String swaggerCodegeneFileName = "swagger-codegen-cli-2.2.1.jar";
	public static String apiCodeFolderName = "apiCode";
	public static String serverCodeFileName = "servercode.js";
	public static String resFolderName = "/AG/";
	
	public static void main(String[] args) {
		
		// TODO: Swagger 2.0 to OpenAPI
		
		//args: ficheroDatos
		if(args.length == 1) {
			fileName = args[0];
		}
		//args: ficheroDatos xml
		else if(args.length == 2) {
			fileName = args[0];
			alternativeFileType = args[1];
		}
		//args: ficheroDatos metro.com /madrid
		else if(args.length == 3) {
			fileName = args[0];
			host = args[1];
			basePath = args[2];
		}
		//args: ficheroDatos xml metro.com /madrid
		else if(args.length == 4) {
			fileName = args[0];
			alternativeFileType = args[1];
			host = args[2];
			basePath = args[3];
		}
		
		//Automatic API Generation process

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

	
	// TODO: Specific RDF (XML) dataset
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

	@Override
	public String toString() {
		return "AG [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
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
            
            File source = new File(csvFile);
            File dest = new File(apiCodeFolderName + "/" + csvFile);
            try {
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
               
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

		String[] args = new String [7];
		args[0] = "generate";
		args[1] = "--lang";
		args[2] = "nodejs-server";
		args[3] = "--input-spec";
		args[4] = apiCodeFolderName + "/" + swaggerFileName;
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
					servercode += lineServer.replace("./filename.csv", "./" + fileName + "." + fileType) + "\n";
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
