//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package data;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Class constituting the link between the program and the external data (stored in an xml file) used by the program */
public class C_Parameters {
	
	// FIELDS
	private static URL parametersFile = C_Parameters.class.getResource("parameters.xml");	
	
	// SPECIFIC METHODS (3)
	/** Returns a Document (DOM) representing the xml file of the application's external parameters */
	private static Document getDocument() throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(parametersFile.getFile());
		return document;
	}
	
	/** Method allowing to retrieve an external data according to the value given in parameter
	 * @param parameters indicates the name of the parameter to retrieve in the xml file
	 * @return an object <code>String</code> representing the information associated with the parameter */
	public static String getParameter(String parameters) throws IOException, ParserConfigurationException,
			SAXException {
		Document document = getDocument();
		return document.getElementsByTagName(parameters).item(0).getFirstChild().getNodeValue();
	}
	
	/** Deboguage */	
	public static void main(String[] args) throws Throwable {
		C_Parameters.getDocument();
		System.out.print(parametersFile.toString());
	}
}