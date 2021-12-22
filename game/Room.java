package game;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Room {
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;
    
    public Room() throws ParserConfigurationException, SAXException, IOException{

        String filename="src/Data/xml/plateau.xml";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(filename));

        doc.getDocumentElement().normalize();

        Node nDepth  = doc.getElementsByTagName("depth").item(0);
        int depth = Integer.parseInt(nDepth.getTextContent());

        Node nWidth = doc.getElementsByTagName("width").item(0);
        int width = Integer.parseInt(nWidth.getTextContent());

        Node nHeigt = doc.getElementsByTagName("width").item(0);
        int height = Integer.parseInt(nHeigt.getTextContent());

        String textureBottom = doc.getElementsByTagName("textureBottom").item(0).getTextContent();

        String textureEast = doc.getElementsByTagName("textureEast").item(0).getTextContent();

        String textureWest = doc.getElementsByTagName("textureWest").item(0).getTextContent();

        String textureNorth = doc.getElementsByTagName("textureNorth").item(0).getTextContent();

        setTextureBottom(textureBottom);
        setTextureEast(textureEast);
        setTextureWest(textureWest);
        setTextureNorth(textureNorth);
        
        this.depth = depth;
        this.width = width;
        this.height = height;
    }
//accesseurs 
    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }

    public String getTextureNorth() {
        return textureNorth;
    }

    public String getTextureEast() {
        return textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public String getTextureSouth() {
        return textureSouth;
    }
// modificateurs
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
    
}