package src.Gui;

import src.classes.DirectedWeightedGraph;
import src.classes.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import javax.swing.plaf.basic.DefaultMenuLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {
    api.DirectedWeightedGraphAlgorithms dwg = new DirectedWeightedGraphAlgorithms();
    JLabel label;
    JButton connected;
    JButton tsp;
    JButton shortestPath;
    JButton shortestPathDist;
    JButton center;


    public Menu(api.DirectedWeightedGraphAlgorithms algo) {
        dwg = algo;
        label = new JLabel("Choose 1 Algorithm");
        connected = new JButton("Connected?");
        tsp = new JButton("TSP Algorithm");
        shortestPath = new JButton("Shortest Path");
        shortestPathDist = new JButton("Shortest Path Distance");
        center = new JButton("Center");
        connected.addActionListener(this);
        shortestPathDist.addActionListener(this);
        shortestPath.addActionListener(this);
        this.add(label);
        this.add(connected);
        this.add(tsp);
        this.add(shortestPath);
        this.add(shortestPathDist);
        this.add(center);
        this.setBackground(Color.orange);
        this.setPreferredSize(new Dimension(200, 700));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JDialog d = new JDialog(f, "isConnected?", true);
        d.setBounds(300, 150, 200, 100);
        int result;
        if (e.getSource() == connected) {
            if (dwg.isConnected()) {
                JLabel lb = new JLabel("The Graph is connected");
                lb.setFont(new Font("Ariel", Font.BOLD, 15));
                d.add(lb);
            } else {
                d.add(new JLabel("The graph is not connected"));
            }
            d.setVisible(true);
            f.remove(d);
        }
        if (e.getSource() == shortestPathDist) {
            int src = -1 ,dest = -1;
            DirectedWeightedGraph testingGraph = dwg.copy();
            while (src < 0 || src > testingGraph.nodeSize() || dest < 0 || dest > testingGraph.nodeSize()) {

                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f,"Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if(src < 0 || src > testingGraph.nodeSize() || dest < 0 || dest > testingGraph.nodeSize()){
                    JOptionPane.showMessageDialog(f,"Wrong inputs please try again",JOptionPane.INPUT_VALUE_PROPERTY,0);
                }
            }
            JOptionPane.showMessageDialog(f,String.valueOf(dwg.shortestPathDist(src,dest)),"Distance",2);
            f.dispose();
        }
        if(e.getSource() == shortestPath){
            int src = -1 ,dest = -1;
            DirectedWeightedGraph testingGraph = dwg.copy();
            while (src < 0 || src > testingGraph.nodeSize() || dest < 0 || dest > testingGraph.nodeSize()) {

                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f,"Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if(src < 0 || src > testingGraph.nodeSize() || dest < 0 || dest > testingGraph.nodeSize()){
                    JOptionPane.showMessageDialog(f,"Wrong inputs please try again",JOptionPane.INPUT_VALUE_PROPERTY,0);

                }

            }
        }
    }
}



