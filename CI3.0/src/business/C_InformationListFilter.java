//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

/** Class inherited from ListInformation which also contains a filter 
 * which allows you to make a selection on all the information */
public class C_InformationListFilter extends C_InformationList {

	// FIELD
	C_Filter filter = null;

	// CONSTRUCTOR
	public C_InformationListFilter(C_DescriptorSimple descripteur, C_Filter filtre) {
		super(descripteur);
		this.filter = filtre;
	}

	// SPECIFIC METHODS (2)
	public String getTypeFilter() {return filter.getType();}

	public String getContentsFilter() {return filter.getContents();}
}
