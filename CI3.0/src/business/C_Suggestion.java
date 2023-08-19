//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt

package business;

/** Class encapsulating data on suggested information */
public class C_Suggestion {

	// FIELDS (3)
    protected int idInformation;
    protected int weight = 0;
    protected String title = "";
 
    // CONSTRUCTOR
    public C_Suggestion(int idInfo,int poids,String titre) {
    this.idInformation = idInfo;
      this.weight = poids;
      this.title = titre;
    }
    
    // GETTERS (3)
    public int getIdInformation(){return idInformation;}
    
    public int getWeight(){return  weight;}
     
    public String getTitle(){return title;}
  
    // SETTERS (3)
    public void setWeight(int poids){this.weight = poids;}
    
    public void setTitle(String titre){this.title = titre;}

    // OVERRIDEN METHOD
    @Override
	public String toString() {
		return "C_Suggestion [idInformation=" + idInformation + ", weight=" 
	            + weight + ", title=" + title + "]";
	}
}
