package AG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import io.swagger.codegen.SwaggerCodegen;
import io.swagger.codegen.languages.SwaggerGenerator;
import io.swagger.models.Swagger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AG {

	// Variables
	public static String fileName = "data";
	public static String fileType = "csv";
	public static String alternativeFileType = "xml";
	public static String host = "virtserver.swaggerhub.com";
	public static String basePath = "/cgmora12/lifts/1.0.0";
	public static String swaggerFileName = "swagger.json";
	public static String swaggerCodegeneFileName = "swagger-codegen-cli-2.2.1.jar";
	public static String apiCodeFolderName = "apiCode";
	public static String serverCodeFileName = "servercode.js";
	public static String resFolderName = "/AG/";
	
	public static void main(String[] args) {
		
		if(args.length == 1) {
			fileName = args[0];
		}
		else if(args.length == 3) {
			fileName = args[0];
			host = args[1];
			basePath = args[2];
		}
		
		//Automatic API Generation process

		String csvFile = fileName + "." + fileType;
		File f = new File(csvFile);
		if(!(f.exists() && !f.isDirectory())) { 
		    convertDataFileIntoCSV();
		}
		if(f.exists() && !f.isDirectory()) { 
			generateApiDefinition();
	        generateServer();
	        addServerDependencies();
	        generateApiCode();
	        System.out.println("Automatic API Generation finished!");
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


		    for(int i = 0; i < document.getElementsByTagName("rdf:Description").getLength(); i++) {
		    	Node n = document.getElementsByTagName("rdf:Description").item(i);
		    	System.out.println(n.getAttributes().getNamedItem("rdf:about").getNodeValue());
		    	Element e = (Element)n;
		    	System.out.println(e.getElementsByTagName("foaf:name").item(0).getFirstChild().getNodeValue());
		    	System.out.println("hasLift " + (e.getElementsByTagName("mao:hasLift").item(0).hasChildNodes() ? e.getElementsByTagName("mao:hasLift").item(0).getFirstChild().getNodeValue() : "NO"));

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
                columns = line.replaceAll(" ", "_").replaceAll("\"", "").replaceAll("\'", "").toLowerCase().split(cvsSplitBy);

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
            
            // Generate API definition and doc
            apiDefinition += "{ \"swagger\" : \"2.0\", \"info\" : { \"version\" : \"1.0.0\", \"title\" : \"" + fileName + "\", \"description\" : \"Obtaining the " + fileNameNoWhiteSpaces  + "\" }, \"host\" : \"" + host + "\", \"basePath\" : \"" + basePath + "\",";
            apiDefinition += "\"paths\" : { \"/\" : { \"get\" : { \"summary\" : \"GET " + fileName + "\", \"operationId\" : \"get" + fileNameNoWhiteSpaces + "\", \"produces\" : [ \"application/json\" ],";
            apiDefinition += "\"parameters\" : [ ";
            		
            for(int i = 0; i < columns.length; i++) {
            	if(i > 0) {
            		apiDefinition += ",";
            	}
            	apiDefinition += "{ \"in\" : \"query\", \"name\" : \"" + columns[i] + "\", \"type\" : \"" + dataTypes[i] + "\", \"description\" : \"" + columnsDescriptions[i] + "\" }";
                if(i == columns.length-1) {
                	apiDefinition += " ],";
                }
            }
            
            apiDefinition += "\"responses\" : { \"200\" : { \"description\" : \"successful operation\", \"schema\" : { \"type\" : \"array\", \"items\" : { \"$ref\" : \"#/definitions/COLUMNS\" } } } } } } },";
            
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
            new File(apiCodeFolderName).mkdirs();
            PrintWriter writer = new PrintWriter(apiCodeFolderName + "/" + swaggerFileName, "UTF-8");
            writer.println(apiDefinition);
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

		String servercode = "";
		
		BufferedReader br = null;
		String line = "";
        String lineFunctionName = "";
		try {
			br = new BufferedReader(new FileReader(apiCodeFolderName + "/controllers/DefaultService.js"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while ((line = br.readLine()) != null) {
				if(line.contains("function(args, res, next) {")) {
					lineFunctionName = line + "\n";
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
				if(lineServer.contains("function(args, res, next) {")) {
					servercode += lineFunctionName + "\n";
				}
				else if(lineServer.contains("var fileName =")) {
					servercode += lineServer.replace("./filename.csv", "./" + fileName + "." + fileType) + "\n";
				} 
				else {
					servercode += lineServer + "\n";
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
		
	}
	
}
