package src.labs.stealth.agents;

// SYSTEM IMPORTS
import edu.bu.labs.stealth.agents.MazeAgent;
import edu.bu.labs.stealth.graph.Vertex;
import edu.bu.labs.stealth.graph.Path;


import edu.cwru.sepia.environment.model.state.State.StateView;


import java.util.HashSet;       // will need for bfs
import java.util.Queue;         // will need for bfs
import java.util.LinkedList;    // will need for bfs
import java.util.Set;           // will need for bfs
import java.util.Stack;         // i do as i please


// JAVA PROJECT IMPORTS


public class BFSMazeAgent
    extends MazeAgent
{

    public BFSMazeAgent(int playerNum)
    {
        super(playerNum);
    }

    @Override
    public Path search(Vertex src,
                       Vertex goal,
                       StateView state)
    {
        // implement the bfs search algorithm to find a path from 
        // the src vertex to the goal vertex
        // should produce a path that ends before the goal vertex

        // create a queue to hold the vertices to be visited
        Queue<Path> nextup = new LinkedList<Path>();
        nextup.add(new Path(src));

        while(!nextup.isEmpty())
        {
            // dequeue the next vertex to visit
            Path path = nextup.remove();

            Vertex lastVertex = path.getDestination();
            int lastVertexX = lastVertex.getXCoordinate();
            int lastVertexY = lastVertex.getYCoordinate();

            for (int xOffset = -1; xOffset <= 1; xOffset++)
            {
                for (int yOffset = -1; yOffset <= 1; yOffset++)
                {
                    if(xOffset == 0 && yOffset == 0)
                    {
                        continue;
                    }
                    int newX = lastVertexX + xOffset;
                    int newY = lastVertexY + yOffset;
                    if(!state.inBounds(newX, newY))
                    {
                        continue;
                    } else if(state.isResourceAt(newX, newY)){
                        continue;
                    }
                    Vertex neighbor = new Vertex(newX, newY);
                    if(neighbor.equals(goal))
                    {
                        return path;
                    } else {
                        nextup.add(new Path(neighbor, 1f, path));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean shouldReplacePlan(StateView state)
    {
        //returns boolean true if the current path is invalid
        Stack<Vertex> currentPath = this.getCurrentPlan();

        for (Vertex v : currentPath)
        {
            if(state.isResourceAt(v.getXCoordinate(), v.getYCoordinate()))
            {
                return true;
            }
        }
        return false;
    }
}
