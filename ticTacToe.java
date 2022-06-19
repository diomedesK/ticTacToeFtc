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
                        }

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
        // System.out.println("86. OptionsPane.getWidth = " + OptionsPane.getWidth());
        // formView = new FormVisualizer(OptionsPane.getWidth());
        formView = new FormVisualizer(50);
        OptionsPane.add(formViewText);
        OptionsPane.add(formView);
    }

    public void updateForm(){
        System.out.println("Updating form");
        if(CurrentForm == ticTacToe.X_FORM){
            System.out.println("Setting CurrentForm from X to O");
            CurrentForm = ticTacToe.O_FORM;
            // formView.paintO();
	}

        else if(CurrentForm == ticTacToe.O_FORM){
            System.out.println("Setting CurrentForm from O to X");
            CurrentForm = ticTacToe.X_FORM;
            // formView.paintX();
        }
    }


    public void run(){
        setBaseGUI();
        MainWindow.setVisible(true); 

        setCells();
        setOptionPane();

        setFormView(); // this necessarily has to come after 


        CurrentForm = ticTacToe.X_FORM;
        updateForm();
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
                gg.setColor(Color.BLUE);
                gg.setStroke(new BasicStroke(10.0f));


                int marginX = (int) (this.getWidth() * 0.165);
                int marginY = (int) (this.getHeight() * 0.165);

                gg.drawLine(0 + marginX, 0 + marginX, this.getWidth() - marginX, this.getHeight() - marginX);
                gg.drawLine(0 + marginX, this.getHeight() - marginY, this.getWidth() - marginX, 0 + marginY );

            }else if(valueOf == ticTacToe.O_FORM){
                //Draws O
                int s = (int) (this.getHeight() * 0.66);
                gg.setColor(Color.PINK);
                gg.setStroke(new BasicStroke(10.0f));
                gg.drawOval(this.getWidth()/2 - s/2, this.getHeight()/2 - s/2, s, s);
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
            Graphics2D gg = this.getGraphics2D();;

	    gg.setColor(Color.red);
	    gg.fillRect(0, 0, 20, 20);


        }

        private Graphics2D getGraphics2D(){
            Graphics2D g2D = (Graphics2D) this.getGraphics();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
            g2D.clearRect(0, 0, this.getWidth(), this.getHeight());

            // System.out.println(this.getWidth());
            // System.out.println(this.getHeight());
            return g2D;
        }

        public void paintX(){
            Graphics2D gg = this.getGraphics2D();;

            gg.setColor(Color.BLACK);
            gg.setStroke(new BasicStroke(10.0f));

            int marginX = (int) (this.getWidth() * 0.165);
            int marginY = (int) (this.getHeight() * 0.165);

            gg.drawLine(0 + marginX, 0 + marginX, this.getWidth() - marginX, this.getHeight() - marginX);
            gg.drawLine(0 + marginX, this.getHeight() - marginY, this.getWidth() - marginX, 0 + marginY );
        }

        public void paintO(){
            Graphics2D gg = this.getGraphics2D();;

            int s = (int) (this.getHeight() * 0.66);
            gg.setColor(Color.BLACK);
            gg.setStroke(new BasicStroke(10.0f));
            gg.drawOval(this.getWidth()/2 - s/2, this.getHeight()/2 - s/2, s, s);
        }

    }
}




