
public class Etudiant {
	long cne;
	String nom,prenom,parcours;
	
	
	public Etudiant(int cne) {
		this.cne = cne;
	}
	
	public Etudiant(long cNE2, String nom2, String prenom, String parcours2) {
		this.cne=cNE2;
		this.nom=nom2;
		this.prenom=prenom;
		this.parcours=parcours2;
	}
	
	

	public long getCne() {
		return this.cne;
	}
	
	public void setCne(int cne) {
		this.cne = cne;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return this.prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getParcours() {
		return this.parcours;
	}
	
	public void setParcours(String parcours) {
		this.parcours = parcours;
	}
	

	public String toString() {
		return "Etudiant [cne=" + this.cne +  "]";
	}
	
 
}
