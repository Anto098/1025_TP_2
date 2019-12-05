package InterfaceGUI;
import javafx.stage.FileChooser;
import InterfaceGUI.DictionaryClasses.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class Main_Frame extends JFrame implements ActionListener {

    private JFrame mainFrame;
    JButton dictionnaire = new JButton("Dictionnaire");
    JButton verifier = new JButton("Verifier");
    JMenuItem ouvrir = new JMenuItem("Ouvrir");
    JMenuItem enregistrer = new JMenuItem("Enregistrer");
    JTextArea textArea;
    public File fichierACorriger;
    public File fichierDictionnaire;
    Engin engin;

    public Main_Frame(Engin engin){
        this.engin = engin;
        mainFrame = new JFrame("Correcteur");                   //on cree les composants de la fenetre
        JPanel panel = (JPanel)mainFrame.getContentPane();
        JMenuBar menuPrincipal = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");                    // On met le JMenu dans JMenuBar
        menuPrincipal.add(menuFichier);

        menuPrincipal.add(dictionnaire); menuPrincipal.add(verifier);   // JButton -> JMenuBar
        dictionnaire.addActionListener(this); verifier.addActionListener(this);

        menuFichier.add(ouvrir); menuFichier.add(enregistrer);          // JMenuItem -> JMenu
        ouvrir.addActionListener(this);
        enregistrer.addActionListener(this);

        textArea = new JTextArea(2,2);
        textArea.addMouseListener(new MouseAdapter() {                  //on ajoute un mouse listener pour quand l'utilisateur clique sur un mot
            public void mousePressed(MouseEvent e) {
                textAreaOnClick(e);
            }
        });

        JScrollPane scrollPanel = new JScrollPane(textArea);            //On donne au text area une option de scroll
        panel.add(scrollPanel);

        mainFrame.setJMenuBar(menuPrincipal);                           //On ajoute la barre principale a la fenetre
        mainFrame.setSize(1000,500);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent E){                     //Event listeners sur la fenetre
        if(E.getSource() == ouvrir){
            openFileToCorrect();
        }
        else if (E.getSource() == enregistrer){
            saveTextInFile();
        }
        else if (E.getSource() == dictionnaire){
            createDictionary();
        }
        else if (E.getSource() == verifier){
            verifyText();
        }
    }

    private void textAreaOnClick(MouseEvent e) {
        try
        {
            String [] words;
            int offset = textArea.viewToModel2D( e.getPoint() );        //On recupere le mot sur lequel l'utilisateur a cliqué
            int start = Utilities.getWordStart(textArea,offset);
            int end = Utilities.getWordEnd(textArea, offset);
            String word = textArea.getDocument().getText(start, end-start);

            words = selectWords(word);

            compareWords(words);
        }
        catch (Exception e2) {
            System.err.println("Error : "+e2);
        }
    }

    private void compareWords(String[] words) throws BadLocationException { // PROBLÈME: si on a plusieurs apostrophes dans le même mot, on va regarder pour chaque mot dans l'array. On ne regarde pas réellement où on clique
        String word;
        Highlighter highLighter = textArea.getHighlighter();        //On recupere les mots surlignés
        Highlighter.Highlight[] highLights = highLighter.getHighlights();
        for(String w : words){                                      // on boucle sur les mots pour trouver lequel n'est pas dans le dictionnaire
            word = w;
            for (Highlighter.Highlight h : highLights) {                //On check si le mot sur lequel on a cliqué est surligné
                int startHighlight = h.getStartOffset();
                int endHighlight = h.getEndOffset();
                String highlightedWord = textArea.getText(startHighlight,endHighlight-startHighlight);
                if (word.equals(highlightedWord)){                      //Si le mot est surligné, on affiche le menu de correction
                    System.out.println("        "+"mot surligné : "+highlightedWord);
                    System.out.println("        "+"mot sélectionné : "+word);
                    System.out.println("        "+"MATCH");
                    engin.comparer(word);
                    return;                                             // On return vide ici pour que le programme ne continue pas de regarder tous les mots alors qu'il en a déjà trouvé un qui correpond à un mot surligné

                }
            }
        }
    }
    private String[] selectWords(String word) {
        String[] words;
        if (word.contains("\'"))                                    // correction de bug: si le mot avait une apostrophe, on prenait les 2 mots (ex: appelle devient m'appelle)
            words = word.split("\'");                         // solution: on met les 2 mots dans l'array et on regarde lequel correspond à un mot surligé
        else
            words = new String [] {word};
        System.out.println( "Selected word(s): " + Arrays.toString(words));
        return words;
    }
    private void verifyText() {             //On surligne les mots qui ne se trouvent pas dans le dictionnaire
        removeHighlights(textArea);

        String text = textArea.getText();
        text = text.replaceAll("\\p{Punct}", " ");  //On ne corrige pas la ponctuation ni les nombres
        text = text.replaceAll("\\p{Digit}", "");
        String textToCorrect="";
        for(String mot : text.split(" ")) {         //On verifie les mots 1 par 1
            if(!mot.equals("")) {
                if(!engin.trouver(mot.toLowerCase())) {
                    highlight(textArea, mot);               //Si le mot n'est pas dans le dictionnaire, on le surligne
                }
            }
        }
    }
    private void saveTextInFile() {                                             //On enregistre le texte de textArea dans un fichier que l'utilisateur selectionne
        JFileChooser filechooser = new JFileChooser(".");
        int returnVal = filechooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if(filechooser.getSelectedFile().getName().endsWith(".txt")) {
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(filechooser.getSelectedFile()));       //On utilise un stream pour ecrire le texte
                    out.write(textArea.getText());
                    out.close();
                } catch (IOException e) {
                    System.err.println("erreur fichier" + e.toString());
                }
            }
        }
    }
    private void openFileToCorrect() {      //L'utilisateur choisit un fichier texte à importer dans le textArea
        JFileChooser filechooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text file","txt");
        filechooser.setFileFilter(filter);
        int returnVal = filechooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if(filechooser.getSelectedFile().getName().endsWith(".txt")) {
                fichierACorriger = filechooser.getSelectedFile();
                try {                       //On utilise un stream pour lire le contenu du fichier selectionné, et on l'ajoute dans le textArea de la fenetre
                    BufferedReader input = new BufferedReader( new FileReader(fichierACorriger.getAbsolutePath()) );
                    String ligne;
                    String text = "";
                    ligne = input.readLine();
                    while (ligne != null) {
                        text+=ligne+"\n";
                        ligne = input.readLine();
                    }
                    input.close();
                    textArea.setText(text);
                }catch (IOException e) {
                    System.err.println("erreur fichier" + e.toString());
                }
            }
            else
                System.out.println("Ce n'est pas un .txt");
        }
    }
    private void createDictionary() {               //On utilise une version de notre TP1 contenant moins de méthodes pour stocker le dictionnaire dans une liste
        JFileChooser filechooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text file","txt");
        filechooser.setFileFilter(filter);
        int returnVal = filechooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if(filechooser.getSelectedFile().getName().endsWith(".txt")) {
                fichierDictionnaire = filechooser.getSelectedFile();
                engin.indexer(fichierDictionnaire); //On indexe le contenu du dictionnaire dans la liste chainée
            }
            else
                System.out.println("Ce n'est pas un .txt");
        }
    }


    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights

        try {
            Highlighter highlight = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;

            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                highlight.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }
    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter highlight = textComp.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        for (int i=0; i<highlights.length; i++) {
            if (highlights[i].getPainter() instanceof MyHighlightPainter) {
                highlight.removeHighlight(highlights[i]);
            }
        }
    }
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);
    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}
