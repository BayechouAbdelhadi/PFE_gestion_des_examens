

import java.util.*;

public class Local {

  boolean occupe=false;
  private String type;
  private String nom;
  int capaOcupe;
  String resp;
  private int capacite;
  List <Etudiant> liste_E_local;
  Module module;
  Professeur surveillant1;
  Professeur surveillant2;
  Professeur surveillant3;
  Representant_adm representant ;
   
   public Local(String nom, int capacite,String type) {
	this.nom = nom;
	capaOcupe=0;
	this.capacite = capacite;
	this.type = type;
	liste_E_local = new ArrayList<>();
	module=null;
	resp="";
	representant= null;
	surveillant1=null;
	surveillant2=null;
	surveillant3=null;
   }
   
public String getNom() {
	return nom;
}

public void setNom(String nom) {
	this.nom = nom;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public int getCapacite() {
	return capacite;
}

public void setCapacite(int capacite) {
	this.capacite = capacite;
}

@Override
public String toString() {
	return nom;
}
}
