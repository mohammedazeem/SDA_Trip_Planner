package com.myntra.tripplanner;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class DisplayGraph {
	public void display(int noOfClusters, Dataset[] clusters) {
		// Create a chart:  
	    Chart2D chart = new Chart2D();

    	for (int i = 0; i < noOfClusters; i++) {
    		PointPainterDisc icon = new PointPainterDisc(); 
        	icon.setDiscSize(10);
    		ITrace2D trace = new Trace2DSimple();
    	    chart.addTrace(trace);
    	    trace.setTracePainter(new TracePainterDisc(10));
    		icon.setColorFill(randomColor());
	        for (Instance instance : clusters[i])
	        {
	        	TracePoint2D point = new TracePoint2D( instance.get(0), instance.get(1) );
		    	point.addAdditionalPointPainter(icon);
		    	trace.addPoint(point);
	        }
        }
	    

	    // Make it visible:
	    // Create a frame.
	    JFrame frame = new JFrame("MinimalStaticChart");
	    // add the chart to the frame: 
	    frame.getContentPane().add(chart);
	    frame.setSize(400,300);
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
	
	public Color randomColor() {

	    int r = (int) (0xff * Math.random());
	    int g = (int) (0xff * Math.random());
	    int b = (int) (0xff * Math.random());

	    return new Color(r, g, b);
	}
}
