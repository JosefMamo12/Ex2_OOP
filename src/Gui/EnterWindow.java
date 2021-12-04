//package src.Gui;
//
//import src.classes.DirectedWeightedGraph;
//import src.classes.DirectedWeightedGraphAlgorithms;
//
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class EnterWindow implements ActionListener {
//    JFrame frame = new JFrame();
//    JButton button1 = new JButton("Graph Builder");
//    JButton button2 = new JButton("Graph Algo");
//    JPanel jPanel = new JPanel();
//    DirectedWeightedGraph dwg;
//
//    public EnterWindow(DirectedWeightedGraph alg){
//        dwg = alg;
//        button1.setBounds(100,160,200,40);
//        button1.setFocusable(false);
//        button1.addActionListener(this);
//
//        button2.setBounds(60,100,200,40);
//        button2.setFocusable(false);
//
//        jPanel.setSize(200,400);
//        jPanel.setBackground(Color.gray);
//        jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        jPanel.setAlignmentY(Component.TOP_ALIGNMENT);
//        jPanel.setLayout(new FlowLayout());
//        jPanel.add(button1);
//        jPanel.add(button2);
//
//        frame.add(jPanel);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setSize(420,420);
//        frame.setLayout(null);
//        frame.setVisible(true);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if(e.getSource() == button1){
//            frame.dispose();
//            MyFrame frame = new MyFrame(dwg);
//        }
//    }
//}
