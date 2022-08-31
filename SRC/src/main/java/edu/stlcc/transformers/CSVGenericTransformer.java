package edu.stlcc.transformers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVGenericTransformer extends GenericTransformer{
	
	public CSVGenericTransformer(String xslFile, String inputType, String outputType, String inputFile, String outputFile, boolean landscape, String logFile) {
		super(xslFile, inputType, outputType, inputFile, outputFile, landscape, logFile);
	}
	
	@Override
	public void transform() {
		transformCSV();
		//Do not assume a generic transformation for CSV files
		//super.transform();
	}
	
	/**
	 * @param type
	 * @param xslname
	 * @param input
	 * @param log
	 * 
	 * Builds an xml file out of a csv input file with tags of <xml_main> and each record containing <record> and <value> tags
	 */
	private void transformCSV() {
		try {
			String xmlFilename = this.getInputFile().replace(".csv", ".xml");
			String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xml_main>\n";
			List<String> csvLines = Files.readAllLines(Paths.get(this.getInputFile()), Charset.defaultCharset());
			
			for(String s : csvLines) {
				xmlContent += "\t<record>\n";
				String[] values = s.split(",");
				for(int i=0; i<values.length; i++) {
					String value = values[i];
					xmlContent += "\t\t<value"+i+">"+value+"</value"+i+">\n";
				}
				xmlContent += "\t</record>\n";
			}
			
			xmlContent += "</xml_main>";
			
			writeFile(xmlFilename, xmlContent, false);
			
			writeFile(this.getLogFile(), "Transformed CSV file to XML\n", true);
			
			this.setInputFile(xmlFilename);
			this.setInputType("XML");
			
		} catch (IOException e) {
			writeFile(this.getLogFile(), "transformCSV Exception: "+e.getMessage()+"\n", true);
			e.printStackTrace();
		}
	}
}
