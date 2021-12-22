package game;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

public class LanceurDeJeu {

    public static void main(String[] args)throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException{
        
        //Declare un Jeu
        Jeu jeu ;
        //Instancie un nouveau jeu
        jeu = new JeuDevineMotLettreOrdre();
        //Execute le jeu
        jeu.execute();
    }
    
}

