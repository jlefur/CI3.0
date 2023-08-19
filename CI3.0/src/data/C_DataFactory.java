//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Random;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import business.C_DescriptorComposite;
import business.C_DescriptorSimple;
import business.C_Filter;
import business.C_Information;
import business.C_InformationWithSuggestion;
import business.C_DescriptorInstance;
import business.C_DescriptorInstances;
import business.C_DescriptorList;
import business.C_InformationList;
import business.C_InformationListFilter;
import business.C_Suggestion;

/** This class establishes the interface between the DataBase and all the application.
 * @author M.Fofana Misako.Ito S.Causse
 * @version 2.0
 * @see data.I_DataFactory */
public class C_DataFactory implements I_DataFactory {

	// FIELDS (4)
	protected Statement statementSQL = null;
	protected ResultSet resultSQL = null;
	protected Connection connectionSQL = null;
	protected String driver = "";

	// CONSTRUCTOR
	/** creates a SQL connection which is used thereafter by the four method defined by the interface I_DataFactory */
	public C_DataFactory() throws SQLException, ClassNotFoundException, IOException, SAXException,
			ParserConfigurationException {
		// the database information connection are store in an XML file read by Parametres.class
		driver = C_Parameters.getParameter("PiloteJDBC");
		String passwd = C_Parameters.getParameter("Passwd");
		if (passwd.equals("null")) passwd = "";
		java.lang.Class.forName(driver);
		connectionSQL = DriverManager.getConnection(C_Parameters.getParameter("BD"), C_Parameters.getParameter("User"),
				passwd);
		statementSQL = connectionSQL.createStatement();
	}

	// SPECIFIC METHODS (12)
	/** Create and return an informationWithException object: -create an InformationWithException -fill information attributes by
	 * a SQl query -Creation of simple descriptors and composites linked to the information by SQL request and methods
	 * addDescriptorSimple and addDescriptorComposite
	 * @see I_DataFactory#getObjectInformation Informations complementaires sur la methode */
	public C_Information getObjectInformation(int idInformation) throws SQLException {
		C_InformationWithSuggestion information = null;
		// select all proprietary and simple descriptors
		resultSQL = statementSQL.executeQuery(
				"SELECT information.title, information.uniqueTag, information.subtitle, information.shortDescription, information.entryDate, "
						+ " ref_author.fullName,ref_author.initials, ref_dimension.dimensionName, ref_source.header, ref_source.detail"
						+ " FROM ref_dimension, information, ref_source, ref_author"
						+ " WHERE ref_dimension.idRef_Dimension = information.idRef_Dimension"
						+ " AND ref_source.idRef_Source =  information.idRef_Source"
						+ " AND ref_author.idRef_Author =  information.idRef_Author"
						+ " AND information.idInformation =" + idInformation + ";");
		while (resultSQL.next()) {
			// convert information into informationWithSuggestion
			information = new C_InformationWithSuggestion(idInformation);

			// instanciate the information fields
			// 1) proprietary fields
			String title = resultSQL.getString("title");
			if (title == null) throw new NullPointerException("title");
			else information.setTitle(title);

			String subtitle = resultSQL.getString("subtitle");
			information.setSubtitle(subtitle == null ? "" : subtitle);

			String authorShort = resultSQL.getString("ref_author.initials");
			information.setAuthorShort(authorShort == null ? "" : authorShort);

			String uniqueTag = resultSQL.getString("uniqueTag");
			information.setUniqueTag(uniqueTag == null ? "" : uniqueTag);

			String entryDate = resultSQL.getString("entryDate");

			entryDate = entryDate == null ? entryDate : entryDate.substring(0, 10); // TODO number in source
			information.setEntryDate(entryDate == null ? "" : entryDate);

			String shortDescription = resultSQL.getString("shortDescription");
			information.setShortDescription(shortDescription == null ? "" : shortDescription);

			// 2) unique descriptors fields
			addSimpleDescriptor(information, "sourceheader", resultSQL);
			addSimpleDescriptor(information, "sourcedetail", resultSQL);
			addSimpleDescriptor(information, "dimensionName", resultSQL);
			addSimpleDescriptor(information, "authorfullName", resultSQL);
			addSimpleDescriptor(information, "authorinitials", resultSQL);
		}
		// Special management of optional misc1 unique descriptor field (can be null)
		resultSQL = statementSQL.executeQuery(
				"SELECT ref_misc1.misc1Name FROM ref_misc1, information WHERE ref_misc1.idRef_Misc1 = information.idRef_Misc1 AND information.idInformation ="
						+ idInformation + ";");
		while (resultSQL.next()) addSimpleDescriptor(information, "misc1Name", resultSQL);

		// elaborate composite descriptors (i.e. owning several C_DescriptorSimple and its sub-classes)
		// Relations counted and grouped by keyword
		ResultSet rs1 = statementSQL.executeQuery("SELECT keywordName, optionalDescription, COUNT(r.idRef_Keyword)"
				+ " FROM ref_keyword r, info_keyword i" + " WHERE r.idRef_Keyword = i.idRef_Keyword"
				+ " AND r.idRef_Keyword IN ( SELECT idRef_Keyword FROM info_keyword WHERE idInformation = "
				+ idInformation + ")" + " GROUP BY r.idRef_Keyword" + " ORDER BY keywordName;");
		addCompositeDescriptor(information, "Keyword", rs1);
		// TODO JLF 11.2015 ICI PB double themes CBGP
		ResultSet rs2 = statementSQL.executeQuery(
				"SELECT DISTINCT ref_metakeyword.metaKeywordName, ref_metakeyword.optionalDescription"
						+ " FROM information, info_keyword, ref_keyword, ref_metakeyword"
						+ " WHERE information.idInformation = info_keyword.idInformation"
						+ " AND ref_keyword.idRef_MetaKeyword = ref_metakeyword.idRef_MetaKeyword"
						+ " AND ref_keyword.idRef_Keyword = info_keyword.idRef_Keyword"
						+ " AND information.idInformation = " + idInformation + ";");
		addCompositeDescriptor(information, "MetaKeyword", rs2);

		ResultSet rs4 = statementSQL.executeQuery(
				"SELECT ref_medium.mediumName, info_medium.fileName, ref_medium.iconFileName, ref_medium.optionalDescription"
						+ " FROM information, info_medium,ref_medium"
						+ " WHERE information.idInformation = info_medium.idInformation"
						+ " AND info_medium.idRef_Medium  = ref_medium.idRef_Medium"
						+ " AND information.idInformation =" + idInformation + " ORDER BY ref_medium.mediumName;");
		addCompositeDescriptor(information, "Medium", rs4);

		ResultSet rs5 = statementSQL.executeQuery("SELECT ref_misc2.misc2Name, ref_misc2.optionalDescription "
				+ "FROM information, info_misc2, ref_misc2 "
				+ "WHERE information.idInformation = info_misc2.idInformation "
				+ "AND info_misc2.idRef_Misc2 = ref_misc2.idRef_Misc2 " + "AND information.idInformation ="
				+ idInformation + " ORDER BY info_misc2.id;");// order to display authors in the order they are entered JLF
																// 09.2022

		addCompositeDescriptor(information, "Misc2", rs5);

		/** Increment update info_access */
		Statement stmt1 = connectionSQL.createStatement();
		ResultSet rs_nbaccess = statementSQL.executeQuery("SELECT nbAccess FROM info_access WHERE idInformation="
				+ idInformation + ";");
		if (rs_nbaccess.next()) {
			long nbAccess = 0;
			nbAccess = rs_nbaccess.getLong("nbAccess") + 1;
			stmt1.executeUpdate("UPDATE info_access SET nbAccess=" + nbAccess + " WHERE idInformation=" + idInformation
					+ ";");
		}
		else stmt1.executeUpdate("insert into info_access values (null," + idInformation + "," + 1 + ");");// TODO jlf 02.2015
																											// null rajouté pour
																											// l'id (à vérifier si
																											// l'enregistrement
																											// est ok)
		stmt1.close();

		/** Learning */
		ResultSet infosLieesGD = statementSQL.executeQuery("SELECT info_link.* , information.title "
				+ "FROM info_link,information " + "WHERE info_link.idInformation1= '" + idInformation + "'"
				+ "AND  idInformation1<>idInformation2 "
				+ "AND  linkWeight >= (select MAX(linkWeight) from info_link where idInformation1= " + idInformation
				+ " and idInformation1<>idInformation2) "
				+ "AND information.idInformation = info_link.idInformation2;");
		// When no other information item is bound to this information item, suggest the information most accessed
		if (!infosLieesGD.next()) {
			ResultSet infos = statementSQL.executeQuery(
					"select info_access.*, information.title from info_access, information where information.idInformation ="
							+ idInformation + "  and information.idInformation = info_access.idInformation "
							+ " order by info_access.nbAccess;");
			if (infos.next()) {
				int idSugg;
				int weight = 0;
				int compteur = 0;
				int indice = 0;
				String title = "";
				indice = new Random().nextInt(10);
				do {
					idSugg = infos.getInt("idinformation");
					weight = infos.getInt("nbAccess");
					title = infos.getString("title");
					compteur++;
				} while (infos.next() && compteur <= indice);
				((C_InformationWithSuggestion) information).addInformationSuggestion(new C_Suggestion(idSugg, weight,
						title));
			}
		}
		else {
			int idSugg = infosLieesGD.getInt("idInformation2");
			int linkWeight = infosLieesGD.getInt("linkWeight");
			String title = infosLieesGD.getString("title");
			((C_InformationWithSuggestion) information).addInformationSuggestion(new C_Suggestion(idSugg, linkWeight,
					title));
		}
		return information;
	}

	/** Create and return an abject ListeInformations used by C_XMLDocumentFactory.getObjectListInformation(C_DescriptorSimple
	 * descriptor, C_Filter filter)
	 * @see I_DataFactory */
	public C_InformationList getObjectInformationList(String descriptor, String name, C_Filter filter)
			throws SQLException {
		String StringSQL = "";
		C_InformationList informationList = null;
		String stringFields = "SELECT information.idInformation,information.uniqueTag,information.title,information.subtitle,ref_author.initials";
		String stringOrder = " ORDER BY information.title;";
		String stringFilter = " AND information.idInformation IN ";
		if (descriptor.equalsIgnoreCase("metaKeywordName")) {
			StringSQL = stringFields + " FROM ref_metakeyword, ref_keyword, information,info_keyword,ref_author"
					+ " WHERE ref_metakeyword.idRef_MetaKeyword = ref_keyword.idRef_MetaKeyword"
					+ " AND information.idInformation = info_keyword.idInformation"
					+ " AND ref_keyword.idRef_Keyword = info_keyword.idRef_Keyword"
					+ " AND ref_metakeyword.metaKeywordName='" + replaceQuote(name) + "'";
			if (filter != null)
				StringSQL = StringSQL + " AND info_keyword.idInformation IN " + buildStringFilter(filter);
			StringSQL = StringSQL + "stringOrder";
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("keywordName")) {
			StringSQL = stringFields + " FROM info_keyword,information,ref_keyword, ref_author"
					+ " WHERE information.idInformation = info_keyword.idInformation"
					+ " AND information.idRef_Author = ref_author.idRef_Author"
					+ " AND ref_keyword.idRef_Keyword = info_keyword.idRef_Keyword" + " AND ref_keyword.keywordname ='"
					+ replaceQuote(name) + "'";
			if (filter != null)
				StringSQL = StringSQL + " AND info_keyword.idInformation IN " + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("misc2Name")) {
			StringSQL = stringFields + " FROM info_misc2,information,ref_misc2, ref_author"
					+ " WHERE information.idInformation = info_misc2.idInformation"
					+ " AND information.idRef_Author = ref_author.idRef_Author"
					+ " AND ref_misc2.idRef_Misc2 = info_misc2.idRef_Misc2" + " AND ref_misc2.misc2name ='"
					+ replaceQuote(name) + "'";
			if (filter != null) StringSQL = StringSQL + " AND info_misc2.idInformation IN " + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("mediumName")) {
			StringSQL = "SELECT info_medium.idInformation,information.uniqueTag,information.title,information.subtitle, ref_author.initials"
					+ " FROM info_medium,information,ref_medium, ref_author "
					+ "WHERE information.idInformation = info_medium.idInformation"
					+ " AND information.idRef_Author = ref_author.idRef_Author"
					+ " AND ref_medium.idRef_Medium = info_medium.idRef_Medium" + " AND ref_medium.mediumName ='" + name
					+ "'";
			if (filter != null)
				StringSQL = StringSQL + " AND info_medium.idInformation IN " + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("dimensionName")) {
			StringSQL = stringFields
					+ " FROM information,ref_dimension, ref_author  WHERE information.idRef_Dimension = ref_dimension.idRef_Dimension"
					+ " AND information.idRef_Author = ref_author.idRef_Author" + " AND ref_dimension.dimensionName ='"
					+ replaceQuote(name) + "'";
			if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("fullName")) {
			StringSQL = stringFields
					+ " FROM information,ref_author  WHERE information.idRef_Author = ref_author.idRef_Author"
					+ " AND ref_author.fullName ='" + replaceQuote(name) + "'";
			if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}
		else if (descriptor.equalsIgnoreCase("entryDate")) {
			StringSQL = stringFields + " FROM information,ref_author WHERE " + descriptor + "= '" + name + "'"
					+ " AND information.idRef_Author = ref_author.idRef_Author";
			if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
			StringSQL = StringSQL + stringOrder;
			resultSQL = statementSQL.executeQuery(StringSQL);
		}

		else
			if (descriptor.equalsIgnoreCase("all"))
				resultSQL = statementSQL.executeQuery(stringFields + " FROM information,ref_author"
						+ " WHERE information.idRef_Author = ref_author.idRef_Author ORDER BY information.uniqueTag");

			else
				if (descriptor.equalsIgnoreCase("source")) {
					StringSQL = stringFields
							+ " FROM information,ref_source,ref_author WHERE information.idRef_Source = ref_source.idRef_Source"
							+ " AND information.idRef_Author = ref_author.idRef_Author" + " AND ref_source.header ='"
							+ replaceQuote(name) + "'";
					if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
					StringSQL = StringSQL + stringOrder;
					resultSQL = statementSQL.executeQuery(StringSQL);
				}
				else if (descriptor.equalsIgnoreCase("ref_misc1")) {
					StringSQL = stringFields
							+ " FROM information,ref_misc1, ref_author WHERE information.idRef_Misc1 = ref_misc1.idRef_Misc1"
							+ " AND information.idRef_Author = ref_author.idRef_Author" + " AND ref_misc1.misc1Name ='"
							+ replaceQuote(name) + "'";
					if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
					StringSQL = StringSQL + stringOrder;
					resultSQL = statementSQL.executeQuery(StringSQL);
				}
				else {
					StringSQL = "SELECT information.*" + " FROM information,ref_author WHERE " + descriptor + " = '"
							+ replaceQuote(name) + "'" + " AND information.idRef_Author = ref_author.idRef_Author";
					if (filter != null) StringSQL = StringSQL + stringFilter + buildStringFilter(filter);
					StringSQL = StringSQL + stringOrder;
					resultSQL = statementSQL.executeQuery(StringSQL);
				}
		if (filter == null) informationList = new C_InformationList(new C_DescriptorSimple(descriptor, name));
		else informationList = new C_InformationListFilter(new C_DescriptorSimple(descriptor, name), filter);

		while (resultSQL.next()) {
			C_Information info = new C_Information(resultSQL.getInt(1));
			String title = resultSQL.getString("title");
			String subtitle = resultSQL.getString("subtitle");
			String uniqueTag = resultSQL.getString("uniqueTag");
			String authorShort = resultSQL.getString("initials");
			if (uniqueTag == null) uniqueTag = " ";
			info.setUniqueTag(uniqueTag);
			if (subtitle == null) subtitle = " ";
			info.setSubtitle(subtitle);
			if (authorShort == null) authorShort = " ";
			info.setAuthorShort(authorShort);
			if (title == null) title = "null";
			info.setTitle(title);
			informationList.addInformation(info);
		}
		return informationList;
	}

	/** Return a list of informations given only a descriptor name used by C_XMLDocumentFactory.getListeInformations()
	 * @param descriptor objet C_DescriptorSimple
	 * @param filter objet C_Filter
	 * @see C_InformationList */
	public C_InformationList getObjectInformationList(C_DescriptorSimple descriptor, C_Filter filter)
			throws SQLException {
		return this.getObjectInformationList(descriptor.getDescriptorType(), descriptor.getDescriptorName(), filter);
	}

	/** Create and return object of C_ListDescriptor type
	 * @return an object of type C_ListDecsriptor containing a type, content, and a simple Descriptor List. The list contains the
	 *         simple descriptors corresponding to the keywords of a metakeyword used by C_XMLDocumentFactory.getListeDescripteurs
	 * @see I_DataFactory#getObjectListDescriptor */
	public C_DescriptorList getObjectMetaDescriptorInstances(String name) throws SQLException {
		C_DescriptorList cld = new C_DescriptorList(name);
		ResultSet resultSQL = null;
		resultSQL = statementSQL.executeQuery(
				"SELECT DISTINCT ref_keyword.keywordName FROM ref_keyword, ref_metakeyword WHERE ref_keyword.idRef_MetaKeyword = ref_metakeyword.idref_metaKeyword AND ref_metakeyword.metaKeywordName =  '"
						+ name + "' ORDER BY ref_keyword.keywordName");
		if (resultSQL == null) {}
		else {
			while (resultSQL.next()) {
				C_DescriptorSimple des = new C_DescriptorSimple("keywordName", resultSQL.getString(1));
				cld.addDescriptor(des);
			}
		}
		return cld;
	}

	/** Creates and returns an object containing the list of instances of the specified descriptor used only by
	 * C_XMLDocumentFactory.getInstancesDescripteur(String type)
	 * @see I_DataFactory#getObjectInstanceDescriptor */
	public C_DescriptorInstances getObjectDescriptorInstances(String type) throws SQLException {
		if (type.equalsIgnoreCase("entryDate"))
			resultSQL = statementSQL.executeQuery(
					"SELECT information.entryDate FROM information WHERE entryDate IS NOT NULL GROUP BY information.entryDate ORDER BY information.entryDate;");
		else
			if (type.equalsIgnoreCase("fullName"))
				resultSQL = statementSQL.executeQuery(
						"SELECT ref_author.fullName FROM ref_author WHERE fullName IS NOT NULL ORDER BY ref_author.fullName;");

			else
				if (type.equalsIgnoreCase("metaKeywordName"))
					resultSQL = statementSQL.executeQuery(
							"SELECT DISTINCT ref_metakeyword.metaKeywordName FROM ref_metakeyword WHERE metaKeywordName IS NOT NULL ORDER BY ref_metakeyword.metaKeywordName");

				else
					if (type.equalsIgnoreCase("keywordName"))
						resultSQL = statementSQL.executeQuery(
								"SELECT ref_keyword.keywordName,optionalDescription FROM ref_keyword WHERE keywordName IS NOT NULL ORDER BY keywordName;");

					else
						if (type.equalsIgnoreCase("dimensionName"))
							resultSQL = statementSQL.executeQuery(
									"SELECT ref_dimension.dimensionName FROM ref_dimension WHERE dimensionName IS NOT NULL ORDER BY dimensionName;");

						else
							if (type.equalsIgnoreCase("mediumName"))
								resultSQL = statementSQL.executeQuery(
										"SELECT ref_medium.mediumName,optionalDescription FROM `ref_medium` WHERE mediumName IS NOT NULL ORDER BY mediumName;");

							else
								if (type.equalsIgnoreCase("misc2Name"))
									resultSQL = statementSQL.executeQuery(
											"SELECT ref_misc2.misc2Name FROM `ref_misc2` WHERE misc2Name IS NOT NULL ORDER BY misc2Name;");

								else
									if (type.equalsIgnoreCase("source"))
										resultSQL = statementSQL.executeQuery(
												"SELECT ref_source.header,detail FROM ref_source WHERE header IS NOT NULL ORDER BY header;");
									else {
										throw new NullPointerException("Type incorrect");
									}

		C_DescriptorInstances liste = new C_DescriptorInstances(type);
		while (resultSQL.next()) {
			if (type.equalsIgnoreCase("entryDate")) {
				C_DescriptorInstance instanceDescripteur3 = new C_DescriptorInstance();
				instanceDescripteur3.addField(resultSQL.getDate("entryDate").toString());
				liste.addInstance(instanceDescripteur3);
			}
			// add description content
			else if (type.equalsIgnoreCase("source")||type.equalsIgnoreCase("mediumName")) {
				C_DescriptorInstance instanceDescriptor = new C_DescriptorInstance();
				String field1 = resultSQL.getString(1);
				String field2 = resultSQL.getString(2);
				instanceDescriptor.addField((field1 != null) ? field1.trim() : "");
				instanceDescriptor.addField((field2 != null) ? field2 : "");
				liste.addInstance(instanceDescriptor);
			}
			else {
				C_DescriptorInstance instanceDescriptor2 = new C_DescriptorInstance();
				String field1 = resultSQL.getString(1).trim();
				instanceDescriptor2.addField((field1 != null) ? field1 : "");
				liste.addInstance(instanceDescriptor2);
			}
		}
		return liste;
	}

	/** Simple descriptor creation */
	private void addSimpleDescriptor(C_Information info, String type, ResultSet rs) throws SQLException {
		String des = null;
		if (type.equalsIgnoreCase("authorfullName")) des = rs.getString("fullName");
		else if (type.equalsIgnoreCase("authorinitials")) des = rs.getString("initials");
		else if (type.equalsIgnoreCase("sourceheader")) des = rs.getString("header");
		else if (type.equalsIgnoreCase("sourcedetail")) des = rs.getString("detail");
		else if (type.equalsIgnoreCase("date")) des = rs.getString("date").toString();
		else des = rs.getString(type);
		if (des == null) des = "";
		info.addDescriptor(new C_DescriptorSimple(type, des));
	}

	/** Method called to complete the list of composite descriptors of information declares a composite descriptor and delegates
	 * its construction to the constructor of C_DescriptorComposite(), used by getObjectInformation()
	 * @param information information of composite descriptor owner
	 * @param type type of the future descriptor (eg: Keyword, MetaKeyword Medium and Misc2)
	 * @param resulset raw result of the SQL request containing the simple descriptors which will be associated with the composite
	 *            descriptor
	 * @throws SQLExecption */
	private void addCompositeDescriptor(C_Information information, String type, ResultSet resultSet)
			throws SQLException {
		C_DescriptorComposite oneCompositeDescriptor = new C_DescriptorComposite(type, resultSet);
		information.addDescriptor(oneCompositeDescriptor);
	}

	/** Replace ' by '' */
	public String replaceQuote(String st) {
		StringBuffer sb = new StringBuffer();
		char cArray[] = st.toCharArray();
		for (int i = 0; i < st.length(); i++) {
			if (cArray[i] == '\'') {
				sb.append('\'');
			}
			sb.append(cArray[i]);
		}
		return new String(sb);
	}

	/** Create or weight links between Information */
	public void setLink(int idInformation1, int idInformation2) throws SQLException {
		long weightTemp = 0;
		resultSQL = statementSQL.executeQuery("select * from info_link where idInformation1='" + idInformation1
				+ "' and idInformation2='" + idInformation2 + "';");
		if (!resultSQL.next()) {
			statementSQL.executeUpdate("insert into info_link values (NULL, '" + idInformation1 + "', '"
					+ idInformation2 + "',1,0);");
		}
		else {
			weightTemp = resultSQL.getLong("linkWeight") + 1;
			statementSQL.executeUpdate("UPDATE info_link SET linkWeight = " + weightTemp + " WHERE idInformation1='"
					+ idInformation1 + "' and idInformation2='" + idInformation2 + "';");
		}
	}

	/** Save the id, ip and date in DB table 'tempo' while reading info */
	public int setLastTouchedInfo(int idInformation, String ipNumber) throws SQLException {
		boolean find = false;
		int idInformation1 = 0;
		resultSQL = statementSQL.executeQuery("select * from tempo where ipNumber='" + ipNumber + "';");
		while (resultSQL.next() && !find) {
			idInformation1 = resultSQL.getInt("idInformation");
			find = true;
		}
		if (!find) {
			DateFormat d = DateFormat.getDateInstance(DateFormat.DEFAULT);
			String now = d.format(new java.util.Date());

			statementSQL.executeUpdate("insert into tempo values (NULL, '" + idInformation + "', '" + ipNumber + "','"
					+ now + "');");
			return 0;
		}
		else {
			statementSQL.executeUpdate("UPDATE tempo SET idInformation" + " = '" + idInformation + "' WHERE ipNumber='"
					+ ipNumber + "';");
			return idInformation1;
		}
	}

	/** Build string for user request filtering */
	public String buildStringFilter(C_Filter filter) {
		String type = filter.getType();
		String contents = filter.getContents();
		String chaineSQL = "";

		if (type.equalsIgnoreCase("dimension"))
			chaineSQL = "(SELECT  information.idInformation FROM information,ref_dimension WHERE"
					+ " information.idRef_Dimension=ref_dimension.idRef_Dimension AND dimensionName = '" + replaceQuote(
							contents) + "')";

		else
			if (type.equalsIgnoreCase("fullName"))
				chaineSQL = "(SELECT  information.idInformation FROM information,ref_author WHERE"
						+ " information.idRef_Author=ref_author.idRef_Author AND fullName = '" + replaceQuote(contents)
						+ "')";

			else
				if (type.equalsIgnoreCase("entryDate"))
					chaineSQL = "(SELECT information.idInformation FROM information WHERE"
							+ " information.entryDate = #" + replaceQuote(contents) + "#)";

				else
					if (type.equalsIgnoreCase("source"))
						chaineSQL = "(SELECT  Informations.idRef_Source FROM information,ref_source WHERE"
								+ " information.idRef_Source=ref_source.idRef_Source AND header = '" + replaceQuote(
										contents) + "')";

					else
						if (type.equalsIgnoreCase("keyword"))
							chaineSQL = "(SELECT  info_keyword.idRef_Keyword FROM info_keyword,ref_keyword WHERE"
									+ " info_keyword.idRef_Keyword=ref_keyword.idRef_Keyword AND keywordName = '"
									+ replaceQuote(contents) + "')";

						else
							if (type.equalsIgnoreCase("medium"))
								chaineSQL = "(SELECT  info_medium.idInformation FROM info_medium,ref_medium WHERE"
										+ " info_medium.idRef_Medium=ref_medium.idRef_Medium AND type = '"
										+ replaceQuote(contents) + "')";

							else
								if (type.equalsIgnoreCase("MetaKeyword"))
									chaineSQL = "(SELECT DISTINCT info_keyword.idInformation FROM info_keyword,ref_keyword,ref_metakeyword,"
											+ "   WHERE info_keyword.idRef_Keyword=ref_keyword.idRef_Keyword AND ref_keyword.idRef_MetaKeyword=ref_metakeyword.idRef_MetaKeyword"
											+ " AND metaKeywordName = '" + replaceQuote(contents) + "')";
		return chaineSQL;
	}

	/** Deboguage */
	public static void main(String[] args) throws Throwable {
		C_DataFactory my = new C_DataFactory();
		C_InformationList infoList = my.getObjectInformationList(new C_DescriptorSimple("all"), null); // TODO quid du string 4
																										// digits ?
		C_Information info = my.getObjectInformation(25);
		info.smartPrint();
		System.err.println(infoList.toString());
	}
}
