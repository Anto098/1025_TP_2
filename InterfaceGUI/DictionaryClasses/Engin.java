package InterfaceGUI.DictionaryClasses;

import java.io.File;
import java.util.Arrays;

public class Engin {

    protected Index index = new Index();

    public void indexer (File dictionnaire){
        Lecture.lire_lignes(dictionnaire.getAbsolutePath(), this);
    }

    public boolean trouver (String mot) {
        Noeud vNode = index.listeVerticale.trouver(""+mot.charAt(0));
        if (vNode != null) {
            if (vNode.horizontale.trouver(mot) != null) {
                return true;
            }
        }
        return false;
    }

    public void afficher() {
        ListeCle vList;
        vList = index.listeVerticale;

        if (vList.premier == null)
            System.out.println("Erreur: soit rien n'a ete indexe ou la liste n'a pas ete inversee");
        else {
            Noeud vNoeud = vList.premier;

            while (vNoeud != null) {
                System.out.println("\nNom de la clef : " + vNoeud.nom);
                ListeValeur hList = vNoeud.horizontale;
                Noeud hNoeud = hList.premier;
                while (hNoeud != null) {
                    System.out.println("    Valeur : " + hNoeud.nom);
                    hNoeud = hNoeud.prochain;
                }
                vNoeud = vNoeud.prochain;
            }
        }
    }

    public String[] comparer(String aComparer){
        ListeCle vList;
        vList = index.listeVerticale;
        int[] distances = {100,100,100,100,100};
        String[] meilleursMatchs = {"","","","",""};


        if (vList.premier == null)
            System.out.println("Erreur: rien n'a ete indexé");
        else {
            findClosestWords(aComparer, vList, distances, meilleursMatchs);
        }
        System.out.println(Arrays.toString(meilleursMatchs));
        return meilleursMatchs;
    }

    private void findClosestWords(String aComparer, ListeCle vList, int[] distances, String[] meilleursMatchs) {
        Noeud vNoeud = vList.premier;
        while (vNoeud != null) {                                                // On boucle sur les noeuds verticaux et horizontaux et on compare tous les mots avec le mot qui est à corriger (le mot sur lequel on a cliqué)
            ListeValeur hList = vNoeud.horizontale;
            Noeud hNoeud = hList.premier;
            while (hNoeud != null) {
                String motDictionnaire = new String(hNoeud.nom);
                int x = Engin.calculateurDistance(motDictionnaire, aComparer);  //x est la distance entre le mot du dictionnaire et le mot qu'on veut corriger
                int largestDistance = distances[0];

                int index = 0;
                for (int i = 0; i <distances.length;i++){                   //Dans le tableau, on cherche le mot qui a la plus grande distance par rapport au mot qu'on veut corriger. On veut le remplacer en priorité.
                    if (distances[i]>largestDistance) {                     // Lorsqu'on trouve le mot ayant la plus grande distance par rapport au mot qui n'est pas dans le dictionnaire, on garde son index
                        largestDistance = distances[i];
                        index = i;
                    }
                }

                if (x<largestDistance) {                        //si la distance du mot du dictionnaire est plus petite que la plus grande distance des 5 mots deja selectionnés, on la met dans le tableau
                    distances[index]=x;
                    meilleursMatchs[index]=motDictionnaire;
                }
                hNoeud = hNoeud.prochain;
            }
            vNoeud = vNoeud.prochain;
        }
    }

    public static int calculateurDistance(String s1, String s2){
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        for(int i=0;i<=s1.length();i++)
            edits[i][0]=i;
        for(int j=1;j<=s2.length();j++)
            edits[0][j]=j;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(
                        edits[i-1][j]+1,
                        Math.min(
                                edits[i][j-1]+1,
                                edits[i-1][j-1]+u
                        )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }

}