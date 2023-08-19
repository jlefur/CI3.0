// This source code is licensed under Creative Commons BY license as detailed in file CI3.0.license.txt - as of 19.12.2020
package business;
/** Descriptor having a unique value (ex: source, author, dimension, misc1)
 * @author Ito, Thibaut, Fofana circa 2000, rev. Pagès 2015, JLF 06.2014, 12.2020 */
public class C_DescriptorSimple extends A_Descriptor {
	//
	// FIELDS
	//
	/** Name of the descriptor item */
	protected String descriptorName = null;
	/** used to count relationships for this particular descriptor */
	protected String relationsCount = null;
	//
	// CONSTRUCTOR
	//
	/** Default constructor<br>
	 * @param type descriptor type of the kind: keyword, source, author, dimension, misc1 */
	public C_DescriptorSimple(String type) {
		super(type);
	}
	/** Constructor using a specific value for type<br>
	 * @param type the descriptor type (keyword, source, author, dimension, misc1)
	 * @param name name of the descriptor item of the selected type */
	public C_DescriptorSimple(String type, String name) {
		super(type);
		this.descriptorName = name;
	}
	/** Constructor including count of the relationships for the given descriptor
	 * @param type the descriptor type (keyword, source, author, dimension, misc1)
	 * @param name name of the descriptor item of the selected type
	 * @param count the number of existing relationships concerning the name of this descriptor */
	public C_DescriptorSimple(String type, String name, String count) {
		super(type);
		this.descriptorName = name;
		this.relationsCount = count;
	}
	//
	// OVERRIDEN METHOD
	//
	@Override
	/** @return the type, name and count of the descriptor */
	public String toString() {
		return "DescriptorSimple type is: " + this.descriptorType + ", name is: " + this.descriptorName
				+ "    count is : " + this.relationsCount;
	}
	//
	// GETTERS
	//
	public String getDescriptorName() {
		return this.descriptorName;
	}
	/** Returns relationsCount (used for XMLDocumentFactory)
	 * @see presentation.XMLDocumentFactory
	 * @return the number of relations established for this descriptor */
	public String getRelationsCountDescriptor() {
		return this.relationsCount;
	}
	@Override
	/** @return false always to indicate the non composite nature of the descriptor */
	public boolean isComposite() {
		return false;
	}
}