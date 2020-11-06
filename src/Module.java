import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Module {
	
	private int couleur;
	private int degre, indice;
	private int nb_etud;
	private String nom_module;
	private String nom_depart;
	private String parcours;
	private Code code_module;
	 Professeur responsable;
	private Local local;
	List<Etudiant> E;
	
	
	public Module() {}
	public Module(Code code_module, String nom_module, String p) {
		this.code_module = code_module;
		this.nom_module = nom_module;
		this.parcours = p;
		this.degre = 0;
		this.couleur = -1;
		this.indice = 0;
		this.responsable= null;
		this.local=null;
		E = new ArrayList<>();
	}

	public Module(Module M) {
		this.code_module = M.code_module;
		this.nom_module = M.nom_module;
		this.parcours = M.parcours;
		this.indice = 0;
		this.degre = M.degre;
		this.couleur = M.couleur;
		this.responsable= null;
		this.local=null;
		this.E = new ArrayList<>();	
		this.local = M.local;

	}

	
	
	public void Ajouter_etudiant() {
		this.E.clear();
		// L'ajout des etudiants dans les modules
		 Connection connexion;
		   Statement statement ;
	    ResultSet result;
		String code=this.getCode_module().toString();
		 try{
	          
			    Class.forName("com.mysql.jdbc.Driver"); 
			  
			   

			   connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");
			 
			   String query="SELECT  * FROM `etudiant` natural join `inscription` where CodeM like "+ "'"+code+"' order by NomE " ;
			      statement    = connexion.createStatement();
			result = statement.executeQuery(query);
			     while(result.next()){     
			    	long CNE=result.getLong("CNE");
			    	 String Nom =result.getString("NomE");
			    	 String Prénom =result.getString("PrénomE");
			    	 String 	Parcours =result.getString("CodeF");
			    	 int inscrit =result.getInt("Inscrit");
	if( inscrit ==1) {
		Etudiant e =new Etudiant(CNE,Nom,Prénom,Parcours);
	this.E.add(e);
			
			
		}
		
	}
	
	connexion.close();
	} catch(SQLException ex ){
		 System.out.println( ex.getMessage());
		 
	                         } 
		 catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	}
		 
		
	}
	
	
	
	 public void affecterResponsable() {
			String code=this.getCode_module().toString();
			Connection connexion;
		   Statement statement ;
		   java.sql.ResultSet  result;
		   String Nom = "" ;
	    	 String Prenom="";
		
		 try{
	        
			    Class.forName("com.mysql.jdbc.Driver"); 
			  
			   

			   connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");
			 
			   String query="SELECT * FROM `personnel` natural join `enseignement` where Responsable=1 && CodeM like"+"'"+code+"'" ;
			      statement    = connexion.createStatement();
			result = statement.executeQuery(query);
			while(result.next()){
				
	    	  Nom =result.getString("NomP");
	    	 Prenom =result.getString("PrénomP");
	    	 Professeur respo= new Professeur(Nom+" "+Prenom);
	    		this.responsable=respo;	 
	    			}
			
			  
	connexion.close();
	} catch(SQLException ex ){
		 System.out.println( ex.getMessage());
		 
	                       } 
		 catch (ClassNotFoundException h) {
		
		h.printStackTrace();
	}
    }	 
	 
	 
	@Override
	public String toString() {
		if(this.responsable==null) {
			return 	"Code module: "+this.code_module+"."+this.indice+" parcours: " + parcours +" Degré: "+this.degre+ " Couleur numero: " +this.couleur+" nb_etudinats: "+this.E.size()+" "+" Responsable: "+ "Non renseigne  "+" Designation: " +this.nom_module;
	
		}else return  "Code module: "+this.code_module+"."+this.indice+" parcours: " + parcours +" Degré: "+this.degre+ " Couleur numero: " +this.couleur+" nb_etudinats: "+this.E.size()+" "+" Responsable: "+this.responsable.toString()+" Designation: " +this.nom_module;
		}
	
	
	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public int getNb_etud() {
		return nb_etud;
	}

	public void setNb_etud(int nb_etud) {
		this.nb_etud = nb_etud;
	}

	public void code() {
		System.out.println(this.getCode_module());
	}

	public int getCouleur() {
		return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}

	public String getParcours() {
		return parcours;
	}

	public void setParcours(String parcours) {
		this.parcours = parcours;
	}

	public int getDegre() {
		return degre;
	}

	public void setDegre(int degre) {
		this.degre = degre;
	}
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Code getCode_module() {
		return code_module;
	}

	public void setCode_module(Code code_module) {
		this.code_module = code_module;
	}

	public String getNom_module() {
		return nom_module;
	}

	public void setNom_module(String nom_module) {
		this.nom_module = nom_module;
	}

	public String getNom_depart() {
		return nom_depart;
	}

	public void setNom_depart(String nom_depart) {
		this.nom_depart = nom_depart;
	}

}
