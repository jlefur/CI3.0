//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

import java.util.ArrayList;
import java.util.Iterator;

/** Class representing an instance of a descriptor
 * @Author mfofana 2002, rev. 2014 jlefur, 2021 SanarSoft */
public class C_DescriptorInstance {

	// FIELD
	/** collection needed for composite instances such as header-detail for source */
	protected ArrayList<String> fields = null;

	// CONSTRUCTOR
	public C_DescriptorInstance() {
		fields = new ArrayList<String>();
	}

	// OVERRIDEN METHOD
	@Override
	public String toString() {
		return "InstanceDescriptor, fields=" + fields;
	}

	// SPECIFIC METHODS (3)
	public String getField(int i) {
		return (String) fields.get(i);
	}

	public Iterator<String> iterator() {
		return fields.iterator();
	}

	public void addField(String field) {
		fields.add(field);
	}
}
