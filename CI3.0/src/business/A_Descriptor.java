// This source code is licensed under Creative Commons BY license as detailed in file CI3.0.license.txt
package business;

/** Abstract class from which simple and composite descriptors inherit<br>
 * Descriptors refer to the fields bound to all tables related to the core information table
 * @see C_DescriptorSimple
 * @see C_DescriptorComposite
 * @author M.Ito, L.Thibaut, M.Fofana circa 2000, rev. B.Fall 2020, P.S.Ndiaye, M.Sall, J.Le Fur 2021 */
public abstract class A_Descriptor {

	// FIELDS (2)
	protected String descriptorType = "no type";
	/** descriptorSimple returns no & descriptorComposite returns yes */
	public abstract boolean isComposite();

	// CONSTRUCTOR
	public A_Descriptor(String typeDescriptor) {this.descriptorType = typeDescriptor;}

	// OVERRIDEN METHOD
	@Override
	public String toString() {return "A_Descriptor [typeDescriptor=" + this.descriptorType + "]";}

	// SPECIFIC METHOD
	public String getDescriptorType() {return this.descriptorType;}
}
