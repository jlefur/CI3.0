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

import data.C_DataFactory;


/** Servlet managing requests to display information data */
public class C_ServletInformation extends HttpServlet {

	// FIELDS (3)
	private static final long serialVersionUID = 1L;
	PrintWriter sortie;
	public static Document docHTML;

	// SPECIFIC METHODS (4)
	/** Method of processing "Get" type requests from a client */
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException {

		try {
			reponse.setContentType("text/html");// Sets the content type of the response being sent to the client, if the response
												// has not been committed yet.
			sortie = reponse.getWriter();// Returns a PrintWriter object that can send character text to the client.
			int id = Integer.parseInt(requete.getParameter("idInformation")); // Returns the value of a request parameter as a String, or null if
			// the parameter does not exist.
			String ip = requete.getRemoteAddr();// Returns the Internet Protocol (IP) address of the client or last proxy that
			int id_tempo;
			C_DataFactory infFact = new C_DataFactory();
			id_tempo = infFact.setLastTouchedInfo(id, ip); // mise en commentaire pour débeuguage à remettre

			if (id_tempo != 0) {
				infFact.setLink(id_tempo, id);
			}

			Document doc = getInformation(id);
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
			 * sortie.println("<p align=\"center\"><H1>Problèmes d'Entree/Sortie: " +ioe.getMessage()+"</p></H1>"); ioe.printStackTrace();
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
			 * sortie.println("<p align=\"center\"><H1> Champ vide ou erronné dans la base de données!!!:"+npe.getMessage()+ "</p></H1>");
			 * sortie.println("<p align=\"center\"><H1>Impossible d'afficher l'information </p></H1>");
			 */
		}

	}

	
	/** retrieve an information item(id) within an HTML document */
	/** @param idInformation
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ClassNotFoundException
	 * @throws IOException */
	private static Document getInformation(int idInformation) throws SQLException, ParserConfigurationException, SAXException, TransformerException,
			ClassNotFoundException, IOException {
		C_XMLDocumentFactory df = new C_XMLDocumentFactory();
		C_HTMLDocumentFactory sf = new C_HTMLDocumentFactory();
		Document docXML = df.getInformation(idInformation);// verified
		Document docHTML = sf.getHTMLDocument(new StreamSource(C_ServletInformation.class.getResourceAsStream("information.xsl")), docXML);
		return docHTML;
	}

	
	/** convert Document to String */
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
	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException,
			TransformerException, IOException {
		docHTML = getInformation(24);
		System.out.println(getStringFromDocument(docHTML));
	}

}
