//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package presentation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import business.A_Descriptor;
import business.C_DescriptorComposite;
import business.C_DescriptorSimple;
import business.C_DescriptorMedium;
import business.C_Filter;
import business.C_Information;
import business.C_InformationWithSuggestion;
import business.C_DescriptorInstance;
import business.C_DescriptorInstances;
import business.C_DescriptorList;
import business.C_InformationList;
import business.C_InformationListFilter;
import business.C_Suggestion;
import data.C_DataFactory;
import data.I_DataFactory;

/** Class used to build xml documents */
public class C_XMLDocumentFactory {

	// CONSTRUCTOR
	public C_XMLDocumentFactory() {}

	// SPECIFIC METHODS (7)
	/** Build then return an xml document corresponding to a given Id Information object
	 * @param id Information Id
	 * @return <code>Document</code> with xml code
	 * @see org.w3c.dom.Document
	 * @see business.C_Information */
	public Document getInformation(int id) throws DOMException, ParserConfigurationException, SQLException,
			UnsupportedEncodingException, ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
		// System.out.println("C_XMLDocumentFactory.getInformation() is started"); //debug
		// instanciation des objets
		I_DataFactory IF = new C_DataFactory();
		C_Information info = IF.getObjectInformation(id);
		Iterator<?> i;

		// instanciation du document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.newDocument();

		// création de la balise information avec son attribut
		Element information = doc.createElement("information");
		information.setAttribute("idInformation", Integer.toString(id));
		doc.appendChild(information);// System.out.println(id);//debug
		
		Element title = doc.createElement("title");
		title.appendChild(doc.createTextNode(info.getTitle()));
		information.appendChild(title);// insere l'element title dans doc

		Element subtitle = doc.createElement("subtitle");
		subtitle.appendChild(doc.createTextNode(info.getSubtitle()));
		information.appendChild(subtitle);
		
		Element author = doc.createElement("authorShort");
		author.appendChild(doc.createTextNode(info.getAuthorShort()));
		information.appendChild(author);
		
		Element uniqueTag = doc.createElement("uniqueTag");
		uniqueTag.appendChild(doc.createTextNode(info.getUniqueTag()));
		information.appendChild(uniqueTag);

		Element entryDate = doc.createElement("entryDate");
		entryDate.appendChild(doc.createTextNode(info.getEntryDate()));
		information.appendChild(entryDate);

		/*
		 * Element referencesource = doc.createElement("referencesource");
		 * referencesource.appendChild(doc.createTextNode(info.getSourceDetail())); information.appendChild(referencesource); //
		 * manque suite au changement d'attributs de information. solution new_methode dans C_DatFactory avec reqête sql sur le //
		 * descripteur simple
		 */

		Element shortDescription = doc.createElement("shortDescription");
		shortDescription.appendChild(doc.createTextNode(info.getShortDescription()));
		information.appendChild(shortDescription);

		// création des autres balises descripteurs
		i = info.iterator();
		while (i.hasNext()) {
			A_Descriptor d = (A_Descriptor) i.next();

			if (d.isComposite()) {
				C_DescriptorComposite dc = (C_DescriptorComposite) d;
				Iterator<?> ic = dc.iterator();
				Element complexe = doc.createElement(dc.getDescriptorType());

				while (ic.hasNext()) {
					C_DescriptorSimple ifils = (C_DescriptorSimple) ic.next();
					Element elem = doc.createElement(ifils.getDescriptorType());
					//ap 2015 - in case keywords - get relations count
					if(ifils.getDescriptorType().equalsIgnoreCase("Keyword"))
					{
						elem.setAttribute("count", URLEncoder.encode(ifils.getRelationsCountDescriptor(), "UTF-8"));						
					} 
					elem.setAttribute("type", URLEncoder.encode(ifils.getDescriptorType(), "UTF-8"));
					elem.setAttribute("elem", URLEncoder.encode(ifils.getDescriptorName(), "UTF-8"));
					if (ifils.getDescriptorType().equalsIgnoreCase("Medium")) {
						C_DescriptorMedium ifilsB = (C_DescriptorMedium) ifils;
						
						//ap.2015 - Define if fileName is URL or local
						String fichier = ifilsB.getFileName();
						String fileType = "local";
						if(isURL(fichier))
						{
							fileType = "url";
						}
						elem.setAttribute("fileType", fileType);
						
						elem.setAttribute("fileName", fichier);
						elem.setAttribute("iconFileName", ifilsB.getIconFileName());
					}
					elem.appendChild(doc.createTextNode(ifils.getDescriptorName()));
					complexe.appendChild(elem);
				}
				information.appendChild(complexe);
			}
			else {

				C_DescriptorSimple ds = (C_DescriptorSimple) d;
				Element elem = doc.createElement(ds.getDescriptorType());
				// encodage de donnée
				elem.setAttribute("type", URLEncoder.encode(ds.getDescriptorType(), "UTF-8"));
				elem.setAttribute("elem", URLEncoder.encode(ds.getDescriptorName(), "UTF-8"));
				elem.appendChild(doc.createTextNode(ds.getDescriptorName()));
				information.appendChild(elem);
			}
		}
		// modif mamadi 07/08/2003
		// apprentissage
		Iterator<?> iterSugg = ((C_InformationWithSuggestion) info).getListInformationSuggestion();
		Element sugg = doc.createElement("suggestion");
		while (iterSugg.hasNext()) {
			Element elem = doc.createElement("suggestion");
			C_Suggestion suggestion = (C_Suggestion) iterSugg.next();
			elem.setAttribute("weight", String.valueOf(suggestion.getWeight()));
			// elem.appendChild(doc.createTextNode(suggestion.getIdInfo()));
			elem.appendChild(doc.createTextNode(suggestion.getTitle()));
			// elem.setAttribute("titre",suggestion.getTitre());
			elem.setAttribute("id", Integer.toString(suggestion.getIdInformation()));
			sugg.appendChild(elem);
		}
		information.appendChild(sugg);

		return doc;
	}
	
	/** Check if the parameter is URL type*/
	public boolean isURL(String content)
	{
		content = content.toLowerCase();
		if(content.contains("www") || content.contains("http"))
		{
			return true;
		}
		
		return false;
	}

	/** Build then return an xml document corresponding to an InformationList object associated with the simple descriptor
	 * @see business.C_DescriptorSimple */
	public Document getInformationList(C_DescriptorSimple descriptor, C_Filter filter, String ip) throws DOMException,
			ParserConfigurationException, SQLException, ClassNotFoundException, IOException, SAXException,
			ParserConfigurationException {

		// instanciation des objets
		I_DataFactory oneDataFactory = new C_DataFactory();
		C_InformationList li = oneDataFactory.getObjectInformationList(descriptor.getDescriptorType(), descriptor
				.getDescriptorName(), filter);
		// Si la liste d'informations ne contient qu'une seule information on affiche l'information
		if (!descriptor.getDescriptorType().equalsIgnoreCase("dimension")) {
			if (li.getSize() == 1) {
				int id_tempo = oneDataFactory.setLastTouchedInfo(li.getFirstInformation().getIdInformation(), ip);
				if (id_tempo != 0) oneDataFactory.setLink(id_tempo, li.getFirstInformation().getIdInformation());
				return getInformation(li.getFirstInformation().getIdInformation());
			}

		}

		Iterator<?> i = li.iterator();
		// instanciation du document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		// création de la balise liste avec ses 2 attributs
		Element list = doc.createElement("list");
		list.setAttribute("type", descriptor.getDescriptorType());
		list.setAttribute("contents", descriptor.getDescriptorName());
		list.setAttribute("number", String.valueOf(li.getSize()));

		// Liste filtrée
		if (filter != null) {
			list.setAttribute("typeFilter", ((C_InformationListFilter) li).getTypeFilter());
			list.setAttribute("contentsFilter", ((C_InformationListFilter) li).getContentsFilter());
		}
		doc.appendChild(list);

		// création des autres balises
		while (i.hasNext()) {
			C_Information info = (C_Information) i.next();
			Element information = doc.createElement("information");
			information.setAttribute("idInformation", Integer.toString(info.getIdInformation()));
			list.appendChild(information);
			Element uniqueTag = doc.createElement("uniqueTag");
			uniqueTag.appendChild(doc.createTextNode(info.getUniqueTag()));
			information.appendChild(uniqueTag);
			Element subtitle = doc.createElement("subtitle");
			subtitle.appendChild(doc.createTextNode(info.getSubtitle()));
			information.appendChild(subtitle);
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(info.getTitle()));
			information.appendChild(title);
			Element authorShort = doc.createElement("authorShort");
			authorShort.appendChild(doc.createTextNode(info.getAuthorShort()));
			information.appendChild(authorShort);

		}
		return doc;
	}

	/** Build then return an xml document corresponding to an InstancesDescriptor object of type "type"
	 * @see business.C_DescriptorInstances */
	public Document getDescriptorInstances(String type) throws DOMException, ParserConfigurationException, SQLException,
			ClassNotFoundException, IOException, SAXException, ParserConfigurationException {

		// instanciation des objets
		C_DataFactory IF = new C_DataFactory();
		C_DescriptorInstances ld = IF.getObjectDescriptorInstances(type);
		Iterator<?> i = ld.iterator();

		// instanciation du document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		// création de la balise liste avec ses 2 attributs
		Element list = doc.createElement("list");
		list.setAttribute("type", type);
		list.setAttribute("number", String.valueOf(ld.getSize()));
		doc.appendChild(list);

		// création des autres balises

		while (i.hasNext()) {
			C_DescriptorInstance inst = (C_DescriptorInstance) i.next();
			Iterator<?> it = inst.iterator();
			Element elem = doc.createElement("instance");
			int k = 0;
			while (it.hasNext()) {
				k++;
				Element elemfils = doc.createElement("champ" + k);
				// encodage de données
				elemfils.setAttribute("type", URLEncoder.encode(type, "UTF-8"));
				String ch = (String) it.next();
				elemfils.setAttribute("contents", URLEncoder.encode(ch, "UTF-8"));
				elemfils.appendChild(doc.createTextNode(ch));
				elem.appendChild(elemfils);
			}
			list.appendChild(elem);
		}
		return doc;
	}
	
	/** Build then return an xml document corresponding to a ListDescriptors object associated with the word "content" whose type is given as a parameter */
	public Document getMetaDescriptorInstances(String contents) throws DOMException, ParserConfigurationException, SQLException,
			ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
		
		I_DataFactory IF = new C_DataFactory();
		C_DescriptorList li = IF.getObjectMetaDescriptorInstances(contents);

		Iterator<?> i = li.iterator();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		Element liste = doc.createElement("list");
		liste.setAttribute("contents", contents);
		liste.setAttribute("number", String.valueOf(li.getSize()));
		doc.appendChild(liste);

		while (i.hasNext()) {
			C_DescriptorSimple des = (C_DescriptorSimple) i.next();
			Element elemDes = doc.createElement("descriptor");
			// encodage de données
			elemDes.setAttribute("type", URLEncoder.encode(des.getDescriptorType(), "UTF-8"));
			elemDes.setAttribute("contents", URLEncoder.encode(des.getDescriptorName(), "UTF-8"));

			liste.appendChild(elemDes);
			Element typeElem = doc.createElement("type");
			typeElem.appendChild(doc.createTextNode(des.getDescriptorType()));
			elemDes.appendChild(typeElem);
			Element contenuElem = doc.createElement("contents");
			contenuElem.appendChild(doc.createTextNode(des.getDescriptorName()));
			elemDes.appendChild(contenuElem);

		}
		return doc;

	}
	
	/** Convert Document to String */
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

	/** Deboguage */
	public static void main(String[] args) throws Exception {
		C_XMLDocumentFactory myXMLFactory = new C_XMLDocumentFactory();
		Document doc = myXMLFactory.getDescriptorInstances("keywordName");
		Element elem = doc.getDocumentElement();
		System.out.println(elem.toString());
	}
}
