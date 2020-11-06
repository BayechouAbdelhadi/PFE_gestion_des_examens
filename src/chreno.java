import java.util.*;
public class chreno {
	List<Local> locauxChreno;
	List<Local> locaux;
	List<Examen> Exam_m_c;
	private String heure;
	 List<Professeur> resp;
	
	public chreno() {
		Exam_m_c = new ArrayList<>();
		locauxChreno=  Arrays.asList(new Local("Forum",120,"F"),new Local("Amphi A",100,"A"),new Local("Amphi B",100,"A"),new Local("Amphi E",100,"A"),new Local("Amphi C",80,"A1"),new Local("Amphi D",80,"A1"),new Local("L2",60,"S"),new Local("Salle psi",60,"S"),new Local("A1",20,"S"),new Local("A2",20,"S"),new Local("A3",20,"S"),new Local("A4",20,"S"),new Local("A5",20,"S"),new Local("A6",20,"S"),new Local("A7",20,"S"),new Local("A8",20,"S"),new Local("A9",20,"S"),new Local("A10",20,"S"),new Local("A11",20,"S"),new Local("A12",20,"S"),new Local("A13",20,"S"),new Local("A14",20,"S"),new Local("A15",20,"S"),new Local("A16",20,"S"),new Local("A17",20,"S"),new Local("A18",20,"S"),new Local("B1",20,"S"),new Local("B2",20,"S"),new Local("B3",20,"S"),new Local("B4",20,"S"),new Local("B5",20,"S"),new Local("B6",20,"S"));
			heure="";
		resp= new ArrayList<>();
		locaux= new ArrayList<>();
		
	}
	public String getHeure() {
		return this.heure;
	}
	public void setHeure(String heure) {
		this.heure = heure;
	}
	@Override
	public String toString() {
		return "chreno "+this.heure+"[  Exam" + this.Exam_m_c+ "]";
	}
	
}
