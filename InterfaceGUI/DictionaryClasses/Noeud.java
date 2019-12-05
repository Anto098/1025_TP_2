package InterfaceGUI.DictionaryClasses;

public class Noeud
{
    protected String nom;
    protected Noeud prochain;
    protected ListeValeur horizontale;

    protected Noeud(String n, Noeud p, ListeValeur h) // Noeud Vertical
    {
        nom = n.toLowerCase();
        prochain = p;
        horizontale = h;
    }
    protected Noeud(String n, Noeud p)   // Noeud Horizontal
    {
        nom = n.toLowerCase();
        prochain = p;
    }
}