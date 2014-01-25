package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Action;
import competition.uu2013.common.Map;

import java.util.ArrayList;
import java.util.LinkedList;

public class AStarAgent extends BasicMarioAIAgent implements Agent
{
    private boolean action[];
    private String name;
    private int jumpCounter;
    private int speedCounter;
//    LinkedList<Node> openList;
//    LinkedList<Node> closedList;
    LinkedList<boolean[]> actionList;



    public AStarAgent()
    {
        super("AStarAgent");
        this.name = "AStarAgent";
        this.action = new boolean[Environment.numberOfKeys];
//        this.openList = new LinkedList<Node>();
//        this.closedList = new LinkedList<Node>();
        this.actionList = new LinkedList<boolean[]>();
    }


    public boolean[] getAction()
    {
        this.pathFind();
        return null;
    }


    public void reset()
    {
        this.action = Action.createAction(Action.RIGHT_SPEED);
        this.jumpCounter = 0;
        this.speedCounter = 0;
    }


    private void pathFind()
    {

/*        Node startNode = null; // Map.getNodeAt(this.marioFloatPos[0], this.marioFloatPos[1]);
        openList.add(startNode);
        ArrayList<boolean[]> possibleActions = Action.getPossibleActions(this.isMarioAbleToJump,this.marioFloatPos[0],
                this.marioFloatPos[1]);

        for (boolean [] pAction:  possibleActions)
        {
            float [] nodePosition = Map.getActionCoOrdinates(pAction,this.marioFloatPos[0], this.marioFloatPos[1],
                      this.isMarioAbleToJump,this.isMarioOnGround, this.marioMode);
            Node temp =  null; //Map.getNodeAt(nodePosition[0], nodePosition[1]);

            //calculate vector distance.
            //temp.distance =

            openList.add(temp);



        }


        //Iterate through processing each node.

            /*
            while (open.size()>0){
                    //Find the smallest element in the open list.
                    // using the estimatedTotalCost.
                    int current= open.getFirst().estTotalCost;
                    NodeRecord currentNode = open.getFirst();
                    for (int i=0; i < open.size(); i++){
                            if (current> open.get(i).estTotalCost){
                                    current = open.get(i).estTotalCost;
                                    currentNode = open.get(i);}
                    }
                    // end != goal?
                    // If it is the goal node, then terminate.
                    if (currentNode.n == end) break;

                    //Otherwise, get its outgoing connections.
                    // connections = graph.getConnections(current) --> not sure what to do here.

            }//end of while loop
            */
    }
}
