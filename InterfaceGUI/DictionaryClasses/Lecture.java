package InterfaceGUI.DictionaryClasses;

import java.io.*;

public class Lecture {
    public static void lire_lignes(String nom,Engin engin) {
        // ouverture du fichier
        try {
            BufferedReader input = new BufferedReader( new FileReader(nom) );
            // lire et traiter chaque ligne
            String ligne;
            ligne = input.readLine();
            while (ligne != null) {
                ligne = ligne.toLowerCase();
                // On met les mots en minuscules pour que le programme ne distingue pas "Abc" de "abc"
                for(String mot : ligne.split("[ .,'!?\\(\\)\\[\\]\\-\\_\\\"\\«\\»\\:\\;\\/\\\\\\{\\}\\>\\<\\|\\*\\&\\^\\%\\$\\+\\=\t\n]")) // On sépare la string en array contenant chaque mots, sépare la string par la ponctuation et les espaces
                    if (!mot.equals(""))                                                                       // On répare un bug: lorsque 2 caractères spéciaux étaient collés, ligne.split créait des strings vides (qui ne sont pas des mots)
                        engin.index.inserer(mot.toLowerCase());

                ligne = input.readLine();
            }
            input.close();
        }
        catch (IOException e) {
            System.err.println("erreur fichier" + e.toString());
        }
    }
}
