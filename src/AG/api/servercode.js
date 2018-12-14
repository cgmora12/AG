'use strict';
var fs = require('fs');
var papa = require('papaparse');

exports.getOperation = function(args, res, next) {
  
  var examples = {};
  var fileName = "./data.csv";
  var result;
  var resultVisualization;
  var jsonResult = new Object();
  var rowNumber = 0;
  var limit = 100;
  var offset = 0;
  var labels = [];
  var datasets = [];

  if (fs.existsSync(fileName)) {
    console.log('file exists');

    fs.readFile(fileName, {encoding: 'utf-8'}, function(err,data){
      if (!err) {
        jsonResult.results = [];

        // Checking arguments

        var argsUndefined = true;
        var filters = false;
        var visualization = false;

        for(var i = 0; i < Object.keys(args).length; i++){
          if (Object.keys(args)[i] === "limit" || Object.keys(args)[i] === "offset"|| Object.keys(args)[i] === "visualization"){
            if (Object.keys(args)[i] === "limit" && args[Object.keys(args)[i]].value != undefined){
              limit = parseInt(args[Object.keys(args)[i]].value);
            } 
            else if (Object.keys(args)[i] === "offset" && args[Object.keys(args)[i]].value != undefined){
              offset = parseInt(args[Object.keys(args)[i]].value);
            }
            else if (Object.keys(args)[i] === "visualization" && args[Object.keys(args)[i]].value != undefined){
              visualization = true;
            }
          }
          else if(args[Object.keys(args)[i]].value != undefined){
            argsUndefined = false;
          }
        }
        
        // From csv to json
        papa.parse(data, 
          { 
            header: true,
            step: function(row) {
              result = row.data[0];

              // Check columns types for visualization
              if(visualization){
	              var datasetsInserted = 0;
	              for(var columnIt = 0; columnIt < Object.keys(result).length; columnIt++){
	              	console.log(columnIt);
	              	console.log(parseInt(result[Object.keys(result)[columnIt]]));
	              	console.log(result[Object.keys(result)[columnIt]]);
	              	if(parseInt(result[Object.keys(result)[columnIt]]) == result[Object.keys(result)[columnIt]]) {
	              		console.log("data is an integer");
					    // data is an integer
					    datasetsInserted +=1;
					    if(datasets.length < datasetsInserted){
					    	var dataset = [];
					    	dataset.push("'" + result[Object.keys(result)[columnIt]] + "'");
					    	datasets.push(dataset);
					    } else {
					    	datasets[datasetsInserted - 1].push("'" +result[Object.keys(result)[columnIt]] + "'");
					    }
					}
	              }
              }

              // Filtering results
              if(rowNumber < limit + offset && rowNumber >= offset) {
                
                if(argsUndefined){
                  jsonResult.results = jsonResult.results.concat(result);
                  labels.push("'" + result[Object.keys(result)[0]] + "'");
                }
                else {
                  for(var j = 0; j < Object.keys(args).length; j++){
                    if(args[Object.keys(args)[j]].value != undefined){
                      if(Object.keys(args)[j] === Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]){
                        if(result[Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]] === args[Object.keys(args)[j]].value + ""){
                          jsonResult.results = jsonResult.results.concat(result);
                		  labels.push("'" + result[Object.keys(result)[0]] + "'");
                        }
                        else if(args[Object.keys(args)[j]].value + "" === "all" && result[Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]].replace(/\s+/g, '') != ""){
                          jsonResult.results = jsonResult.results.concat(result);
                		  labels.push("'" + result[Object.keys(result)[0]] + "'");
                        }
                      }
                    } 
                  }
                }
              }

              rowNumber++;
            },
            complete: function(){
              console.log('ReturnJSONFile');
              //result = result.data;
              try{
                // Creating json object
                //result = "{ \"results\": " + JSON.stringify(result) + " }";

                console.log("Completed!");
                examples['application/json'] = jsonResult;
              } catch (err) {
                  console.log(err);
              }

              if(Object.keys(examples).length > 0) {
                if(visualization){
                  res.setHeader('Content-Type' , 'text/html');
                  var visualizationHtmlFile = readTextFile("./Controllers/visualization.html");
                  visualizationHtmlFile = visualizationHtmlFile.replace("labelsLineChart", labels.join());
                  visualizationHtmlFile = visualizationHtmlFile.replace("labelsBarChart", labels.join());
                  visualizationHtmlFile = visualizationHtmlFile.replace("labelsPieChart", labels.join());

                  console.log(datasets.toString());

                  var iteratorLineChart = 0;
                  while(visualizationHtmlFile.indexOf("dataLineChart") >= 0) {
                  	visualizationHtmlFile = visualizationHtmlFile.replace("dataLineChart", datasets[iteratorLineChart].join());
                  	iteratorLineChart++;
                  }
                  var iteratorBarChart = 0;
                  while(visualizationHtmlFile.indexOf("dataBarChart") >= 0) {
                  	visualizationHtmlFile = visualizationHtmlFile.replace("dataBarChart", datasets[iteratorBarChart].join());
                  	iteratorBarChart++;
                  }
                  var iteratorPieChart = 0;
                  while(visualizationHtmlFile.indexOf("dataPieChart") >= 0) {
                  	visualizationHtmlFile = visualizationHtmlFile.replace("dataPieChart", datasets[iteratorPieChart].join());
                  	iteratorPieChart++;
                  }
                  writeTextFile("./Controllers/visualizationGenerated.html", visualizationHtmlFile);
                  res.write(visualizationHtmlFile);
                  res.end();
                } else {
                  res.setHeader('Content-Type', 'application/json');
                  res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
                }
              }
              else {
                res.end();
              }
            },
            error: function(err){
              console.log(err);
              res.end();
            }       
          }
        );
      } else {
          console.log(err);
      }
    });
  } 
  else {
        res.end();
  }
}

function readTextFile(file)
{
  var fs = require('fs');
 
  try {  
    var data = fs.readFileSync(file, 'utf8');
    return(data.toString());    
  } catch(e) {
    console.log('Error:', e.stack);
  }
  return("error reading file");
}
function writeTextFile(file, content)
{
  var fs = require('fs');
 
  try {  
    fs.writeFileSync(file, content, 'utf8'); 
  } catch(e) {
    console.log('Error:', e.stack);
  }
}


