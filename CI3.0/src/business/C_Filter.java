//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
package business;

 /**
 * @author  mfofana
 * @version 1.0
 * 
 * Class encapsulating the information necessary for filtering a user request
 */
 public class C_Filter {

	// FIELDS (2)
    protected String type = "";
    protected String contents = "";
    
    // CONSTRUCTOR
    /** Creates new Filtre */
    public C_Filter(String type,String contents){
    this.type = type;
     this.contents =contents;
    }
    
    // GETTERS (2)
    public String getType(){return type;}
    
    public String getContents(){return contents;}
    
    // SETTERS (2)
    public void setType(String type){this.type = type;}
    
    public void setContents(String contents){this.contents = contents;} 
    
 }
