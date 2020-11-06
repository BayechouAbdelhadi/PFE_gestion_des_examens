
public class Professeur {

 private String nom,prenom,nomComplet, departement;
 
 
 public String getNomComplet() {
	return nomComplet;
}

public void setNomComplet(String nomComplet) {
	this.nomComplet = nomComplet;
}


private int cin,moy;
 
	public String getNom() {
	return nom;
}
	
	public Professeur( String nomComplet) {
	    
	   
		this.nomComplet = nomComplet;
	}



public Professeur(int cin, String nom, String prenom) {
	    
	    this.cin = cin;
		this.nom = nom;
		this.prenom = prenom;
	}

public Professeur(String nom, int cin, String departement) {
    
	this.nom = nom;
    this.cin = cin;
	this.departement = departement;
}



@Override
	public String toString() {
		return  nom + " "+prenom ;
	}





public void setNom(String nom) {
	this.nom = nom;
}

public String getPrenom() {
	return prenom;
}

public void setPrenom(String prenom) {
	this.prenom = prenom;
}

public String getDepartement() {
	return departement;
}

public void setDepartement(String departement) {
	this.departement = departement;
}

public int getCin() {
	return cin;
}

public void setCin(int cin) {
	this.cin = cin;
}

public Professeur(int c) {
	this.cin=c;
	
}
	public Professeur(int c,String dep) {
		this.cin=c;
		this.departement=dep;
		this.moy=0;      
	}


	public int getMoy() {
		return moy;
	}


	public void setMoy(int moy) {
		this.moy = moy;
	}
	
	 
	
 
}
