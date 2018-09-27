package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
/**
* This class loads user settings from a .xml-File.
* */
public final class PreferencesUtil {
    public PreferencesUtil(){
        throw new AssertionError();
    }

    private static String initialValidationImportPath;
    private static String initialLuaExportPath;
    private static String initialLuaLoadPath;

    private static void setInitialValidationImportPath(String path){ initialValidationImportPath = path; }
    private static void setInitialLuaExportPath(String path){ initialLuaExportPath = path; }
    private static void setInitialLuaLoadPath(String path){ initialLuaLoadPath = path; }

    /**
    * This method loads user settings from a .xml-file and sets the initial paths in the class variables
    * */
    public static void loadPreferences() throws IOException {
        byte[] xml2data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/Preferences.xml"));
        String code = new String(xml2data, "UTF-8");
        InputSource is = new InputSource(new StringReader(code));

        try {
            DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuild = dbFac.newDocumentBuilder();

            Document doc = dBuild.parse(is);
            doc.getDocumentElement().normalize();

            NodeList prefs = doc.getElementsByTagName("Preferences");

            for(int i = 0; i < prefs.getLength(); i++){
                Node iNode = prefs.item(i);

                if(iNode.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) iNode;
                    // Hier werden die initialen Pfade gesetzt
                    if(e.getAttribute("name").equals("path")) {
                        String val = e.getElementsByTagName("initialValidationImportPath").item(0).getTextContent();
                        setInitialValidationImportPath(val);
                        String lua = e.getElementsByTagName("initialLuaExportPath").item(0).getTextContent();
                        setInitialLuaExportPath(lua);
                        String loadlua = e.getElementsByTagName("initialLuaLoadPath").item(0).getTextContent();
                        setInitialLuaLoadPath(loadlua);
                    }
                }
            }

        } catch (ParserConfigurationException|SAXException p){}

    }

    /**
    * test method
    * */
    public static void printPref(){
        System.out.println("initialLuaExportPath: " + initialLuaExportPath);
        System.out.println("initialValidationImportPath: " + initialValidationImportPath);
    }

    /**
    * Returns the initial path for the validation import
    * @return String path
    * */
    public static String getInitValidationImportPath(){
        return initialValidationImportPath;
    }

    /**
     * Returns the initial path for the lua export
     * @return String path
     * */
    public static String getInitialLuaExportPath(){
        return initialLuaExportPath;
    }

    /**
     * Returns the initial path for loading the lua-file
     * @return String path
     * */
    public static String getInitialLuaLoadPath() { return initialLuaLoadPath; }
}
