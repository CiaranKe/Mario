package ch.idsia.gui;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 17/11/13
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public class GUIScenario
{

    public GUIScenario(MarioAIOptions newOptions, int iterations)
    {
        //newOptions.setFrozenCreatures(true);
        //newOptions.setEnemies("");
        final BasicTask basicTask = new BasicTask(newOptions);
        basicTask.doEpisodes(iterations,true,1);
        try
        {
            System.out.println(basicTask.getEvaluationInfo().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }
}
