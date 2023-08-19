//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package business;

import java.util.*;

/** Class inherited from the Information class and which also contains a list of information 
 * linked to a given piece of information (learning) */
public class C_InformationWithSuggestion extends C_Information {

	// FIELD 
	protected ArrayList<C_Suggestion> listSuggestion = null;

	// CONSTRUCTOR
	public C_InformationWithSuggestion(int idInformation) {
		super(idInformation);
		listSuggestion = new ArrayList<C_Suggestion>();
	}
	
	// GETTER
	public Iterator<C_Suggestion> getListInformationSuggestion() {
		return listSuggestion.iterator();
	}

	// OVERRIDEN METHOD
	@Override
	public String toString() {
		return "Information [idInformation=" + idInformation + ", title=" + title + ", subtitle=" + subtitle
				+ ", entryDate=" + entryDate + ", shortDescription=" + shortDescription + ", listDescriptor="
				+ listDescriptor + "C_InformationWithSuggestion [listSuggestion=" + listSuggestion + "]";
	}

	// SPECIFIC METHODS (3)
	public void addInformationSuggestion(C_Suggestion sugg) {
		if (idInformationExists(sugg.getIdInformation())) {
			if (sugg.getWeight() > getSuggestionLinkedById(sugg.getIdInformation()).getWeight()) getSuggestionLinkedById(
					sugg.getIdInformation()).setWeight(sugg.getWeight());
		}
		else listSuggestion.add(sugg);
	}

	public C_Suggestion getSuggestionLinkedById(int id) {
		C_Suggestion sugg = null;
		if (idInformationExists(id)) {
			Iterator<C_Suggestion> iter = listSuggestion.iterator();
			while (iter.hasNext()) {
				sugg = (C_Suggestion) iter.next();
				if (sugg.getIdInformation() == id) break;
			}
		}
		return sugg;
	}

	public boolean idInformationExists(int idInformation) {
		boolean answer = false;
		Iterator<C_Suggestion> iter = listSuggestion.iterator();
		while (iter.hasNext()) {
			C_Suggestion sugg = (C_Suggestion) iter.next();
			if (sugg.getIdInformation()==idInformation) {
				answer = true;
				break;
			}
		}
		return answer;
	}

	public void smartPrint() {
		super.smartPrint();
		Iterator<C_Suggestion> itr2 = listSuggestion.iterator();
		while (itr2.hasNext()) {
			C_Suggestion Suggestion = itr2.next();
			System.out.println(Suggestion.toString());
		}
	}
}
