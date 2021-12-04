package src;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import src.Gui.MyFrame;
//import src.Gui.GraphDraw;

import javax.swing.*;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 extends JPanel {

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = new src.classes.DirectedWeightedGraph();
        return ans;
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new src.classes.DirectedWeightedGraphAlgorithms();
        ans.init(new src.classes.DirectedWeightedGraph());
        ans.load(json_file);
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {

        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        MyFrame frame = new MyFrame(alg);


    }

    public static void main(String[] args) {
        runGUI("Assignments/Ex2/data/G1.json");
    }

}