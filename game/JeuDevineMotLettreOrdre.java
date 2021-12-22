package game;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class JeuDevineMotLettreOrdre extends Jeu{
    // le nombre de lettres qui ne sont pas a la bonne position
    private int nbLettresRestantes;
    private Chronometre chrono;
    
    
    public JeuDevineMotLettreOrdre() throws SAXException, IOException, ParserConfigurationException{
        super();
    }
    
    @Override
    protected void demarrePartie(Partie partie, int limite){
        this.chrono = new Chronometre(limite);
        // au debut le nombre de lettres restantes est egal a la longeur du mot 
        this.nbLettresRestantes = getLongeurMot();
        this.chrono.start();
    }
     
    //renvoie vrai si le mot a été retrouvé ou le temps s'est ecoulé
    @Override
    protected boolean appliqueRegles(Partie partie, char[] lettresDansLordre){

        boolean fini = false;

        int lettres_restantes = getLongeurMot(); // longueur du mot

        //les lettre sont trié en fonction de leur position
        Collections.sort(lettres);

        //initialise et instancie des lettres à des positions aléatoires
        for(int i=0; i<lettres.size(); i++){ // taille de la liste = longueur du mot
            if(lettres.get(i).getLetter() == lettresDansLordre[i] && lettres.get(i).getY() < tux.getY() + 5){
                lettres_restantes--;
            }
        }

        nbLettresRestantes=lettres_restantes;

        chrono.stop();
        if(chrono.getSeconds() == chrono.getLimite() || nbLettresRestantes==0){
            fini = true;
        }


        return fini;
    }  
    // enregistre les informations de la partie
    @Override
    protected void terminePartie(Partie partie){

        int trouvé = (int) (( ((double)(getLongeurMot()-nbLettresRestantes))/(double)(getLongeurMot()))*100);
        int temps = chrono.getSeconds();

        partie.setTrouve(trouvé);
        partie.setTemps(temps);
    }
    
    
    //accesseur de nombre de lettres restantes
    protected int getNbLettresRestantes(){
        return this.nbLettresRestantes;
    }
    //affiche le temps restant
    @Override
    public void afficherChrono(Partie partie){

        
        menuText.addText("Temps restant : "+ (chrono.getLimite()-chrono.getSeconds())+ " secondes", "chrono", 200, 100);
        menuText.getText("chrono").display();
        env.advanceOneFrame();
        menuText.getText("chrono").clean();
        menuText.popText("chrono");

    }


    

}