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

open class CustomPieTab: NSTabViewController {
    override open func viewDidLoad() {
        
        super.viewDidLoad()
        
        let columns = ["stop_desc", "zone_id", "location_type", "wheelchair_boarding"]
        
        for column in columns {
            let view = PieChartView()
            let controller = NSViewController()
            controller.view = view
            loadChart(columnName: column, pieChartView: view)
            let tabItem = NSTabViewItem.init(viewController: controller)
            tabItem.label = column
            self.addTabViewItem(tabItem)
        }
        
        if self.tabViewItems.count > 0 {
            if self.tabViewItems[0].label == "item" {
                self.removeTabViewItem(self.tabViewItems[0])
            }
        }
        
    }
    
    open func loadChart(columnName: String, pieChartView: PieChartView) {
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
                            
                            let paragraphStyle: NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
                            paragraphStyle.lineBreakMode = .byTruncatingTail
                            paragraphStyle.alignment = .center
                            let centerText: NSMutableAttributedString = NSMutableAttributedString(string: "stopsmetro")
                            centerText.setAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-Light", size: 15.0)!, NSAttributedStringKey.paragraphStyle: paragraphStyle], range: NSMakeRange(0, centerText.length))
                            
                            pieChartView.centerAttributedText = centerText
                            
                            pieChartView.data = data
                            
                            pieChartView.chartDescription?.text = "Piechart"
                        }
                    }
                    
                }
            } catch {
                print("error")
            }
        })
        
        task.resume()
        
        // Do any additional setup after loading the view.
        /*let ys1 = Array(1..<10).map { x in return sin(Double(x) / 2.0 / 3.141 * 1.5) * 100.0 }
         
         let yse1 = ys1.enumerated().map { x, y in return PieChartDataEntry(value: y, label: String(x)) }
         
         let data = PieChartData()
         let ds1 = PieChartDataSet(values: yse1, label: "Hello")
         
         ds1.colors = ChartColorTemplates.vordiplom()
         
         data.addDataSet(ds1)
         
         let paragraphStyle: NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
         paragraphStyle.lineBreakMode = .byTruncatingTail
         paragraphStyle.alignment = .center
         let centerText: NSMutableAttributedString = NSMutableAttributedString(string: "Charts\nby Daniel Cohen Gindi")
         centerText.setAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-Light", size: 15.0)!, NSAttributedStringKey.paragraphStyle: paragraphStyle], range: NSMakeRange(0, centerText.length))
         centerText.addAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-Light", size: 13.0)!, NSAttributedStringKey.foregroundColor: NSColor.gray], range: NSMakeRange(10, centerText.length - 10))
         centerText.addAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-LightItalic", size: 13.0)!, NSAttributedStringKey.foregroundColor: NSColor(red: 51 / 255.0, green: 181 / 255.0, blue: 229 / 255.0, alpha: 1.0)], range: NSMakeRange(centerText.length - 19, 19))
         
         self.pieChartView.centerAttributedText = centerText
         
         self.pieChartView.data = data
         
         self.pieChartView.chartDescription?.text = "Piechart Demo"*/
    }
}
