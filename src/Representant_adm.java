
public class Representant_adm {
private String nomComplet;
public String getNomComplet() {
	return nomComplet;
}
public void setNomComplet(String nomComplet) {
	this.nomComplet = nomComplet;
}
private int num;



public Representant_adm(String n,int num) {
	this.nomComplet=n;
	this.num=num;
}
	public Representant_adm(int num) {
		this.num=num;
	}
	public Representant_adm(String nom) {
		this.nomComplet=nom;
	}
	public String getNom() {
		return nomComplet;
	}
	public void setNom(String nom) {
		this.nomComplet = nom;
	}
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return " num :" + num ;
	}

}
