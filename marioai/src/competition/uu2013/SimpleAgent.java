package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Map;

public class SimpleAgent // extends BasicMarioAIAgent implements Agent
{
    /*
    private boolean DangerOfAny()
    {

        if ((getReceptiveFieldCellValue(marioEgoRow + 2, marioEgoCol + 1) == 0 &&
                getReceptiveFieldCellValue(marioEgoRow + 1, marioEgoCol + 1) == 0) ||
                getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 0 ||
                getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 2) != 0 ||
                getEnemiesCellValue(marioEgoRow, marioEgoCol + 1) != 0 ||
                getEnemiesCellValue(marioEgoRow, marioEgoCol + 2) != 0)
            return true;
        else
            return false;
    }

    public boolean[] getAction()
    {
        long startTime = System.currentTimeMillis();
        action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;


        Node[][] NodeArray = getNodeArray(mergedObservation);

        printArray(NodeArray);
        System.out.println("Mario Postion: " + marioFloatPos[0] + ": " + marioFloatPos[1]);
        System.out.println("Cell X:" + (((int) marioFloatPos[0] /16) -1) + " Cell Y:" + (((int) marioFloatPos[1] /16) -1) );
        System.out.println("MarioX: " + this.marioEgoRow + " MarioY: " + this.marioEgoCol );

        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        return action;
    }

    private Node[][] getNodeArray(byte[][] mergedObservationIn)
    {
        Node [] [] temp = new Node[mergedObservationIn.length][mergedObservationIn[0].length];
        for (int x = 0; x < mergedObservationIn.length; x++)
        {
            for (int y = 0; y < mergedObservationIn[x].length; y++ )
            {
                temp[x][y] = new Node(mergedObservationIn[x][y]);
            }
        }
        return temp;
    }

    public void reset()
    {
        action = new boolean[Environment.numberOfKeys];
        action[Mario.KEY_RIGHT] = true;
        action[Mario.KEY_SPEED] = true;
    }

    public void printArray (Node[][] array)
    {
        System.out.println(name + "--------------------------------------------------------");
        for (int x= 0; x < array.length; x++)
        {
            printRow(array[x], x);
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public void printRow(Node[] row, int x)
    {
        for (Node node : row)
        {
            System.out.print(node);
            System.out.print("\t");
        }
        System.out.println();
    }

    public SimpleAgent(String s)
    {
        super("Simple Agent");
    }
    */
}
