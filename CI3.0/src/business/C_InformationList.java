//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Class containing a list of information with one or more instances of the Information class */
public class C_InformationList {

	// FIELDS (2)
	protected Collection<C_Information> list = null;
	protected C_DescriptorSimple descriptor = null;

	// CONSTRUCTOR
	public C_InformationList(C_DescriptorSimple descripteur) {
		list = new ArrayList<C_Information>();
		this.descriptor = descripteur;
	}

	// GETTERS (2)
	public Iterator<C_Information> iterator() {return list.iterator();}

	public C_DescriptorSimple getDescriptor() {return descriptor;}

	// SPECIFIC METHODS (3)
	public void addInformation(C_Information information) {
		list.add(information);
	}
	public int getSize() {return list.size();}

	public C_Information getFirstInformation() {
		return (C_Information) ((ArrayList<C_Information>) list).get(0);
	}
}
