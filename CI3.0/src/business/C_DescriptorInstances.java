//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Class encapsulating the list of instances of a descriptor This class being
 * made up of one or more instances of the InstanceDescriptor class
 * @see C_DescriptorInstance*/
public class C_DescriptorInstances {

	// FIELDS (2)
	protected String type = null;
	protected Collection<C_DescriptorInstance> listContents;

	// CONSTRUCTOR
	public C_DescriptorInstances(String type) {
		this.type = type;
		listContents = new ArrayList<C_DescriptorInstance>();
	}

	// GETTER
	public int getSize() {return listContents.size();}
	
	// OVERRIDEN METHOD
	@Override
	public String toString() {
		return "InstancesDescriptor [type=" + type + ", listContents=" + listContents + "]";
	}
	
	// SPECIFIC METHODS (2)
	public Iterator<C_DescriptorInstance> iterator() {
		return listContents.iterator();
	}
	public void addInstance(C_DescriptorInstance instance) {
		listContents.add(instance);
	}
}
