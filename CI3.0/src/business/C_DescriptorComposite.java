//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Descriptor with multiple values. A Composite Descriptor is
 * made up of one or more Simple Descriptors. */
public class C_DescriptorComposite extends A_Descriptor {
	
	// FIELD
	/** Collection of all descriptor values */
	protected Collection<C_DescriptorSimple> contentsDescriptor = null;
	
	// CONSTRUCTORS (2)
	/** @param type Descriptor type
	 * @throws SQLException*/
	public C_DescriptorComposite(String type, ResultSet resultSet) throws SQLException {
		super(type);
		contentsDescriptor = new ArrayList<C_DescriptorSimple>();
		while (resultSet.next()) {
			String name = resultSet.getString(1);
			if (type.equalsIgnoreCase("medium")) {
				String fichier = resultSet.getString(2);
				String logo = resultSet.getString(3);
				String optionalDescription = resultSet.getString(4);
				if (fichier == null)
					throw new NullPointerException("fileName");
				this.addDescriptorSimple(new C_DescriptorMedium(type, name, fichier, logo,
						optionalDescription == null ? "" : optionalDescription));
			} else if (type.equalsIgnoreCase("misc2") || type.equalsIgnoreCase("metaKeyword")) {
				if (!duplicateName(name)) {
					String optionalDescription = resultSet.getString(2);
					this.addDescriptorSimple(new C_DescriptorSimple(type, name == null ? " " : name,
							optionalDescription == null ? " " : optionalDescription));
				} else {
				}
			} else if (type.equalsIgnoreCase("keyword")) {
				String relationsCount = resultSet.getString(3);
				this.addDescriptorSimple(new C_DescriptorSimple(type, name == null ? " " : name, relationsCount));
			}

			else {
				this.addDescriptorSimple(new C_DescriptorSimple(type, name == null ? " " : name));
			}
		}
	}
	
	public C_DescriptorComposite(Collection<C_DescriptorSimple> contentsDescriptor, String type) {
		super(type);
		this.contentsDescriptor = contentsDescriptor;
	}
	
	// OVERRIDEN METHOD
	@Override
	public String toString() {
		String res = "" + "\n";
		Iterator<C_DescriptorSimple> itr = contentsDescriptor.iterator();
		while (itr.hasNext()) {
			res = res + "      " + itr.next().toString() + "\n";
		}
		return "\n" + "DescriptorComposite  :   " + "type is:" + this.descriptorType + "\n" + res;
	}
	
	// SPECIFIC METHODS (4)
	/** Track if descriptor name is already in this.contentsDescriptor, JLF 11.2015*/
	private boolean duplicateName(String name) {
		Iterator<C_DescriptorSimple> itr = this.contentsDescriptor.iterator();
		while (itr.hasNext()) {
			String existingName = itr.next().descriptorName;
			if (existingName.equals(name))
				return true;
		}
		return false;
	}
	
	public Iterator<C_DescriptorSimple> iterator() {
		return contentsDescriptor.iterator();
	}
	
	public boolean isComposite() {return true;}
	
	public void addDescriptorSimple(C_DescriptorSimple contents) {
		contentsDescriptor.add(contents);
	}
}