package game;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XMLUtil;


public class ConsulteProfil{

    //attributs
    private String nom;
    private String avatar;
    private String anniversaire;

    //liste de parties
    protected  ArrayList<Partie> parties;

    //document DOM
    public Document _doc;



    //constructeur
    public ConsulteProfil(String nom){

        //initialisation de la class
        this.nom = nom;
        parties = new ArrayList<Partie>();

    }


    //cette methode lit le profil du joueur de cette class grâce au nom du joueur, puis récupère ses informations dans le profil
    //
    //On utilise la lecture DOM d'un fichier
    public void lireProfil(){

        _doc = fromXML("src/Data/xmlJoueurs/" + nom + ".xml");
        NodeList nParties = _doc.getElementsByTagName("partie");
        for (int i=0; i<nParties.getLength(); i++){

            //on caste la partie en element pour faciliter les manipulations
            Element ePartie = (Element) nParties.item(i);

            // on treansforme la date du fichier xml en date java avec la fonction fornie
            String date = (nParties.item(i).getAttributes().item(0).getTextContent());

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


    //methode d'affichage des informations du joueur
    public String toString(int i, int j){
        String s = "Profil\n";
        s = s + "Nom:" + this.nom + "\n";

        this.anniversaire = _doc.getElementsByTagName("anniversaire").item(0).getTextContent();

        s = s + "Anniversaire : " + this.anniversaire + "\n";

        s = s + "Les parties jouées : \n"; 

        for(int n=i; n<j; n++){
            s = s + parties.get(n).toString();
        }

        return s;

    }

    public String afficher(){
        String s = "Profil\n";
        s = s + "Nom:" + this.nom + "\n";

        this.anniversaire = _doc.getElementsByTagName("anniversaire").item(0).getTextContent();

        s = s + "Anniversaire : " + this.anniversaire + "\n";

        s = s + "Les parties jouées : \n"; 

        for(Partie p : parties){
            s = s + p.toString();
        }

        return s;
    }


    // fourni
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }


    //methode qui renvoie vrai si un profil existe
    public boolean charge(String nomJoueur){
        boolean charge = false;

        //si le fichier avec ce nom existe retourne vrai
        File f = new File("src/Data/xmlJoueurs/" + nomJoueur + ".xml");
        if(f.exists() && !f.isDirectory()) { 
            charge = true;
        }
        return charge;
    }



    
}