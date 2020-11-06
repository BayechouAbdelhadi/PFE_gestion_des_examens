
import java.util.ArrayList;
import java.util.List;

public class Examen {
private int n_exam;
public Module derivee;
static int num=0;
Representant_adm rep;
Professeur respo;
List<Local> LocalM;
 
public Examen(Module derivee) {
	this.derivee=derivee;
	num++;
	this.n_exam=num;
	this.LocalM=new ArrayList<>();
	}

public String toString() {
	return derivee.getCode_module().toString()+derivee.getIndice();}
/*public String toString() {
	return "Examen N� :" + n_exam + "  ||" + derivee.getCode_module()+derivee.getIndice()+"  ||couleur:"+derivee.getCouleur() + "  || represantant d'administration:" + rep + ", responsable :" + respo + "  ||surveillant 1 :" + surv1 +" moyenne="+surv1.getMoy()+ "  || surveillant 2 :=" + surv2 +" moyenne="+surv2.getMoy();
}
*/

public int getN_exam() {
	return this.n_exam;
}

public void setN_exam(int n_exam) {
	this.n_exam = n_exam;
}

public Module getDerivee() {
	return derivee;
}

public void setDerivee(Module derivee) {
	this.derivee = derivee;
}






}