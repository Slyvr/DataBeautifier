package edu.stlcc;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ChartBuilder {

	private String chartType;
	private String inputContent;
	private ArrayList<Chart> charts;
	
	public ChartBuilder(String chartType, String inputContent) {
		this.chartType = chartType;
		this.inputContent = inputContent;
		charts = new ArrayList<Chart>();
	}
	
	/**
	 * @throws Exception
	 * 
	 * Builds the dataset from given xml input file.
	 * Takes the built dataset and compiles chart objects which can be saved as jpegs or pngs
	 */
	public void build() throws Exception {
		generateDataSets();
		
		for(Chart chart : charts) {
			if (chart.getType().toUpperCase().contains("PIE")) {
				if (chart.getDataset().getColumnCount() < 50) {
					DefaultPieDataset pieDataset = new DefaultPieDataset();
					for(int row=0; row<chart.getDataset().getRowCount(); row++) {
						for(int column=0; column<chart.getDataset().getColumnCount(); column++) {
							pieDataset.setValue(chart.getDataset().getColumnKey(column), chart.getDataset().getValue(row, column));
						}
					}
					
					JFreeChart jchart = ChartFactory.createPieChart(
						chart.getTitle(),
						pieDataset,
						chart.isShowLegend(),
						chart.isShowTooltips(),
						false);
					chart.setChart(jchart);
				}
				else {
					throw new Exception("Column count is too high for a pie chart.  Must be less than 50.");
				}
			}
			else if (chart.getType().toUpperCase().contains("BAR")) {
				JFreeChart jchart = ChartFactory.createBarChart(
					chart.getTitle(),
					chart.getCategoryLabel(),
					chart.getValueLabel(),
					chart.getDataset(),
					chart.getOrientation(),
					chart.isShowLegend(),
					chart.isShowTooltips(),
					false);
				chart.setChart(jchart);
			}
			else if (chart.getType().toUpperCase().contains("LINE")) {
				JFreeChart jchart = ChartFactory.createLineChart(
					chart.getTitle(),
					chart.getCategoryLabel(),
					chart.getValueLabel(),
					chart.getDataset(),
					chart.getOrientation(),
					chart.isShowLegend(),
					chart.isShowTooltips(),
					false);
				chart.setChart(jchart);
			}
			else if (chart.getType().toUpperCase().contains("AREA")) {
				JFreeChart jchart = ChartFactory.createAreaChart(
					chart.getTitle(),
					chart.getCategoryLabel(),
					chart.getValueLabel(),
					chart.getDataset(),
					chart.getOrientation(),
					chart.isShowLegend(),
					chart.isShowTooltips(),
					false);
				chart.setChart(jchart);
			}
		}
	}
	
	/**
	 * Reads xml file for any <chart> tags and builds a Chart object based on the data given.
	 * <options> tag is read to determine output details for the specific chart.
	 * 		Title = Sets title of the chart
	 * 		Category = Sets the name of the category axis
	 * 		Value = Sets the name of the value axis
	 * 		Orientation = Sets HORIZONTAL or VERTICAL orientation
	 * 		ShowLegend = Determines whether to display the legend in the chart (true/false)
	 * 		ShowTooltips = Determines whether to display the tooltips in the chart (true/false)
	 * 		Width = Sets the width of the chart
	 * 		Height = Sets the height of the chart
	 * If no <options> are provided, defaults are used.
	 * Chart data should be compiled in <rec> items with content including <cateogry>, <name>, and <value>.
	 * Pie charts will only need values in <name> and <value> while <category> can have anything input.
	 * This method builds an array of charts in case more than one <chart> tag is given in one XML input file.
	 */
	public void generateDataSets() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(inputContent.replaceAll("\n", "").replaceAll("\t", "")));
			doc = builder.parse(is);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		if (doc != null) {
			NodeList chartNodes = doc.getElementsByTagName("chart");
			
			//If there's multiple charts within one xml, loop through all of them
			for(int chartIndex=0; chartIndex<chartNodes.getLength(); chartIndex++) {
				
				Node chartNode = chartNodes.item(chartIndex);
				Chart chart = new Chart();
				
				if (chartNode.hasChildNodes()) {
					NodeList nList = chartNode.getChildNodes();
					
					//Loop through all items within this chart and build out the dataset for it
					for (int i = 0; i < nList.getLength(); i++) {
						Node nNode = nList.item(i);
						
						//Set the options for this chart
						if (nNode.getNodeName().equals("options")) {
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String type = eElement.getElementsByTagName("type").item(0).getTextContent();
								String title = eElement.getElementsByTagName("title").item(0).getTextContent();
								String category = eElement.getElementsByTagName("categoryLabel").item(0).getTextContent();
								String value = eElement.getElementsByTagName("valueLabel").item(0).getTextContent();
								String orientation = eElement.getElementsByTagName("orientation").item(0).getTextContent();
								String showLegend = eElement.getElementsByTagName("showLegend").item(0).getTextContent();
								String showTooltips = eElement.getElementsByTagName("showTooltips").item(0).getTextContent();
								String width = eElement.getElementsByTagName("width").item(0).getTextContent();
								String height = eElement.getElementsByTagName("height").item(0).getTextContent();
								
								//Set value options from XML.  If no value, make sure to use program default
								chart.setType((type.isEmpty()) ? chart.getType() : type);
								chart.setTitle((title.isEmpty()) ? chart.getTitle() : title);
								chart.setCategoryLabel((category.isEmpty()) ? chart.getCategoryLabel() : category);
								chart.setValueLabel((value.isEmpty()) ? chart.getValueLabel() : value);
								chart.setOrientation((!orientation.isEmpty() && orientation.equals("HORIZONTAL")) ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
								chart.setShowLegend((!showLegend.isEmpty() && showLegend.toUpperCase().equals("FALSE")) ? false : true);
								chart.setShowTooltips((!showTooltips.isEmpty() && showTooltips.toUpperCase().equals("FALSE")) ? false : true);
								chart.setWidth((width.isEmpty()) ? chart.getWidth() : Integer.parseInt(width));
								chart.setHeight((height.isEmpty()) ? chart.getHeight() : Integer.parseInt(height));
							}
						}
						else {
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String category = eElement.getElementsByTagName("category").item(0).getTextContent();
								String name = eElement.getElementsByTagName("name").item(0).getTextContent();
								String value = eElement.getElementsByTagName("value").item(0).getTextContent();
								chart.getDataset().addValue(Float.parseFloat(value),category,name);
							}
						}
					}
					
					charts.add(chart);
				}
			}
		}
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getInputContent() {
		return inputContent;
	}

	public void setInputContent(String inputContent) {
		this.inputContent = inputContent;
	}

	public ArrayList<Chart> getCharts() {
		return charts;
	}

	public void setCharts(ArrayList<Chart> charts) {
		this.charts = charts;
	}
	
}
