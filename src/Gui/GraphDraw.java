package src.Gui;

import src.Gui.MyFrame;
import src.Util.Point3D;
import src.Util.Range;
import src.Util.Range2D;
import src.Util.Range2Range;
import src.classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

public class GraphDraw extends JPanel {
    DirectedWeightedGraph graph;
    Range2Range range;


    public GraphDraw(api.DirectedWeightedGraphAlgorithms graph) {
        this.graph = graph.copy();
        this.setPreferredSize(new Dimension(1000,700));
    }

    public void paint(Graphics g) {
        super.paint(g);
        resize();
        importImages();
        Graphics2D g2d = (Graphics2D) g.create();
        paintBackground(g2d);
        drawGraph(g2d);
    }

    private void drawGraph(Graphics2D g2d) {
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : graph.getGraph().entrySet()) {
            int nodeKey = entry.getKey();
            Iterator<EdgeData> eItr = graph.edgeIter(nodeKey);
            while (eItr.hasNext()) {
                drawEdge(g2d, eItr.next());
            }
        }
        Iterator<NodeData> nd = graph.nodeIter();
        while (nd.hasNext()) {
            drawNode(g2d, nd.next());
        }
    }

    private void drawEdge(Graphics2D g2d, EdgeData edge) {
        GeoLocation s = (GeoLocation) graph.getNodes().get(edge.getSrc()).getLocation();
        GeoLocation d = (GeoLocation) graph.getNodes().get(edge.getDest()).getLocation();
        Point3D sP = (Point3D) range.world2frame(s);
        Point3D dP = (Point3D) range.world2frame(d);
        g2d.setColor(new Color(173, 122, 68));
//        g2d.drawLine((int)sP.x(),(int) sP.y(),(int)dP.x(),(int) dP.y());
        drawArrow(g2d, (int) sP.x(), (int) sP.y(), (int) dP.x(), (int) dP.y());

    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g2d.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        int ARR_SIZE = 10;
        g.setStroke(new BasicStroke(5));
        g.drawLine(0, 0, len, 0);
        // Draw horizontal arrow starting in (0, 0)
        g.fillPolygon(new int[]{len - 10, len - ARR_SIZE - 20, len - ARR_SIZE - 20, len - 10}, new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }


    private void drawNode(Graphics g2d, NodeData next) {
        GeoLocation pos = (GeoLocation) next.getLocation();
        Point3D fp = (Point3D) range.world2frame(pos);
        g2d.setColor(Color.BLACK);
        g2d.drawImage(nodePaint,(int) fp.x() - 10, (int) fp.y() - 15, null,this);
        g2d.setFont(new Font("MV Boli", Font.BOLD, 20));
        g2d.setColor(new Color(120,35,255));
        g2d.drawString(" " + next.getKey(), (int) fp.x() - 14, (int) fp.y() - 20);

    }

    private void resize() {
        Range rx = new Range(120, this.getWidth() - 120);
        Range ry = new Range(this.getHeight() - 70, 170);
        Range2D frame = new Range2D(rx, ry);
        range = new Range2Range(graphRange(), frame);
    }

    private Range2D graphRange() {
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        for (Map.Entry<Integer, NodeData> runner : graph.getNodes().entrySet()) {
            GeoLocation p = (GeoLocation) graph.getNode(runner.getKey()).getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) x0 = p.x();
                if (p.x() > x1) x1 = p.x();
                if (p.y() < y0) y0 = p.y();
                if (p.y() > y1) y1 = p.y();
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    static BufferedImage backGround = null;
    static BufferedImage nodePaint = null;


    private void paintBackground(Graphics2D g2d) {
        g2d.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);

    }

    public void importImages() {
        try {
            backGround = ImageIO.read(new File("Assignments/Ex2/data/Background1.jpg"));
            nodePaint = ImageIO.read(new File("Assignments/Ex2/data/new.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

