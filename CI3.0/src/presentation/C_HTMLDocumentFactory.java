//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package presentation;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Class which allows to transform an xml file into an html file using an xsl style sheet */
public class C_HTMLDocumentFactory {

	// CONSTRUCTOR
	public C_HTMLDocumentFactory() {}

	// SPECIFIC METHOD
	/** Transform an xml object into an html object using a stylesheet */
	public Document getHTMLDocument(StreamSource documentXSLT, Document documentXML)
			throws TransformerException, TransformerConfigurationException, IOException, SAXException,
			ParserConfigurationException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(documentXSLT);

		transformer.transform(new DOMSource(documentXML), new DOMResult(doc));
		return doc;
	}
}

