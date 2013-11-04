package ch.idsia.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Ciaran Kearney
 * Date: 04/11/13
 */
public class MarioGUI extends Component {
    //agent List
    private JList selectAgentList;

    //checkboxes
    private JCheckBox enableGameViewerCheckBox;
    private JCheckBox enableContinuousUpdatesToGameCheckBox;
    private JCheckBox enableVisualisationCheckBox;
    private JCheckBox enableOnTopTextBox;
    private JCheckBox enableInfiniteTimeCheckbox;
    private JCheckBox enableStopOnWinCheckBox;
    private JCheckBox disableInteractionsCheckBox;
    private JCheckBox enablePowerRestoreCheckBox;
    private JCheckBox enableExitOnSimulationEndCheckBox;
    private JCheckBox enableDeadEndsCheckBox;
    private JCheckBox enableTubesCheckBox;
    private JCheckBox enableCannonsCheckBox;
    private JCheckBox enableHillsCheckBox;
    private JCheckBox enableGapsCheckBox;
    private JCheckBox enableHiddenBlocksCheckBox;
    private JCheckBox enableEnemiesCheckBox;
    private JCheckBox enableBlocksCheckBox;
    private JCheckBox enableCoinsCheckBox;

    //labels
    private JLabel mapZoomLevelJLabel;
    private JLabel enemyZoomLevelJLabel;
    private JLabel timeLimitJLabel;
    private JLabel marioModeJLabel;
    private JLabel levelTypeJLabel;
    private JLabel randomSeedJLabel;
    private JLabel fpsJLabel;
    private JLabel levelLengthJLabel;
    private JLabel levelHeightJLabel;
    private JLabel levelDifficultyJLabel;

    //combo boxes
    private JComboBox enemyZoomLevelComboBox;
    private JComboBox zoomLevelComboBox;
    private JComboBox marioModeComboBox;
    private JComboBox levelTypeComboBox;

    //spinners
    private JSpinner timeLimitJSpinner;
    private JSpinner fpsJSpinner;
    private JSpinner randomSeedJSpinner;
    private JSpinner levelLengthJSpinner;
    private JSpinner levelDifficultlyJSpinner;
    private JSpinner levelHeightJSpinner;

    //text fields
    private JTextField matLabOutputField;

    //buttons
    private JButton runButton;
    private JButton matlabOutputFileButton;

    //panels
    private JPanel guiPanel;
    private JPanel agentPanel;
    private JPanel matLabPanel;
    private JPanel enemyPanel;
    private JPanel visualizationPanel;
    private JPanel levelPanel;
    private JPanel innerLevelPanel;
    private JPanel innerVisualizationPanel;
    private JPanel timePanel;


    public MarioGUI()
    {

        matlabOutputFileButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final JFileChooser chooser = new JFileChooser();
                int returnValue = chooser.showOpenDialog(MarioGUI.this);

                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    MarioGUI.this.matLabOutputField.setEnabled(true);
                    MarioGUI.this.matLabOutputField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }


    private void createUIComponents()
    {
        //setup

        //checkboxes
        this.enableBlocksCheckBox = new JCheckBox(StringLookup.ENABLE_BLOCKS_TEXT);
        this.enableCannonsCheckBox = new JCheckBox(StringLookup.ENABLE_CANNONS_TEXT);
        this.enableCoinsCheckBox = new JCheckBox(StringLookup.ENABLE_COINS_TEXT);
        this.enableContinuousUpdatesToGameCheckBox = new JCheckBox(StringLookup.UPDATE_GAME_VIEW_TEXT);
        this.enableDeadEndsCheckBox = new JCheckBox(StringLookup.LEVEL_DEAD_ENDS_TEXT);
        this.enableEnemiesCheckBox = new JCheckBox(StringLookup.ENABLE_ENEMIES_TEXT);
        this.disableInteractionsCheckBox = new JCheckBox(StringLookup.DISABLE_INTERACTIONS_TEXT);
        this.enableGameViewerCheckBox = new JCheckBox(StringLookup.UPDATE_GAME_VIEW_TEXT);
        this.enableVisualisationCheckBox = new JCheckBox(StringLookup.ENABLE_VISUALISATION_TEXT);
        this.enableOnTopTextBox = new JCheckBox(StringLookup.ON_TOP_TEXT);
        this.enableInfiniteTimeCheckbox = new JCheckBox(StringLookup.INFINITE_TIME_TEXT);
        this.enableStopOnWinCheckBox = new JCheckBox(StringLookup.STOP_ON_WIN_TEXT);
        this.enablePowerRestoreCheckBox = new JCheckBox(StringLookup.POWER_RESTORE_TEXT);
        this.enableExitOnSimulationEndCheckBox = new JCheckBox(StringLookup.EXIT_ON_SIMULATION_END_TEXT);
        this.enableTubesCheckBox = new JCheckBox(StringLookup.ENABLE_TUBES_TEXT);
        this.enableHillsCheckBox = new JCheckBox(StringLookup.ENABLE_HILLS_TEXT);
        this.enableGapsCheckBox = new JCheckBox(StringLookup.ENABLE_GAPS_TEXT);
        this.enableHiddenBlocksCheckBox = new JCheckBox(StringLookup.ENABLE_HIDDEN_BLOCKS_TEXT);

        //labels
        this.mapZoomLevelJLabel = new JLabel(StringLookup.MAP_ZOOM_LEVEL_TEXT);
        this.enemyZoomLevelJLabel = new JLabel(StringLookup.ENEMY_ZOOM_LEVEL_TEXT);
        this.timeLimitJLabel = new JLabel(StringLookup.TIME_LIMIT_TEXT);
        this.marioModeJLabel = new JLabel(StringLookup.MARIO_MODE_TEXT);
        this.levelTypeJLabel = new JLabel(StringLookup.LEVEL_TYPE_TEXT);
        this.randomSeedJLabel = new JLabel(StringLookup.RANDOM_SEED_TEXT);
        this.fpsJLabel = new JLabel(StringLookup.CHANGE_FPS_TEXT);
        this.levelLengthJLabel = new JLabel(StringLookup.LEVEL_LENGTH_TEXT);
        this.levelHeightJLabel = new JLabel(StringLookup.LEVEL_HEIGHT_TEXT);
        this.levelDifficultyJLabel = new JLabel(StringLookup.LEVEL_DIFFICULTY_TEXT);

        //spinners
        //default, lower, upper, step
        this.timeLimitJSpinner = new JSpinner(new SpinnerNumberModel(200,1, Integer.MAX_VALUE,1));
        this.fpsJSpinner = new JSpinner(new SpinnerNumberModel(24,1,100,1));
        this.randomSeedJSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
        this.levelLengthJSpinner = new JSpinner(new SpinnerNumberModel(320,1,4096,1));
        this.levelDifficultlyJSpinner = new JSpinner(new SpinnerNumberModel(0,0,30,1));
        this.levelHeightJSpinner = new JSpinner(new SpinnerNumberModel(15,15,20,1));

        //combo Boxes
        this.enemyZoomLevelComboBox = new JComboBox(new String[]{StringLookup.LEAST_DETAIL_TEXT, StringLookup.MEDIUM_DETAIL_TEXT, StringLookup.MOST_DETAILED_TEXT});
        this.zoomLevelComboBox = new JComboBox(new String[]{StringLookup.LEAST_DETAIL_TEXT, StringLookup.MEDIUM_DETAIL_TEXT, StringLookup.MOST_DETAILED_TEXT});
        this.marioModeComboBox = new JComboBox(new String[]{StringLookup.MARIO_MODE_SMALL_TEXT, StringLookup.MARIO_MODE_BIG_TEXT, StringLookup.MARIO_MODE_FIRE_TEXT});
        this.levelTypeComboBox = new JComboBox(new String[]{StringLookup.LEVEL_TYPE_OVERGROUND_TEXT, StringLookup.LEVEL_TYPE_UNDERGROUND_TEXT,StringLookup.LEVEL_TYPE_CASTLE_TEXT});

        //text field
        this.matLabOutputField = new JTextField();
        this.matLabOutputField.setEditable(false);
        this.matLabOutputField.setEnabled(false);

        //buttons
        this.runButton = new JButton("Run");
        this.matlabOutputFileButton = new JButton(StringLookup.MATLAB_OUTPUT_TEXT);

        //list
        this.selectAgentList = new JList();

        this.agentPanel = new JPanel();
        this.matLabPanel = new JPanel();
        this.enemyPanel = new JPanel();
        this.visualizationPanel = new JPanel();
        this.levelPanel = new JPanel();
        this.timePanel = new JPanel();
        this.agentPanel.setBorder(BorderFactory.createTitledBorder("Select Agent"));
        this.matLabPanel.setBorder(BorderFactory.createTitledBorder("Output file"));
        this.enemyPanel.setBorder(BorderFactory.createTitledBorder("Enemy Options"));
        this.visualizationPanel.setBorder(BorderFactory.createTitledBorder("Visualisation Options"));
        this.levelPanel.setBorder(BorderFactory.createTitledBorder("Level Options"));
        this.timePanel.setBorder(BorderFactory.createTitledBorder("Time Options"));
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MarioGUI");
        frame.setContentPane(new MarioGUI().guiPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

