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
  var allDatasets = [];

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


              // Filtering results
              if(rowNumber < limit + offset && rowNumber >= offset) {
                
                if(argsUndefined){
                  jsonResult.results = jsonResult.results.concat(result);
                  if(visualization){
                    labels.push("'" + result[Object.keys(result)[0]] + "'");
                    datasets = resultToDataset(visualization, result, datasets);
                    allDatasets = resultToAllDataset(visualization, result, allDatasets);
                  }
                }
                else {
                  for(var j = 0; j < Object.keys(args).length; j++){
                    if(args[Object.keys(args)[j]].value != undefined){
                      if(Object.keys(args)[j] === Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]){
                        if(result[Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]] === args[Object.keys(args)[j]].value + ""){
                          jsonResult.results = jsonResult.results.concat(result);
                          if(visualization){
                            labels.push("'" + result[Object.keys(result)[0]] + "'");
                            datasets = resultToDataset(visualization, result, datasets);
                            allDatasets = resultToAllDataset(visualization, result, allDatasets);
                          }
                        }
                        else if(args[Object.keys(args)[j]].value + "" === "all" && result[Object.keys(result)[Object.keys(result).indexOf(Object.keys(args)[j])]].replace(/\s+/g, '') != ""){
                          jsonResult.results = jsonResult.results.concat(result);
                          if(visualization){
                            labels.push("'" + result[Object.keys(result)[0]] + "'");
                            datasets = resultToDataset(visualization, result, datasets);
                            allDatasets = resultToAllDataset(visualization, result, allDatasets);
                          }
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
                  if (typeof labels !== 'undefined' && labels !== null && labels.length > 0){
                    visualizationHtmlFile = visualizationHtmlFile.replace("labelsLineChart", labels.join());
                    visualizationHtmlFile = visualizationHtmlFile.replace("labelsBarChart", labels.join());
                  }

                  //console.log(datasets.toString());

                  if (typeof datasets !== 'undefined' && datasets !== null && datasets.length > 0){

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

                    if (typeof allDatasets !== 'undefined' && allDatasets !== null && allDatasets.length > 0){
                      var iteratorPieChart = 0;
                      while(visualizationHtmlFile.indexOf("dataPieChart") >= 0) {
                        var differentValues = 0;
                        let unique = [...new Set(allDatasets[iteratorPieChart])]; 
                        differentValues = unique.length

                      	if(differentValues <= 6 && differentValues > 0) {
                          var arr = allDatasets[iteratorPieChart];
                          var result = pieAux(arr);

                          visualizationHtmlFile = visualizationHtmlFile.replace("dataPieChart", result[1].join());
                          visualizationHtmlFile = visualizationHtmlFile.replace("labelsPieChart", result[0].join());
                        }else {
                          visualizationHtmlFile = visualizationHtmlFile.replace("dataPieChart", "'1'");
                          visualizationHtmlFile = visualizationHtmlFile.replace("labelsPieChart", "'data not classifiable'");
                        }
                        iteratorPieChart++;
                      }
                    }

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

function pieAux(arr) {
    var a = [], b = [], prev;

    arr.sort();
    for ( var i = 0; i < arr.length; i++ ) {
        if ( arr[i] !== prev ) {
            a.push(arr[i]);
            b.push(1);
        } else {
            b[b.length-1]++;
        }
        prev = arr[i];
    }

    return [a, b];
}

function resultToDataset(visualization, result, datasets)
{
  // Check columns types for visualization
  var datasetsInserted = 0;
  for(var columnIt = 0; columnIt < Object.keys(result).length; columnIt++){
    if(parseInt(result[Object.keys(result)[columnIt]]) == result[Object.keys(result)[columnIt]]) {
      //console.log("data is an integer");
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

  return datasets;
}

function resultToAllDataset(visualization, result, allDatasets)
{
  // Check columns types for visualization
  var datasetsInserted = 0;
  for(var columnIt = 0; columnIt < Object.keys(result).length; columnIt++){
      datasetsInserted +=1;
      if(allDatasets.length < datasetsInserted){
        var dataset = [];
        dataset.push("'" + result[Object.keys(result)[columnIt]] + "'");
        allDatasets.push(dataset);
      } else {
        allDatasets[datasetsInserted - 1].push("'" +result[Object.keys(result)[columnIt]] + "'");
      }
  }

  return allDatasets;
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


