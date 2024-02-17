package src.labs.infexf.agents;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

// SYSTEM IMPORTS
import edu.bu.labs.infexf.agents.SpecOpsAgent;
import edu.bu.labs.infexf.distance.DistanceMetric;
import edu.bu.labs.infexf.graph.Vertex;
import edu.bu.labs.infexf.graph.Path;


import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;



// JAVA PROJECT IMPORTS


public class InfilExfilAgent
    extends SpecOpsAgent
{

    public InfilExfilAgent(int playerNum)
    {
        super(playerNum);
    }

    // if you want to get attack-radius of an enemy, you can do so through the enemy unit's UnitView
    // Every unit is constructed from an xml schema for that unit's type.
    // We can lookup the "range" of the unit using the following line of code (assuming we know the id):
    // int attackRadius = state.getUnit(enemyUnitID).getTemplateView().getRange();
    @Override
    public float getEdgeWeight(Vertex src, Vertex dst, StateView state) {
        float cost = 1f;

        int dstX = dst.getXCoordinate();
        int dstY = dst.getYCoordinate();

        for (Integer enemyUnitID : getOtherEnemyUnitIDs()) {
            if(enemyUnitID == null || state.getUnit(enemyUnitID) == null) continue;
            int attackRadius = state.getUnit(enemyUnitID).getTemplateView().getRange();
            int enemyX = state.getUnit(enemyUnitID).getXPosition();
            int enemyY = state.getUnit(enemyUnitID).getYPosition();
            Vertex enemyLocation = new Vertex(enemyX, enemyY);

            // If the destination is within the enemy's attack radius, increase the cost dramatically
            if (dstX >= (enemyX - attackRadius) && dstX <= (enemyX + attackRadius) &&
                dstY >= (enemyY - attackRadius) && dstY <= (enemyY + attackRadius))
                cost += 1000f;
            // Incentivize moving away from the enemy
            float srcToEnemy = DistanceMetric.euclideanDistance(src, enemyLocation);
            float dstToEnemy = DistanceMetric.euclideanDistance(dst, enemyLocation);
            cost += 2 * (srcToEnemy - dstToEnemy);
        }

        return cost;
    }
    @Override
    public boolean shouldReplacePlan(StateView state)
    {
        return true;
    }

}
