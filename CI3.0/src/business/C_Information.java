//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Class representing information from the database*/
public class C_Information {
	
	// FIELDS (8)
	/** Id Information */
	protected String uniqueTag;
	protected int idInformation;
	protected String title = "";
	protected String subtitle = "";
	protected String authorShort = "";
	protected String entryDate = "";
	protected String shortDescription = "";
	public Collection<A_Descriptor> listDescriptor = null;

	// CONSTRUCTOR
	public C_Information(int idInformation) {
		this.idInformation = idInformation;
		listDescriptor = new ArrayList<A_Descriptor>();
	}
	
	// GETTERS (8)
	public int getIdInformation() {return idInformation;}

	public String getUniqueTag() {return uniqueTag;}

	public String getauthorShort() {return authorShort;}

	public String getTitle() {return title;}

	public String getShortDescription() {return shortDescription;}

	public String getSubtitle() {return subtitle;}
	
	public String getAuthorShort() {return authorShort;}
	
	public String getEntryDate() {return this.entryDate;}

	// SETTERS (6)
	public void setUniqueTag(String uT) {this.uniqueTag = uT;}

	public void setTitle(String title) {this.title = title;}

	public void setSubtitle(String subtitle) {this.subtitle = subtitle;}
	
	public void setAuthorShort(String author) {this.authorShort = author;}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setEntryDate(String entryDate) {this.entryDate = entryDate;}

	// OVERRIDEN METHOD
	@Override
	public String toString() {
		return "Information [idInformation=" + idInformation + ", author=" + authorShort + ", title=" + title + ", subtitle="
				+ subtitle + ", entryDate=" + entryDate + ", shortDescription=" + shortDescription + ", listDescriptor="
				+ listDescriptor + "]";
	}

	// SPECIFIC METHODS (3)
	public void smartPrint() {
		System.out.println("the idInformation is  :  " + this.idInformation);
		System.out.println("the title is : " + this.title);
		System.out.println("the subtitle is  :  " + this.subtitle);
		System.out.println("the authorShort is  :  " + this.authorShort);
		System.out.println("the entry date is  :  " + this.entryDate);
		System.out.println("the short description is  :  " + this.shortDescription);
		System.out.println("");
		Iterator<A_Descriptor> itr = listDescriptor.iterator();
		while (itr.hasNext()) {
			A_Descriptor descriptor = itr.next();
			System.out.println(descriptor.toString());
		}
	}

	public Iterator<A_Descriptor> iterator() {
		return (listDescriptor.iterator());
	}

	/** Returns the specified descriptor of the information
	 * @param type the type of descriptor to return */
	public A_Descriptor getDescriptor(String type) {
		System.out.println("C_Information.getDescriptor()" + type);
		boolean found = false;
		A_Descriptor des = null;
		Iterator<A_Descriptor> i = listDescriptor.iterator();
		while (i.hasNext() && found == false) {
			des = (A_Descriptor) i.next();
			if (des.getDescriptorType().equalsIgnoreCase(type)) {
				found = true;
				break;
			}
		}
		if (found == true) return des;
		else return null;
	}

	/** Add a simple descriptor to the information */
	public void addDescriptor(A_Descriptor descriptor) {
		listDescriptor.add(descriptor);
	}
}
