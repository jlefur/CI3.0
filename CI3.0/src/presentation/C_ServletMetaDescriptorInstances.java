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

/** Servlet processing requests for the display of a list of descriptors */
public class C_ServletMetaDescriptorInstances extends HttpServlet {
	
	// FIELDS (3)
	private static final long serialVersionUID = 1L;
	PrintWriter sortie;
	public static Document docHTML;

	// SPECIFIC METHODS (4)
	/** Method of processing "Get" type requests from a client */
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException {

		try {
			reponse.setContentType("text/html");
			sortie = reponse.getWriter();
			String type = requete.getParameter("type");
			String contents = requete.getParameter("contents");
			Document doc = getMetaDescriptorInstances(type, contents);
			sortie.println(getStringFromDocument(doc));
			sortie.close();

		} catch (SQLException sqle) {
			throw new ServletException(sqle);
			/*
			 * sortie.println("<p align=\"center\"><H1>Problèmes SQL: " + sqle.getMessage()+"</p></H1>");
			 * sortie.println("<p align=\"center\"><H1>Parametres incorrects </p></H1>"); sqle.printStackTrace();
			 */
		} catch (IOException ioe) {
			throw new ServletException(ioe);
			/*
			 * sortie.println("<p align=\"center\"><H1>Problèmes d'Entree/Sortie: " +ioe.getMessage()+"</p></H1>");
			 * ioe.printStackTrace();
			 */
		} catch (SAXException se) {
			throw new ServletException(se);
			/*
			 * sortie.println(se.getMessage()); se.printStackTrace();
			 */
		} catch (ParserConfigurationException pce) {
			throw new ServletException(pce);
			// sortie.println(pce.getMessage());
		} catch (TransformerException te) {
			throw new ServletException(te);
			// sortie.println(te.getMessage());
		} catch (ClassNotFoundException cnfe) {
			throw new ServletException(cnfe);
			// sortie.println(cnfe.getMessage());
		} catch (NullPointerException npe) {
			throw new ServletException(npe);
			/*
			 * sortie.println("<p align=\"center\"><H1> Champ vide ou erronné dans la base de données!!!:"+npe.getMessage()+
			 * "</p></H1>"); sortie.println("<p align=\"center\"><H1>Impossible d'afficher l'information </p></H1>");
			 */
		}

	}

	/** Transform an xml document into an html document */
	private static Document getMetaDescriptorInstances(String type, String contents) throws SQLException,
			ParserConfigurationException, SAXException, TransformerException, IOException,
			ClassNotFoundException {

		C_XMLDocumentFactory df = new C_XMLDocumentFactory();
		C_HTMLDocumentFactory sf = new C_HTMLDocumentFactory();
		Document docXML = df.getMetaDescriptorInstances(contents);
		Document docHTML = null;
		docHTML = sf.getHTMLDocument(new StreamSource(C_ServletInformation.class
				.getResourceAsStream("metaDescriptorInstances.xsl")), docXML);

		return docHTML;
	}

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

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException,
			ParserConfigurationException, SAXException, TransformerException, IOException {

		docHTML = getMetaDescriptorInstances("MetaKeyword","tool");
		System.out.println(getStringFromDocument(docHTML));
	}
}
