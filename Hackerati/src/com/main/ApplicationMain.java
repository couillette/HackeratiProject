package com.main;

import javax.swing.JPanel;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.GUI.ApplicationControlPanel;
import com.model.MysqlConnectionManager;




public class ApplicationMain extends ApplicationFrame {


			public ApplicationMain(String title) {   
		        super(title);   
		        setContentPane(new ApplicationControlPanel());  
		    }   
		   

		    public static JPanel createApplicationPanel() {   
		        return new ApplicationControlPanel();   
		    }   
		   
		    
		    public static void main(String[] args) {
		        ApplicationMain app = new ApplicationMain("Data Dashboard");   
		        app.pack();   
		        RefineryUtilities.centerFrameOnScreen(app);   
		        app.setVisible(true);   
		   
		    }   
		      
		

}

