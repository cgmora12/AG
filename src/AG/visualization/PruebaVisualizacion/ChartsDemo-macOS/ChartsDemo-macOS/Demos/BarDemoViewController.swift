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

open class BarDemoViewController: NSViewController
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
                        if let metro_stops = json["results"] as? [Any] {
                            var stop_ids = Array<Int>()
                            var stop_accessibilities = Array<Int>()
                            
                            for metro_stop in metro_stops  as! [[String : Any]] {
                                stop_ids.append(Int((metro_stop["stop_id"] as! NSString).intValue))
                                stop_accessibilities.append(Int((metro_stop["wheelchair_boarding"] as! NSString).intValue))
                            }
                            
                            let data = BarChartData()
                            /*var i = 0
                            while i < stop_codes.count {
                                let ds1 = BarChartDataSet(values: [BarChartDataEntry(x: Double(i), y: Double(stop_accessibilities[i]))], label: String(stop_codes[i]))
                                ds1.colors = [NSUIColor.red]
                                data.addDataSet(ds1)
                                i += 1
                            }*/
                            
                            let yse2 = stop_accessibilities.enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(x), y: Double(y)) }
                            let ds2 = BarChartDataSet(values: yse2, label: "wheelchair_boarding")
                            ds2.colors = [NSUIColor.blue]
                            data.addDataSet(ds2)
                            /*let yse1 = stop_codes.enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(x), y: Double(y)) }
                            let yse2 = stop_accessibilities.enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(x), y: Double(y)) }
                            
                            let data = BarChartData()
                            let ds1 = BarChartDataSet(values: BarChartDataEntry(, label: "stop_code")
                            ds1.colors = [NSUIColor.red]
                            data.addDataSet(ds1)
                            
                            let ds2 = BarChartDataSet(values: yse2, label: "wheelchair_boarding")
                            ds2.colors = [NSUIColor.blue]
                            data.addDataSet(ds2)*/
                            
                            let barWidth = 0.4
                            let barSpace = 0.05
                            let groupSpace = 0.1
                            
                            data.barWidth = barWidth
                            self.barChartView.xAxis.axisMinimum = Double(stop_accessibilities[0])
                            self.barChartView.xAxis.axisMaximum = Double(stop_accessibilities[0]) + data.groupWidth(groupSpace: groupSpace, barSpace: barSpace) * Double(stop_accessibilities.count)
                            // (0.4 + 0.05) * 2 (data set count) + 0.1 = 1
                            data.groupBars(fromX: Double(stop_accessibilities[0]), groupSpace: groupSpace, barSpace: barSpace)
                            
                            self.barChartView.data = data
                            
                            self.barChartView.gridBackgroundColor = NSUIColor.white
                            
                            self.barChartView.chartDescription?.text = "Barchart Demo"
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
