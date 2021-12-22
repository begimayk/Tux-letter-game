package game;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Partie {
    
    //attributs
    private String date;
    private String mot;
    private int niveau;
    private int trouve;
    private int temps;
    

    //constructeur
    public Partie(String date, String mot,int niveau){
        
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
        
        
    }
    
    //constructeur par défaut
    public Partie(){
        
    }

   //accesseurs 
    public int getTrouve() {
        return trouve;
    }

    public String getDate(){
        return this.date;
    }

    public int getNiveau(){
        return this.niveau;
    }
    
    public String getMot(){
        return this.mot;
    }
     public int getTemps() {
        return temps;
    }

    // modificateurs 

    public void setTrouve(int nbLettresRestantes){
        this.trouve = nbLettresRestantes;
    }
    
    public void setTemps(int temps){
        this.temps = temps;
    }
    
    public void setMot(String mot){
        this.mot = mot;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
  

    @Override 
//les informations sur la partie
    public String toString(){

        String s = "Date : " + this.date  + "  Mot : " + this.mot  + "  Niveau : " + this.niveau;
        if(getTrouve() == 100){
            s = s + "  Temps : " + this.temps + "\n";
        }else{
            s = s + "  Trouvé : " + this.trouve + "\n";
        }
        return s;
    }

}
