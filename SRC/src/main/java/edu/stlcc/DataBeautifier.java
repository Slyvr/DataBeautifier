package edu.stlcc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jfree.chart.ChartUtilities;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import edu.stlcc.transformers.CSVGenericTransformer;
import edu.stlcc.transformers.GenericTransformer;
import edu.stlcc.transformers.PDFTransformer;

/**
 * 20180906
 * @author mschrum4
 * 
 * This project was created to allow programmers to submit an xsl and xml file and output
 * either a formatted [Custom-File-Extension] or PDF file.  This will be primarily used with banner
 * job submission processes.  The programmer should develop a shell file to execute this
 * process DataBeautifier.jar with four parameters
 * 
 * To compile this code, Run As Maven Goals: clean compile assembly:single
 * To test run java, Run DataBeautifier.java As Java App with Parameters: PDF input/test.xsl input/test.xml
 * 
 * See footprints 101755
 * 
 * args[0] - Output type - [Custom-File-Extension] or PDF or PDFPORTRAIT
 * args[1] - Stylesheet filename - pathtofile/filename.xsl
 * args[2] - XML filename - pathtofile/filename.xml
 * args[3] - Log filename - pathtofile/logfile.log
 *
 *
 * Repository details:
 * SVN $Revision: 3611 $
 * SVN $LastChangedDate: 2019-09-24 11:46:16 -0500 (Tue, 24 Sep 2019) $
 * SVN $Author: mschrum4 $
 * SVN $HeadURL: https://subversion.stlcc.edu/svn/Programmers/general/DataBeautifier/trunk/SRC/DataBeautifier/src/main/java/edu/stlcc/DataBeautifier.java $
 */
public class DataBeautifier {
	
	static Document document;
	
	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.err.println("Error: Requires four parameters\n"+
								"Output type - [Custom-File-Extension] or PDF or PDFPORTRAIT\n"+
								"Stylesheet filename - pathtofile/filename.xsl\n"+
								"XML filename - pathtofile/filename.xml\n"+
								"Log filename - pathtofile/logfile.log");
			System.exit(1);
		}
		
		//Determine input values and what we're trying to do
		String type = args[0];
		String xsl = args[1];
		String input = args[2];
		String log = args[3];
		
		writeFile(log, new Date()+": DataBeautifier starting with params: \n - "+type+"\n - "+xsl+"\n - "+input+"\n - "+log+"\n", true);
		
		String inputType = "XML";
		String outputType = type.toUpperCase().trim().replace("PORTRAIT", "");
		String inputFile = input;
		String xslFile = xsl;
		String outputFile = inputFile.replace(inputType.toLowerCase(), outputType.toLowerCase());
		boolean landscape = type.toUpperCase().contains("PORTRAIT")?false:true;
		String logFile = log;
		Object transformer = null;
		
		//Process an alternative input file like CSV
		if (!input.toUpperCase().contains(".XML")) {
			inputType = input.toUpperCase().substring(input.indexOf(".")+1, input.length());
		}
		
		//Transform input CSV file into usable XML file
		if (inputType.equals("CSV")) {
			CSVGenericTransformer csvTransform = new CSVGenericTransformer(xslFile, inputType, outputType, inputFile, outputFile, landscape, logFile);
			csvTransform.transform();
			inputFile = csvTransform.getInputFile();
			inputType = csvTransform.getInputType();
		}
		
		//Determine what file transformer to utilize.  Unless it's a special case that needs specific code written for it, it should be GenericTransformer
		if (outputType.contains("PDF")) {
			transformer = new PDFTransformer(xslFile, inputType, outputType, inputFile, outputFile, landscape, logFile);
		}else if (outputType.contains("CHART")) {
			//Create ChartBuilder object with chart type, and XML string data input
			ChartBuilder cb = new ChartBuilder(type, readFile(input,Charset.forName("UTF-8")));
			cb.build();
			
			//After charts are built, output all of them as png files
			for(int i=0; i<cb.getCharts().size(); i++) {
				Chart chart = cb.getCharts().get(i);
				File fileChart = new File(input.replace(".xml", "_"+i+".png"));
				ChartUtilities.saveChartAsPNG(fileChart,chart.getChart(),chart.getWidth(),chart.getHeight());
				writeFile(log, "Chart generated: "+fileChart.getName()+"\n", true);
			}
		}
		else {
			transformer = new GenericTransformer(xslFile, inputType, outputType, inputFile, outputFile, landscape, logFile);
		}
		
		//Begin file transformation
		if (transformer != null) {
			//Although this is initializing it as GenericTransformer, it will still utilize the specific transform and postTransformation methods of the specific class.
			GenericTransformer t = (GenericTransformer)transformer;
			t.transform();
			t.postTransformation();
		}
	}
	
	public static void writeFile(String filename, String content, boolean append) {
		try{
			FileWriter fileWriter = new FileWriter(filename, append);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(content);
		    printWriter.close();
		}
		catch(IOException ex) {
			System.out.println("Write to file error occured for "+filename);
			ex.printStackTrace();
		}
	}
	
	public static String readFile(String filepath, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(filepath));
		return new String(encoded, encoding);
	}
}
