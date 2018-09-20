//
//  BarDemoViewController.swift
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

open class PruebaBarDemoViewController: NSViewController
{
    @IBOutlet var barChartView: BarChartView!
    
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
                            var id_int = false;
                            var column_values = [Int]()
                            var column_array = [[Int]]()
                            let column_names = ["stop_code", "location_type", "wheelchair_boarding"]
                            
                            let data = BarChartData()
                            var column_values_min = 0
                            var column_values_max = 0
                            var column_values_min_aux = 0
                            var column_values_max_aux = 0
                            
                            for result in jsonResults  as! [[String : Any]] {
                                ids.append(result[ids_name] as! String)
                                if type(of: result[ids_name]!) == type(of: 0) {
                                    print(type(of: result[ids_name]!))
                                    id_int = true
                                }
                            }
                            for column in column_names {
                                column_values.removeAll()
                                for result in jsonResults  as! [[String : Any]] {
                                    column_values.append(Int((result[column] as! NSString).intValue))
                                }
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
                            
                            var i = 0
                            while i < column_array.count {
                                var yse : [BarChartDataEntry]! = nil
                                if id_int {
                                    yse = column_array[i].enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(ids[x])!, y: Double(y)) }
                                } else {
                                    yse = column_array[i].enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(x), y: Double(y)) }
                                }
                                let ds = BarChartDataSet(values: yse, label: column_names[i])
                                ds.setColor(NSUIColor(calibratedRed: CGFloat(arc4random() % 256 ) / 256, green: CGFloat(arc4random() % 256 ) / 256, blue: CGFloat(arc4random() % 256 ) / 256, alpha: 1.0))
                                data.addDataSet(ds)
                                i += 1
                            }
                            
                            let barWidth = 0.4
                            let barSpace = 0.05
                            let groupSpace = 0.1
                            
                            data.barWidth = barWidth
                            
                            if id_int {
                                self.barChartView.xAxis.axisMinimum = Double(Int(ids.min() ?? "0") ?? 0)
                                self.barChartView.xAxis.axisMaximum = Double(Int(ids.min() ?? "0") ?? 0) + Double(Int(ids.max()!)!) * 0.1
                                data.groupBars(fromX: Double(Int(ids.min() ?? "0") ?? 0), groupSpace: groupSpace, barSpace: barSpace)
                            } else {
                                /*self.barChartView.xAxis.axisMinimum = Double(Int(stops_ids.min() ?? "0") ?? 0)
                                self.barChartView.xAxis.axisMaximum = Double(Int(stops_ids.min() ?? "0") ?? 0) + data.groupWidth(groupSpace: groupSpace, barSpace: barSpace) * Double(stops_ids.count)
                                 // (0.4 + 0.05) * 2 (data set count) + 0.1 = 1
                                data.groupBars(fromX: Double(Int(stops_ids.min() ?? "0") ?? 0), groupSpace: groupSpace, barSpace: barSpace)*/
                                self.barChartView.xAxis.valueFormatter = DefaultAxisValueFormatter { (value, axis) -> String in return ids[Int(value)] }
                            }
                            self.barChartView.data = data
                            
                            //self.barChartView.gridBackgroundColor = NSUIColor.white
                            
                            self.barChartView.xAxis.setLabelCount(ids.count, force: true)
                            self.barChartView.xAxis.granularityEnabled = true
                            self.barChartView.xAxis.drawLabelsEnabled = true
                            self.barChartView.xAxis.granularity = 1.0
                            self.barChartView.xAxis.decimals = 0
                            
                            let formatter = NumberFormatter()
                            formatter.numberStyle = .none
                            formatter.maximumFractionDigits = 0
                            formatter.multiplier = 1.0
                            self.barChartView.data?.setValueFormatter(DefaultValueFormatter(formatter: formatter))
                            self.barChartView.leftYAxisRenderer.axis?.axisMinimum = Double(column_values_min)
                            self.barChartView.leftYAxisRenderer.axis?.axisMaximum = Double(column_values_max) + Double(column_values_max) * 0.1
                            //self.barChartView.xAxis.valueFormatter = DefaultAxisValueFormatter(formatter: formatter)
                            
                            self.barChartView.xAxis.labelPosition = .bottom
                            self.barChartView.xAxis.drawGridLinesEnabled = false
                            self.barChartView.chartDescription?.enabled = false
                            self.barChartView.legend.enabled = true
                            self.barChartView.rightAxis.enabled = false
                            self.barChartView.chartDescription?.text = "Barchart"
                        }
                    }
                    
                }
            } catch {
                print("error")
            }
        })
        
        task.resume()
        
        /*let xArray = Array(1..<10)
         let ys1 = xArray.map { x in return sin(Double(x) / 2.0 / 3.141 * 1.5) }
         let ys2 = xArray.map { x in return cos(Double(x) / 2.0 / 3.141) }
         let yse1 = ys1.enumerated().map { x, y in return BarChartDataEntry(x: Double(x), y: y) }
         let yse2 = ys2.enumerated().map { x, y in return BarChartDataEntry(x: Double(x), y: y) }
         
         let data = BarChartData()
         let ds1 = BarChartDataSet(values: yse1, label: "Hello")
         ds1.colors = [NSUIColor.red]
         data.addDataSet(ds1)
         
         let ds2 = BarChartDataSet(values: yse2, label: "World")
         ds2.colors = [NSUIColor.blue]
         data.addDataSet(ds2)
         
         let barWidth = 0.4
         let barSpace = 0.05
         let groupSpace = 0.1
         
         data.barWidth = barWidth
         self.barChartView.xAxis.axisMinimum = Double(xArray[0])
         self.barChartView.xAxis.axisMaximum = Double(xArray[0]) + data.groupWidth(groupSpace: groupSpace, barSpace: barSpace) * Double(xArray.count)
         // (0.4 + 0.05) * 2 (data set count) + 0.1 = 1
         data.groupBars(fromX: Double(xArray[0]), groupSpace: groupSpace, barSpace: barSpace)
         
         self.barChartView.data = data
         
         self.barChartView.gridBackgroundColor = NSUIColor.white
         
         self.barChartView.chartDescription?.text = "Barchart Demo"*/
        
    }
    
    @IBAction func save(_ sender: AnyObject)
    {
        let panel = NSSavePanel()
        panel.allowedFileTypes = ["png"]
        panel.beginSheetModal(for: self.view.window!) { (result) -> Void in
            if result.rawValue == NSFileHandlingPanelOKButton
            {
                if let path = panel.url?.path
                {
                    let _ = self.barChartView.save(to: path, format: .png, compressionQuality: 1.0)
                }
            }
        }
    }
    
    override open func viewWillAppear()
    {
        self.barChartView.animate(xAxisDuration: 1.0, yAxisDuration: 1.0)
    }
}
