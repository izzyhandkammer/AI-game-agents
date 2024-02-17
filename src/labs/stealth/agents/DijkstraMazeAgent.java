package src.labs.stealth.agents;

// SYSTEM IMPORTS
import edu.bu.labs.stealth.agents.MazeAgent;
import edu.bu.labs.stealth.graph.Vertex;
import edu.bu.labs.stealth.graph.Path;


import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.util.Direction;                           // Directions in Sepia


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue; // heap in java
import java.util.Set;
import java.util.Stack;


// JAVA PROJECT IMPORTS


public class DijkstraMazeAgent
    extends MazeAgent
{

    public DijkstraMazeAgent(int playerNum)
    {
        super(playerNum);
    }

    @Override
    public Path search(Vertex src,
                       Vertex goal,
                       StateView state)
    {
		//Direction costs: Direction.EAST or Direction.WEST edge costs are 5f
        //Direction costs: Direction.SOUTH edge costs are 1f
        //Direction costs: Direction.NORTH edge costs are 10f
        //when moving on a diagonal, take the two cardinal directions used in the diagonal,
        //add the squares of their edge weights, and take the square root for the final edge cost

        // Create a priority queue to hold the vertices to be visited, ordered by total cost
        PriorityQueue<Path> nextup = new PriorityQueue<>(Comparator.comparing(Path::getCost));

        // Create a map to hold the shortest known distance to each vertex
        Map<Vertex, Float> shortestDistances = new HashMap<>();
        shortestDistances.put(src, 0f);

        nextup.add(new Path(src, 0f, null));

        while (!nextup.isEmpty()) {
            // Dequeue the next vertex to visit
            Path path = nextup.poll();
            Vertex lastVertex = path.getDestination();

            if (lastVertex.equals(goal)) {
                return path;
            }

            for (Direction direction : Direction.values()) {
                Vertex neighbor = lastVertex.getNeighbor(direction);
                if (neighbor == null || !state.inBounds(neighbor.getXCoordinate(), neighbor.getYCoordinate())) {
                    continue;
                }

                float edgeCost;
                switch (direction) {
                    case EAST:
                    case WEST:
                        edgeCost = 5f;
                        break;
                    case NORTH:
                        edgeCost = 10f;
                        break;
                    case SOUTH:
                        edgeCost = 1f;
                        break;
                    default: // diagonal
                        Direction[] cardinalDirections = direction.getCardinalDirections();
                        float cost1 = cardinalDirections[0] == Direction.NORTH ? 10f : 5f;
                        float cost2 = cardinalDirections[1] == Direction.NORTH ? 10f : 5f;
                        edgeCost = (float) Math.sqrt(cost1 * cost1 + cost2 * cost2);
                        break;
                }

                float newCost = path.getCost() + edgeCost;
                if (!shortestDistances.containsKey(neighbor) || newCost < shortestDistances.get(neighbor)) {
                    shortestDistances.put(neighbor, newCost);
                    nextup.add(new Path(neighbor, newCost, path));
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
