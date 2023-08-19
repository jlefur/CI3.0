//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package data;

import java.sql.SQLException;
import business.C_Filter;
import business.C_Information;
import business.C_DescriptorInstances;
import business.C_DescriptorList;
import business.C_InformationList;

/** Interface used to retrieve the business objects */
public interface I_DataFactory {

	// FIELD
	public static final boolean VERBOSE = true;
	
	// SPECIFIC METHODS (6)
	/**Returns an information object according to the id given in parameter
	 * @param id the identification number of the information in the database
	 * @return an object <code>Information</code> representing the id number information
	 * @see business.C_Information */
	public C_Information getObjectInformation(int id) throws SQLException;

	/** Returns a listInformation object according to the parameters "descriptor", "type" and filter
	 * @param descriptor indicates the type of descriptor (dimension, component, groups etc.) associated with the string to search
	 * @param name indicates the string to search for in the database
	 * @param filtre allows to specify a filter on the list to filter
	 * @return an objectt <code>ListeInformations</code> containing a set of information having a descriptor "descriptor" of value "name"
	 * @throws SQlException When the given parameters are incompatible with the data of the DB
	 * @see business.A_Descriptor
	 * @see business.C_InformationList */
	public C_InformationList getObjectInformationList(String descriptor, String name, C_Filter filtre)
			throws SQLException;

	/** Returns an InstancesDescriptor object according to the "type" parameter
	 * @param type indicates the type of descriptor (dimension, component, groups etc.)
	 * @return an object <code>InstancesDescripteur</code> containing all instances of the type descriptor "type"
	 * @throws SQlException When the type given in parameter does not exist in the DB
	 * @see business.A_Descriptor
	 * @see business.C_DescriptorInstances */
	public C_DescriptorInstances getObjectDescriptorInstances(String type) throws SQLException;

	/** Returns a listDescriptors object according to the "name" and "type" parameters
	 * @param name indicates the string to search for in the database
	 * @return an object <code>ListeDescripteurs</code> containing a set of descriptors
	 * @throws SQlException When the given parameters are incompatible with the data of the DB
	 * @see business.A_Descriptor
	 * @see business.C_DescriptorList */
	public C_DescriptorList getObjectMetaDescriptorInstances(String name) throws SQLException;

	/** Create or weight links between information */
	public void setLink(int id_tempo, int idInformation) throws SQLException;

	/** Save the triplet id, ip, date in the tempo table when accessing an info */
	public int setLastTouchedInfo(int id, String ip) throws SQLException;
}
