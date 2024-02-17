package src.labs.stealth.agents;

// SYSTEM IMPORTS
import edu.bu.labs.stealth.agents.MazeAgent;
import edu.bu.labs.stealth.graph.Vertex;
import edu.bu.labs.stealth.graph.Path;


import edu.cwru.sepia.environment.model.state.State.StateView;


import java.util.HashSet;   // will need for dfs
import java.util.Stack;     // will need for dfs
import java.util.Set;       // will need for dfs


// JAVA PROJECT IMPORTS


public class DFSMazeAgent
    extends MazeAgent
{

    public DFSMazeAgent(int playerNum)
    {
        super(playerNum);
    }

    @Override
    public Path search(Vertex src,
                       Vertex goal,
                       StateView state)
    {
        // implement the dfs search algorithm to find a path from 
        // the src vertex to the goal vertex
        // should produce a path that ends before the goal vertex

        // create a stack to hold the vertices to be visited
        Stack<Path> nextup = new Stack<Path>();
        nextup.push(new Path(src));

        Set<Vertex> visited = new HashSet<Vertex>();
        visited.add(src);    

        while(!nextup.isEmpty()) {
            // Pop the next vertex to visit
            Path path = nextup.pop();
    
            Vertex lastVertex = path.getDestination();
            int lastVertexX = lastVertex.getXCoordinate();
            int lastVertexY = lastVertex.getYCoordinate();
    
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    if(xOffset == 0 && yOffset == 0) {
                        continue;
                    }
                    int newX = lastVertexX + xOffset;
                    int newY = lastVertexY + yOffset;
                    if(!state.inBounds(newX, newY)) {
                        continue;
                    } else if(state.isResourceAt(newX, newY)){
                        continue;
                    }
                    Vertex neighbor = new Vertex(newX, newY);
                    if(neighbor.equals(goal)) {
                        return path;
                    } else if (!visited.contains(neighbor)) {
                        nextup.push(new Path(neighbor, 1f, path));
                        visited.add(neighbor);
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