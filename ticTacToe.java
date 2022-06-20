import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;


public class ticTacToe{
    JFrame MainWindow;
    JPanel GameGrid;

    JPanel OptionsPane;
    FormVisualizer formView;

    int CurrentForm;
    int PlayedTimes;

    GameStatusWatcher gsw = new GameStatusWatcher();

    static final int NULL_FORM = -1;
    static final int X_FORM = 1;
    static final int O_FORM = 0;

    TicTacToeCell[][] CellHolder = new TicTacToeCell[3][3];

    public void setBaseGUI(){
        MainWindow = new JFrame();
        MainWindow.setTitle("Tic Tac Toe");
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setSize( new Dimension(500, 500));
        MainWindow.setLocationRelativeTo(null);
        MainWindow.setResizable(false);

        GameGrid = new JPanel( new GridLayout(3, 3));
    }

    public void setOptionPane(){
        OptionsPane = new JPanel();

        OptionsPane.setLayout(new BoxLayout(OptionsPane, BoxLayout.Y_AXIS));

        ArrayList<JButton> OptionButtons = new ArrayList<>();

        PrettyJButton ClearButton = new PrettyJButton("Restart");
        //System.out.println(ClearButton.getFont());
        ClearButton.setFont(new Font("SansSerif", Font.PLAIN, 20));

        OptionButtons.add(ClearButton);

		ClearButton.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
                for(int i=0; i<CellHolder.length; i++){
                    TicTacToeCell[] column = CellHolder[i];

                    for(int j=0; j<column.length; j++){
                        TicTacToeCell c = CellHolder[i][j];
                        c.valueOf = ticTacToe.NULL_FORM;
                    }
                }

                System.out.println("Restarting game");

                CurrentForm = ticTacToe.X_FORM;
                PlayedTimes = 0;
                MainWindow.repaint();
                
                //enables back clicks in game grid
                Component[] grid_components = GameGrid.getComponents();
                for(int c = 0; c < grid_components.length; c++){
                    Component comp = grid_components[c];
                    comp.setEnabled(true);
                }

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
                cell.valueOf = ticTacToe.NULL_FORM;

                CellHolder[i][j] = cell;
                GameGrid.add(cell);

                cell.addActionListener( new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        if(cell.valueOf == ticTacToe.NULL_FORM){
                            cell.valueOf = CurrentForm;

                            updateForm();
                            GameStatusResponse gr = gsw.checkForWinner(CellHolder);

                            if(gr.hasWinner){
                                System.out.println(gr.message);
                                
                                //disables clicks in game grid
                                Component[] grid_components = GameGrid.getComponents();
                                for(int c = 0; c < grid_components.length; c++){
                                    Component comp = grid_components[c];
                                    comp.setEnabled(false);
                                }

                                // System.out.println(Arrays.toString(gr.winningPositionStart));
                                // System.out.println(Arrays.toString(gr.winningPositionEnd));
                            }


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

        MainWindow.add(GameGrid, BorderLayout.CENTER );
    }

    public void setFormView(){
        PrettyJLabel formViewText = new PrettyJLabel("Next:");
        formViewText.setFont( new Font(Font.DIALOG, Font.PLAIN, 20));
        formViewText.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        formView = new FormVisualizer(50);
        OptionsPane.add(formViewText);
        OptionsPane.add(Box.createVerticalStrut(5));
        OptionsPane.add(formView);
    }

  
    public void updateForm(){
        if(CurrentForm == ticTacToe.X_FORM){
            //System.out.println("Setting CurrentForm from X to O");
            CurrentForm = ticTacToe.O_FORM;
	}

        else if(CurrentForm == ticTacToe.O_FORM){
            //System.out.println("Setting CurrentForm from O to X");
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
        //100 = null; 1 = X; 2 = O;
        int valueOf;

        TicTacToeCell(){
            this.setBorder( BorderFactory.createLineBorder(Color.gray));
            // this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

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
            //System.out.println("PaintComponent Called in FormVisualizer"); 
            super.paintComponent(g);

            Graphics2D gg = (Graphics2D) g;
            if(CurrentForm == ticTacToe.NULL_FORM){
                //System.out.println("Drawing NULL in view"); 
            }
            else if(CurrentForm == ticTacToe.X_FORM){
                //System.out.println("Drawing X in view"); 
                Graphics2DFormPainter.drawX(gg, this.getWidth(), this.getWidth());
            }else if(CurrentForm == ticTacToe.O_FORM){
                //System.out.println("Drawing O in view"); 
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

    class GameStatusResponse{
        public boolean hasWinner;
        public int WinnerForm;
        public int[] winningPositionStart = new int[2];
        public int[] winningPositionEnd = new int[2];
        public String message;

    }

    class GameStatusWatcher{
        //flags 
        private final int TL_DIAGONAL   = 1;
        private final int TR_DIAGONAL   = 2;
        private final int HORIZONTAL    = 3;
        private final int VERTICAL      = 4;

        private final String X_WIN = "111";
        private final String O_WIN = "000";

        private Map<Integer, String> MethodMessages = Map.of
            (
             TL_DIAGONAL    , "FROM TOP LEFT TO BOTTOM RIGHT",
             TR_DIAGONAL    , "FROM TOP RIGHT TO BOTTOM LEFT",
             HORIZONTAL     , "FROM HORIZONTAL POSITION",
             VERTICAL       , "FROM VERTICAL POSITION"
            );

      

        public GameStatusResponse checkForWinner(TicTacToeCell[][] gameState){
            GameStatusResponse status = new GameStatusResponse(); 
            status.hasWinner = false;

            for(int x=0; x < gameState.length; x++){
                for(int y =0; y < gameState.length; y++){

                    ArrayList<GameStatusResponse> analyzes = new ArrayList<>();

                    analyzes.add(analyzePosition(gameState, TL_DIAGONAL, x, y));
                    analyzes.add(analyzePosition(gameState, TR_DIAGONAL, x, y));
                    analyzes.add(analyzePosition(gameState, HORIZONTAL, x, y));
                    analyzes.add(analyzePosition(gameState, VERTICAL, x, y));

                    for(int a = 0; a < analyzes.size(); a++){
                        GameStatusResponse analyze = analyzes.get(a);
                        if(analyze.hasWinner){
                            return analyze;
                        }
                    }

                }//inner
            }// outter
            
            return status;
        }

        public GameStatusResponse analyzePosition(TicTacToeCell[][] gameState, int method, int x, int y){
            GameStatusResponse status = new GameStatusResponse();
            status.hasWinner = false;

            StringBuilder cellstream = new StringBuilder();
            TicTacToeCell currentcell;

            int lx = 0;
            int ly = 0;

            for(int n = 0; n < 3; n++){

                if(method == TL_DIAGONAL){
                    lx = x + n;
                    ly = y + n;
                } else if(method == TR_DIAGONAL){
                    lx = x - n;
                    ly = y + n;
                } else if(method == HORIZONTAL){
                    lx = x + n;
                    ly = y + 0;
                } else if(method == VERTICAL){
                    lx = x + 0;
                    ly = y + n;
                }

                try{
                    currentcell = CellHolder[ly][lx];
                    cellstream.append(currentcell.valueOf);
                }catch(ArrayIndexOutOfBoundsException e){
                    break;
                }
            }

            if(cellstream.toString().equals(X_WIN)){
                status.hasWinner = true;
                status.WinnerForm = ticTacToe.X_FORM;
                status.message = "X FORM WINS " + MethodMessages.get(method);

            }else if(cellstream.toString().equals(O_WIN)){
                status.hasWinner = true;
                status.WinnerForm = ticTacToe.O_FORM;
                status.message = "O FORM WINS " + MethodMessages.get(method);
            }

            if(status.hasWinner){
                status.winningPositionStart[0] = x;
                status.winningPositionStart[1] = y;
                status.winningPositionEnd[0] = lx;
                status.winningPositionEnd[1] = ly;
            }
                                     
            return status;
        }

        //public void checkIfWin(){
        //    String X_WIN = "111";
        //    String O_WIN = "000";

        //    for(int x=0; x < CellHolder.length; x++){
        //        for(int y =0; y < CellHolder.length; y++){
        //            StringBuilder cellstream = new StringBuilder();
        //            TicTacToeCell currentcell;
        //            int lx; int ly;

        //            //diagonal from TOP LEFT to BOTTOM RIGHT
        //            for(int n = 0; n < 3; n++){
        //                lx = x + n;
        //                ly = y + n;

        //                try{
        //                    currentcell = CellHolder[ly][lx];
        //                    cellstream.append(currentcell.valueOf);
        //                }catch(ArrayIndexOutOfBoundsException e){
        //                    break;
        //                }
        //            }
        //            if(cellstream.toString().equals(X_WIN)){
        //                System.out.println("X_WIN FROM TOP LEFT TO BOTTOM RIGHT");
        //            }else if(cellstream.toString().equals(O_WIN)){
        //                System.out.println("O_WIN FROM TOP LEFT TO BOTTOM RIGHT");
        //            }
        //            cellstream.setLength(0); //reset

        //            //diagonal from TOP RIGHT to BOTTOM LEFT
        //            for(int n = 0; n < 3; n++){
        //                lx = x - n;
        //                ly = y + n;

        //                try{
        //                    currentcell = CellHolder[ly][lx];
        //                    cellstream.append(currentcell.valueOf);
        //                }catch(ArrayIndexOutOfBoundsException e){
        //                    break;
        //                }
        //            }
        //            if(cellstream.toString().equals(X_WIN)){
        //                System.out.println("X_WIN FROM TOP RIGHT TO BOTTOM LEFT");
        //            }else if(cellstream.toString().equals(O_WIN)){
        //                System.out.println("O_WIN FROM TOP RIGHT TO BOTTOM LEFT");
        //            }
        //            cellstream.setLength(0); //reset

        //            //diagonal from TOP to BOTTOM // Fix this shit nigga  
        //            for(int n = 0; n < 3; n++){
        //                lx = x;
        //                ly = y + n;

        //                try{
        //                    currentcell = CellHolder[ly][lx];
        //                    cellstream.append(currentcell.valueOf);
        //                }catch(ArrayIndexOutOfBoundsException e){
        //                    break;
        //                }
        //            }
        //            if(cellstream.toString().equals(X_WIN)){
        //                System.out.println("X_WIN FROM VERTICAL POSITION");
        //            }else if(cellstream.toString().equals(O_WIN)){
        //                System.out.println("O_WIN FROM VERTICAL POSITION");
        //            }
        //            cellstream.setLength(0); //reset


        //            //diagonal from TOP to BOTTOM // Fix this shit nigga  
        //            for(int n = 0; n < 3; n++){
        //                lx = x + n;
        //                ly = y;

        //                try{
        //                    currentcell = CellHolder[ly][lx];
        //                    cellstream.append(currentcell.valueOf);
        //                }catch(ArrayIndexOutOfBoundsException e){
        //                    break;
        //                }
        //            }
        //            if(cellstream.toString().equals(X_WIN)){
        //                System.out.println("X_WIN FROM HORIZONTAL POSITIOn");
        //            }else if(cellstream.toString().equals(O_WIN)){
        //                System.out.println("O_WIN FROM HORIZONTAL POSITIOn");
        //            }
        //            cellstream.setLength(0); //reset



        //        }//inner
        //    }// outter

        //}

    }


}


class PrettyJLabel extends JLabel{
    PrettyJLabel(){
        super();
    }

    PrettyJLabel(String str_arg){
        super(str_arg);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    }
}

class PrettyJButton extends JButton{
    PrettyJButton(){
        super();
    }

    PrettyJButton(String str_arg){
        super(str_arg);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    }
}
