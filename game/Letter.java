package game;

import env3d.advanced.EnvNode;


import org.lwjgl.input.Keyboard;

import env3d.Env;


//implement l'interface Comparable du java.lang pour trier la liste des lettres du jeu
public class Letter extends EnvNode implements Comparable<Letter>{
    
    private char letter;

    public Letter(char l, double x, double z){
        letter=l;
        //letter = letter.toLowerCase();



        //initialisation de l'attribut room
        setScale(3.0);
        setX( x );// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ( z ); // positionnement au milieu de la profondeur de la room

        if(letter==' '){
            String s="src/Data/letter/cube.png";
            setTexture(s);
            setModel("src/Data/letter/cube.obj");  
        }  
        else{
            String s="src/Data/letter/"+letter+".png";
            setTexture(s);
            setModel("src/Data/letter/cube.obj");  
        }

    }

    public char getLetter() {
        return letter;
    }
    

//compare les lettre en fonction de leur position sur l'axe x pour ensuite trier la liste des lettres du jeu
        @Override
        public int compareTo(Letter lettre) {
                int diff = (int) (this.getX() - lettre.getX());
            return diff;
        }


}


