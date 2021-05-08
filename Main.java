import TP1.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// TP1 IFT 1025 AUTOMNE 2019
// ANTONIN ROY 20145595
// HUGO BAUDCHON 20071284
public class Main {
    public static void main(String[] args) throws IOException {
        Engin engin = new Engin();

        while(true) {                                               // Logique du code en général, on boucle en entrant des commandes, tant que la commande n'est pas "end"
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Entrez votre commande");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s = br.readLine();
            Commandes.traiterCommande(s, engin);
        }
    }
}
