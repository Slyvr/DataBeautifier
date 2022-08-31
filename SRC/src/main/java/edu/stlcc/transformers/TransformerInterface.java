package edu.stlcc.transformers;

import java.io.IOException;
import java.nio.charset.Charset;

public interface TransformerInterface {

	public void transform();
	
	public void postTransformation();
	
	public void writeFile(String filename, String content, boolean append);
	
	public String readFile(String filename, Charset encoding) throws IOException;
}
