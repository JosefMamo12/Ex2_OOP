package Gui;

import Util.Point3D;
import classes.DirectedWeightedGraph;
import classes.DirectedWeightedGraphAlgorithms;
import classes.GeoLocation;
import classes.NodeData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Menu bar thats include file (save,load) and Edit(Add edge, Add Node ....)
 */
public class myMenuBar extends JMenuBar implements ActionListener, MouseListener {
    GraphDraw gd;
    DirectedWeightedGraphAlgorithms dwg;
    JMenuBar menu;
    JMenu file;
    JMenu edit;
    JMenuItem save;
    JMenuItem addNode;
    JMenuItem addEdge;
    JMenuItem cleanGraph;
    JMenuItem removeNode;
    JMenu load;
    JMenuItem G1;
    JMenuItem G2;
    JMenuItem G3;
    JMenuItem removeEdge;
    boolean enabled = false;

    myMenuBar(api.DirectedWeightedGraphAlgorithms g, GraphDraw gd) {
        dwg = (DirectedWeightedGraphAlgorithms) g;
        this.gd = gd;

        menu = new JMenuBar();

        edit = new JMenu("Edit");
        file = new JMenu("File");
        load = new JMenu("Load");

        removeNode = new JMenuItem("Remove Node");
        removeEdge = new JMenuItem("Remove Edge");
        addNode = new JMenuItem("Add node");
        addEdge = new JMenuItem("Add edge");
        cleanGraph = new JMenuItem("Clean");


        save = new JMenuItem("Save");
        G1 = new JMenuItem("G1.json");
        G2 = new JMenuItem("G2.json");
        G3 = new JMenuItem("G3.json");

        load.add(G1);
        load.add(G2);
        load.add(G3);

        file.add(save);
        file.add(load);


        edit.add(addNode);
        edit.add(addEdge);
        edit.add(removeEdge);
        edit.add(removeNode);
        edit.add(cleanGraph);

        menu.add(file);
        menu.add(edit);

        cleanGraph.addActionListener(this);
        save.addActionListener(this);
        G1.addActionListener(this);
        G2.addActionListener(this);
        G3.addActionListener(this);
        addEdge.addActionListener(this);
        addNode.addActionListener(this);
        removeEdge.addActionListener(this);
        removeNode.addActionListener(this);

    }

    public JMenuBar menuBar() {
        return menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (e.getSource() == save) {
            String fileToSave = JOptionPane.showInputDialog("Please enter file name");
            dwg.save("data/" + fileToSave + ".json");
        }
        if (e.getSource() == G1) {
//            dwg.clean();
            dwg.whenLoadClicked();
            dwg.load("data/G1.json");
            gd.repaint();
        }
        if (e.getSource() == G2) {
//            dwg.clean();
            dwg.whenLoadClicked();
            dwg.load("data/G2.json");
            gd.repaint();
        }
        if (e.getSource() == G3) {

//            dwg.clean();
            dwg.whenLoadClicked();
            dwg.load("data/G3.json");
            gd.repaint();
        }
        if (e.getSource() == addNode) {
            enabled = true;
            JDialog d = new JDialog(f, "ADD NODE", true);
            d.setBounds(300, 150, 200, 100);
            d.add(new JLabel("Please tap on the screen"));
            Timer timer = new Timer(1000, (a) -> d.dispose());
            timer.start();
            gd.addMouseListener(this);
            d.setVisible(true);


        }
        if (e.getSource() == addEdge) {
            DirectedWeightedGraph dwa = dwg.getGraph();
            while (true) {
                int src = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter source node(Integer)", "Source"));
                int dest = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter destination node(Integer)", "Destination"));
                double weight = Double.parseDouble(JOptionPane.showInputDialog(f, "Please enter weight bigger than 0 (Double)", "Weight"));

                if (dwa.getNode(src) != null && dwa.getNode(dest) != null && weight > 0) {
                    dwa.connect(src, dest, weight);
                    gd.repaint();
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong inputs please try again", JOptionPane.INPUT_VALUE_PROPERTY, JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == removeEdge) {
            DirectedWeightedGraph dwa = dwg.getGraph();
            while (true) {
                int src = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter source node(Integer)", "Source"));
                int dest = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter destination node(Integer)", "Destination"));
                if (dwa.getNode(src) != null && dwa.getNode(dest) != null) {
                    dwa.removeEdge(src, dest);
                    gd.repaint();
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong inputs please try again", JOptionPane.INPUT_VALUE_PROPERTY, JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == cleanGraph) {
            dwg.clean();
            gd.repaint();
        }

        if (e.getSource() == removeNode) {
            DirectedWeightedGraph dwa = dwg.getGraph();
            while (true) {
                int nodeToRemove = Integer.parseInt(JOptionPane.showInputDialog(f, "Please enter node number to remove(Integer)", "RemovedNODE"));
                if (dwa.getNode(nodeToRemove) != null) {
                    dwa.removeNode(nodeToRemove);
                    gd.repaint();
                    break;
                } else
                    JOptionPane.showMessageDialog(f, "Wrong input please try again", JOptionPane.INPUT_VALUE_PROPERTY, JOptionPane.ERROR_MESSAGE);
            }

        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(enabled) {
            DirectedWeightedGraph dwa = dwg.getGraph();
            int x = e.getX();
            int y = e.getY();
            System.out.println(x);
            System.out.println(y);
            Point3D p = gd.range.frame2world(new GeoLocation(x, y, 0));
            dwa.addNode(new NodeData(new GeoLocation(p.x(), p.y(), p.z())));
            gd.repaint();
            enabled = false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
