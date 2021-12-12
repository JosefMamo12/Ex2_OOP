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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the menu of the algorithms which sit on the panel at the right side of the screen.
 */

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

    final int PANEL_WIDTH = 225, PANEL_HEIGHT = 700;
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
        tsp.addActionListener(this);

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
        ImageIcon backgroundImage = null;
        JDialog d = new JDialog(f, "Is Connected?");
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setBounds(200, 130, 250, 150);
        javax.swing.Timer timer = new Timer(2000, (a) -> d.dispose());
        if (e.getSource() == connected) {
            if (dwg.isConnected()) {
                JLabel lb = new JLabel("The Graph is connected");
                lb.setFont(new Font("Ariel", Font.BOLD, 15));
                d.add(lb);
            } else {
                d.add(new JLabel("The graph is not connected"));
            }
            timer.start();
            d.setVisible(true);
        }
        if (e.getSource() == shortestPathDist) {
            int src = -1, dest = -1;
            DirectedWeightedGraph testingGraph = dwg.getGraph();
            int size = testingGraph.nodeSize();
            while (true) {
                System.out.println(testingGraph.nodeSize());
                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f, "Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if (testingGraph.contains(src) && testingGraph.contains(dest)) {
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
            DirectedWeightedGraph testingGraph = dwg.getGraph();
            int size = testingGraph.nodeSize();
            while (true) {
                String sourceUserResponse = JOptionPane.showInputDialog(f, "Please type in the source node", "Source");
                src = Integer.parseInt(sourceUserResponse);
                String destUserResponse = JOptionPane.showInputDialog(f, "Please type in the destination node", "Destination");
                dest = Integer.parseInt(destUserResponse);
                if (testingGraph.contains(src) && testingGraph.contains(dest)) {
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong inputs please try again", JOptionPane.INPUT_VALUE_PROPERTY, 0);
            }
            StringBuilder ans = new StringBuilder("Starting Node");
            for (NodeData n : dwg.shortestPath(src, dest)) {
                ans.append("->").append(n.getKey());
            }
            ans.append("<-Ending Node");
            gd.repaint();
            JOptionPane.showMessageDialog(f, ans, "Path", JOptionPane.PLAIN_MESSAGE);
        }


        if (e.getSource() == center) {
            dwg.center();
            gd.repaint();

        }
        if (e.getSource() == tsp) {
            DirectedWeightedGraph testingGraph = dwg.getGraph();
            List<NodeData> lst = new LinkedList<>();
            int counter = 0;
            while (true) {
                int num = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter travel node (-1 to stop adding)", "TSP", JOptionPane.QUESTION_MESSAGE));
                if (lst.size() == 0 && testingGraph.contains(num)) {
                    lst.add(new NodeData(testingGraph.getNode(num)));
                    counter++;
                } else if (lst != null && !lst.contains(testingGraph.getNode(num)) && testingGraph.contains(num) && counter < testingGraph.nodeSize()) {
                    lst.add(new NodeData(testingGraph.getNode(num)));
                    counter++;
                } else if (counter == testingGraph.nodeSize()) {
                    JOptionPane.showMessageDialog(f, "You reach to limit nodes in the graph");
                    break;
                } else if (num == -1) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(f, "Worng input!!! please try again");
                }

            }
            if (dwg.tsp(lst) == null) {
                JOptionPane.showMessageDialog(f, "Returned null means there is no possible path between this relative nodes.");
            } else {
                List<NodeData> lst1 = dwg.tsp(lst);
                dwg.clean();
                StringBuilder ans = new StringBuilder("Starting Node");
                int src = -1, dest = -1;
                counter = 0;
                for (NodeData n : lst1) {
                    ans.append("->").append(n.getKey());
                    testingGraph.getNode(n.getKey()).setInfo("Path");
                    if (counter == 0)
                        src = n.getKey();
                    else if (counter == 1) {
                        dest = n.getKey();
                        testingGraph.getEdge(src, dest).setInfo("ToPaint");
                    } else {
                        src = dest;
                        dest = n.getKey();
                        testingGraph.getEdge(src, dest).setInfo("ToPaint");
                    }
                    counter++;
                }
                ans.append("<-Ending Node");
                gd.repaint();
                JOptionPane.showMessageDialog(f, ans, "Path", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }
}





