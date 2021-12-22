package game;

import env3d.Env;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe gère l'ensemble des textes de type EnvText du jeu
 *
 * @author gladen
 */
public class EnvTextMap {

    public Env env = null;    // référence vers l'environnement
    Map<String, EnvText> map; // contient les textes associés à une clef de type String

    /**
     * constructeur
     *
     * @param env
     */
    public EnvTextMap(Env env) {
        this.map = new HashMap<String, EnvText>();
        this.env = env;
    }

    /**
     * ajouter du texte (de type envtext) dans la hashmap
     *
     * @param phrase
     * @param cle
     * @param x
     * @param y
     */
    public void addText(String phrase, String cle, int x, int y) {
        EnvText text = new EnvText(env, phrase, x, y);
        map.put(cle, text);
    }
    public void popText(String cle) {
        map.remove(cle);
    }

    /**
     * obtenir du texte à partir de sa clé
     * 
     * @param cle
     * @return 
     */
    public EnvText getText(String cle) {
        return map.get(cle);
    }

}

