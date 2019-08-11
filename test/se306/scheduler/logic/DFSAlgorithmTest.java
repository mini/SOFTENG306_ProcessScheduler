package se306.scheduler.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import se306.scheduler.DotFile;
import se306.scheduler.exception.InvalidFileFormatException;
import se306.scheduler.graph.Node;

public class DFSAlgorithmTest {
    
    @Test
    void testDFSAlgorithm() {
        List<Node> graph = new ArrayList<Node>();

        Node a = new Node("a", 2);
        Node b = new Node("b", 3);
        Node c = new Node("c", 3);
        Node d = new Node("d", 2);
        a.addChild(b, 1);
        a.addChild(c, 2);
        b.addChild(d, 2);
        c.addChild(d, 1);
        graph.addAll(Arrays.asList(a, b, c, d));
        
        Algorithm algorithm = new DFSAlgorithm(2);
        algorithm.setGraph(graph);
        algorithm.addListener(new AlgorithmListener() {
            @Override
            public void algorithmCompleted(List<Node> schedule) {
                try {
                    DotFile dot = new DotFile("test_data/test1.dot");
                    
                    dot.write("test_data/test1_outdfs.dot", schedule);
                    
                    Scanner outScanner = new Scanner(new File("test_data/test1_out_dfs.dot"));
                    Scanner validOutScanner = new Scanner(new File("test_data/test1_out_dfs_valid.dot"));
                    
                    // https://stackoverflow.com/a/3403112
                    String output = outScanner.useDelimiter("\\Z").next();
                    String validOutput = validOutScanner.useDelimiter("\\Z").next();
                    assertEquals(validOutput + "s", output);
                    
                    outScanner.close();
                    validOutScanner.close();
                } catch (InvalidFileFormatException e) {
                    e.printStackTrace();
                    fail("Could not open test_data/test1.dot.");
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("Could not write to test_data/test1_outdfs.dot.");
                }
            }
        });
    }
}
