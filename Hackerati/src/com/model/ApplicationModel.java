package com.model;

import java.sql.Date;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

public class ApplicationModel extends AbstractTableModel implements TableModel {

	private TimeSeries series;
	private Object[][] data;
	MysqlConnectionManager datab; 

	
	public ApplicationModel(int rows) {
		this.data = new Object[rows][7];
		this.datab = new MysqlConnectionManager();
	}
	
	
//	Creates a sample dataset.
	public XYDataset createDataset(String name, double base, RegularTimePeriod start, int count) {
		
		
		this.series = new TimeSeries(name, start.getClass());
		RegularTimePeriod period = start;
		double value = base;
		for (int i = 0; i < count; i++) {
			this.series.add(period, datab.getListXY().get(i));
			period = period.next();
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(this.series);

		return dataset;
	}
	
	
	
	public void dataModeling(JFreeChart c, ApplicationModel model, TimeSeries series){
		XYPlot plot = c.getXYPlot();
		XYDataset dataset = plot.getDataset();
		Comparable seriesKey = dataset.getSeriesKey(0);
		double xx = plot.getDomainCrosshairValue();

		// update the table...
		model.setValueAt(seriesKey, 0, 0);
		long millis = (long) xx;
		model.setValueAt(new Long(millis), 0, 1);
		int itemIndex = series.getIndex(new Minute(
				new Date(millis)));
		if (itemIndex >= 0) {
			TimeSeriesDataItem item = series.getDataItem(Math.min(
					199, Math.max(0, itemIndex)));
			TimeSeriesDataItem prevItem = series.getDataItem(Math
					.max(0, itemIndex - 1));
			TimeSeriesDataItem nextItem = series.getDataItem(Math
					.min(199, itemIndex + 1));
			long x = item.getPeriod().getMiddleMillisecond();
			double y = item.getValue().doubleValue();
			long prevX = prevItem.getPeriod().getMiddleMillisecond();
			double prevY = prevItem.getValue().doubleValue();
			long nextX = nextItem.getPeriod().getMiddleMillisecond();
			double nextY = nextItem.getValue().doubleValue();
			model.setValueAt(new Long(x), 0, 1);
			model.setValueAt(new Double(y), 0, 2);
			model.setValueAt(new Long(prevX), 0, 3);
			model.setValueAt(new Double(prevY), 0, 4);
			model.setValueAt(new Long(nextX), 0, 5);
			model.setValueAt(new Double(nextY), 0, 6);
		
	}
}
	

	public int getColumnCount() {
		return 7;
	}


	public int getRowCount() {
		return 1;
	}


	public Object getValueAt(int row, int column) {
		return this.data[row][column];
	}


	public void setValueAt(Object value, int row, int column) {
		this.data[row][column] = value;
		fireTableDataChanged();
	}


	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Series Name:";
		case 1:
			return "X:";
		case 2:
			return "Y:";
		case 3:
			return "X (prev)";
		case 4:
			return "Y (prev):";
		case 5:
			return "X (next):";
		case 6:
			return "Y (next):";
		}
		return null;
	}
	
	
	public TimeSeries getSeries() {
		return series;
	}


	public void setSeries(TimeSeries series) {
		this.series = series;
	}

}
