//
//  LineDemoViewController.swift
//  ChartsDemo-OSX
//
//  Copyright 2015 Daniel Cohen Gindi & Philipp Jahoda
//  A port of MPAndroidChart for iOS
//  Licensed under Apache License 2.0
//
//  https://github.com/danielgindi/ios-charts

import Foundation
import Cocoa
import Charts

open class LineDemoViewController: NSViewController
{
    @IBOutlet var lineChartView: LineChartView!
    
    override open func viewDidLoad()
    {
        super.viewDidLoad()
        
        let url : String = "http://localhost:8080/v1/"
        var request = URLRequest(url: URL(string: url)!)
        request.httpMethod = "GET"
        
        let session = URLSession.shared
        let task = session.dataTask(with: request, completionHandler: { data, response, error -> Void in
            //print(response ?? "Empty response")
            do {
                if(response != nil) {
                    let json = try JSONSerialization.jsonObject(with: data!) as! Dictionary<String, AnyObject>
                    let results = json["results"] as? [[String: Any]]
                    if !(results?.isEmpty)! {
                        if let jsonResults = json["results"] as? [Any] {
                            var ids = Array<String>()
                            let ids_name = "stop_id"
                            var column_values = [Int]()
                            var column_array = [[Int]]()
                            let column_names = ["stop_code", "location_type", "wheelchair_boarding"]
                            
                            let data = LineChartData()
                            var column_values_min = 0
                            var column_values_max = 0
                            var column_values_min_aux = 0
                            var column_values_max_aux = 0
                            
                            for result in jsonResults  as! [[String : Any]] {
                                if result[ids_name] != nil {
                                    ids.append(result[ids_name] as! String)
                                }
                            }
                            for column in column_names {
                                column_values.removeAll()
                                for result in jsonResults  as! [[String : Any]] {
                                    if result[column] != nil {
                                        column_values.append(Int((result[column] as! NSString).intValue))
                                    }
                                }
                                if(column_values.count > 0){
                                    column_values_min_aux = column_values.min()!
                                    column_values_max_aux = column_values.max()!
                                    if(column_values_min_aux < column_values_min) {
                                        column_values_min = column_values_min_aux
                                    }
                                    if(column_values_max_aux > column_values_max) {
                                        column_values_max = column_values_max_aux
                                    }
                                    column_array.append(column_values)
                                }
                            }
                            
                            var i = 0
                            while i < column_array.count {
                                let yse : [ChartDataEntry]! = column_array[i].enumerated().map { (arg) -> ChartDataEntry in let (x, y) = arg; return ChartDataEntry(x: Double(x), y: Double(y)) }
                                let ds = LineChartDataSet(values: yse, label: column_names[i])
                                ds.setColor(NSUIColor(calibratedRed: CGFloat((i * 150) % 256) / 256, green: CGFloat((i * 100) % 256) / 256, blue: CGFloat((i * 50) % 256) / 256, alpha: 1.0))
                                data.addDataSet(ds)
                                i += 1
                            }
                            
                            self.lineChartView.xAxis.valueFormatter = IndexAxisValueFormatter(values: ids)
                            self.lineChartView.data = data
                            
                            self.lineChartView.xAxis.setLabelCount(ids.count, force: true)
                            self.lineChartView.xAxis.granularityEnabled = true
                            self.lineChartView.xAxis.drawLabelsEnabled = true
                            self.lineChartView.xAxis.granularity = 1
                            
                            self.lineChartView.xAxis.labelCount = ids.count
                            
                            self.lineChartView.pinchZoomEnabled = true
                            self.lineChartView.scaleYEnabled = true
                            self.lineChartView.scaleXEnabled = true
                            self.lineChartView.highlighter = nil
                            self.lineChartView.doubleTapToZoomEnabled = true
                            
                            let formatter = NumberFormatter()
                            formatter.numberStyle = .none
                            formatter.maximumFractionDigits = 0
                            formatter.multiplier = 1.0
                            self.lineChartView.data?.setValueFormatter(DefaultValueFormatter(formatter: formatter))
                            self.lineChartView.leftYAxisRenderer.axis?.axisMinimum = Double(column_values_min)
                            self.lineChartView.leftYAxisRenderer.axis?.axisMaximum = Double(column_values_max) + Double(column_values_max) * 0.1
                            //self.barChartView.xAxis.valueFormatter = DefaultAxisValueFormatter(formatter: formatter)
                            
                            self.lineChartView.xAxis.labelPosition = .bottom
                            //self.lineChartView.xAxis.centerAxisLabelsEnabled = true
                            self.lineChartView.xAxis.drawGridLinesEnabled = false
                            self.lineChartView.chartDescription?.enabled = false
                            self.lineChartView.legend.enabled = true
                            self.lineChartView.rightAxis.enabled = false
                            self.lineChartView.chartDescription?.text = "Linechart"
                        }
                    }
                    
                }
            } catch {
                print("error")
            }
        })
        
        task.resume()
        
        // Do any additional setup after loading the view.
        /*let ys1 = Array(1..<10).map { x in return sin(Double(x) / 2.0 / 3.141 * 1.5) }
         let ys2 = Array(1..<10).map { x in return cos(Double(x) / 2.0 / 3.141) }
         
         let yse1 = ys1.enumerated().map { x, y in return ChartDataEntry(x: Double(x), y: y) }
         let yse2 = ys2.enumerated().map { x, y in return ChartDataEntry(x: Double(x), y: y) }
         
         let data = LineChartData()
         let ds1 = LineChartDataSet(values: yse1, label: "Hello")
         ds1.colors = [NSUIColor.red]
         data.addDataSet(ds1)
         
         let ds2 = LineChartDataSet(values: yse2, label: "World")
         ds2.colors = [NSUIColor.blue]
         data.addDataSet(ds2)
         self.lineChartView.data = data
         
         self.lineChartView.gridBackgroundColor = NSUIColor.white
         
         self.lineChartView.chartDescription?.text = "Linechart Demo"*/
    }
    
    override open func viewWillAppear()
    {
        self.lineChartView.animate(xAxisDuration: 0.0, yAxisDuration: 1.0)
    }
}
