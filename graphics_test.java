import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.imageio.ImageIO;

public class graphics_test {
   private static final String INTRO = "intro";
   private static final String GAME = "game";
   private static final String HELP = "help";
   private CardLayout cardlayout = new CardLayout();
   private JPanel mainPanel = new JPanel(cardlayout);
   private IntroPanel introPanel = new IntroPanel();
   private GamePanel gamePanel = new GamePanel();
   private HelpPanel helpPanel = new HelpPanel();

   public graphics_test() {
      mainPanel.add(introPanel.getMainComponent(), INTRO);
      mainPanel.add(helpPanel.getMainComponent(), HELP);
      //mainPanel.add(gamePanel.getMainComponent(), GAME);

      introPanel.addBazBtnActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            //cardlayout.show(mainPanel, GAME);
            JFrame game_window = new JFrame("Game Window");
            game_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game_window.getContentPane().add(new GamePanel().getMainComponent());
            game_window.pack();
            game_window.setResizable(false);
            game_window.setLocationRelativeTo(null);
            game_window.setVisible(true);
            Window win = SwingUtilities.getWindowAncestor(mainPanel);
            win.dispose();
         }
      });

      introPanel.addHelpBtnActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            cardlayout.show(mainPanel, HELP);
         }
      });

      helpPanel.addStartBtnActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            //cardlayout.show(mainPanel, GAME);
            JFrame game_window = new JFrame("Game Window");
            game_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game_window.getContentPane().add(new GamePanel().getMainComponent());
            game_window.pack();
            game_window.setResizable(false);
            game_window.setLocationRelativeTo(null);
            game_window.setVisible(true);
            Window win = SwingUtilities.getWindowAncestor(mainPanel);
            win.dispose();
         }
      });

      helpPanel.addBackBtnActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            cardlayout.show(mainPanel, INTRO);
         }
      });
   }

   private JComponent getMainComponent() {
      return mainPanel;
   }

   private static void createAndShowUI() {
      JFrame frame = new JFrame("Panic at the Pizzarizzia");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      ImageIcon background = new ImageIcon("image/Placeholder_Menu.png");

      frame.getContentPane().add(new graphics_test().getMainComponent());
      frame.pack();
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);

      frame.setVisible(true);
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            createAndShowUI();
         }
      });
   }
}

class IntroPanel {
   private JPanel mainPanel = new JPanel();//new GridBagLayout());
   private JButton start;
   private JButton help;
   private JButton exit;

   public IntroPanel() {
      //GridBagConstraints c = new GridBagConstraints();
      ImageIcon breadStick = new ImageIcon("image/Breadstick Button.png");

      Image img = breadStick.getImage();
      Image newimg = img.getScaledInstance( 300, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
      breadStick = new ImageIcon( newimg );

      start = new JButton("Start", breadStick);
      //start.setPreferredSize(new Dimension(200,100));
      start.setHorizontalTextPosition(JButton.CENTER);
      start.setVerticalTextPosition(JButton.CENTER);
      start.setOpaque(false);
      start.setContentAreaFilled(false);
      start.setMargin(new Insets(0, 0, 0, 0));
      start.setBorderPainted(false);
      help = new JButton("Help", breadStick);
      help.setHorizontalTextPosition(JButton.CENTER);
      help.setVerticalTextPosition(JButton.CENTER);
      help.setOpaque(false);
      help.setContentAreaFilled(false);
      help.setMargin(new Insets(0, 0, 0, 0));
      help.setBorderPainted(false);
      exit = new JButton("Exit", breadStick);
      exit.setHorizontalTextPosition(JButton.CENTER);
      exit.setVerticalTextPosition(JButton.CENTER);
      exit.setOpaque(false);
      exit.setContentAreaFilled(false);
      exit.setMargin(new Insets(0, 0, 0, 0));
      exit.setBorderPainted(false);

      ImageIcon image = new ImageIcon("image/Title.png"); //add title logo
      JLabel label = new JLabel("", image, JLabel.CENTER);
      //JPanel panel = new JPanel(new BorderLayout());
      //mainPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
      /*c.fill = GridBagConstraints.BOTH;
      c.gridx = 1;
      c.gridy = 0;
      c.weightx = 0.0;
      mainPanel.add(label, c);

      GridBagConstraints ctest = new GridBagConstraints();

      JButton test = new JButton("test");
      JButton test2 = new JButton("test2");

      ctest.fill = GridBagConstraints.HORIZONTAL;
      ctest.weightx = 0.0;
      ctest.gridwidth = 1;
      ctest.gridx = 0;
      ctest.gridy = 1;
      mainPanel.add(test, ctest);

      ctest.gridx = 2;
      ctest.gridy = 0;
      mainPanel.add(test2, ctest);

      GridBagConstraints c1 = new GridBagConstraints();

      c1.fill = GridBagConstraints.HORIZONTAL;
      c1.weightx = 0.0;
      c1.gridwidth = 2;
      c1.gridx = 1;
      c1.gridy = 2;
      mainPanel.add(start, c1);

      c1.gridy = 3;
      mainPanel.add(help, c1);

      GridBagConstraints c2 = new GridBagConstraints();

      c2.fill = GridBagConstraints.HORIZONTAL;
      c2.weightx = 0.0;
      c2.gridwidth = 2;
      c2.anchor = GridBagConstraints.PAGE_END;
      c2.gridx = 1;
      c2.gridy = 4;
      mainPanel.add(exit, c2);*/

      Box box = Box.createHorizontalBox();    
      //box.setAlignmentX(Component.RIGHT_ALIGNMENT);
      // box.add( label, BorderLayout.CENTER );
      box.add(Box.createGlue());
      // box.add(start);



      Box box2 = Box.createVerticalBox();
      box2.add(start);
      // box2.add(Box.createVerticalStrut(5));
      box2.add(help);
      // box2.add(Box.createVerticalStrut(5));
      box2.add(exit);
      box.add(box2);

      Box labelBox = Box.createHorizontalBox();
      labelBox.add(label);

      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      mainPanel.add(Box.createGlue());
      mainPanel.add(labelBox);
      mainPanel.add(Box.createGlue());
      mainPanel.add(box);
      mainPanel.add(Box.createGlue());

      exit.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Window win = SwingUtilities.getWindowAncestor(mainPanel);
            win.dispose();
         }
      });
   }

   public void addBazBtnActionListener(ActionListener listener) {
      start.addActionListener(listener);
   }

   public void addHelpBtnActionListener(ActionListener listener) {
      help.addActionListener(listener);
   }

   public JComponent getMainComponent() {
      return mainPanel;
   }

}

class HelpPanel {
   private static final Dimension MAIN_SIZE = new Dimension(400, 400);
   private JPanel mainPanel = new JPanel();

   private JButton start;
   private JButton back;

   public HelpPanel() {
      ImageIcon breadStick = new ImageIcon("image/Breadstick Button.png");
      Image img = breadStick.getImage();
      Image newimg = img.getScaledInstance( 200, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
      breadStick = new ImageIcon( newimg );
      ImageIcon breadStick_flipped = new ImageIcon("image/Breadstick Button Flipped.png");
      Image img2 = breadStick_flipped.getImage();
      Image newimg2 = img2.getScaledInstance( 200, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
      breadStick_flipped = new ImageIcon( newimg2 );

      start = new JButton("Start", breadStick);
      //start.setPreferredSize(new Dimension(200,100));
      start.setHorizontalTextPosition(JButton.CENTER);
      start.setVerticalTextPosition(JButton.CENTER);
      start.setOpaque(false);
      start.setContentAreaFilled(false);
      start.setMargin(new Insets(0, 0, 0, 0));
      start.setBorderPainted(false);
      
      back = new JButton("Back", breadStick_flipped);
      back.setHorizontalTextPosition(JButton.CENTER);
      back.setVerticalTextPosition(JButton.CENTER);
      back.setOpaque(false);
      back.setContentAreaFilled(false);
      back.setMargin(new Insets(0, 0, 0, 0));
      back.setBorderPainted(false);

      ImageIcon image = new ImageIcon("image/Placeholder_Menu.png"); //add title logo
      Image img1 = image.getImage();
      Image newimg1 = img1.getScaledInstance( 300, 300,  java.awt.Image.SCALE_SMOOTH ) ;  
      image = new ImageIcon( newimg1 );

      JLabel label = new JLabel("", image, JLabel.CENTER);   

      Box box = Box.createVerticalBox();
      Box box2 = Box.createHorizontalBox();
      Box labelBox = Box.createHorizontalBox();
      box2.add(back);
      box2.add(Box.createGlue());
      box2.add(start);
      labelBox.add(label);
      box.add(Box.createGlue());
      box.add(labelBox);
      box.add(Box.createGlue());
      box.add(box2);

      mainPanel.add(box);
      mainPanel.setPreferredSize(MAIN_SIZE);
   }

   public JComponent getMainComponent() {
      return mainPanel;
   }

   public void addStartBtnActionListener(ActionListener listener) {
      start.addActionListener(listener);
   }

   public void addBackBtnActionListener(ActionListener listener) {
      back.addActionListener(listener);
   }

}

class GamePanel {
   private static final Dimension GAME_SIZE = new Dimension(1280, 720);
   private JPanel mainPanel = new JPanel();

   

   public GamePanel() {
      ImageIcon image = new ImageIcon("image/Stations.png"); //add title logo
      Image img1 = image.getImage();
      Image newimg1 = img1.getScaledInstance( 650, 585,  java.awt.Image.SCALE_SMOOTH ) ;  
      image = new ImageIcon( newimg1 );

      JLabel label = new JLabel("", image, JLabel.CENTER);

      Box gameBox = Box.createHorizontalBox();
      Box boxLeft = Box.createVerticalBox();
      Box boxMid = Box.createVerticalBox();
      Box boxRight = Box.createVerticalBox();

      //left side
      Box playerBox1 = Box.createVerticalBox(); 
      Box labelBox1 = Box.createHorizontalBox(); //for player name and icon
      Box actionBox1 = Box.createHorizontalBox(); //for four player actions
      
      // actionBox1.add(action1); //dont have action graphics
      // actionBox1.add(action2);
      // actionBox1.add(action3);
      // actionBox1.add(action4);

      labelBox1.add(new JLabel("Player 1"));
      // labelBox1.add(playerhead); //dont have player head graphic

      playerBox1.add(labelBox1);
      playerBox1.add(actionBox1);

      boxLeft.add(playerBox1);

      Box playerBox3 = Box.createVerticalBox();
      Box labelBox3 = Box.createHorizontalBox();
      Box actionBox3 = Box.createHorizontalBox();

      // actionBox3.add(action1);
      // actionBox3.add(action2);
      // actionBox3.add(action3);
      // actionBox3.add(action4);

      labelBox3.add(new JLabel("Player 3"));
      // labelBox3.add(playerhead);

      playerBox3.add(labelBox3);
      playerBox3.add(actionBox3);

      boxLeft.add(playerBox3);

      gameBox.add(boxLeft);

      //middle
      Box queueBox = Box.createHorizontalBox();

      boxMid.add(label);
      boxMid.add(queueBox);

      gameBox.add(boxMid);



      //right
      Box playerBox2 = Box.createVerticalBox();
      Box labelBox2 = Box.createHorizontalBox();
      Box actionBox2 = Box.createHorizontalBox();
      
      boxRight.add(playerBox2);
      
      Box playerBox4 = Box.createVerticalBox();
      Box labelBox4 = Box.createHorizontalBox();
      Box actionBox4 = Box.createHorizontalBox();

      
      boxRight.add(playerBox4);

      gameBox.add(boxRight);

      mainPanel.add(gameBox);
      mainPanel.setPreferredSize(GAME_SIZE);
   }

   public JComponent getMainComponent() {
      return mainPanel;
   }

   //public void addBackBtnActionListener(ActionListener listener) {
   //   back.addActionListener(listener);
   //}

}