package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.logging.Level;
import java.util.logging.Logger;
import xml.XMLUtil;



public class EditeurDico {

    //attributs

    //liste de différents niveau
    //sans doublons 
    private Set<String> listeNiveau1;
    private Set<String> listeNiveau2;
    private Set<String> listeNiveau3;
    private Set<String> listeNiveau4;
    private Set<String> listeNiveau5;

    //document DOM
    public Document doc;


    //constructeur
    public EditeurDico(){

        //instantiation des listes
        listeNiveau1 = new HashSet<String>();
        listeNiveau2 = new HashSet<String>();
        listeNiveau3 = new HashSet<String>();
        listeNiveau4 = new HashSet<String>();
        listeNiveau5 = new HashSet<String>();
    }


    //cette methode lit le fichier fourni en utilisant DOM
    public void lireDOM(String fichier) throws SAXException, IOException{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      try {

          DocumentBuilder db = dbf.newDocumentBuilder();

          this.doc = db.parse(new File(fichier));

          this.doc.getDocumentElement().normalize();

          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
          System.out.println("------");

        NodeList mots = doc.getElementsByTagName("tux:mot");
        //System.out.println(" - La taille de ma liste de mots est de : "+ mots.getLength() +"\n");



        for (int temp = 0; temp < mots.getLength(); temp++) {

            Node node = (Node) mots.item(temp);


            Element element = (Element) node;

                // get staff's attribute
            String niveau = element.getAttribute("niveau");

            switch(niveau){
                case "1":
                    System.out.println(" - J'ajoute le mot (1) : "+ node.getTextContent() +"\n");
                    ajouteMotADico(niveau, node.getTextContent());
                break;
                case "2":
                    System.out.println(" - J'ajoute le mot (2) : "+ node.getTextContent() +"\n");
                    ajouteMotADico(niveau, node.getTextContent());
                break;
                case "3":
                    System.out.println(" - J'ajoute le mot (3) : "+ node.getTextContent() +"\n");
                    ajouteMotADico(niveau, node.getTextContent());
                break;
                case "4":
                    System.out.println(" - J'ajoute le mot (4) : "+ node.getTextContent() +"\n");
                    ajouteMotADico(niveau, node.getTextContent());
                    break;
                case "5":
                    System.out.println(" - J'ajoute le mot (5) : "+ node.getTextContent() +"\n");
                    ajouteMotADico(niveau, node.getTextContent());
                break;
                }
                

        }

    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    }

    }



    //fournie
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(EditeurDico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //ajoute un mot dans la liste correspondant à son niveau
    public void ajouteMotADico(String niveau, String mot){
        switch (niveau){

            case "1":
                listeNiveau1.add(mot);
            break;
            case "2":
                listeNiveau2.add(mot);
            break;
            case "3":
                listeNiveau3.add(mot);
            break;
            case "4":
                listeNiveau4.add(mot);
            break;
            case "5":
                listeNiveau5.add(mot);
            break;
            default:
                break;
        }
    }


    //vérifie le niveau
    private int verifieNiveau(int niveau){
        
        if(niveau<1 || niveau>5){
            return 1;
        }
        return niveau;
    }


    //cette methode génère un nouveau fichier dico grâce à l'utilisation de DOM
    public void ecrireDOM(String fichier) throws ParserConfigurationException, SAXException, IOException{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = dbf.newDocumentBuilder();
        this.doc = builder.newDocument();


        Node nDico = doc.createElement("tux:dictionnaire");
        doc.appendChild(nDico);

        for(String mot : listeNiveau1){
            Element nMot = doc.createElement("tux:mot");
            nDico.appendChild(nMot);

            Node motText = doc.createTextNode(mot);
            nMot.appendChild(motText);
            nMot.setAttribute("niveau", "1");
        }

        for(String mot : listeNiveau2){
            Element nMot = doc.createElement("tux:mot");
            nDico.appendChild(nMot);

            Node motText = doc.createTextNode(mot);
            nMot.appendChild(motText);
            nMot.setAttribute("niveau", "2");
        }

        for(String mot : listeNiveau3){
            Element nMot = doc.createElement("tux:mot");
            nDico.appendChild(nMot);

            Node motText = doc.createTextNode(mot);
            nMot.appendChild(motText);
            nMot.setAttribute("niveau", "3");
        }

        for(String mot : listeNiveau4){
            Element nMot = doc.createElement("tux:mot");
            nDico.appendChild(nMot);

            Node motText = doc.createTextNode(mot);
            nMot.appendChild(motText);
            nMot.setAttribute("niveau", "4");
        }

        for(String mot : listeNiveau5){
            Element nMot = doc.createElement("tux:mot");
            nDico.appendChild(nMot);

            Node motText = doc.createTextNode(mot);
            nMot.appendChild(motText);
            nMot.setAttribute("niveau", "5");
        }

        toXML("src/Data/xml/dico.xml");
    }



}