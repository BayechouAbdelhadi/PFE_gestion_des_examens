

public class Code {
	private String nom_depart;
	private int a,b,c;
	
	
	public Code(String nom_depart, int a, int b, int c) {
	
		this.nom_depart = nom_depart;
		this.a = a;
		this.b = b;
		this.c = c;
		
	}
	
	@Override
	public String toString() {
		return  this.nom_depart  + a + b +  c ;
	}
	
	
	public String getNom_depart() {
		return this.nom_depart;
	}
	public void setNom_depart(String nom_depart) {
		this.nom_depart = nom_depart;
	}
	public int getA() {
		return this.a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return this.b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public int getC() {
		return this.c;
	}
	public void setC(int c) {
		this.c = c;
	}
	
	
}
