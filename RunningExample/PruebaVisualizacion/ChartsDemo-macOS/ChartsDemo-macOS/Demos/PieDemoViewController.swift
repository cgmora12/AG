//
//  PieDemoViewController.swift
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

open class PieDemoViewController: NSViewController
{
    @IBOutlet var pieChartView: PieChartView!
    
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
                            var stops_ids = Array<String>()
                            var stops_accessibilities = Array<Int>()
                            var stops_zones = Array<String>()
                            
                            for metro_stop in metro_stops  as! [[String : Any]] {
                                stops_ids.append(metro_stop["stop_name"] as! String)
                                stops_accessibilities.append(Int((metro_stop["wheelchair_boarding"] as! NSString).intValue))
                                stops_zones.append(metro_stop["zone_id"] as! String)
                            }
                            
                            let stops_zones_unique = Array(Set(stops_zones))
                            var stops_zones_count = Array<Int>()
                            var i = 0
                            for zone_unique in stops_zones_unique {
                                stops_zones_count.append(0)
                                for zone in stops_zones {
                                    if zone_unique == zone {
                                        stops_zones_count[i] += 1
                                    }
                                }
                                i += 1
                            }
                            
                            let stops_accessibilities_unique = Array(Set(stops_accessibilities))
                            var stops_accessibilities_count = Array<Int>()
                            var j = 0
                            for accessibility_unique in stops_accessibilities_unique {
                                stops_accessibilities_count.append(0)
                                for accessibility in stops_accessibilities {
                                    if accessibility_unique == accessibility {
                                        stops_accessibilities_count[j] += 1
                                    }
                                }
                                j += 1
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
                            let yse3 = stops_accessibilities_count.enumerated().map { x, y in return PieChartDataEntry(value: Double(y), label: String(stops_accessibilities_unique[x])) }
                            let ds3 = PieChartDataSet(values: yse3, label: "wheelchair boarding")
                            ds3.colors = ChartColorTemplates.vordiplom()
                            data.addDataSet(ds3)
                            
                            let paragraphStyle: NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
                            paragraphStyle.lineBreakMode = .byTruncatingTail
                            paragraphStyle.alignment = .center
                            let centerText: NSMutableAttributedString = NSMutableAttributedString(string: "Alcobendas Metro Stops")
                            centerText.setAttributes([NSAttributedStringKey.font: NSFont(name: "HelveticaNeue-Light", size: 15.0)!, NSAttributedStringKey.paragraphStyle: paragraphStyle], range: NSMakeRange(0, centerText.length))
                            
                            self.pieChartView.centerAttributedText = centerText
                            
                            self.pieChartView.data = data
                            
                            self.pieChartView.chartDescription?.text = "Piechart Demo"
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
    
    override open func viewWillAppear()
    {
        self.pieChartView.animate(xAxisDuration: 0.0, yAxisDuration: 1.0)
    }
}
