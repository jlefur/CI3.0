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
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import business.C_DescriptorSimple;
import business.C_Filter;

/** Class dealing with requests relating to the display of a list of information use for: Search a type of knowledge */
public class C_ServletInformationList extends HttpServlet {

	// FIELD
	private static final long serialVersionUID = 1L;
	PrintWriter sortie;

	// SPECIFIC METHODS (4)
	/** Method of processing "Get" type requests from a client */
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException {

		try {
			reponse.setContentType("text/html");
			sortie = reponse.getWriter();
			String type = requete.getParameter("type");
			String ip = requete.getRemoteAddr();
			String contents = requete.getParameter("contents");
			C_Filter filter = null;
			// ajout mamadi 14/08/2003
			C_DescriptorSimple lastList = new C_DescriptorSimple(type, contents);
			HttpSession session = requete.getSession(true);
			if (session != null) {
				filter = (C_Filter) session.getAttribute("filter");
				System.out.println("filter is " + filter);
				session.setAttribute("lastList", lastList);
			}

			C_DescriptorSimple dessimple = new C_DescriptorSimple(type, contents);
			Document doc = getInformationList(dessimple, filter, ip);
			sortie.println(getStringFromDocument(doc));
			sortie.close();

		}
		catch (SQLException sqle) {
			throw new ServletException(sqle);

			/*
			 * sortie.println("<p align=\"center\"><H1>Problèmes SQL: " + sqle.getMessage()+"</p></H1>");
			 * sortie.println("<p align=\"center\"><H1>Parametres incorrects </p></H1>"); sqle.printStackTrace();
			 */
		}
		catch (IOException ioe) {
			throw new ServletException(ioe);
			/*
			 * sortie.println("<p align=\"center\"><H1>Problèmes d'Entree/Sortie: " +ioe.getMessage()+"</p></H1>");
			 * ioe.printStackTrace();
			 */
		}
		catch (SAXException se) {
			throw new ServletException(se);
			/*
			 * sortie.println(se.getMessage()); se.printStackTrace();
			 */
		}
		catch (ParserConfigurationException pce) {
			throw new ServletException(pce);
			// sortie.println(pce.getMessage());
		}
		catch (TransformerException te) {
			throw new ServletException(te);
			// sortie.println(te.getMessage());
		}
		catch (ClassNotFoundException cnfe) {
			throw new ServletException(cnfe);
			// sortie.println(cnfe.getMessage());
		}
		catch (NullPointerException npe) {
			throw new ServletException(npe);
			/*
			 * sortie.println("<p align=\"center\"><H1> Champ vide ou erronné dans la base de données!!!:"+npe.getMessage()+
			 * "</p></H1>"); sortie.println("<p align=\"center\"><H1>Impossible d'afficher l'information </p></H1>");
			 */
		}

	}

	/** Transform an xml document into an html document */
	public static Document getInformationList(C_DescriptorSimple descripteur, C_Filter filtre, String ip)
			throws SQLException, ParserConfigurationException, SAXException, TransformerException, IOException,
			ClassNotFoundException {
		C_XMLDocumentFactory df = new C_XMLDocumentFactory();
		C_HTMLDocumentFactory sf = new C_HTMLDocumentFactory();
		Document docXML = df.getInformationList(descripteur, filtre, ip);
		Document docHTML = null;
		// Si la liste d'informations ne contient qu'une seule information on affiche l'information au lieu d'afficher la liste
		if (docXML.getDocumentElement().getTagName().equalsIgnoreCase("information"))
			docHTML = sf.getHTMLDocument(new StreamSource(C_ServletInformation.class.getResourceAsStream(
					"information.xsl")), docXML);
		else
			docHTML = sf.getHTMLDocument(new StreamSource(C_ServletInformation.class.getResourceAsStream(
					"informationList.xsl")), docXML);
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
		}
		catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/** Deboguage */
	public static void main(String[] args) {
		C_Filter filter = null;
		String ip = null;
		C_DescriptorSimple dessimple = new C_DescriptorSimple("keywordName", "consigne");
		Document doc = null;
		try {
			try {
				doc = getInformationList(dessimple, filter, ip);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(doc);
	}
}
