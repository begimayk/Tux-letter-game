package game;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.lwjgl.input.Keyboard;
import org.xml.sax.SAXException;

import env3d.Env;




public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }
    // l'environnement 3d
    protected final Env env;
    // le personnage tux
    protected Tux tux;

    private final Room mainRoom;
    private final Room menuRoom;
    private Letter letter;
    protected Profil profil;
    private final Dico dico;
    protected EnvTextMap menuText;
    //text (affichage des texte du jeu)
    //la liste des littres du mot à assembler
    protected ArrayList<Letter> lettres;
    
    
    public Jeu() throws SAXException, IOException, ParserConfigurationException {
        
        // Instancie une Room
        mainRoom = new Room();
        
        // Crée un nouvel environnement
        env = new Env();
        
        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        
    

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();
        
        // Dictionnaire
        //instanciation de dico
        dico = new Dico("src/Data/xml/dico.xml");
        dico.lireDictionnaireSAX();

        // instancie le menuText
        menuText = new EnvTextMap(env);
        
        // Textes affichés à l'écran
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("3. Sortir de ce jeu ?", "Jeu3", 250, 260);
        menuText.addText("4. Quitter le jeu ?", "Jeu4", 250, 240);

        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("Tapez votre Annee de naissance : ", "AnneAnnivJoueur", 200, 300);
        menuText.addText("Tapez votre Mois de naissance : ", "MoisAnnivJoueur", 200, 300);
        menuText.addText("Tapez votre Jour de naissance : ", "JourAnnivJoueur", 200, 300);

        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Ajouter un mot au dictionnaire ?", "Principal3", 250, 240);
        menuText.addText("4. Consulter le profil ?", "Principal4", 250, 220);
        menuText.addText("5. Sortir du jeu ?", "Principal5", 250, 200);

        menuText.addText("Règles du jeu", "ExplicationRegles", 250, 300);
        menuText.addText("Vous devez assembler un mot le plus rapidement possible.", "desc1", 50, 280);
        menuText.addText("Pour cela, il vous faudra déplacer un petit pesonnage à l'aide des touches", "desc2", 50, 260);
        menuText.addText("directionnelles, pour récupèrer des cubes représentant les lettres de ce mot.", "desc3", 50, 240);
        menuText.addText("Les touches (A) et (E) vous permettrons de prendre et poser ces cubes et", "desc4", 50, 220);
        menuText.addText("ainsi les ordonner sur l'axe horizontal (axe X).", "desc5", 50, 200);
        menuText.addText("Cliquer sur (ESPACE) pour continuer", "desc6", 50, 100);

        menuText.addText("VICTORY", "Victoire", 250, 300);
        menuText.addText("GAME OVER", "Défaite", 250, 300);

        menuText.addText("Vous avez gagné la partie !", "AffichageFinGagné", 50, 280);
        menuText.addText("C'est perdu dommage... Mais vous pouvez toujours reessayer !", "AffichageFinPerdu", 50, 280);
        
        menuText.addText("Ce profil n'existe pas !", "PasTrouveProfil1", 200, 300);
        menuText.addText("Veuillez créer un nouveau profil", "PasTrouveProfil2", 200, 280);

        menuText.addText("L'écriture de la date de naissance est incorrecte","ErreurDateAnniv", 200, 300);
    
        menuText.addText("Entrez votre mot : ", "MotPourDictionnaire", 250, 300);

        menuText.addText("Choisissez un niveau de difficulté (1-2-3-4-5) : ", "ChoixNiveau", 50, 300);
        menuText.addText("Entrez le niveau de votre mot (1-2-3-4-5): ", "NiveauPourDictionnaire1", 200, 300);
        menuText.addText(" - niveau 1 : très facile et rapide  10 secondes ", "NiveauPourDictionnaire2", 50, 280);
        menuText.addText(" - niveau 2 : facile 20 secondes ", "NiveauPourDictionnaire3", 50, 260);
        menuText.addText(" - niveau 3 : tranquille 30 secondes ", "NiveauPourDictionnaire4", 50, 240);
        menuText.addText(" - niveau 4 : dur 60 secondes ", "NiveauPourDictionnaire5", 50, 220);
        menuText.addText(" - niveau 5 : hardcore 120 secondes ", "NiveauPourDictionnaire6", 50, 200);

        menuText.addText("Votre profil a été ajouté créé avec succés !","ProfilCree", 200, 300);
        menuText.addText("Votre profil a bien été trouvé !","ProfilTrouvé", 200, 300);


        menuText.addText("Votre mot a été ajouté au dictionnaire avec succé !","MotAjouté", 200, 300);


    }

    /**
     * Gère le menu principal
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerConfigurationException
     *
     */
    public void execute() throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException {
        
        
        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
        env.exit();
    }


    //interactions avec le joueur :

    //demande à l'utilisateur d'entrer son nom et le return
    private String getNomJoueur() {
        String s = "";
        menuText.getText("NomJoueur").display();
        s = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return s;
    }

    //demande à l'utilisateur d'entrer son année d'anniversaire et le return
    private String getAnneAnnivJoueur() {
        String s = "";
        menuText.getText("AnneAnnivJoueur").display();
        s = menuText.getText("AnneAnnivJoueur").lire(true);
        menuText.getText("AnneAnnivJoueur").clean();
        return s;
    }

    //demande à l'utilisateur d'entrer son mois d'anniversaire et le return
    private String getMoisAnnivJoueur() {
        String s = "";
        menuText.getText("MoisAnnivJoueur").display();
        s = menuText.getText("MoisAnnivJoueur").lire(true);
        menuText.getText("MoisAnnivJoueur").clean();
        return s;
    }

    //demande à l'utilisateur d'entrer son jour d'anniversaire et le return
    private String getJourAnnivJoueur() {
        String nomJoueur = "";
        menuText.getText("JourAnnivJoueur").display();
        nomJoueur = menuText.getText("JourAnnivJoueur").lire(true);
        menuText.getText("JourAnnivJoueur").clean();
        return nomJoueur;
    }
    //demande à l'utilisateur d'entrer un mot et le return
    private String getMotDico(){
        String motDico = "";
        menuText.getText("MotPourDictionnaire").display();
        motDico = menuText.getText("MotPourDictionnaire").lire(true);
        menuText.getText("MotPourDictionnaire").clean();
        return motDico;
    }
    //demande à l'utilisateur d'entrer un niveau et le return
    private String getNiveauDico(){
        String nivDico = "";
        menuText.getText("NiveauPourDictionnaire1").display();
        menuText.getText("NiveauPourDictionnaire2").display();
        menuText.getText("NiveauPourDictionnaire3").display();
        menuText.getText("NiveauPourDictionnaire4").display();
        menuText.getText("NiveauPourDictionnaire5").display();
        menuText.getText("NiveauPourDictionnaire6").display();
        nivDico = menuText.getText("NiveauPourDictionnaire1").lire(true);
        menuText.getText("NiveauPourDictionnaire1").clean();
        menuText.getText("NiveauPourDictionnaire2").clean();
        menuText.getText("NiveauPourDictionnaire3").clean();
        menuText.getText("NiveauPourDictionnaire4").clean();
        menuText.getText("NiveauPourDictionnaire5").clean();
        menuText.getText("NiveauPourDictionnaire6").clean();
        return nivDico;
    }
    






    final static String DATE_FORMAT = "dd-MM-yyyy";

    //verifie qu'une date entree existe ou non
    public static boolean isDateValid(String date) 
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    
    // methode affichant le menu joueur
    private MENU_VAL menuJeu() throws TransformerConfigurationException {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            //menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();
            
            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            //menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();

            // restaure la room du jeu
            env.setRoom(mainRoom);

            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------                
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******

                    // crée un nouvelle partie
                    partie = new Partie();

                    // on demande le niveau de difficulté mot

                    // joue
                    joue(partie);
                    // enregistre la partie dans le profil --> enregistre le profil
                    // .......... profil.******
                    profil.ajouterPartie(partie);
                    profil.sauvegarde();
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 2 : Voir meilleurs scores
                // ----------------------------------------- 
                case Keyboard.KEY_2:

                    //pas encore implémenté dans le jeu

                    break;
     
                
                // -----------------------------------------
                // Touche 3 : Quitter menu joueur
                // ----------------------------------------- 
                case Keyboard.KEY_3:
                    System.out.println("\nQuitte le profil, retour au menu principal");

                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // -----------------------------------------                
                case Keyboard.KEY_4:
                    System.out.println("\nQuitte le jeu");

                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }





    // methode affichant le menu joueur
    private MENU_VAL menuPrincipal() throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;
        String anneJoueur="0";
        String moisJoueur="0";
        String jourJoueur="0";
        String dateAnnivJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("Principal4").display();
        menuText.getText("Principal5").display();
               
        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_5)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("Principal4").clean();
        menuText.getText("Principal5").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_1:

                // demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // charge le profil de ce joueur si possible
                if (profil.charge(nomJoueur)) {
                    profil = new Profil(nomJoueur);
                    profil.setNom(nomJoueur);


                    menuText.getText("ProfilTrouvé").display();
                    menuText.getText("desc6").display();

                    //affichage dans le terminal des infos du joueur
                    System.out.println("\nVotre profil a été trouvé !");
                    System.out.println("Votre nom est : "+profil.getNom());
                    System.out.println("Votre date de naissance est : "+profil.getAnniversaire()+"\n");

                    // vérifie que la touche espace est pressé 
                    while (!(touche == Keyboard.KEY_SPACE)) {
                        touche = env.getKey();
                        env.advanceOneFrame();
                    }

                    menuText.getText("ProfilTrouvé").clean();
                    menuText.getText("desc6").clean();
                    env.advanceOneFrame();

                    choix = menuJeu();

                } else {
                    menuText.getText("PasTrouveProfil1").display();
                    menuText.getText("PasTrouveProfil2").display();
                    menuText.getText("desc6").display();

                    // vérifie que la touche espace est pressé 
                    while (!(touche == Keyboard.KEY_SPACE)) {
                        touche = env.getKey();
                        env.advanceOneFrame();
                    }

                    menuText.getText("PasTrouveProfil1").clean();
                    menuText.getText("PasTrouveProfil2").clean();
                    menuText.getText("desc6").clean();
                    env.advanceOneFrame();

                    //menuPrincipal();//CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                
                nomJoueur = getNomJoueur();

                boolean verif=false;
                //format verification date : dd-MM-yyyy
                while(verif==false){
                    anneJoueur = getAnneAnnivJoueur();
                    moisJoueur = getMoisAnnivJoueur();
                    jourJoueur = getJourAnnivJoueur();

                    verif=isDateValid(jourJoueur+"-"+moisJoueur+"-"+anneJoueur);
                    if(verif==false){

                        menuText.getText("ErreurDateAnniv").display();
                        menuText.getText("desc6").display();
                        touche = 0;
                        // vérifie que la touche espace est pressé 
                        while (!(touche == Keyboard.KEY_SPACE)) {
                            touche = env.getKey();
                            env.advanceOneFrame();
                        }

                        menuText.getText("ErreurDateAnniv").clean();
                        menuText.getText("desc6").clean();

                    }

                }

                //modifie le format
                dateAnnivJoueur = anneJoueur+"-"+moisJoueur+"-"+jourJoueur;

                // crée un profil avec le nom d'un nouveau joueur
                profil = new Profil(nomJoueur,dateAnnivJoueur);

                menuText.getText("ProfilCree").display();
                menuText.getText("desc6").display();

                System.out.println("\nVotre profil a été créé !");
                System.out.println("Votre nom est : "+nomJoueur);
                System.out.println("Votre date de naissance est : "+dateAnnivJoueur+"\n");

                // vérifie que la touche espace est pressé 
                while (!(touche == Keyboard.KEY_SPACE)) {
                    touche = env.getKey();
                    env.advanceOneFrame();
                }

                menuText.getText("ProfilCree").clean();
                menuText.getText("desc6").clean();



                choix = menuJeu();
                break;

            // -------------------------------------
            // Touche 3 : Ajout mot
            // -------------------------------------
            case Keyboard.KEY_3:

                EditeurDico editeur = new EditeurDico();
                editeur.lireDOM("src/Data/xml/dico.xml");

                //récupère le mot entrée pas l'utilisateur
                String motDico = getMotDico();

                //récupère le mot entrée pas l'utilisateur
                String niveauDico = "0";
                while(!(niveauDico.equals("1") || niveauDico.equals("2") || niveauDico.equals("3") || niveauDico.equals("4") || niveauDico.equals("5"))){
                    niveauDico = getNiveauDico();
                }
                
                //ajoute le mot à la liste
                editeur.ajouteMotADico(niveauDico, motDico);

                //génère un nouveau fichier dico avec le mot
                editeur.ecrireDOM("src/Data/xml/dico.xml");

                dico.ajouteMotADico(Integer.parseInt(niveauDico), motDico);



                //affichages
                menuText.getText("MotAjouté").display();
                menuText.getText("desc6").display();

                System.out.println("Ajout du mot '"+ motDico +"' dans le dictionnaire");

                // vérifie que la touche espace est pressé 
                while (!(touche == Keyboard.KEY_SPACE)) {
                    touche = env.getKey();
                    env.advanceOneFrame();
                }

                menuText.getText("MotAjouté").clean();
                menuText.getText("desc6").clean();

                choix = MENU_VAL.MENU_JOUE;
                
                break;
            
            // -------------------------------------
            // Touche 4 : Voir scores meilleurs joueurs
            // -------------------------------------
            case Keyboard.KEY_4:

                String nom = getNomJoueur();
                ConsulteProfil consulte = new ConsulteProfil(nom);

                if(consulte.charge(nom)){

                //si le joueurs a joué plus que 10 parties on affiche chaques 10 par une page
                // sinon on affiche simplement ses partie
                    consulte.lireProfil();
                    int reste = consulte.parties.size()%10;
                    int i=0;
                    if(consulte.parties.size()>10){
                        while( i< consulte.parties.size()-10){
                            menuText.addText(consulte.toString(i, i+10), "Consulter", 50, 420);
                            menuText.getText("Consulter" ).display();
                            menuText.getText("desc6").display();
    
                            touche = 0;
    
                            // vérifie que la touche espace est pressé 
                            while (!(touche == Keyboard.KEY_SPACE)) {
                            touche = env.getKey();
                            env.advanceOneFrame();
                        }
                            menuText.getText("Consulter").clean();
                            menuText.getText("desc6").clean();
                            menuText.popText("Consulter");
                            i=i+10;
                        }
                            int k = i;
                            menuText.addText(consulte.toString(k, reste + k), "Consulterbis", 50, 420);
                            menuText.getText("Consulterbis").display();
                            menuText.getText("desc6").display();
                            touche = 0;
                            // vérifie que la touche espace est pressé 
                            while (!(touche == Keyboard.KEY_SPACE)) {
                            touche = env.getKey();
                            env.advanceOneFrame();
                            }
                            menuText.getText("Consulterbis").clean();
                            menuText.getText("desc6").clean();
                    } else{
                        menuText.addText(consulte.afficher(), "Consulter2", 50, 420);
                        menuText.getText("Consulter2" ).display();
                        menuText.getText("desc6").display();

                        touche = 0;

                        // vérifie que la touche espace est pressé 
                        while (!(touche == Keyboard.KEY_SPACE)) {
                        touche = env.getKey();
                        env.advanceOneFrame();
                    }
                        menuText.getText("Consulter2").clean();
                        menuText.getText("desc6").clean();
                        menuText.popText("Consulter2");


                    }

                }else {
                    menuText.getText("PasTrouveProfil1").display();
                    //menuText.getText("PasTrouveProfil2").display();
                    menuText.getText("desc6").display();

                    // vérifie que la touche espace est pressé 
                    while (!(touche == Keyboard.KEY_SPACE)) {
                        touche = env.getKey();
                        env.advanceOneFrame();
                    }

                    menuText.getText("PasTrouveProfil1").clean();
                    //menuText.getText("PasTrouveProfil2").clean();
                    menuText.getText("desc6").clean();
                    env.advanceOneFrame();

                }

                choix = MENU_VAL.MENU_JOUE;
                
                break;



            // -------------------------------------
            // Touche 5 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_5:                    
                System.out.println("\nQuitte le jeu");

                choix = MENU_VAL.MENU_SORTIE;
        }
        return choix;
    }


    //menu d'affichage avant une partie
    //
    //
    //on montre les règles au joueur
    //on lui demande quel niveau de partie il veut faire
    //
    public int menuAvantPartie(){
        int touche = 0;

        // restaure la room du menu
        env.setRoom(menuRoom);

        //affiche les règles du jeu
        menuText.getText("ExplicationRegles").display();

        menuText.getText("desc1").display();
        menuText.getText("desc2").display();
        menuText.getText("desc3").display();
        menuText.getText("desc4").display();
        menuText.getText("desc5").display();
        menuText.getText("desc6").display();


        // vérifie que la touche espace est pressé 
        while (!(touche == Keyboard.KEY_SPACE)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        //retire l'affichage des règles du jeu
        menuText.getText("ExplicationRegles").clean();

        menuText.getText("desc1").clean();
        menuText.getText("desc2").clean();
        menuText.getText("desc3").clean();
        menuText.getText("desc4").clean();
        menuText.getText("desc5").clean();
        menuText.getText("desc6").clean();



        //affiche menu avant partie
        menuText.getText("ChoixNiveau").display();
        menuText.getText("NiveauPourDictionnaire2").display();
        menuText.getText("NiveauPourDictionnaire3").display();
        menuText.getText("NiveauPourDictionnaire4").display();
        menuText.getText("NiveauPourDictionnaire5").display();
        menuText.getText("NiveauPourDictionnaire6").display();

        // vérifie qu'une touche 1, 2, 3, 4 ou 5 est pressé 
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_5)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        //retire l'affichage des règles du jeu
        menuText.getText("ChoixNiveau").clean();
        menuText.getText("NiveauPourDictionnaire2").clean();
        menuText.getText("NiveauPourDictionnaire3").clean();
        menuText.getText("NiveauPourDictionnaire4").clean();
        menuText.getText("NiveauPourDictionnaire5").clean();
        menuText.getText("NiveauPourDictionnaire6").clean();

        // restaure la room du jeu
        env.setRoom(mainRoom);

        
        //renvoie le niveau du mot entrée
        switch(touche){
            case Keyboard.KEY_1:
                return 1;
            case Keyboard.KEY_2:
                return 2;
            case Keyboard.KEY_3:
                return 3;
            case Keyboard.KEY_4:
                return 4;
            case Keyboard.KEY_5:
                return 5; 
            default:
                return 1;
        }
    }



    //menu d'affichage fin de partie
    //
    //
    //on vérifie si le joueur a gagné ou non 
    //on affiche le menu de Victoire ou Défaite
    //on affiche le score selon
    //
    public void menuFinPartie(Partie partie){

        //récupère le temps et le trouvé
        int trouvé = partie.getTrouve();
        int temps = partie.getTemps();

        menuText.addText("Vous avez trouvé "+trouvé+"% du mot", "MotTrouvé", 50, 260);
        menuText.addText("Votre temps est de "+ temps +" secondes", "TempsFin", 50, 240);

        //affichage à l'écran
        // restaure la room du menu
        env.setRoom(menuRoom);

        //affiche victoire ou défaite
        if(trouvé==100){
            menuText.getText("Victoire").display();
            menuText.getText("AffichageFinGagné").display();

            System.out.println("\nVICTOIRE");
        }else{
            menuText.getText("Défaite").display();
            menuText.getText("AffichageFinPerdu").display();

            System.out.println("\nDEFAITE");
        }
        menuText.getText("desc6").display();


        //affiche le pourcentage de mot trouvé 
        //dans l'environnement
        menuText.getText("MotTrouvé").display();
        menuText.getText("TempsFin").display();

        //affichage sur la console
        System.out.println("mot a chercher :" + partie.getMot());
        System.out.println("partie jouée le :" + partie.getDate());
        System.out.println("Vous avez trouvé "+trouvé+"% du mot");
        System.out.println("Votre temps est de "+ temps +" secondes\n");

        env.advanceOneFrame();

        // vérifie que la touche espace est pressé
        int touche=0; 
        while (!(touche == Keyboard.KEY_SPACE)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Victoire").clean();
        menuText.getText("Défaite").clean();
        menuText.getText("AffichageFinGagné").clean();
        menuText.getText("AffichageFinPerdu").clean();
        menuText.getText("desc6").clean();
        menuText.getText("MotTrouvé").clean();
        menuText.getText("TempsFin").clean();

        menuText.popText("MotTrouvé");
        menuText.popText("TempsFin");

        env.advanceOneFrame();

        // restaure la room du jeu
        env.setRoom(mainRoom);
    }




    //cette methode joue une partie
    //
    //initialise une limite de temps selon le niveau du mot
    //
    //
    //
    public void joue(Partie partie) {

        int niveau = menuAvantPartie();

        int limite=20;
        switch(niveau){
            case 1:
                limite=10;
                break;
            case 2:
                limite=20;
                break;
            case 3:
                limite=30;
                break;
            case 4:
                limite=60;
                break;
            case 5:
                limite=120;
                break;
            default:
                break;

        }

        //initialise la date a la date actielle
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        //tire un mot de dictionnaire au hasard
        String mot = dico.getMotDepuisListeNiveau(niveau);


        env.setRoom(menuRoom);

        //initialise le chronomotre a 2 secondes pour afficher le mot
        Chronometre c = new Chronometre(2);
        c.start();

        //affiche le mot 2 secondes
        menuText.addText("Le mot a assembler est : "+mot, "AfficherLeMot", 100, 300);
        boolean affiche=true;
        env.advanceOneFrame();

        while (affiche) {
            c.stop();
            menuText.getText("AfficherLeMot").display();
            env.advanceOneFrame();

            if(c.getSeconds() > 2){
                affiche=false;
            }
        }
        //enleve l'affichage
        menuText.getText("AfficherLeMot").clean();
        menuText.popText("AfficherLeMot");
        env.setRoom(mainRoom);




        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        //initialisation d'un mot choisi aleatoirement, correspondant au niveau tapé, ainsi que la date courante
        partie.setMot(mot);
        partie.setDate(date);
        partie.setNiveau(niveau);


        //Instancie la liste des lettres
        lettres = new ArrayList<Letter>() ;

        //Instancie le tableau de verification
        char[] lettresDansLordre = new char[mot.length()];
        
        //créé un tableau de caractéres du mot pour faciliter les lanipulations
        for(int i=0; i<mot.length(); i++){
            lettresDansLordre[i] = mot.charAt(i);
        }




        //initialise et instancie des lettres à des positions aléatoires
        for(int i=0; i<mot.length(); i++){
            int positionX = new Random().nextInt(80) + 15;
            int positionZ = new Random().nextInt(60) + 10;
            Letter  l = new Letter(mot.charAt(i),positionX,positionZ);
            
            lettres.add(l);

            //env.addObject(l);
        }

        //vérifie que la disposition des lettres au début de la partie 
        //afin de voir si la partie générée n'est pas déjà gagné        
        int lettres_restantes;
        boolean drapeau=false;
        while(drapeau==false){
            lettres_restantes = getLongeurMot();

            //les lettre sont trié en fonction de leur position
            Collections.sort(lettres);

            //vérifie l'ordre de position des lettres
            //pour voir si elle correspondent à celle du mot
            for(int i=0; i<lettres.size(); i++){ // taille de la liste = longueur du mot
                if(lettres.get(i).getLetter() == lettresDansLordre[i]){
                    lettres_restantes--;
                    
                }
            }

            //si elles correspondent, on sort de la boucle
            if(lettres_restantes!=0){
                drapeau=true;
            
            }
            //sinon on regénère des lettres aleatoirement
            else{
                //vide la liste
                lettres.clear();

                //initialise et instancie des lettres à des positions aléatoires
                for(int i=0; i<mot.length(); i++){
                    int positionX = new Random().nextInt(80) + 15;
                    int positionZ = new Random().nextInt(60) + 10;
                    Letter  l = new Letter(mot.charAt(i),positionX,positionZ);
                    
                    //les ajoutes à la liste
                    lettres.add(l);
                    
                }

            }

        }

        //si c'est ok, on ajoute les lettres dans l'environnement
        for(int i=0; i<lettres.size(); i++){
            env.addObject(lettres.get(i));
        }


        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        demarrePartie(partie,limite);

        // Boucle de jeu
        Boolean finished;
        finished = false;
        boolean touche_space_active=false;
        boolean lettre_sur_tete=false;
        //boolean drapeau=false;
        Letter lettre=null;
        menuText.addText("Test 1 2 1 2", "ChoixPrendreLettre", 250, 240);
        menuText.addText("Test 3 4 3 4", "ChoixPoserLettre", 250, 240);

        while (!finished) {

            //affiche le temps restant
            afficherChrono(partie);

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.déplace();

            // Si l'utilisateur a appuyé sue l'espace il a ala possibillité de deplacer la lettre 
            if(touche_space_active==true){
                lettre.setX(tux.getX());
                lettre.setZ(tux.getZ());
            }

            if(collisionAvecLettres(lettres) || lettre_sur_tete==true){

                if(lettre_sur_tete==false){
                    menuText.popText("ChoixPoserLettre");
                    menuText.popText("ChoixPrendreLettre");
                    lettre=lettre_touché(lettres);
                    menuText.addText("Veux-tu PRENDRE la lettre " + lettre.getLetter()+"? (touche E)\n", "ChoixPrendreLettre", 250, 240);
                    menuText.addText("Veux-tu POSER la lettre " + lettre.getLetter()+"? (touche A)\n", "ChoixPoserLettre", 250, 240);
                }

                //si la lettre n'etait pas prise on propose a deplacer la lettre
                if(lettre_sur_tete==false){
                    menuText.getText("ChoixPrendreLettre").display();
                }
                //si la lettre est prise on propose a la poser
                else{
                    menuText.getText("ChoixPoserLettre").display();
                }

                //si on appuie sur la touche E tux prend la lettre
                if(env.getKeyDown(Keyboard.KEY_E) && lettre_sur_tete==false){
                
                    if(lettre!=null){
                        touche_space_active=true;
                        lettre_sur_tete=true;
    
                        lettre.setX(tux.getX());
                        lettre.setZ(tux.getZ());
                        lettre.setY(tux.getY() + 5);
                    }
    
                    else{
                        touche_space_active=false;
                    }
                }

                //si on appuie sue la touche A tux pose la lettre
                else if(env.getKeyDown(Keyboard.KEY_A) && lettre_sur_tete==true){
                    
                    
                    touche_space_active=false;
                    lettre_sur_tete=false;

                    lettre.setX(tux.getX());
                    lettre.setZ(tux.getZ());
                    lettre.setY(lettre.getScale() * 1.1);

                }
                
            }
            else{
                menuText.getText("ChoixPoserLettre").clean();
                menuText.getText("ChoixPrendreLettre").clean();
            }

            
            // a faire  ajouter l'affichage des lettres normales
            // Ici, on applique les regles
            finished=appliqueRegles(partie, lettresDansLordre);

            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1) {
                finished = true;
            }

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            //env.advanceOneFrame();
        }

        menuText.getText("ChoixPoserLettre").clean();
        menuText.getText("ChoixPrendreLettre").clean();
        menuText.popText("ChoixPoserLettre");
        menuText.popText("ChoixPrendreLettre");

        
        terminePartie(partie);
        menuFinPartie(partie);
        env.removeObject(tux);

        System.out.println("\nFin de partie, retour au menu joueur");



    }


    protected abstract int getNbLettresRestantes();

    protected abstract void demarrePartie(Partie partie, int limite);

    protected abstract boolean appliqueRegles(Partie partie, char[] lettresDansLordre);

    protected abstract void terminePartie(Partie partie);

    protected abstract void afficherChrono(Partie partie);
    





//autres methodes utiles
    
    //calcule la distance entre le tux et la lettre
    public double distance(Letter letter){

        double x = (this.tux.getX() - letter.getX())*(tux.getX() - letter.getX());
        double y = (this.tux.getY() - letter.getY())*(tux.getY() - letter.getY());
        double z = (this.tux.getZ() - letter.getZ())*(tux.getZ() - letter.getZ());

        double distance = Math.sqrt(x + y + z);


        return distance;
    }

    //renvoie vrai si tux est en collision la lettre en parametre
    public boolean collision(Letter lettre){
        boolean res = false;

        if(distance(lettre) < this.tux.getScale()/2 + lettre.getScale()/2){
            res = true;
        }
        return res;
    }

    //renvoie vrai si tux est en collision avec au moins une lettre de la liste passée en parametre
    public boolean collisionAvecLettres(ArrayList<Letter> lettres){
        boolean res = false;

        for(Letter L : lettres)
            if(distance(L) < this.tux.getScale()/2 + L.getScale()/2){
                res = true;
        }
        return res;
    }
    
    
    
    //getter
    public int getLongeurMot(){
        return this.lettres.size();
    }

    
    //renvoie la lettre qui est en collision avec tux
    //si aucune lettre est en collision renvoie null
    public Letter lettre_touché(ArrayList<Letter> lettres){
       
        for(Letter lettre : lettres){
            if(collision(lettre)){
                return lettre;                                
           }
        }
        return null;
    }






}







