package com.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.DateCellRenderer;
import org.jfree.ui.NumberCellRenderer;
import org.jfree.ui.RectangleInsets;

import com.model.ApplicationModel;

public class ApplicationControlPanel extends JPanel implements ChangeListener, ChartProgressListener {

	
	private ChartPanel chartPanel;
	private JFreeChart chart;
	private JSlider slider;
	private ApplicationModel model;
	
	

	/**
	 * Creates a new application panel.
	 */
	public ApplicationControlPanel() {

		super(new BorderLayout());
		System.out.println("creation chart .....");
		this.chart = createChart();
		System.out.println("chart creer");
		
//		Registers "chart" for notification of progress events relating to the chart.
//		will call chartProgress(ChartProgressEvent event) below...
		this.chart.addProgressListener(this);
		
		this.chartPanel = new ChartPanel(this.chart);
		this.chartPanel.setPreferredSize(new java.awt.Dimension(600, 270));
		this.chartPanel.setDomainZoomable(true);
		this.chartPanel.setRangeZoomable(true);
		

		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createEtchedBorder());

		this.chartPanel.setBorder(border);
		add(this.chartPanel);

		JPanel dashboard = new JPanel(new BorderLayout());
		dashboard.setPreferredSize(new Dimension(400, 60));
		dashboard.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
		
		//databinding in the graph
		dashboard.add(new JScrollPane(graphTheModel()));

		this.slider = new JSlider(0, 100, 50);
		this.slider.addChangeListener(this);
		dashboard.add(this.slider, BorderLayout.SOUTH);
		add(dashboard, BorderLayout.SOUTH);
	}
	
	
	private JTable graphTheModel() {

		

		JTable table = new JTable(this.model);
		TableCellRenderer renderer1 = new DateCellRenderer(
				new SimpleDateFormat("HH:mm"));
		
		return table;
		
	}

	/**
	 * Creates the demo chart.
	 */
	private JFreeChart createChart() {
		
		this.model = new ApplicationModel(3);
		
		XYDataset dataset1 = model.createDataset("Random 1", 100.0, new Minute(), 200);
		JFreeChart chart1 = ChartFactory.createTimeSeriesChart("Application",
				"Time of Day", "Value", dataset1, true, true, false);
		
		chart1.setBackgroundPaint(Color.white);
		XYPlot plot = chart1.getXYPlot();
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		plot.setDomainCrosshairVisible(true);
		plot.setDomainCrosshairLockedOnData(false);
		plot.setRangeCrosshairVisible(false);
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setPaint(Color.black);

		return chart1;
	}
	
	



	
	
	/**
	 * Handles a chart progress event.
	 * JFreeChart chart : event
	 */
	public void chartProgress(ChartProgressEvent event) {
		if (event.getType() != ChartProgressEvent.DRAWING_FINISHED) {
			return;
		}
		if (this.chartPanel != null) {
			JFreeChart c = this.chartPanel.getChart();
			if (c != null) {
				model.dataModeling(c, this.model, model.getSeries());
				}

			}
		}
	
	
	
	/**
	 * Handles a state change event.
	 * Invoked when the target of the listener has changed its state.
	 * JSlider slider : event
	 */
	public void stateChanged(ChangeEvent event) {
		int value = this.slider.getValue();
		XYPlot plot = this.chart.getXYPlot();
		ValueAxis domainAxis = plot.getDomainAxis();
		
		//Represents an immutable range of values.
		Range range = domainAxis.getRange();
		double c = domainAxis.getLowerBound() + (value / 100.0) * range.getLength();
		
//		Registered listeners are notified that the plot has been modified, but only if the crosshair is visible.
//		Required to fill the bottom table
		plot.setDomainCrosshairValue(c);
	}
	


		
	


}
