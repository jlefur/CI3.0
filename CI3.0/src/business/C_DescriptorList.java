/*
 * ListeElements.java
 *
 * Created on 10 mai 2002, 17:36
 */

package business;

/**
 *
 * @author  mfofana
 * @version 
 */
import java.util.*;

/** Classe contenant un ensemble de descripteurs liés à un autre descripteur */
public class C_DescriptorList {

	/** Collection contenant les elements */
	protected Collection<C_DescriptorSimple> list = null;
	/** Type partagé par l'ensemble des descripteurs(ex bateau */
	protected String type = "type null";
	/** Chaine identifiant le type(ex bateau pour type=groupe) */
	protected String contents = "contents null";

	/** Creates new ListeDescripteurs */
	public C_DescriptorList(String contenu) {
		this.type = type;
		this.contents = contenu;
		list = new ArrayList<C_DescriptorSimple>();
	}

	/** Retourne un iterator sur l'ensemble des Descripteurs */
	public Iterator<C_DescriptorSimple> iterator() {
		return list.iterator();
	}

	/** Retourne le type de liste */
	public String getType() {
		return type;
	}

	/** Retourne la chaine d'identification */
	public String getContents() {
		return contents;
	}

	/** Ajoute une descripteur à la liste */
	public void addDescriptor(C_DescriptorSimple des) {
		list.add(des);
	}

	/** Retourne le nombre de descripteurs de la liste */
	public int getSize() {
		return list.size();
	}

	public C_DescriptorSimple getFirstDescriptor() {
		Iterator<C_DescriptorSimple> iter = list.iterator();
		return ((C_DescriptorSimple) iter.next());
	}

	@Override
	public String toString() {
		return "ListDescriptor [list=" + list + ", type=" + type + ", contents=" + contents + "]";
	}

}
