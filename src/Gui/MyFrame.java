package src.Gui;

import src.classes.DirectedWeightedGraph;
import src.classes.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame  {
    DirectedWeightedGraphAlgorithms algo;
    GraphDraw gd;
    Menu menu;



    public MyFrame(api.DirectedWeightedGraphAlgorithms dwg){
        menu = new Menu(dwg);
        gd = new GraphDraw(dwg);
        this.setLayout(new BorderLayout());
        this.add(gd);
        this.add(menu,BorderLayout.EAST);
        this.setSize(1200,700);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
