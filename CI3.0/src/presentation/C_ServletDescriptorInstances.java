//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Servlet processing requests for the display of the list of instances of a descriptor */
public class C_ServletDescriptorInstances extends HttpServlet {
	
	// FIELDS (3)
	private static final long serialVersionUID = 1L;
	public static Document docHTML;
	PrintWriter sortie;

	// SPECIFIC METHODS (4)
	/** Method of processing "Get" type requests from a client */
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException {
		try {
			reponse.setContentType("text/html");
			sortie = reponse.getWriter();
			String parametre = requete.getParameter("type");
			Document doc = getDescriptorInstances(parametre);
			sortie.println(getStringFromDocument(doc));
			sortie.close();
		} catch (SQLException sqle) {
			throw new ServletException(sqle);
		} catch (IOException ioe) {
			throw new ServletException(ioe);
		} catch (SAXException se) {
			throw new ServletException(se);
		} catch (ParserConfigurationException pce) {
			throw new ServletException(pce);
		} catch (TransformerException te) {
			throw new ServletException(te);
		} catch (ClassNotFoundException cnfe) {
			throw new ServletException(cnfe);
		} catch (NullPointerException npe) {
			throw new ServletException(npe);
		}
	}

	/** Transforme un document xml en un document html */
	private static Document getDescriptorInstances(String type) throws SQLException,
			ParserConfigurationException, SAXException, TransformerException, IOException,
			ClassNotFoundException {
		C_XMLDocumentFactory df = new C_XMLDocumentFactory();
		C_HTMLDocumentFactory sf = new C_HTMLDocumentFactory();
		Document docXML = df.getDescriptorInstances(type);
		Document docHTML = sf.getHTMLDocument(new StreamSource(C_ServletInformation.class
				.getResourceAsStream("descriptorInstances.xsl")), docXML);
		return docHTML;
	}
	
	/** convert Document to String*/
	public static String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/** Deboguage
	 * @throws IOException
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 * @throws ClassNotFoundException */
	public static void main(String[] args) throws ClassNotFoundException, SQLException,
			ParserConfigurationException, SAXException, TransformerException, IOException {
		String type = "misc2Name";
		docHTML = getDescriptorInstances(type);
		System.out.println(getStringFromDocument(docHTML));
	}
}
