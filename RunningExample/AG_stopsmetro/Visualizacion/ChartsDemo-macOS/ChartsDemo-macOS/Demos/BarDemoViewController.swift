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

open class BarDemoViewController: NSViewController, NSTextFieldDelegate
{
    let column_names = ["stop_code", "location_type", "wheelchair_boarding"]
    let ids_name = "stop_id"
    var checkboxes: [NSButton] = []
    var jsonResults: [Any] = []
    
    @IBOutlet var barChartView: BarChartView!
    
    @IBOutlet weak var limitTextField: NSTextField!
    @IBOutlet weak var offsetTextField: NSTextField!
    @IBOutlet weak var stackView: NSStackView!
    
    @IBAction func refreshButtonAction(_ sender: Any) {
        print("Refresh")
        if limitTextField.stringValue.isEmpty {
            limitTextField.stringValue = "100"
        }
        if offsetTextField.stringValue.isEmpty {
            offsetTextField.stringValue = "0"
        }
        let url : String = "http://localhost:8080/v1/?limit=" + limitTextField.stringValue + "&offset=" + offsetTextField.stringValue
        callApi(url: url)
    }
    
    override open func controlTextDidChange(_ obj: Notification) {
        let object = obj.object as! NSTextField
        
        if object == limitTextField {
            guard let typed = object.stringValue.last else { return }
            if !"0123456789".contains(typed) {
                limitTextField!.stringValue = String(limitTextField!.stringValue.dropLast())
            }
        }
        else if object == offsetTextField {
            guard let typed = object.stringValue.last else { return }
            if !"0123456789".contains(typed) {
                limitTextField!.stringValue = String(limitTextField!.stringValue.dropLast())
            }
        }
    }
    
    override open func viewDidLoad()
    {
        super.viewDidLoad()
        
        self.limitTextField.delegate = self
        self.offsetTextField.delegate = self
        checkboxes.removeAll()
        
        var i = 0
        for column in column_names {
            let button = NSButton(frame: CGRect(x: (100 * i) + 10, y: 0, width: 100, height: 20))
            button.title = column
            button.setButtonType(NSButton.ButtonType.switch)
            button.state = NSControl.StateValue.on
            button.target = self
            button.action = #selector(BarDemoViewController.showDataInChart)
            self.stackView.addSubview(button)
            checkboxes.append(button)
            i+=1
        }
        
        let url : String = "http://localhost:8080/v1/"
        callApi(url: url)
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
    
    func callApi(url: String){
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
                            self.jsonResults = jsonResults
                            self.showDataInChart()
                        }
                    }
                    
                }
            } catch {
                print("error")
            }
        })
        
        task.resume()
    }
    
    @objc func showDataInChart() {
        var ids = Array<String>()
        var column_values = [Int]()
        var column_array = [[Int]]()
        
        let data = BarChartData()
        var column_values_min = 0
        var column_values_max = 0
        var column_values_min_aux = 0
        var column_values_max_aux = 0
        
        for result in jsonResults  as! [[String : Any]] {
            if result[self.ids_name] != nil {
                ids.append(result[self.ids_name] as! String)
            }
        }
        
        var checkboxPosition = 0
        var columnNames: [String] = []
        for column in self.column_names {
            if self.checkboxes[checkboxPosition].state == NSControl.StateValue.on {
                column_values.removeAll()
                for result in jsonResults  as! [[String : Any]] {
                    if result[column] != nil {
                        column_values.append(Int((result[column] as! NSString).intValue))
                    }
                }
                if column_values.count > 0 {
                    column_values_min_aux = column_values.min()!
                    column_values_max_aux = column_values.max()!
                    if(column_values_min_aux < column_values_min) {
                        column_values_min = column_values_min_aux
                    }
                    if(column_values_max_aux > column_values_max) {
                        column_values_max = column_values_max_aux
                    }
                    columnNames.append(column)
                    column_array.append(column_values)
                }
            }
            checkboxPosition += 1
        }
        
        var column_position = 0
        while column_position < column_array.count {
            let yse : [BarChartDataEntry]! = column_array[column_position].enumerated().map { (arg) -> BarChartDataEntry in let (x, y) = arg; return BarChartDataEntry(x: Double(x), y: Double(y)) }
            let ds = BarChartDataSet(values: yse, label: columnNames[column_position])
            ds.setColor(NSUIColor(calibratedRed: CGFloat(arc4random() % 256 ) / 256, green: CGFloat(arc4random() % 256 ) / 256, blue: CGFloat(arc4random() % 256 ) / 256, alpha: 1.0))
            data.addDataSet(ds)
            column_position += 1
        }
        
        /*let barWidth = 0.4
         let barSpace = 0.05
         let groupSpace = 0.1*/
        let groupSpace = 0.4
        let barSpace = 0.03
        let barWidth = 0.2
        
        data.barWidth = barWidth
        self.barChartView.xAxis.axisMinimum = 0
        self.barChartView.xAxis.axisMaximum = data.groupWidth(groupSpace: groupSpace, barSpace: barSpace) * Double(ids.count)
        data.groupBars(fromX: 0, groupSpace: groupSpace, barSpace: barSpace)
        
        
        
        self.barChartView.xAxis.granularityEnabled = true
        self.barChartView.xAxis.drawLabelsEnabled = true
        //self.barChartView.xAxis.granularity = 1
        self.barChartView.xAxis.decimals = 0
        
        self.barChartView.xAxis.labelCount = ids.count
        self.barChartView.pinchZoomEnabled = true
        self.barChartView.scaleYEnabled = true
        self.barChartView.scaleXEnabled = true
        self.barChartView.highlighter = nil
        self.barChartView.doubleTapToZoomEnabled = true
        self.barChartView.xAxis.granularity = self.barChartView.xAxis.axisMaximum / Double(ids.count)
        self.barChartView.xAxis.centerAxisLabelsEnabled = true
        self.barChartView.data = data
        
        
        //self.barChartView.xAxis.valueFormatter = IndexAxisValueFormatter(values: ids)
        let formatterLabels = CustomLabelsAxisValueFormatter()
        formatterLabels.labels = ids
        self.barChartView.xAxis.valueFormatter = formatterLabels
        
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
        
        self.barChartView.notifyDataSetChanged(); // let the chart know it's data changed
        self.barChartView.data?.notifyDataChanged(); // let the chart know it's data changed
        self.barChartView.animate(xAxisDuration: 1.0, yAxisDuration: 1.0)
    }
}

class CustomLabelsAxisValueFormatter : NSObject, IAxisValueFormatter {
    
    var labels: [String] = []
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        
        let count = self.labels.count
        
        guard let axis = axis, count > 0 else {
            
            return ""
        }
        
        let factor = axis.axisMaximum / Double(count)
        
        let index = Int((value / factor).rounded())
        
        if index >= 0 && index < count {
            
            return self.labels[index]
        }
        
        return ""
    }
}
