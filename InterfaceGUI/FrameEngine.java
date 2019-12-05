package InterfaceGUI;


import InterfaceGUI.DictionaryClasses.*;

public class FrameEngine {
    public Engin engin;
    public FrameEngine() {
        Engin engin = new Engin();
        Main_Frame mainframe = new Main_Frame(engin);

    }

}
