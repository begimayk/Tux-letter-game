package game;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xml.XMLUtil;

public class Profil {

    private String nom;
    private String avatar;
    private String anniversaire;

    private ArrayList<Partie> parties;

    public Document _doc;

    //constructeur par défaut
    public Profil(){
        nom="Bob";
        anniversaire="2000-01-01";

        
    }


    //constructeur
    public Profil(String nomJoueur, String annivJoueur){
        nom=nomJoueur;
        anniversaire=annivJoueur;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = dbf.newDocumentBuilder();
            _doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        parties = new ArrayList<Partie>();

                //on crée l'element recine profil du fichier 
                Node profil = _doc.createElement("profil");
                //on ajoute cet element au document 
                _doc.appendChild(profil);
        
                //on crée l'element nom (balise nom)
                Element nom = _doc.createElement("nom");
        
                //on crée le contenu de cet element
                Node textNom = _doc.createTextNode(this.nom);
        
                //on ajoute cet element a l'element racine profil
                profil.appendChild(nom);
        
                //on ajoute le contenu de la balise a l'element nom
                nom.appendChild(textNom);
        
                //on crée l'element anniversaire
                Element anniversaire = _doc.createElement("anniversaire");
        
                //on crée le node contenu de cet element et on y place la donnée saisie par l'utilisateur
                Node textAnniversaire = _doc.createTextNode(this.anniversaire);
        
                //on ajoute ces elements a la racine profil
                profil.appendChild(anniversaire);
                anniversaire.appendChild(textAnniversaire);
        
                //on crée l'element partie ou l'on mettra les parties de la liste 
               Element nParties = this._doc.createElement("parties");
        
                // on ajoute cet element a l'element racine
               profil.appendChild(nParties);

               toXML("src/Data/xmlJoueurs/"+ this.nom + ".xml");

    }



    // Cree un DOM à partir d'un fichier XML
    public Profil(String nomFichier) {

        _doc = fromXML("src/Data/xmlJoueurs/" + nomFichier + ".xml");

        parties = new ArrayList<Partie>();

        // on cherche le nom du joeurs dans son fichier xml
        this.nom = _doc.getElementsByTagName("nom").item(0).getTextContent();
        
        //idem pour son anniversaire 
        this.anniversaire = _doc.getElementsByTagName("anniversaire").item(0).getTextContent();

        //pour reecrire les parties déjà presentes dans le document on les sauvegarde dans la liste
        // des parties du profil

        //La liste des parties dans le document
        NodeList nParties = _doc.getElementsByTagName("partie");
        for (int i=0; i<nParties.getLength(); i++){

            //on caste la partie en element pour faciliter les manipulations
            Element ePartie = (Element) nParties.item(i);

            // on treansforme la date du fichier xml en date java avec la fonction fornie
            String date = xmlDateToProfileDate(nParties.item(i).getAttributes().item(0).getTextContent());

            // si la partie est perdue 
            if(ePartie.hasAttribute("trouvé")){

                //on cherche le mot de la partie
                String mot = nParties.item(i).getChildNodes().item(1).getTextContent();
                
                //on cherche le niveau de la partie
                int niveau = Integer.parseInt(nParties.item(i).getChildNodes().item(1).getAttributes().item(0).getTextContent());
                
                //on inctancie une nouvelle partie avec les données retrouvées
                Partie partie = new Partie(date, mot, niveau);

                //on cherche le pourcentage du mot trouvé
                int nTrouve = Integer.parseInt(nParties.item(i).getAttributes().item(1).getTextContent());
                
                //on rajoute cette donnée a la oartie
                partie.setTrouve(nTrouve);

                //on rejoute la partie dans la liste des parties
                this.parties.add(partie);

            //si la partie est gagnée
            }else{

                //on cherche le temps et on le convertie en integer
                int nTemps = Integer.parseInt(nParties.item(i).getChildNodes().item(1).getTextContent());

                //on cherche le mot 
                String mot = nParties.item(i).getChildNodes().item(3).getTextContent();
               
                //on cherche le mot et on e convertie en integer
                int niveau = Integer.parseInt(nParties.item(i).getChildNodes().item(3).getAttributes().item(0).getTextContent());

                //on instancie una partie connaissant ces données
                Partie partie = new Partie(date, mot, niveau);

                //on ajoute le temps a la partie instanciée
                partie.setTemps(nTemps);

                //on ajoute cette partie dans la liste des parties jouées
                this.parties.add(partie);
            }

        }
    }
    

    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }


    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }


//une fonction qui retourne vrai si un joueurs existe
    public boolean charge(String nomJoueur){
        boolean charge = false;

        //si le fichier avec ce nom existe retourne vrai
        File f = new File("src/Data/xmlJoueurs/" + nomJoueur + ".xml");
        if(f.exists() && !f.isDirectory()) { 
            charge = true;
        }
        return charge;
    }

    public void sauvegarde() throws TransformerConfigurationException{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = dbf.newDocumentBuilder();
            _doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        //on crée l'element recine profil du fichier 
        Node profil = _doc.createElement("profil");
        //on ajoute cet element au document 
        _doc.appendChild(profil);

        //on crée l'element nom (balise nom)
        Element nom = _doc.createElement("nom");

        //on crée le contenu de cet element
        Node textNom = _doc.createTextNode(this.nom);

        //on ajoute cet element a l'element racine profil
        profil.appendChild(nom);

        //on ajoute le contenu de la balise a l'element nom
        nom.appendChild(textNom);

        //on crée l'element anniversaire
        Element anniversaire = _doc.createElement("anniversaire");

        //on crée le node contenu de cet element et on y place la donnée saisie par l'utilisateur
        Node textAnniversaire = _doc.createTextNode(this.anniversaire);

        //on ajoute ces elements a la racine profil
        profil.appendChild(anniversaire);
        anniversaire.appendChild(textAnniversaire);

        //on crée l'element partie ou l'on mettra les parties de la liste 
       Element nParties = this._doc.createElement("parties");

        // on ajoute cet element a l'element racine
       profil.appendChild(nParties);
       
    
       for(Partie p : this.parties){

           if(p.getTrouve() ==100){
               //si la partie est gagnée 

               // on crée l'element partie
               Element nPartie = _doc.createElement("partie");

               //on convertie la date java en date xml
               String date = profileDateToXmlDate(p.getDate());

               //on ajoute attribut date a l'element partie
               nPartie.setAttribute("date", date);

               //on ajoute la partie a l'element parties
               nParties.appendChild(nPartie);

               //on crée l'element temps
               Element nTemps = _doc.createElement("temps");

               //on ajoute l'lement temps dans l'element partie
               nPartie.appendChild(nTemps);

               //on crée le contenu de l'element temps
               Node textTemps = _doc.createTextNode(p.getTemps() + "");

               //on ajoute ce contenu a l'element temps
               nTemps.appendChild(textTemps);

               //on crée l'element mot
               Element nmot = _doc.createElement("mot");

               //on ajoute cet element mot a l'element partie
               nPartie.appendChild(nmot);

               //on crée le contenu du mot
               Node textMot = _doc.createTextNode(p.getMot());

               //on charge ce contenu dans l'element mot
               nmot.appendChild(textMot);

               //on ajoute dans l'element mot l'attribut temps
               nmot.setAttribute("niveau", p.getNiveau() + "");
           }
           else{

               //On crée l'element partie
               Element nPartie = _doc.createElement("partie");

               // on convertir une date java en date xml
               String date = profileDateToXmlDate(p.getDate());

               //on ajoute l'attribut date a l'element partie
               nPartie.setAttribute("date", date);

               //on ajoute l'attribut trouvé a l'element partie
               nPartie.setAttribute("trouvé", p.getTrouve() + "");

               //on ajoute l'element partie a l'element parties
               nParties.appendChild(nPartie);

               //on crée l'element mot
               Element nmot = _doc.createElement("mot");

               //on ajoute l'element mot dans l'element partie
               nPartie.appendChild(nmot);

               //on crée le contenu du mot 
               Node textMot = _doc.createTextNode(p.getMot());

               //on ajoute le contenu a l'element mot
               nmot.appendChild(textMot);

               //on ajoute un attribut niveau a l'element mot
               nmot.setAttribute("niveau", p.getNiveau() + "");
           }
       }

       toXML("src/Data/xmlJoueurs/" + this.nom + ".xml");

        //applique la transformation xslt pour generer le fchier html du profil (dans le dossier html)
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/Data/xslt/profil.xsl"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File("src/Data/xmlJoueurs/"+ this.nom +".xml"));
        try {
            transformer.transform(text, new StreamResult(new File("src/Data/html/" + this.nom  + ".html")));
        } catch (TransformerException e) {
            System.out.println("Erreur transformation xslt");
            e.printStackTrace();
        }
    }

   public String getNom() {
       return nom;
   }

   public void ajouterPartie(Partie partie){
        this.parties.add(partie);
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnniversaire() {
        return anniversaire;
    }

} 