package game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import env3d.Env;
import env3d.advanced.EnvNode;

public class Tux extends EnvNode{
    
    private Env env;
    private Room room;
    
    public Tux(Env env, Room room){

        //Dans le constructeur
        this.env=env; // initialisation de l'attribut env
        this.room=room;
        
        //initialisation de l'attribut room
        setScale(4.0);
        setX( room.getWidth()/2 );// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ( room.getDepth()/2 ); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux.png");
        setModel("models/tux/tux.obj");  
        
    }
    

    public void déplace() {
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
            // Haut
            this.setRotateY(180);
            if(!testeRoomCollision(getX(), getZ()-1.0)){
                this.setZ(this.getZ() - 1.0);
            }
        }

        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            // Gauche
            this.setRotateY(-90);
            if(!testeRoomCollision(getX()-1.0, getZ())){
                this.setX(this.getX() - 1.0);
            }  
        }
        

        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { // Fleche 'gauche' ou Q
            // Droite
            this.setRotateY(90);
            if(!testeRoomCollision(getX()+1.0, getZ())){
                this.setX(this.getX() + 1.0);  
            }    
        }

        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { // Fleche 'haut' ou Z
        // Haut
            this.setRotateY(0);
            if(!testeRoomCollision(getX(), getZ()+1.0)){
                this.setZ(this.getZ() + 1.0);  
            }
        }

    }

    public Boolean testeRoomCollision(double x,double z){

        if((x<getScale()/2 || x>room.getWidth()-getScale()/2) || (z<getScale()/2 || z>room.getDepth()-getScale()/2)){
            return true;
        }
        return false;
    }

}
   
    

