package Gui;

import classes.DirectedWeightedGraph;
import classes.DirectedWeightedGraphAlgorithms;
import classes.NodeData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel implements ActionListener {
    DirectedWeightedGraphAlgorithms dwg = new DirectedWeightedGraphAlgorithms();
    GraphDraw gd;
    JLabel label;
    JLabel label2;
    JButton connected;
    JButton tsp;
    JButton shortestPath;
    JButton shortestPathDist;
    JButton center;

    final int PANEL_WIDTH = 225 , PANEL_HEIGHT = 700;
    static BufferedImage menuBackGround = null;

    public Menu(api.DirectedWeightedGraphAlgorithms algo, GraphDraw gd) {


        dwg = (DirectedWeightedGraphAlgorithms) algo;
        this.gd = gd;

        label = new JLabel("Choose an algorithm", JLabel.CENTER);
        label.setForeground(Color.green);
        label.setFont(new Font("MV Boli", Font.BOLD, 20));

        label2 = new JLabel("Edit Graph");
        label2.setForeground(Color.MAGENTA);


        connected = new JButton("Connected?");
        tsp = new JButton("TSP Algorithm");
        shortestPath = new JButton("Shortest Path");
        shortestPathDist = new JButton("Shortest Path Distance");
        center = new JButton("Center");

        connected.addActionListener(this);
        shortestPathDist.addActionListener(this);
        shortestPath.addActionListener(this);
        center.addActionListener(this);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        try {
//            menuBackGround = ImageIO.read(getClass().getResourceAsStream("/resources/menuBackGround.jpg"));  // for jar load image
            menuBackGround = ImageIO.read(new File("resources/menuBackGround.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(label);
        this.add(connected);
        this.add(tsp);
        this.add(shortestPath);
        this.add(shortestPathDist);
        this.add(center);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackGround(g);
    }


    private void drawBackGround(Graphics g) {
        g.drawImage(menuBackGround, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JDialog d = new JDialog(f, "isConnected?", true);
        d.setBounds(300, 150, 200, 100);
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
            int src = -1, dest = -1;
            DirectedWeightedGraph testingGraph = dwg.copy();
            int size = testingGraph.nodeSize();
            while (true) {
                System.out.println(testingGraph.nodeSize());
                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f, "Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if (src >= 0 && src < size && dest >= 0 && dest < size) {
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong inputs please try again", JOptionPane.INPUT_VALUE_PROPERTY, 0);
            }
            if (dwg.shortestPathDist(src, dest) != -1) {
                JOptionPane.showMessageDialog(f, String.valueOf(dwg.shortestPathDist(src, dest)), "Distance", 2);
            } else {
                JOptionPane.showMessageDialog(f, "There is no path between this two nodes", "Distance", 0);
            }
            f.dispose();
        }
        if (e.getSource() == shortestPath) {
            int src = -1, dest = -1;
            DirectedWeightedGraph testingGraph = dwg.copy();
            int size = testingGraph.nodeSize();
            while (true) {
                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f, "Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if (src >= 0 && src < size && dest >= 0 && dest < size) {
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong inputs please try again", JOptionPane.INPUT_VALUE_PROPERTY, 0);
            }
            StringBuilder ans = new StringBuilder("Starting Node");
            for (NodeData n: dwg.shortestPath(src,dest)) {
                ans.append("->").append(n.getKey());
            }
            ans.append("<-Ending Node");
            gd.repaint();
            JOptionPane.showMessageDialog(f,ans,"Path",JOptionPane.PLAIN_MESSAGE);
            }


        if (e.getSource() == center) {
            dwg.center();
            gd.repaint();

        }

    }
}



