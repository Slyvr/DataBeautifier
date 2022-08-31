package edu.stlcc;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart {

	private String type;
	private String title;
	private String categoryLabel;
	private String valueLabel;
	private PlotOrientation orientation;
	private boolean showLegend;
	private boolean showTooltips;
	private int width;
	private int height;
	private DefaultCategoryDataset dataset;
	private JFreeChart chart;
	
	public Chart() {
		type = "PIE";
		title = "Title";
		categoryLabel = "Category";
		valueLabel = "Values";
		orientation = PlotOrientation.VERTICAL;
		showLegend = true;
		showTooltips = true;
		width = 480;
		height = 480;
		dataset = new DefaultCategoryDataset();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryLabel() {
		return categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}

	public PlotOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(PlotOrientation orientation) {
		this.orientation = orientation;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	public boolean isShowTooltips() {
		return showTooltips;
	}

	public void setShowTooltips(boolean showTooltips) {
		this.showTooltips = showTooltips;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public DefaultCategoryDataset getDataset() {
		return dataset;
	}

	public void setDataset(DefaultCategoryDataset dataset) {
		this.dataset = dataset;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}
	
	
}
