
import java.util.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.awt.Desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.ParseException;



public class Planning
{
    static int pos = 0, col = 1, p = 0;

    static List<String> locaux =new ArrayList<>();
    static List<chreno> chrenoliste = new ArrayList<>();
    static List<Module> liste_module = new ArrayList<>();
    static List<List<Integer>> incompatibiltyMatrice;//????
    static List<Examen> liste_exam = new ArrayList<>();
    static List<Local> amphi =  Arrays.asList(new Local("Forum",120,"F"),new Local("Amphi A",100,"A"),new Local("Amphi B",100,"A"),new Local("Amphi E",100,"A"),new Local("Amphi C",80,"A1"),new Local("Amphi D",80,"A1"),new Local("L2",60,"S"),new Local("Salle psi",60,"S"),new Local("A1",20,"S"),new Local("A2",20,"S"),new Local("A3",20,"S"),new Local("A4",20,"S"),new Local("A5",20,"S"),new Local("A6",20,"S"),new Local("A7",20,"S"),new Local("A8",20,"S"),new Local("A9",20,"S"),new Local("A10",20,"S"),new Local("A11",20,"S"),new Local("A12",20,"S"),new Local("A13",20,"S"),new Local("A14",20,"S"),new Local("A15",20,"S"),new Local("A16",20,"S"),new Local("A17",20,"S"),new Local("A18",20,"S"),new Local("B1",20,"S"),new Local("B2",20,"S"),new Local("B3",20,"S"),new Local("B4",20,"S"),new Local("B5",20,"S"),new Local("B6",20,"S"));
    static List<jour> jours = new ArrayList<>();
    
    static List<Representant_adm> representant = new ArrayList<>();
    
    static List<Professeur> surveillant = new ArrayList<>();
    static List<Professeur> surveillant1 = new ArrayList<>();
    static List<Professeur> surveillantTemp1 = new ArrayList<>();
    static List<Professeur> surveillantTemp2 = new ArrayList<>();
    static List<Professeur> surveillantTemp4 = new ArrayList<>();
    static List<Professeur> surveillantTemp5 = new ArrayList<>();
    static List<Professeur> surveillantTemp3 = new ArrayList<>();

    public static final WebColors NAMES = new WebColors();
    static
    {
        NAMES.put("wheat", new int[] { 0xf5, 0xde, 0xb3, 0xff });
        NAMES.put("lightgrey", new int[] { 0xd3, 0xd3, 0xd3, 0xff });
    }
    static BaseColor sandy = new BaseColor(26, 159, 199);
    static BaseColor wheat = new BaseColor(251, 155, 42);
    static BaseColor smock = WebColors.getRGBColor("lightgrey");
 


    // la fonction Etudiant_existe vérifie la présence d'un étudaint dans deux modules
    static boolean exist;
    public static  boolean Etudiant_exist(Module m, Module n)
    {
        exist = false;
        int a = 0;
        do
        {
            for (int b = 0; b < n.E.size(); b++)
            {
                if (m.E.isEmpty() || n.E.isEmpty())
                {
                    exist = false;
                }
                else
                {
                    if (m.E.get(a).getCne() == n.E.get(b).getCne())
                    {
                        exist = true;
                        break;
                    }
                }
            }
            a++;

        }
        while ((exist == false) && a < m.E.size());

        return exist;
    }

    /* La matrice d'incompatibilité des modules, c à d 0 si pas d'etudiant en commun entre
    deux modules ou le nombre total d'étudiant ne dépassent pas la capacité des locaux et 1 sinon*/
    public static void matrice(List<Module> liste)
    {
        int capacityMax=1093;
        incompatibiltyMatrice = new ArrayList<>();
        for(int i=0; i<liste.size(); i++)
        {
            List<Integer> l=new ArrayList<>();
            for(int j=0; j<liste.size(); j++)
                if(i==j)
                        l.add(0); // 0 pour la diagonale
                    else
                        if((liste_module.get(i).E.size()+liste_module.get(j).E.size())>capacityMax)
                            l.add(1);
                        else
                            if(Etudiant_exist(liste_module.get(i), liste_module.get(j)))
                                l.add(1);
                            else
                                l.add(0);
            incompatibiltyMatrice.add(i,l);
        }
    }

    //calcule de degrés des modules
    public static void calcule_deg(List<List<Integer>> matrice)
    {
        for(Module m:liste_module)
        {
            m.setDegre(0);
        }
        for (int i = 0; i < matrice.size(); i++)
        {
            for (int j = 0; j < matrice.size(); j++)
            {
                liste_module.get(i).setDegre(liste_module.get(i).getDegre() + matrice.get(i).get(j));
            }
        }
    }


    // trie décroissant des modules selon leurs degrés
    public static void triBulleDecroissant(List<Module> liste)
    {
        Module tampon;
        boolean permut;

        do
        {
            // hypothèse : le tableau est trié
            permut = false;
            for (int i = 0; i < liste.size() - 1; i++)
            {
                // Teste si 2 éléments successifs sont dans le bon ordre ou non
                if (liste.get(i).getDegre() < liste.get(i + 1).getDegre())
                {
                    // s'ils ne le sont pas, on échange leurs positions
                    tampon = liste.get(i);
                    liste.set(i, liste.get(i + 1));
                    liste.set(i + 1, tampon);

                    permut = true;
                }
            }
        }
        while (permut);
    }


    // welsh and powel
    public static void  coloration1()
    {
        // on parcourt la liste des modules:
        for (int i = 0; i < liste_module.size(); i++)
        {
            boolean v = false;
            // on donne une couleur au premier sommet non encore color�
            if (liste_module.get(i).getCouleur() == -1)
            {
                liste_module.get(i).setCouleur(col);

                col++; //------incrementation du numero de la couleur
                for (int j = 0; j < liste_module.size(); j++)
                {
                    if (liste_module.get(j).getCouleur() == -1)  // si le sommet j est non color�
                    {
                        if (incompatibiltyMatrice.get(i).get(j) == 0)  // si le sommet j est non adjacent au sommet i
                        {
                            for (int pos = 0; pos < liste_module.size(); pos++)
                            {
                                if (incompatibiltyMatrice.get(j).get(pos) == 1)
                                {
                                    if (liste_module.get(pos).getCouleur() == liste_module.get(i).getCouleur())
                                    {
                                        v = true;           /*on test si le sommet adjacents au sommet j
										                                  n'ont pas la meme couleur de i */
                                    }
                                }
                            }
                            if (v == false)
                            {
                                liste_module.get(j).setCouleur(liste_module.get(i).getCouleur());

                            }
                        }
                    }
                }
            }
        }
    }


    public static void creer_chreno()
    {
        chrenoliste.clear();
        for (int i = 0; i <maxColor(liste_module); i++)
        {

            chrenoliste.add(new chreno());
        }

        for (int i = 0; i < chrenoliste.size(); i++)
        {
            chrenoliste.get(i).Exam_m_c.clear();
            for (int j = 0; j < liste_module.size(); j++)
            {
                if (liste_module.get(j).getCouleur() == i )
                {

                    chrenoliste.get(i).Exam_m_c.add(new Examen(liste_module.get(j)));
                }
            }
        }
    }



    public static void affecterLocal()
    {

        int loc=0;
        int cap=0;
        for (int i = 0; i < chrenoliste.size(); i++)
        {
            for(int j=0; j<chrenoliste.get(i).Exam_m_c.size(); j++)
            {
                cap=chrenoliste.get(i).locauxChreno.get(loc).getCapacite();
                for(int k=0; k<chrenoliste.get(i).Exam_m_c.get(j).getDerivee().E.size(); k++)
                {

                    if(k < cap)
                    {
                        chrenoliste.get(i).locauxChreno.get(loc).liste_E_local.add(chrenoliste.get(i).Exam_m_c.get(j).getDerivee().E.get(k));

                    }
                    else
                    {
                        chrenoliste.get(i).locauxChreno.get(loc).module=chrenoliste.get(i).Exam_m_c.get(j).getDerivee();
                        chrenoliste.get(i).Exam_m_c.get(j).LocalM.add(chrenoliste.get(i).locauxChreno.get(loc));
                        loc++;
                        chrenoliste.get(i).locauxChreno.get(loc).liste_E_local.add(chrenoliste.get(i).Exam_m_c.get(j).getDerivee().E.get(k));
                        cap+=chrenoliste.get(i).locauxChreno.get(loc).getCapacite();
                    }
                }
                chrenoliste.get(i).locauxChreno.get(loc).module=chrenoliste.get(i).Exam_m_c.get(j).getDerivee();
                chrenoliste.get(i).Exam_m_c.get(j).LocalM.add(chrenoliste.get(i).locauxChreno.get(loc));
                loc++;
            }
            loc=0;
        }

    }


    public static void affecterRepresentant()
    {
        ajouterRepresentant();
        Representant_adm temp;
        for (int i = 0; i < chrenoliste.size(); i++)
        {
            for(int j=0; j<chrenoliste.get(i).locauxChreno.size(); j++)
            {
                if(chrenoliste.get(i).locauxChreno.get(j).liste_E_local.size()!=0)
                {

                    chrenoliste.get(i).locauxChreno.get(j).representant=representant.get(j);

                }
                temp = representant.get(j);
                representant.remove(j);
                representant.add(temp);

            }


        }
    }

    public static void ResponsableChreno()
    {
        for (int i = 0; i < chrenoliste.size(); i++)
        {
            chrenoliste.get(i).resp.clear();
            for(int t=0; t<chrenoliste.get(i).Exam_m_c.size(); t++)
            {
                chrenoliste.get(i).Exam_m_c.get(t).getDerivee().affecterResponsable();
                if(chrenoliste.get(i).Exam_m_c.get(t).getDerivee().responsable!=null)
                    chrenoliste.get(i).resp.add(chrenoliste.get(i).Exam_m_c.get(t).getDerivee().responsable);
            }
        }
    }

    public static void EchangerContenu(List<Professeur> resp)
    {
        List<Professeur> surv2=new ArrayList<>();

        List<String> respTemp=new ArrayList<>();

        for(Professeur p:resp)
        {
            respTemp.add(p.getNomComplet());
        }

        List<String> survTepm=new ArrayList<>();
        for(Professeur p:surveillant)
        {
            survTepm.add(p.getNomComplet());
        }
        for(int i=0; i< surveillant.size(); i++)
        {
            if(respTemp.contains(survTepm.get(i)))
                surv2.add(surveillant.get(i));
        }
        surveillant.removeAll(surv2);
        surveillant.addAll(surv2);
    }



    public static void affecterSurveillants()
    {
        int surv=0;
        ajouterSurveillant();
        ResponsableChreno();
        ajouterSurveillant();
        for (int i = 0; i < chrenoliste.size(); i++)
        {

            EchangerContenu(chrenoliste.get(i).resp);

            for(int j=0; j<chrenoliste.get(i).locauxChreno.size(); j++)
            {
                if(!chrenoliste.get(i).locauxChreno.get(j).liste_E_local.isEmpty())
                {
                    if(chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Forum")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Amphi A")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Amphi B")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Amphi E")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Amphi C")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Amphi D")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("Salle psi")||chrenoliste.get(i).locauxChreno.get(j).getNom().equals("L2"))
                    {
                        chrenoliste.get(i).locauxChreno.get(j).surveillant3=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant3=surveillant.get(surv);
                        surv++;
                        if(surv==surveillant.size())
                            surv=0;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant2=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant2=surveillant.get(surv);
                        surv++;
                        if(surv==surveillant.size())
                            surv=0;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant1=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant1=surveillant.get(surv);
                        surv++;
                        if(surv==surveillant.size())
                            surv=0;
                    }
                    else
                    {
                        chrenoliste.get(i).locauxChreno.get(j).surveillant3=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant2=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant2=surveillant.get(surv);
                        surv++;
                        if(surv==surveillant.size())
                            surv=0;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant1=null;
                        chrenoliste.get(i).locauxChreno.get(j).surveillant1=surveillant.get(surv);
                        surv++;
                        if(surv==surveillant.size())
                            surv=0;
                    }
                }

            }

        }
    }




    public static void créerLesJours()
    {
        if(chrenoliste.size()%4==0)
        {
            for(int i=0; i<chrenoliste.size()/4; i++)
                jours.add(new jour());
        }
        else
        {
            for(int i=0; i<(chrenoliste.size()/4)+1; i++)
                jours.add(new jour());
        }

    }

    public static void chrenoDuJour()
    {
        créerLesJours();
        List<String> heures=  Arrays.asList("08:00-10:00","10:30-12:30","14:30-16:30","17:00-19:00");
        int k=0;
        int t =0;
        for (int i =0; i <jours.size(); i++)
        {
            for(int j=k; j<k+4 && j<chrenoliste.size(); j++)
            {
                chrenoliste.get(j).setHeure(heures.get(t));
                jours.get(i).chrenosDujour.add(chrenoliste.get(j));
                t++;
            }
            k+=4;
            t=0;
        }
    }


    public static void ajouterSurveillant()
    {
        surveillant.clear();
        Connection connexion;
        Statement statement ;
        java.sql.ResultSet  result;

        try
        {

            Class.forName("com.mysql.jdbc.Driver");

            connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");

            String query="SELECT * FROM personnel WHERE TypeP='E' " ;
            statement    = connexion.createStatement();
            result = statement.executeQuery(query);
            while(result.next())
            {
                String  Nom =result.getString("NomP");
                String Prenom =result.getString("PrénomP");
                surveillant.add(new Professeur( Nom+" "+ Prenom));
            }


            connexion.close();
        }
        catch(SQLException ex )
        {
            System.out.println( ex.getMessage());

        }
        catch (ClassNotFoundException h)
        {
        }
    }


    static int num;
    public static  void ajouterRepresentant()
    {
        representant.clear();
        Connection connexion;
        Statement statement ;
        java.sql.ResultSet  result;

        try
        {

            Class.forName("com.mysql.jdbc.Driver");
            connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");
            String query="SELECT * FROM personnel WHERE TypeP='A'" ;
            statement    = connexion.createStatement();
            result = statement.executeQuery(query);
            while(result.next())
            {
                //int cin =result.getInt("CIN");
                String  Nom =result.getString("NomP");
                String Prenom =result.getString("PrénomP");
                representant.add(new Representant_adm(Nom+" "+Prenom));
            }

            connexion.close();
        }
        catch(SQLException ex )
        {
            System.out.println( ex.getMessage());

        }
        catch (ClassNotFoundException h)
        {

            h.printStackTrace();
        }
    }

    public static int totalE()
    {
        int total=0;
        Connection connexion;
        Statement statement ;
        ResultSet result;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");
            String query= "SELECT COUNT(*) AS nombreTotal FROM etudiant";
            statement= connexion.createStatement();
            result = statement.executeQuery(query);
            while(result.next())
            {
                total =result.getInt("nombreTotal");
            }

            connexion.close();
        }
        catch(SQLException ex )
        {
            System.out.println( ex.getMessage());
        }

        catch (ClassNotFoundException e)
        {

            e.printStackTrace();
        }
        return total;
    }

    public static void AjouterLesModules()
    {
        liste_module.clear();

        java.sql.Connection connexion;
        java.sql.Statement statement ;
        ResultSet result;


        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/dt","root","");
            String query="SELECT * FROM module where Désignation not like 'Projet de fin d%'" ;
            statement= connexion.createStatement();
            result = statement.executeQuery(query);

            while(result.next())
            {
                String cod =result.getString("CodeM");
                String Désignation =result.getString("Désignation");
                String CodeF =result.getString("CodeF");
                String SemestreM = result.getString("SemestreM");
                char parcour=cod.charAt(0);
                char A =cod.charAt(1);
                char  B =cod.charAt(2);
                char C=cod.charAt(3);
                String parc=Character.toString(parcour);
                int A1=Integer.parseInt(Character.toString(A));
                int B1=Integer.parseInt(Character.toString(B));
                int C1=Integer.parseInt(Character.toString(C));


                Planning.liste_module.add(new Module(new Code(parc, A1, B1, C1),Désignation, CodeF+"/"+SemestreM) );
            }
            connexion.close();
        }
        catch(SQLException ex )
        {
            System.out.println( ex.getMessage());

        }
        catch (ClassNotFoundException e)
        {
        }

    }

    public static void eleminerModuleZeroEtudiant()
    {
        List<Module> temp=new ArrayList<>();
        for(Module m :Planning.liste_module)
        {
            if(m.E.isEmpty())
                temp.add(m);
        }
        liste_module.removeAll(temp);

    }

    public static boolean areAdjacent(List<List<Integer>> k, int i, int j)
    {

        if (i<k.size() && j<k.size() && k.get(i).get(j) == 0)

            return false;

        else
            return true ;
    }


    static int[] degreesArray;
    static int mapSize;
    List<Integer> les_couleurs;

    public static void coloration()
    {
        mapSize = Planning.incompatibiltyMatrice.size();
        degreesArray = Planning.calculateVerticesDegrees(Planning.liste_module);

        Map<Integer, Integer> resultingColor = new LinkedHashMap<>();

        List<Integer> coloredVertices = new ArrayList<>();

        int[] coloring = new int[mapSize];

        for (int i = 0; i < mapSize; i++)
        {
            coloring[i] = -1;
        }
        List<Integer> notColored = new ArrayList<>();

        for (int i = 0; i < mapSize; i++)
        {
            notColored.add(i);
        }

        int highestDegreeVertex = Planning.getHighestDegreeVertex(degreesArray);

        coloring[highestDegreeVertex] = 0;

        liste_module.get(highestDegreeVertex).setCouleur(col);
        col++;
        coloredVertices.add(highestDegreeVertex);

        resultingColor.put(highestDegreeVertex, 0);

        notColored.remove(highestDegreeVertex);

        int  m = 0;

        while (notColored.size() >0)
        {

            int vertice = Planning.getHighestSaturation(Planning.incompatibiltyMatrice, coloring);


            while (resultingColor.containsKey(vertice))
            {
                vertice = Planning.getHighestSaturation(Planning.incompatibiltyMatrice, coloring);

                break;
            }

            boolean[] coresDisponiveis = new boolean[mapSize];
            for (int j = 0; j < mapSize; j++)
            {
                coresDisponiveis[j] = true;

            }

            int lastColor = 0;
            for (int t = 0; t < coloredVertices.size(); t++)
            {

                int currentVertex = coloredVertices.get(t);
                if (Planning.areAdjacent(Planning.incompatibiltyMatrice, vertice, currentVertex))
                {
                    int color = resultingColor.get(currentVertex);
                    coresDisponiveis[color] = false;
                }
            }
            for (int j = 0; j < coresDisponiveis.length; j++)
            {
                if (coresDisponiveis[j])
                {

                    lastColor = j;
                    break;
                }
            }
            resultingColor.put(vertice, lastColor);
            coloredVertices.add(vertice);
            coloring[vertice] = lastColor;
            notColored.remove(highestDegreeVertex);
        }
        int lescouleurs;

        for(int h=0; h<liste_module.size(); h++)
        {
            if(resultingColor.size()>h)
            {
                lescouleurs = resultingColor.get(h);
                liste_module.get(h).setCouleur(lescouleurs);
            }
            else
            {
                liste_module.get(h).setCouleur(-1);
                m++;
            }
        }
    }

    public static int getHighestSaturation(List< List<Integer>> graph, int[] coloring)
    {
        int maxSaturation = 0;
        int vertexWithMaxSaturation = 0;
        int length = graph.size();
        for (int i = 0; i < length; i++)
        {
            if (coloring[i] == -1)
            {
                Set<Integer> colors = new TreeSet<>();
                for (int j = 0; j < length; j++)
                {
                    if (Planning.areAdjacent(graph, i, j) && coloring[j] != -1)
                    {
                        colors.add(coloring[j]);
                    }
                }
                int tempSaturation = colors.size();
                if (tempSaturation > maxSaturation)
                {
                    maxSaturation = tempSaturation;
                    vertexWithMaxSaturation = i;
                }
                else if (tempSaturation == maxSaturation && degreesArray[i] >= degreesArray[vertexWithMaxSaturation])
                {
                    vertexWithMaxSaturation = i;
                }
            }
        }
        return vertexWithMaxSaturation;
    }

    public static int [] calculateVerticesDegrees(List<Module>  liste)
    {
        int[] degreeArray = new int[liste.size()];

        for (int k=0; k<liste.size(); k++ )
        {
            degreeArray[k] = liste.get(k).getDegre();
        }

        return degreeArray;
    }

    public static int getHighestDegreeVertex(int[] degreeArray)
    {
        int highestDegVertexIndex = 0;

        for (int i = 0; i < degreeArray.length; i++)
        {
            if (degreeArray[i] > degreeArray[highestDegVertexIndex])
                highestDegVertexIndex = i;
        }

        return highestDegVertexIndex;
    }

    public static int maxColor(List<Module > m)
    {
        int highestColor = 0;
        for (int i = 0; i < m.size(); i++)
        {
            if (m.get(i).getCouleur() > highestColor)
                highestColor =m.get(i).getCouleur() ;
        }
        return highestColor+1;
    }


    public static void planningAdministation(String semestre,date dat) throws ParseException
    {

        Document document;
        //création de document
        document = new Document();
        document.setMargins(15, 15, 3, 3);
        try
        {
            String responsable="";
            String representant="";
            String code="";
            String heure="";
            String local="";
            String surv1="";
            String surv2="";
            String surv3="";
            String Designation="";
            String Section="";
            String s="";
            int b=4;
            String url1="planning";
            String url="Ressources\\images\\fst_logo.jpg";
            com.itextpdf.text.Image  image=com.itextpdf.text.Image.getInstance(url);
            PdfWriter.getInstance(document, new FileOutputStream(url1+"\\planning administration .pdf"));
            document.setPageSize(PageSize.LETTER.rotate());
            document.open();
            image.setAlignment(Image.MIDDLE);
            document.add( image );

            document.add( new Paragraph ("\n"));
            document.add(new LineSeparator(4f, 100, null, 0, -5));
            if (semestre.contentEquals("1"))
                s="Planning des examens  de la session d'Automne, année uneversitaire ";
            else
                s="Planning des examens  de la session de Printemps, année uneversitaire ";
            String s1=s+(dat.annee-1)+"/"+(dat.annee)+".";

            Paragraph ph= new Paragraph(s1, FontFactory.getFont("Times New Roman",22,Font.BOLD));
            ph.setAlignment(Element.ALIGN_CENTER);
            document.add(ph);
            document.add( new Paragraph ("\n"));

            for(int j=0; j<Planning.jours.size(); j++)
            {
                PdfPTable tab = new PdfPTable(7);
                tab.setWidthPercentage(100);
                float[] TAB= {60,270+150-90,130,120,120,90+90+80,280-80};
                tab.setWidths(TAB);

                PdfPCell cel1;
                String ss=  Integer.toString(dat.jour)+"-"+Integer.toString(dat.mois)+"-"+Integer.toString(dat.annee);
                cel1 = new PdfPCell(new Phrase(new Phrase("Jour : "+ss, FontFactory.getFont("Times New Roman",22,Font.BOLD))));
                cel1.setColspan(7);
                cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel1.setPaddingBottom(b);
                cel1.setPaddingTop(b);
                cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                tab.addCell(cel1);
                document.add( new Paragraph ("\n"));
                dat.jour++;
                if(dat.mois==1||dat.mois==3||dat.mois==5||dat.mois==7||dat.mois==8||dat.mois==10||dat.mois==12)
                {
                    if (dat.jour>31)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }
                }
                else if(dat.mois==2)
                {
                    if (dat.jour>29)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }

                }
                else
                {
                    if (dat.jour>30)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }

                }

                for(int t=0; t<Planning.jours.get(j).chrenosDujour.size(); t++)
                {
                    heure =Planning.jours.get(j).chrenosDujour.get(t).getHeure();

                    cel1 = new PdfPCell(new Phrase("Horaire : "+heure, FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel1.setColspan(7);
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(sandy);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    tab.addCell(cel1);


                    cel1 = new PdfPCell(new Phrase("Code", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Designation", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Responsable", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Section", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);


                    cel1 = new PdfPCell(new Phrase("Local", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Surveillants", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Representant Ad", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);


                    for(int p=0; p<Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.size(); p++)
                    {
                        if(!Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).liste_E_local.isEmpty())
                        {
                            Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.affecterResponsable();
                            code =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getCode_module().toString();
                            Designation =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getNom_module();
                            Section=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getParcours();

                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.responsable!=null)
                                responsable =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.responsable.getNomComplet();

                            else
                                responsable =" ";


                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p)!=null)
                                local =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).getNom();
                            else
                                local=" ";

                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant1!=null)
                                surv1=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant1.getNomComplet();
                            else
                                surv1=" ";

                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant2!=null)
                                surv2=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant2.getNomComplet();
                            else
                                surv2=" ";

                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant3!=null)
                                surv3=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).surveillant3.getNomComplet();
                            else
                                surv3=" ";
                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).representant!=null)
                                representant=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).representant.getNomComplet();
                            else
                                representant=" ";

                            cel1 = new PdfPCell(new Phrase(code));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(Designation ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(responsable ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(Section ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(local));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(surv1+"\n"+surv2+"\n"+surv3));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setVerticalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(representant));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);



                        }
                    }




                }
                document.add(tab);
                if(j<Planning.jours.size()-1)
                {
                    document.add( new Paragraph ("\n"));
                    document.add( new Paragraph ("\n"));
                    LineSeparator line=new LineSeparator(4f, 75, null, 0, -5);
                    line.setAlignment(Element.ALIGN_CENTER);
                    document.add(line);
                    document.add( new Paragraph ("\n"));
                    document.add( new Paragraph ("\n"));
                }
            }
            File fich;
            fich = new File(url1+"\\planning administration .pdf");
            Desktop.getDesktop().open(fich);
            document.close();
        }

        catch (DocumentException e)
        {

            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void planningEtudiant(String semestre,date dat) throws ParseException
    {

        Document document;

        //création de document
        document = new Document();
        document.setMargins(15, 15, 3, 3);
        try
        {

            String url1="planning";
            String url="Ressources\\images\\fst_logo.jpg";
            String responsable="";
            String code="";
            String heure="";
            String local="";
            String Designation="";
            String Section="";
            int b=4;
            String s="";
            com.itextpdf.text.Image  image=com.itextpdf.text.Image.getInstance(url);
            PdfWriter.getInstance(document, new FileOutputStream(url1+"\\planning Etudiants .pdf"));
            document.setPageSize(PageSize.LETTER.rotate());
            document.open();
            image.setAlignment(Image.MIDDLE);
            document.add( image );
            document.add( new Paragraph ("\n"));
            document.add(new LineSeparator(4f, 100, null, 0, -5));


            if (semestre.contentEquals("1"))
                s="Planning des examens  de la session d'Automne, année uneversitaire ";
            else
                s="Planning des examens  de la session de Printemps, année uneversitaire ";
            String s1=s+(dat.annee-1)+"/"+(dat.annee)+".";

            Paragraph ph= new Paragraph(s1, FontFactory.getFont("Times New Roman",22,Font.BOLD));
            ph.setAlignment(Element.ALIGN_CENTER);
            document.add(ph);
            document.add( new Paragraph ("\n"));




            for(int j=0; j<Planning.jours.size(); j++)
            {
                PdfPTable tab = new PdfPTable(5);
                tab.setWidthPercentage(100);
                float[] TAB= {60+100,270+150-90+200,130+100,120+100+20,120+100};
                tab.setWidths(TAB);

                PdfPCell cel1;
                String ss=  Integer.toString(dat.jour)+"-"+Integer.toString(dat.mois)+"-"+Integer.toString(dat.annee);
                cel1 = new PdfPCell(new Phrase(new Phrase("Jour : "+ss, FontFactory.getFont("Times New Roman",22,Font.BOLD))));
                cel1.setColspan(7);
                cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel1.setPaddingBottom(b);
                cel1.setPaddingTop(b);
                tab.addCell(cel1);

                document.add( new Paragraph ("\n"));
                dat.jour++;
                if(dat.mois==1||dat.mois==3||dat.mois==5||dat.mois==7||dat.mois==8||dat.mois==10||dat.mois==12)
                {
                    if (dat.jour>31)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }
                }
                else if(dat.mois==2)
                {
                    if (dat.jour>29)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }

                }
                else
                {
                    if (dat.jour>30)
                    {
                        dat.jour=1;
                        dat.mois++;

                    }

                }

                for(int t=0; t<Planning.jours.get(j).chrenosDujour.size(); t++)
                {
                    heure =Planning.jours.get(j).chrenosDujour.get(t).getHeure();

                    cel1 = new PdfPCell(new Phrase("Horaire : "+heure, FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel1.setColspan(7);
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(sandy);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);

                    tab.addCell(cel1);


                    cel1 = new PdfPCell(new Phrase("Code", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Designation", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Responsable", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Section", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);


                    cel1 = new PdfPCell(new Phrase("Local", FontFactory.getFont("Times New Roman",12,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setBackgroundColor(wheat);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    tab.addCell(cel1);

                    for(int p=0; p<Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.size(); p++)
                    {
                        if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).liste_E_local.size()!=0)
                        {
                            Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.affecterResponsable();
                            code =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getCode_module().toString();
                            Designation =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getNom_module();
                            Section=Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.getParcours();

                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.responsable!=null)
                                responsable =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).module.responsable.getNomComplet();

                            else
                                responsable =" ";


                            if(Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p)!=null)
                                local =Planning.jours.get(j).chrenosDujour.get(t).locauxChreno.get(p).getNom();
                            else
                                local=" ";

                            cel1 = new PdfPCell(new Phrase(code));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(Designation ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(responsable ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(Section ));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);

                            cel1 = new PdfPCell(new Phrase(local));
                            cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cel1.setPaddingBottom(b);
                            cel1.setPaddingTop(b);
                            tab.addCell(cel1);
                        }
                    }

                }
                document.add(tab);
                if(j<Planning.jours.size()-1)
                {
                    document.add( new Paragraph ("\n"));
                    document.add( new Paragraph ("\n"));
                    LineSeparator line=new LineSeparator(4f, 75, null, 0, -5);
                    line.setAlignment(Element.ALIGN_CENTER);
                    document.add(line);
                    document.add( new Paragraph ("\n"));
                    document.add( new Paragraph ("\n"));
                }
            }
            File fich;
            fich = new File(url1+"\\planning Etudiants .pdf");
            Desktop.getDesktop().open(fich);
            document.close();
        }

        catch (DocumentException | IOException e)
        {
        }
    }

    public static void  afficherLiseEtudiant(int jour,int chreno,int p)
    {
        if(!Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.isEmpty())
        {
            Document document;
            String url1="planning";
            String url="Ressources\\images\\fst_logo.jpg";
            try
            {
                com.itextpdf.text.Image  image=com.itextpdf.text.Image.getInstance(url);
                String local;
                String code;
                String respo;
                String design;
                int b=5;
                if(!Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.isEmpty())
                {
                    local= Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).getNom();
                    Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).module.affecterResponsable();
                    code= Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).module.getCode_module().toString();
                    if(jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).module.responsable!=null)
                        respo= jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).module.responsable.getNomComplet();
                    else
                        respo="";
                    design= Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).module.getNom_module();
                    document = new Document();
                    document.setMargins(15, 15, 3, 3);
                    PdfWriter.getInstance(document, new FileOutputStream(url1+"\\Etudiants de "+local+" chreno "+(chreno+1)+" jour "+(jour+1)+".pdf"));
                    document.setPageSize(PageSize.LETTER.rotate());

                    document.open();

                    image.setAlignment(Image.MIDDLE);
                    document.add( image );
                    document.add( new Paragraph ("\n"));
                    document.add(new LineSeparator(4f, 100, null, 0, -5));
                    document.add( new Paragraph ("\n"));
                    document.add( new Paragraph ("\n"));

                    String dat=Planning.jours.get(jour).chrenosDujour.get(chreno).getHeure();

                    PdfPTable tab = new PdfPTable(3);
                    PdfPTable tab1 = new PdfPTable(5);
                    tab.setWidthPercentage(100);
                    tab1.setWidthPercentage(100);

                    PdfPCell cel1;
                    PdfPCell cel2;


                    cel2= new PdfPCell(new Phrase("Code Module", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setBackgroundColor(sandy);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase("Désignation", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setBackgroundColor(sandy);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase("Responsable", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setBackgroundColor(sandy);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase("Local", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setBackgroundColor(sandy);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);
                    cel2 = new PdfPCell(new Phrase("Horaire", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setBackgroundColor(sandy);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase(code, FontFactory.getFont("Times New Roman",15,null)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase(design, FontFactory.getFont("Times New Roman",15,null)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);

                    cel2 = new PdfPCell(new Phrase(respo, FontFactory.getFont("Times New Roman",15,null)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);


                    cel2 = new PdfPCell(new Phrase(local, FontFactory.getFont("Times New Roman",15,null)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);
                    tab1.addCell(cel2);
                    cel2 = new PdfPCell(new Phrase(dat, FontFactory.getFont("Times New Roman",15,null)));
                    cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel2.setPaddingBottom(b);
                    cel2.setPaddingTop(b);

                    tab1.addCell(cel2);


                    document.add(tab1);
                    document.add( new Paragraph ("\n"+"\n"));
                    cel1 = new PdfPCell(new Phrase("CNE", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    cel1.setBackgroundColor(wheat);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Nom et Prénom", FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    cel1.setBackgroundColor(wheat);
                    tab.addCell(cel1);

                    cel1 = new PdfPCell(new Phrase("Numero de l'Examen",  FontFactory.getFont("Times New Roman",17,Font.BOLD)));
                    cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cel1.setPaddingBottom(b);
                    cel1.setPaddingTop(b);
                    cel1.setBackgroundColor(wheat);
                    tab.addCell(cel1);

                    BaseColor color =null;

                    for(int k=0; k<Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.size(); k++)
                    {
                        if(k%2==0)
                            color=BaseColor.WHITE;
                        else
                            color =smock;

                        long CNE=Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.get(k).getCne();
                        String Nom=	Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.get(k).getNom();
                        String Prénom=Planning.jours.get(jour).chrenosDujour.get(chreno).locauxChreno.get(p).liste_E_local.get(k).getPrenom();

                        cel1 = new PdfPCell(new Phrase(Long.toString(CNE), FontFactory.getFont("Times New Roman",15,null)));
                        cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cel1.setPaddingBottom(b);
                        cel1.setPaddingTop(b);
                        cel1.setBackgroundColor(color);
                        tab.addCell(cel1);

                        cel1 = new PdfPCell(new Phrase(Nom +" "+ Prénom, FontFactory.getFont("Times New Roman",15,null)));
                        cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cel1.setPaddingBottom(b);
                        cel1.setPaddingTop(b);
                        cel1.setBackgroundColor(color);
                        tab.addCell(cel1);

                        cel1 = new PdfPCell(new Phrase(Integer.toString((k+1)), FontFactory.getFont("Times New Roman",15,null)));
                        cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cel1.setPaddingBottom(b);
                        cel1.setPaddingTop(b);
                        cel1.setBackgroundColor(color);
                        tab.addCell(cel1);


                    }
                    document.add(tab);
                    File fich;
                    fich = new File(url1+"\\Etudiants de "+local+" chreno "+(chreno+1)+" jour "+(jour+1)+".pdf");
                    Desktop.getDesktop().open(fich);
                    document.close();
                }

            }
            catch (DocumentException | IOException e)
            {
            }
        }
    }

    static List<String> jour =new ArrayList<>();
    public static void affecterDonner()
    {
        for(int i=0; i<Planning.jours.size(); i++)
        {
            jour.add(Integer.toString((i+1)));
        }

        for(int i=0; i<Planning.amphi.size(); i++)
        {
            String nom=Planning.amphi.get(i).getNom();
            locaux.add(nom);
        }
    }

}

