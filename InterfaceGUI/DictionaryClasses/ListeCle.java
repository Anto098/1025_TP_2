package InterfaceGUI.DictionaryClasses;

public class ListeCle {

    protected Noeud premier;

    protected Noeud trouver(String name)
    {
        Noeud n = premier;
        while (n != null && !n.nom.equals(name) )
            n = n.prochain;
        return n;
    }

    protected void inserer(String nom, ListeValeur l) // compare to : plus petit que 0, a est avant b
    //              plus grand que 0, b est avant a
    {
        if (premier == null) premier = new Noeud(nom, null, l);
        else inserer(nom,premier,l);
    }
    private void inserer (String nom, Noeud n, ListeValeur l){
        if (n.prochain == null) n.prochain = new Noeud (nom, null, l);
        else
        {
            if (nom.compareTo(n.prochain.nom) < 0)
                n.prochain = new Noeud(nom, n.prochain, l);
            else
                inserer(nom,n.prochain,l);
        }
    }

}
