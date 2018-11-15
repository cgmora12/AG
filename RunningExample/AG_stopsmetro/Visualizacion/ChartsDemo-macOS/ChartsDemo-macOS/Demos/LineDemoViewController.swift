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

open class LineDemoViewController: NSViewController, NSTextFieldDelegate
{
    let ids_name = "stop_id"
    let column_names = ["stop_code", "location_type", "wheelchair_boarding"]
    var checkboxes: [NSButton] = []
    var jsonResults: [Any] = []
    
    @IBOutlet var lineChartView: LineChartView!
    
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
            button.action = #selector(LineDemoViewController.showDataInChart)
            self.stackView.addSubview(button)
            checkboxes.append(button)
            i+=1
        }
        
        let url : String = "http://localhost:8080/v1/"
        callApi(url: url)
    }
    
    override open func viewWillAppear()
    {
        self.lineChartView.animate(xAxisDuration: 0.0, yAxisDuration: 1.0)
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
        
        let data = LineChartData()
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
                if(column_values.count > 0){
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
            let yse : [ChartDataEntry]! = column_array[column_position].enumerated().map { (arg) -> ChartDataEntry in let (x, y) = arg; return ChartDataEntry(x: Double(x), y: Double(y)) }
            let ds = LineChartDataSet(values: yse, label: columnNames[column_position])
            ds.setColor(NSUIColor(calibratedRed: CGFloat((column_position * 150) % 256) / 256, green: CGFloat((column_position * 100) % 256) / 256, blue: CGFloat((column_position * 50) % 256) / 256, alpha: 1.0))
            data.addDataSet(ds)
            column_position += 1
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
        
        self.lineChartView.notifyDataSetChanged(); // let the chart know it's data changed
        self.lineChartView.data?.notifyDataChanged(); // let the chart know it's data changed
        self.lineChartView.animate(xAxisDuration: 0.0, yAxisDuration: 1.0)
    }
}
