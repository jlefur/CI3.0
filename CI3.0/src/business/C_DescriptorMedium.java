//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

/** Descriptor having a unique value and having in addition a file and a logo field. 
 * This class has been inserted to deal specifically with the case of the support descriptor
 * @author mfofana
 * @version Created on 19 novembre 2001, 11:44 */
public class C_DescriptorMedium extends C_DescriptorSimple {

	// FIELDS (3)
	/** File associated with the descriptor */
	protected String fileName = null;
	/** Logo associated with the descriptor */
	protected String iconFileName = null;
	protected String optionalDescription = null;

	// CONSTRUCTOR
	/** Creates new DescriptorSimpleMedium
	 * @param type Descriptor type (support only)
	 * @param contents Descriptor content
	 * @param file (image,text,graphic...) associated with the descriptor
	 * @param logo the logo chosen for the representation of the file associated with the descriptor */
	public C_DescriptorMedium(String type, String contents, String file, String logo,
		   String optionalDescription) {
		super(type, contents);
		this.fileName = file;
		this.iconFileName = logo;
		this.optionalDescription = optionalDescription;
	}
	
	// GETTERS (2)
	public String getFileName() {return fileName;}
	
	public String getIconFileName() {return iconFileName;}
	
	// OVERRIDEN METHOD
	@Override
	public String toString() {
		return "C_DescriptorSimpleMedium:   type is:   " + this.descriptorType + "   mediumName is:   "
				+ this.descriptorName + "   fileName is:   " + fileName + "   iconFileName is:   "
				+ iconFileName + "   the optionalDescription is:   " + this.optionalDescription;
	}
}
