package ch.idsia.gui;

import ch.idsia.agents.AgentsPool;
import ch.idsia.tools.MarioAIOptions;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 16/11/13
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class MarioGUI extends Component
{
    private MarioAIOptions options;

    private JCheckBox byteCodeCountingCheckBox;
    private JCheckBox cannonsCheckBox;
    private JCheckBox coinsCheckBox;
    private JCheckBox continuousUpdatesGVCheckBox;
    private JCheckBox deadEndsCheckBox;
    private JCheckBox doubleScaleCheckBox;
    private JCheckBox exitOnSimulationEndCheckBox;
    private JCheckBox flatLevelCheckBox;
    private JCheckBox gameViewerCheckBox;
    private JCheckBox gapsCheckBox;
    private JCheckBox goombaCheckBox;
    private JCheckBox greenKoopaCheckBox;
    private JCheckBox hiddenBlocksCheckBox;
    private JCheckBox hillsCheckBox;
    private JCheckBox pauseWorldCheckBox;
    private JCheckBox powerRestoreCheckBox;
    private JCheckBox redKoopaCheckBox;
    private JCheckBox runOnTopCheckBox;
    private JCheckBox spikyCheckBox;
    private JCheckBox stopGameplayCheckBox;
    private JCheckBox tubesCheckBox;
    private JCheckBox visibleBlocksCheckBox;
    private JCheckBox visibleFieldCheckBox;
    private JCheckBox visualisationCheckBox;
    private JCheckBox wingedGoombaCheckBox;
    private JCheckBox wingedGreenKoopaCheckBox;
    private JCheckBox wingedRedKoopaCheckBox;
    private JCheckBox wingedSpikyCheckBox;

    private JComboBox levelTypeCombo;
    private JComboBox marioModeCombo;
    private JComboBox enemyObservationLevelCombo;

    private JLabel enemyOberservationLevelLabel;
    private JLabel fieldHeightLabel;
    private JLabel fieldWidthLabel;
    private JLabel fpsLabel;
    private JLabel levelDifficultyLabel;
    private JLabel levelHeightLabel;
    private JLabel levelLengthLabel;
    private JLabel levelTypeLabel;
    private JLabel marioGravityLabel;
    private JLabel marioModeLabel;
    private JLabel randomSeedLabel;
    private JLabel timeLimitLabel;
    private JLabel iterationsLabel;

    private JList agentList;

    private JSpinner fieldHeightSpinner;
    private JSpinner fieldWidthSpinner;
    private JSpinner fpsSpinner;
    private JSpinner levelDifficulty;
    private JSpinner levelHeightSpinner;
    private JSpinner levelLengthSpinner;
    private JSpinner marioGravitySpinner;
    private JSpinner randomSeedSpinner;
    private JSpinner timeLimitSpinner;
    private JSpinner iterationsSpinner;

    private JPanel enemiesPanel;
    private JPanel guiPanel;
    private JPanel levelOptionsPanel;
    private JPanel visualisationPanel;
    private JPanel agentOptionsPanel;
    private JPanel simulationOptionsPanel;
    private JPanel agentSelectionPanel;

    private JButton runButton;

    /**
     * set up the custom components
     */
    private void createUIComponents()
    {
        this.options = new MarioAIOptions();

        //borders and headings
        this.enemiesPanel = new JPanel();
        this.enemiesPanel.setBorder(BorderFactory.createTitledBorder("Available Enemies"));
        this.levelOptionsPanel = new JPanel();
        this.levelOptionsPanel.setBorder(BorderFactory.createTitledBorder("Level Options"));
        this.visualisationPanel = new JPanel();
        this.visualisationPanel.setBorder(BorderFactory.createTitledBorder("Visualisation Options"));
        this.agentOptionsPanel = new JPanel();
        this.agentOptionsPanel.setBorder(BorderFactory.createTitledBorder("Agent Options"));
        this.simulationOptionsPanel = new JPanel();
        this.simulationOptionsPanel.setBorder(BorderFactory.createTitledBorder("Simulation Options"));
        this.agentSelectionPanel = new JPanel();
        this.agentSelectionPanel.setBorder(BorderFactory.createTitledBorder("Select and run"));

        //checkboxes
        this.byteCodeCountingCheckBox = new JCheckBox();
        this.cannonsCheckBox = new JCheckBox();
        this.coinsCheckBox = new JCheckBox();
        this.continuousUpdatesGVCheckBox = new JCheckBox();
        this.deadEndsCheckBox = new JCheckBox();
        this.doubleScaleCheckBox = new JCheckBox();
        this.exitOnSimulationEndCheckBox = new JCheckBox();
        this.flatLevelCheckBox = new JCheckBox();
        this.gameViewerCheckBox = new JCheckBox();
        this.gapsCheckBox = new JCheckBox();
        this.goombaCheckBox = new JCheckBox();
        this.greenKoopaCheckBox = new JCheckBox();
        this.hiddenBlocksCheckBox = new JCheckBox();
        this.hillsCheckBox = new JCheckBox();
        this.powerRestoreCheckBox = new JCheckBox();
        this.redKoopaCheckBox = new JCheckBox();
        this.runOnTopCheckBox = new JCheckBox();
        this.spikyCheckBox = new JCheckBox();
        this.stopGameplayCheckBox = new JCheckBox();
        this.tubesCheckBox = new JCheckBox();
        this.visibleBlocksCheckBox = new JCheckBox();
        this.visibleFieldCheckBox = new JCheckBox();
        this.visualisationCheckBox = new JCheckBox();
        this.wingedGoombaCheckBox = new JCheckBox();
        this.wingedGreenKoopaCheckBox = new JCheckBox();
        this.wingedRedKoopaCheckBox = new JCheckBox();
        this.wingedSpikyCheckBox = new JCheckBox();

        //comboBoxes
        this.enemyObservationLevelCombo = new JComboBox(new String[]{"High detail","Medium detail","Low detail"});
        this.levelTypeCombo = new JComboBox(new String[]{"Overground","Underground","Castle"});
        this.marioModeCombo = new JComboBox(new String[]{"Small","Big","Big + Fire"});

        //Spinners
        //default, lower, upper, step
        this.fieldHeightSpinner = new JSpinner(new SpinnerNumberModel(22,0,4096,2));
        this.fieldWidthSpinner = new JSpinner(new SpinnerNumberModel(22,0,4096,2));
        this.fpsSpinner = new JSpinner(new SpinnerNumberModel(24,1,100,1));
        this.levelDifficulty = new JSpinner(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
        this.levelHeightSpinner = new JSpinner(new SpinnerNumberModel(15,1,4096,1));
        this.levelLengthSpinner = new JSpinner(new SpinnerNumberModel(320,1,4096,1));
        this.marioGravitySpinner = new JSpinner(new SpinnerNumberModel(1,0,2,0.1));
        this.randomSeedSpinner = new JSpinner(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
        this.timeLimitSpinner = new JSpinner(new SpinnerNumberModel(200,-1,Integer.MAX_VALUE,1));
        this.iterationsSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));

        //lists
        this.agentList = new JList(new String[] {"prototypes.LearningAgent","prototypes.StatefulAgent","prototypes.PixelAgent", "AStarAgent", "FirstAgent"});

        this.runButton = new JButton();

        this.setDefaults();
        this.addActionListners();

    }

    private void setDefaults()
    {
        this.options.setEcho(true);
        this.options.setZLevelScene(0);
        this.enemyObservationLevelCombo.setSelectedItem("High detail");
        this.options.setVisualization(true);
        this.visualisationCheckBox.setSelected(true);
        this.options.setViewerAlwaysOnTop(true);
        this.runOnTopCheckBox.setSelected(true);
        this.options.setTimeLimit(200);
        this.timeLimitSpinner.setValue(200);
        this.options.setPowerRestoration(false);
        this.powerRestoreCheckBox.setSelected(false);
        this.options.setExitProgramWhenFinished(true);
        this.exitOnSimulationEndCheckBox.setSelected(true);
        this.options.setGameViewer(false);
        this.gameViewerCheckBox.setSelected(false);
        this.options.setGameViewerContinuousUpdates(false);
        this.continuousUpdatesGVCheckBox.setSelected(false);
        this.options.setReceptiveFieldWidth(22);
        this.fieldWidthSpinner.setValue(22);
        this.options.setReceptiveFieldHeight(22);
        this.fieldHeightSpinner.setValue(22);
        this.options.setReceptiveFieldVisualized(false);
        this.visibleFieldCheckBox.setSelected(false);
        this.options.setMarioMode(2);
        this.marioModeCombo.setSelectedItem("Big + Fire");
        this.options.setMarioGravity(1);
        this.marioGravitySpinner.setValue(1);
        this.options.setFPS(24);
        this.fpsSpinner.setValue(24);
        this.options.setLevelType(0);
        this.levelTypeCombo.setSelectedItem("Overground");
        this.options.setLevelRandSeed(0);
        this.randomSeedSpinner.setValue(0);
        this.options.setLevelLength(320);
        this.levelLengthSpinner.setValue(320);
        this.options.setLevelHeight(15);
        this.levelHeightSpinner.setValue(15);
        this.options.setLevelDifficulty(0);
        this.levelDifficulty.setValue(0);
        this.options.setDeadEndsCount(false);
        this.deadEndsCheckBox.setSelected(false);
        this.options.setCannonsCount(true);
        this.cannonsCheckBox.setSelected(true);
        this.options.setHillStraightCount(true);
        this.hillsCheckBox.setSelected(true);
        this.options.setTubesCount(true);
        this.tubesCheckBox.setSelected(true);
        this.options.setGapsCount(true);
        this.gapsCheckBox.setSelected(true);
        this.options.setHiddenBlocksCount(true);
        this.hiddenBlocksCheckBox.setSelected(true);
        this.options.setEnemies("g,gw,gk,gkw,rk,rkw,s,sw");
        this.goombaCheckBox.setSelected(true);
        this.wingedGoombaCheckBox.setSelected(true);
        this.greenKoopaCheckBox.setSelected(true);
        this.wingedGreenKoopaCheckBox.setSelected(true);
        this.redKoopaCheckBox.setSelected(true);
        this.wingedRedKoopaCheckBox.setSelected(true);
        this.spikyCheckBox.setSelected(true);
        this.wingedSpikyCheckBox.setSelected(true);
        this.options.setBlocksCount(true);
        this.visibleBlocksCheckBox.setSelected(true);
        this.options.setCoinsCount(true);
        this.coinsCheckBox.setSelected(true);
        this.options.setFlatLevel(false);
        this.flatLevelCheckBox.setSelected(false);
        this.options.setPunj(false);
        this.byteCodeCountingCheckBox.setSelected(false);
        this.options.setStopGamePlay(false);
        this.stopGameplayCheckBox.setSelected(false);
        this.options.setScale2X(true);
        this.doubleScaleCheckBox.setSelected(true);

        this.options.setRecordFile("C:\\test.txt");

    }


    private void addActionListners()
    {
        //checkboxes
        //enemies group
        this.goombaCheckBox.addActionListener(new EnemiesActionListener());
        this.wingedGoombaCheckBox.addActionListener(new EnemiesActionListener());
        this.greenKoopaCheckBox.addActionListener(new EnemiesActionListener());
        this.wingedGreenKoopaCheckBox.addActionListener(new EnemiesActionListener());
        this.redKoopaCheckBox.addActionListener(new EnemiesActionListener());
        this.wingedRedKoopaCheckBox.addActionListener(new EnemiesActionListener());
        this.spikyCheckBox.addActionListener(new EnemiesActionListener());
        this.wingedSpikyCheckBox.addActionListener(new EnemiesActionListener());
        //end enemies group

        this.stopGameplayCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setStopGamePlay(((JCheckBox)e.getSource()).getModel().isSelected());
            }
        });
        this.tubesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setTubesCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.visibleBlocksCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setBlocksCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.visibleFieldCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setReceptiveFieldVisualized(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.visualisationCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setVisualization(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.runOnTopCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setViewerAlwaysOnTop(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.hiddenBlocksCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setHiddenBlocksCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.hillsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setHillStraightCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.powerRestoreCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setPowerRestoration(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.byteCodeCountingCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setPunj(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.cannonsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setCannonsCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.coinsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setCoinsCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.continuousUpdatesGVCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setGameViewerContinuousUpdates(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.deadEndsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setDeadEndsCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.doubleScaleCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setScale2X(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.exitOnSimulationEndCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setExitProgramWhenFinished(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.flatLevelCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setFlatLevel(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.gameViewerCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setGameViewer(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });
        this.gapsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarioGUI.this.options.setGapsCount(((JCheckBox) e.getSource()).getModel().isSelected());
            }
        });

        //comboBoxes
        this.enemyObservationLevelCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)((JComboBox) e.getSource()).getSelectedItem();
                int detailLevel = 0;
                if (selected.compareTo("Medium detail") == 0)
                {
                    detailLevel = 1;
                }
                else if (selected.compareTo("Low detail") == 0)
                {
                    detailLevel = 2;
                }
                MarioGUI.this.options.setZLevelScene(detailLevel);
            }
        });
        this.levelTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)((JComboBox) e.getSource()).getSelectedItem();
                int detailLevel = 0;
                if (selected.compareTo("Underground") == 0)
                {
                    detailLevel = 1;
                }
                else if (selected.compareTo("Castle") == 0)
                {
                    detailLevel = 2;
                }
                MarioGUI.this.options.setLevelType(detailLevel);
            }
        });
        this.marioModeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)((JComboBox) e.getSource()).getSelectedItem();
                int detailLevel = 0;
                if (selected.compareTo("Big") == 0)
                {
                    detailLevel = 1;
                }
                else if (selected.compareTo("Big + Fire") == 0)
                {
                    detailLevel = 2;
                }
                MarioGUI.this.options.setMarioMode(detailLevel);
            }
        });

        //spinners
        this.fieldHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setReceptiveFieldHeight((Integer)((JSpinner) e.getSource()).getValue());
            }
        });
        this.fieldWidthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setReceptiveFieldWidth((Integer)((JSpinner) e.getSource()).getValue());
            }
        });
        this.levelHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setLevelHeight((Integer)((JSpinner) e.getSource()).getValue());
            }
        });
        this.levelLengthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setLevelLength((Integer)((JSpinner) e.getSource()).getValue());
            }
        });
        this.fpsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setFPS((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        this.levelDifficulty.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setLevelDifficulty((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        this.marioGravitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setMarioGravity((Float) ((JSpinner) e.getSource()).getValue());
            }
        });
        this.randomSeedSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setLevelRandSeed((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        this.timeLimitSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MarioGUI.this.options.setTimeLimit((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        //buttons
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("marioAction called");

                List selectedAgents = MarioGUI.this.agentList.getSelectedValuesList();
                MarioAIOptions finalOptions = MarioGUI.this.options;

                for (int agentIterator = 0; agentIterator  < selectedAgents.size(); agentIterator++)
                {
                    finalOptions.setAgent(AgentsPool.loadAgent("competition.uu2013."+selectedAgents.get(agentIterator).toString(),finalOptions.isPunj()));
                    competition.uu2013.common.Map.setMap(((Integer) levelHeightSpinner.getValue()).intValue(), ((Integer) levelLengthSpinner.getValue()).intValue(), finalOptions);
                    new GUIScenario(finalOptions, (Integer)MarioGUI.this.iterationsSpinner.getValue());
                }

            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MarioGUI");
        frame.setContentPane(new MarioGUI().guiPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Implements an action listener for checkboxes on the enemiesPanel.  These checkboxes are a group and must be
     * acted on together.
     */
    private class EnemiesActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //String containing the list of enemies to include
            StringBuilder enemyString = new StringBuilder();

            //for each of the enemy checkboxes, check if we're including that type
            if (MarioGUI.this.goombaCheckBox.getModel().isSelected())
            {
                enemyString.append("g,");
            }
            if (MarioGUI.this.wingedGoombaCheckBox.getModel().isSelected())
            {
                enemyString.append("gw,");
            }
            if (MarioGUI.this.greenKoopaCheckBox.getModel().isSelected())
            {
                enemyString.append("gk,");
            }
            if (MarioGUI.this.wingedGreenKoopaCheckBox.getModel().isSelected())
            {
                enemyString.append("gkw,");
            }
            if (MarioGUI.this.redKoopaCheckBox.getModel().isSelected())
            {
                enemyString.append("rk,");
            }
            if (MarioGUI.this.wingedRedKoopaCheckBox.getModel().isSelected())
            {
                enemyString.append("rkw,");
            }
            if (MarioGUI.this.spikyCheckBox.getModel().isSelected())
            {
                enemyString.append("s,");
            }
            if (MarioGUI.this.wingedSpikyCheckBox.getModel().isSelected())
            {
                enemyString.append("sw,");
            }

            //remove the last comma
            if (enemyString.length() >0 )
            {
                enemyString.deleteCharAt(enemyString.length()-1);
            }

            //set the level enemies.
            MarioGUI.this.options.setEnemies(enemyString.toString());

        }
    }
}
