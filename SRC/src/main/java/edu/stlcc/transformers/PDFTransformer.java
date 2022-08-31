package edu.stlcc.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PDFTransformer extends GenericTransformer{
	
	private String pdfFile;
	
	public PDFTransformer(String xslFile, String inputType, String outputType, String inputFile, String outputFile, boolean landscape, String logFile) {
		super(xslFile, inputType, outputType, inputFile, outputFile, landscape, logFile);
		this.setOutputType("PDF");
		this.setPdfFile(this.getInputFile().replace("."+this.getInputType().toLowerCase(), ".pdf"));
		this.setOutputFile(this.getInputFile().replace("."+this.getInputType().toLowerCase(), ".html"));
	}
	
	@Override
	public void postTransformation() {
		super.postTransformation();
		
		try {
			writePDFFile();
			
			//Delete the html file after the pdf is created
			File htmlFile = new File(this.getOutputFile());
			htmlFile.delete();
			
			writeFile(this.getLogFile(), "Transformed PDF Written\n", true);
			
		} catch (DocumentException e) {
			e.printStackTrace();
			writeFile(this.getLogFile(), "DocumentException: "+e.getMessage()+"\n", true);
		} catch (IOException e) {
			e.printStackTrace();
			writeFile(this.getLogFile(), "IOException: "+e.getMessage()+"\n", true);
		}
	}
	
	/**
	 * @param inputfile - name of input HTML file
	 * @param outputfile - name of the output PDF file
	 * @param landscape - Whether to write file in portrait or landscape mode
	 * @throws DocumentException
	 * @throws IOException
	 * 
	 * Special file writer method to handle PDFs.  Utilizes the itextpdf library to build PDF file from HTML input file.
	 */
	private void writePDFFile() throws DocumentException, IOException {
		com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
		//Set the pdf to landscape
		if (this.isLandscape()) {
			doc.setPageSize(PageSize.A4.rotate());
		}
	    PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(this.getPdfFile()));
        doc.open();
        //Parse the content of output file (HTML file) into the PDF file
	    XMLWorkerHelper.getInstance().parseXHtml(writer, doc, new FileInputStream(this.getOutputFile()));
	    doc.close();
	}

	public String getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}
}
