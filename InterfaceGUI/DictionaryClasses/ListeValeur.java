package InterfaceGUI.DictionaryClasses;

public class ListeValeur {
    protected Noeud premier;

    protected Noeud trouver(String name) {
        Noeud n = premier;
        while (n != null && !n.nom.equals(name) )
            n = n.prochain;
        return n;
    }

    public void inserer(String nom) // compare to : plus petit que 0, a est avant b
    //              plus grand que 0, b est avant a
    {
        if (premier == null) premier = new Noeud(nom, null);
        else inserer(nom,premier);
    }

    private void inserer (String nom, Noeud n){

        if (n.prochain == null) n.prochain = new Noeud (nom, null);
        else
        {
            if (nom.compareTo(n.prochain.nom) < 0)
                n.prochain = new Noeud(nom, n.prochain);
            else
                inserer(nom,n.prochain);
        }
    }
}
