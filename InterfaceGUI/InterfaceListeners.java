/*package Interface_GUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class InterfaceListeners implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent e){
        Object source = e.getSource();
        String sourceName = source.getClass().getName();

        System.out.println(source.toString());
        if (sourceName == "fichierACorriger"){
            System.out.println("le fichier a corriger existe desormais");
        }
        else if(sourceName == "fichierDictionnaire"){
            System.out.println("le dico existe desormais");
        }
    }
}
*/