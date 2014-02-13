package competition.uu2013;

import ch.idsia.agents.Agent;
import competition.uu2013.Hmmm.Level;
import competition.uu2013.Hmmm.LevelScene;

public class LevelSceneAgent extends MarioAIAgent implements Agent
{

    private LevelScene levelScene;

    public LevelSceneAgent()
    {
        super("LevelSceneAgent");
        this.levelScene = new LevelScene();
        this.levelScene.level = new Level(1500,15);
    }

}
