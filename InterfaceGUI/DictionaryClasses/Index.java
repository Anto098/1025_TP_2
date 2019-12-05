package InterfaceGUI.DictionaryClasses;

public class Index {
    protected ListeCle listeVerticale = new ListeCle();

    public void inserer(String valeur) {
        String cle = String.valueOf(valeur.charAt(0));
        Noeud vNode = listeVerticale.trouver(cle);

        if (vNode==null) {
            /*Si le noeud vertical "cle" n'existe pas, on l'ajoute et on lui assigne une
            nouvelle liste horizontale contenant uniquement le noeud avec "valeur" et "freq" */
            ListeValeur horizontale = new ListeValeur();
            horizontale.inserer(valeur);
            listeVerticale.inserer(cle, horizontale);
        }
        else {
            /*Le noeud vertical existe et on trouver si le noeud contenant "valeur" et "int" existe,
            sinon on ajoute un noeud horizontal*/
            Noeud hNode = vNode.horizontale.trouver(valeur);
            if (hNode == null) vNode.horizontale.inserer(valeur);
        }
    }
}
