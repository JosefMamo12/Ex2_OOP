package Gui;

import javax.swing.*;
import java.awt.*;

/**
 *Frame of the screen.
 */
public class MyFrame extends JFrame {
    GraphDraw gd;
    Menu menu;
    myMenuBar menuBar;


    public MyFrame(api.DirectedWeightedGraphAlgorithms dwg) {
        gd = new GraphDraw(dwg);
        menu = new Menu(dwg, gd);
        menuBar = new myMenuBar(dwg,gd);
        this.setLayout(new BorderLayout());
        this.add(gd, BorderLayout.WEST);
        this.add(menu, BorderLayout.EAST);
        this.setJMenuBar(menuBar.menuBar());
        this.setSize(1200, 700);
        this.setTitle("Graph");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
