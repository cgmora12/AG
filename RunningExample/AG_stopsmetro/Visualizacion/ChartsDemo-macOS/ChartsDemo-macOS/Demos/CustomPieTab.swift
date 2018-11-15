//
//  CustomPieTab.swift
//  ChartsDemo-macOS
//
//  Created by César González Mora on 24/9/18.
//  Copyright © 2018 dcg. All rights reserved.
//

import AppKit
import Cocoa
import Charts

open class CustomPieTab: NSTabViewController, NSTextFieldDelegate {
    
    let columns = ["stop_desc", "zone_id", "location_type", "wheelchair_boarding"]
    var limitTextField : NSTextField?
    var offsetTextField : NSTextField?
    var piecharts : [PieChartView] = []
    
    override open func viewDidLoad() {
        
        super.viewDidLoad()
        
        let url : String = "http://localhost:8080/v1/"
        
        for column in columns {
            let view = PieChartView()
            let controller = NSViewController()
            controller.view = view
            piecharts.append(view)
            loadChart(url: url, columnName: column, pieChartView: view)
            let tabItem = NSTabViewItem.init(viewController: controller)
            tabItem.label = column
            self.addTabViewItem(tabItem)
        }
        
        if self.tabViewItems.count > 0 {
            if self.tabViewItems[0].label == "item" {
                self.removeTabViewItem(self.tabViewItems[0])
            }
        }
        
        self.limitTextField = NSTextField(frame: CGRect(x: 10, y: 100, width: 60, height: 30))
        self.limitTextField!.placeholderString = "Limit"
        self.limitTextField!.delegate = self
        self.view.addSubview(self.limitTextField!)
        self.offsetTextField = NSTextField(frame: CGRect(x: 10, y: 65, width: 60, height: 30))
        self.offsetTextField!.placeholderString = "Offset"
        self.offsetTextField!.delegate = self
        self.view.addSubview(offsetTextField!)
        let button = NSButton(frame: CGRect(x: 10, y: 30, width: 60, height: 30))
        button.title = "Refresh"
        //button.setButtonType(NSButton.ButtonType.momentaryPushIn)
        //button.imagePosition = NSControl.ImagePosition.imageOnly
        //button.image = NSImage(named: NSImage.Name("refresh")) as NSImage?
        button.target = self
        button.action = #selector(CustomPieTab.refresh)
        self.view.addSubview(button)
        
    }
    
    override open func controlTextDidChange(_ obj: Notification) {
        let object = obj.object as! NSTextField
        
        if object == limitTextField {
            guard let typed = object.stringValue.last else { return }
            if !"0123456789".contains(typed) {
                self.limitTextField!.stringValue = String(self.limitTextField!.stringValue.dropLast())
            }
        }
        else if object == offsetTextField {
            guard let typed = object.stringValue.last else { return }
            if !"0123456789".contains(typed) {
                self.limitTextField!.stringValue = String(self.limitTextField!.stringValue.dropLast())
            }
        }
    }
    
    @objc func refresh() {
        print("Refresh")
        if self.limitTextField!.stringValue.isEmpty {
            self.limitTextField!.stringValue = "100"
        }
        if self.offsetTextField!.stringValue.isEmpty {
            self.offsetTextField!.stringValue = "0"
        }
        let url : String = "http://localhost:8080/v1/?limit=" + self.limitTextField!.stringValue + "&offset=" + self.offsetTextField!.stringValue
        
        var i = 0
        while i < columns.count {
            if columns[i] != "columnNames" {
                loadChart(url: url, columnName: columns[i], pieChartView: piecharts[i])
            }
            i+=1
        }
    }
    
    open func loadChart(url: String, columnName: String, pieChartView: PieChartView) {
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
                            var classified_values = Array<String>()
                            
                            for result in jsonResults  as! [[String : Any]] {
                                if result[columnName] != nil {
                                    classified_values.append(result[columnName] as! String)
                                }
                            }
                            
                            let classified_values_unique = Array(Set(classified_values))
                            var classified_values_count = Array<Int>()
                            var i = 0
                            for classified_value_unique in classified_values_unique {
                                classified_values_count.append(0)
                                for value in classified_values {
                                    if classified_value_unique == value {
                                        classified_values_count[i] += 1
                                    }
                                }
                                i += 1
                            }
                            
                            let data = PieChartData()
                            
                            // PieChart Accessibility for each station
                            /*let yse1 = stops_accessibilities.enumerated().map { x, y in return PieChartDataEntry(value: Double(y), label: String(stops_ids[x])) }
                             let ds1 = PieChartDataSet(values: yse1, label: "wheelchair_boarding")
                             ds1.colors = ChartColorTemplates.vordiplom()
                             data.addDataSet(ds1)*/
                            
                            // PieChart Zones stations count
                            /*let yse2 = stops_zones_count.enumerated().map { x, y in return PieChartDataEntry(value: Double(y), label: String(stops_zones_unique[x])) }
                             let ds2 = PieChartDataSet(values: yse2, label: "metro zones")
                             ds2.colors = ChartColorTemplates.vordiplom()
                             data.addDataSet(ds2)*/
                            
                            // PieChart accessibility stations count
                            let yse3 = classified_values_count.enumerated().map { x, y in return PieChartDataEntry(value: Double(y), label: String(classified_values_unique[x])) }
                            let ds3 = PieChartDataSet(values: yse3, label: "")
                            ds3.colors = ChartColorTemplates.vordiplom()
                            data.addDataSet(ds3)
                            
                            /*let paragraphStyle: NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
                            paragraphStyle.lineBreakMode = .byTruncatingTail
                            paragraphStyle.alignment = .center
                            let centerText: NSMutableAttributedString = NSMutableAttributedString(string: "stopsmetro")
                            centerText.setAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-Light", size: 15.0)!, NSAttributedStringKey.paragraphStyle: paragraphStyle], range: NSMakeRange(0, centerText.length))
                            
                            pieChartView.centerAttributedText = centerText*/
                            
                            pieChartView.data = data
                            
                            pieChartView.chartDescription?.text = "Piechart"
                            
                            pieChartView.notifyDataSetChanged(); // let the chart know it's data changed
                            pieChartView.data?.notifyDataChanged(); // let the chart know it's data changed
                            pieChartView.animate(xAxisDuration: 0.0, yAxisDuration: 1.0)
                        }
                    }
                    
                }
            } catch {
                print("error")
            }
        })
        
        task.resume()
    }
}
