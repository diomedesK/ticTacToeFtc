import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class ticTacToe{
    JFrame MainWindow;
    JPanel GridContainer;

    JPanel OptionsPane;
    FormVisualizer formView;

    int CurrentForm;
    int PlayedTimes;

    static final int NULL_FORM = 0;
    static final int X_FORM = 1;
    static final int O_FORM = 2;

    TicTacToeCell[][] CellHolder = new TicTacToeCell[3][3];

    public void setBaseGUI(){
        MainWindow = new JFrame();
        MainWindow.setTitle("Tic Tac Toe");
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setSize( new Dimension(500, 500));
        MainWindow.setLocationRelativeTo(null);
        // MainWindow.setResizable(false);

        GridContainer = new JPanel( new GridLayout(3, 3));
    }

    public void setOptionPane(){
        OptionsPane = new JPanel();

        OptionsPane.setLayout(new BoxLayout(OptionsPane, BoxLayout.Y_AXIS));

        ArrayList<JButton> OptionButtons = new ArrayList<>();

        JButton ClearButton = new JButton("Restart");
        OptionButtons.add(ClearButton);

		ClearButton.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
                for(int i=0; i<CellHolder.length; i++){
                    TicTacToeCell[] column = CellHolder[i];

                    for(int j=0; j<column.length; j++){
                        TicTacToeCell c = CellHolder[i][j];
                        c.valueOf = 0;
                    }
                }

                System.out.println("Restarting game");
                CurrentForm = ticTacToe.X_FORM;
                PlayedTimes = 0;
                MainWindow.repaint();

			}// end ActionEvent
        });

        OptionsPane.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for(JButton bt : OptionButtons){
            bt.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            bt.setContentAreaFilled(false);

            OptionsPane.add(bt);
            OptionsPane.add(Box.createVerticalStrut(5));
        }

        MainWindow.add(OptionsPane, BorderLayout.EAST);
    }


    public void setCells(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                TicTacToeCell cell = new TicTacToeCell();

                CellHolder[i][j] = cell;
                GridContainer.add(cell);

                cell.addActionListener( new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        if(cell.valueOf == ticTacToe.NULL_FORM){
                            cell.valueOf = CurrentForm;
                            updateForm();

                            PlayedTimes += 1;
                        }

                        if(PlayedTimes == (3*3) ){
                            CurrentForm = ticTacToe.NULL_FORM;
                        }

                        MainWindow.repaint();

                    }// end ActionEvent
                }
                ); // end ActionListener
            }
        };

        MainWindow.add(GridContainer, BorderLayout.CENTER );
    }

    public void setFormView(){
        JLabel formViewText = new JLabel("Next:");
        formViewText.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        formView = new FormVisualizer(50);
        OptionsPane.add(formViewText);
        OptionsPane.add(Box.createVerticalStrut(5));
        OptionsPane.add(formView);
    }

    public void updateForm(){
        if(CurrentForm == ticTacToe.X_FORM){
            System.out.println("Setting CurrentForm from X to O");
            CurrentForm = ticTacToe.O_FORM;
	}

        else if(CurrentForm == ticTacToe.O_FORM){
            System.out.println("Setting CurrentForm from O to X");
            CurrentForm = ticTacToe.X_FORM;
        }
    }


    public void run(){
        setBaseGUI();
        setCells();
        setOptionPane();

        MainWindow.setVisible(true); 

        setFormView(); // this necessarily has to come after 

        CurrentForm = ticTacToe.X_FORM;

        MainWindow.repaint();
    }

    public static void main(String[] args){
        new ticTacToe().run();
    }

    class TicTacToeCell extends JButton{
        //0 = null; 1 = X; 2 = O;
        int valueOf;

        TicTacToeCell(){
            this.setBorder( BorderFactory.createLineBorder(Color.gray));
            // this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
        }

        public void paintComponent(Graphics g){
            super.paintComponent( g);

            Graphics2D gg = (Graphics2D) g;

            //Deixa o traço da pintura menos pixelado
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 

            if(valueOf == ticTacToe.X_FORM){
                //Draws X
                Graphics2DFormPainter.drawX(gg, this.getWidth(), this.getHeight());

            }else if(valueOf == ticTacToe.O_FORM){
                //Draws O
                Graphics2DFormPainter.drawCircle(gg, this.getWidth(), this.getHeight());
            }


        }
    }

    class FormVisualizer extends JPanel{
        int Side; 

        FormVisualizer(int side){
            this.Side = side;
            this.setMinimumSize( new Dimension(side, side) );
            this.setMaximumSize( new Dimension(side, side) );
        }

        public void paintComponent(Graphics g){
            System.out.println("PaintComponent Called in FormVisualizer"); 
            super.paintComponent(g);

            Graphics2D gg = (Graphics2D) g;
            if(CurrentForm == ticTacToe.NULL_FORM){
                System.out.println("Drawing NULL in view"); 
            }
            else if(CurrentForm == ticTacToe.X_FORM){
                System.out.println("Drawing X in view"); 
                Graphics2DFormPainter.drawX(gg, this.getWidth(), this.getWidth());
            }else if(CurrentForm == ticTacToe.O_FORM){
                System.out.println("Drawing O in view"); 
                Graphics2DFormPainter.drawCircle(gg, this.getWidth(), this.getWidth());
            }

            // g.setColor(Color.black);
            // g.drawRect(0, 0, 20, 20);

        }

    }// end class


    class Graphics2DFormPainter{
        public static Graphics2D drawX(Graphics2D gg, int width, int height){
            gg.setColor(Color.BLUE);
            gg.setStroke(new BasicStroke(10.0f));

            int marginX = (int) (width * 0.165);
            int marginY = (int) (height * 0.165);

            //Desenha duas linhas
            gg.drawLine(0 + marginX, 0 + marginY, width - marginX, height - marginY);
            gg.drawLine(0 + marginX, height - marginY, width - marginX, 0 + marginY );

            return gg;
        }

        public static Graphics2D drawCircle(Graphics2D gg, int width, int height){
            int s = (int) (height * 0.66);
            gg.setColor(Color.PINK);
            gg.setStroke(new BasicStroke(10.0f));
            gg.drawOval(width/2 - s/2, height/2 - s/2, s, s);

            return gg;
        }

    }// end class

}
