package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class Dico extends DefaultHandler{

    //attributs

    //listes de mots de différentes niveau
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    
    //chemin du fichier dico.xml
    private String cheminFichierDico;

    //buffer pour SAX
    StringBuffer buffer;

    //attributs nécessaires pour SAX
    private boolean inDictionnaire=false;
    private boolean inMot=false;
    private int niveau;
    
    
    //constructeur
    public Dico(String cheminFichierDico){
        
        //initialisation 
        super();

        listeNiveau1 = new ArrayList<String>();
        listeNiveau2 = new ArrayList<String>();
        listeNiveau3 = new ArrayList<String>();
        listeNiveau4 = new ArrayList<String>();
        listeNiveau5 = new ArrayList<String>();

        
        this.cheminFichierDico = cheminFichierDico;
 
        
    }


    
    //cette methode lit le dictionnaire en utilisant DOM
    public void lireDictionnaireDOM() throws SAXException, IOException{
        
        String filename=cheminFichierDico;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      try {

          DocumentBuilder db = dbf.newDocumentBuilder();

          Document doc = db.parse(new File(filename));

          doc.getDocumentElement().normalize();

          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
          System.out.println("------");

        NodeList mots = doc.getElementsByTagName("tux:mot");
        System.out.println(" - La taille de ma liste de mots est de : "+ mots.getLength() +"\n");



        for (int temp = 0; temp < mots.getLength(); temp++) {

            Node node = mots.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // get staff's attribute
                String niveau = element.getAttribute("niveau");

                switch(niveau){
                    case "1":
                        System.out.println(" - J'ajoute le mot (1) : "+ node.getTextContent() +"\n");
                        ajouteMotADico(1, node.getTextContent());
                        break;
                    case "2":
                        System.out.println(" - J'ajoute le mot (2) : "+ node.getTextContent() +"\n");
                        ajouteMotADico(2, node.getTextContent());
                        break;
                    case "3":
                        System.out.println(" - J'ajoute le mot (3) : "+ node.getTextContent() +"\n");
                        ajouteMotADico(3, node.getTextContent());
                        break;
                    case "4":
                        System.out.println(" - J'ajoute le mot (4) : "+ node.getTextContent() +"\n");
                        ajouteMotADico(4, node.getTextContent());
                        break;
                    case "5":
                        System.out.println(" - J'ajoute le mot (5) : "+ node.getTextContent() +"\n");
                        ajouteMotADico(5, node.getTextContent());
                        break;
                }
                

            }
        }

    } catch (ParserConfigurationException e) {
        e.printStackTrace();
        }

    }




    //cette methode lit le dictionnaire en utilisant SAX
    public void lireDictionnaireSAX(){

        try{ 
			// création d'une fabrique de parseurs SAX 
			SAXParserFactory fabrique = SAXParserFactory.newInstance(); 

			// création d'un parseur SAX 
			SAXParser parseur = fabrique.newSAXParser(); 

			// lecture d'un fichier XML avec un DefaultHandler 
			//File fichier = new File(this.cheminFichierDico); 
			DefaultHandler handler = new DefaultHandler(){


                //lorsque le parseur SAX rencontre une balise ouvrante
                //
                //ici, on vérifie l'état de l'automate et on le met à jour
                //puis, dans le cas où on trouve un element mot, on récupère le niveau de ce mot dans l'attribut de la classe
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    buffer = new StringBuffer();
                    
                    if(qName.equals("tux:dictionnaire")){
                        inDictionnaire=true;
                        System.out.println("----début Dictionnaire----\n"); 
                    }

                    else if(qName.equals("tux:mot")){
                        inMot=true;
                        niveau = Integer.parseInt(attributes.getValue("niveau"));
                    }
                    
                    else{
                        System.out.println("Element ouvrant non reconnu\n"); 
                    }
                }
                
                //lorsque le parseur SAX rencontre une balise fermante
                //
                //ici, on change les etats de l'automate,
                //puis, on ajoute le mot aux listes de dictionnaire correspondant au niveau du mot
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if(qName.equals("tux:dictionnaire")){
                        inDictionnaire=false;
                        System.out.println("----fin Dictionnaire----\n"); 
                    }

                    else if(qName.equals("tux:mot")){
                        inMot=false;
                        System.out.println("mot (niveau : "+niveau+") : "+ buffer+"\n");
                        ajouteMotADico(niveau, buffer.toString());
                    }
                    
                    else{
                        System.out.println("Element fermant non reconnu\n");
                    }
                }
                    
                //lorsque le parseur SAX rencontre du texte
                //
                //ici, on copie le text dans le cas où on est dans un element mot
                //puis on l'ajoute dans le buffer
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    String motText="";
                    if(inDictionnaire){
                        if(inMot){
                            motText=new String(ch,start,length);
                            buffer.append(motText);
                        }
                    }

                }

                //lorsque le parseur SAX rencontre un début de document
                @Override
                public void startDocument() throws SAXException {
                    System.out.println("Début du parsing\n"); 
                }

                //lorsque le parseur SAX rencontre une fin de document
                @Override
                public void endDocument() throws SAXException {
                    System.out.println("Fin du parsing\n");
                
                }




            
            }; 
			parseur.parse(this.cheminFichierDico, handler); 
  
    //cas d'erreur
		}catch(ParserConfigurationException pce){ 
			System.out.println("Erreur de configuration du parseur"); 
			System.out.println("Lors de l'appel à newSAXParser()"); 
		}catch(SAXException se){ 
			System.out.println("Erreur de parsing"); 
			System.out.println("Lors de l'appel à parse()"); 
		}catch(IOException ioe){ 
			System.out.println("Erreur d'entrée/sortie"); 
			System.out.println("Lors de l'appel à parse()"); 
		} 


    }

    

    //methode qui récupère un mot dans la liste de niveau correspondant au niveau entrée en paramètre
    public String getMotDepuisListeNiveau(int niveau){
        

        switch (verifieNiveau(niveau)){

            case 1:
                return getMotDepuisListe(listeNiveau1);

            case 2:
                return getMotDepuisListe(listeNiveau2);

            case 3:
                return getMotDepuisListe(listeNiveau3);

            case 4:
                return getMotDepuisListe(listeNiveau4);

            case 5:
                return getMotDepuisListe(listeNiveau5); 

            default:
                return null;
        }
    }
    

    //methode qui ajoute un mot dans les listes 
    //prend en paramètre le mot et son niveau
    public void ajouteMotADico(int niveau, String mot){
        switch (verifieNiveau(niveau)){

            case 1:
                listeNiveau1.add(mot);
                break;

            case 2:
                listeNiveau2.add(mot);
                break;

            case 3:
                listeNiveau3.add(mot);
                break;

            case 4:
                listeNiveau4.add(mot);
                break;

            case 5:
                listeNiveau5.add(mot);
                break;

            default:
                break;
        }
    }
    

    //methode qui vérifie le niveau du mot entrée,
    // retourne par défaut 1 dans le cas où c'est incorrect
    private int verifieNiveau(int niveau){
        
        if(niveau<1 || niveau>5){
            return 1;
        }
        return niveau;
    } 
    

    //methode qui return un mot aléatoire dans une liste donnée
    private String getMotDepuisListe(ArrayList<String> list){
        int index = new Random().nextInt(list.size());
        String mot = list.get(index);
        return mot;
    }
    
    
    
    
}
    
    
    
