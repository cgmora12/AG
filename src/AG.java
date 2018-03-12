import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AG {

	public static void main(String[] args) {
		
		String fileName = "London Underground Lifts";
		String fileType = "csv";
		String host = "virtserver.swaggerhub.com";
		String basePath = "/cgmora12/lifts/1.0.0";
		
		String csvFile = System.getProperty("user.dir") + "\\" + fileName + "." + fileType;

		String apiDefinition = "";
		String fileNameNoWhiteSpaces = fileName.replaceAll(" ", "");
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] columns = null;
        String[] columnsDescriptions = null;
        String[] exampleData = null;
        String[] dataTypes = null;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            if ((line = br.readLine()) != null) {
                // use comma as separator
                columnsDescriptions = line.split(cvsSplitBy);
                columns = line.replaceAll(" ", "_").toLowerCase().split(cvsSplitBy);

            }
            if ((line = br.readLine()) != null) {
                exampleData = line.split(cvsSplitBy);
                dataTypes = new String[exampleData.length];
                for(int i = 0; i < exampleData.length; i++) {
                	try {
                		int integerValue = Integer.parseInt(exampleData[i]);
                		dataTypes[i] = "integer";
                	} catch (NumberFormatException e) {
                		dataTypes[i] = "string";
                	}
                }

            }
            
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
            
            System.out.println(apiDefinition);
            PrintWriter writer = new PrintWriter("swagger.json", "UTF-8");
            writer.println(apiDefinition);
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
	
}
