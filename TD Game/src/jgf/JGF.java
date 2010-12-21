package jgf;


import javax.swing.JOptionPane;
import jgf.util.Version;

/*
 */

/**
 *
 * @author Vinicius
 */
public class JGF {

    public static final Version VERSION = new Version(1,0,0);
    
    public static void main(String[] args) {
        String text = "<html>Java Game Framework " + (VERSION.isBeta() ? "<font color=\"red\">" : "") + "v." + VERSION;
                
        JOptionPane.showMessageDialog(null, text);        
    }

}
