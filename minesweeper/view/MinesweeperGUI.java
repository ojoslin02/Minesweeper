package minesweeper.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import minesweeper.model.Artifact;
import minesweeper.model.GameState;
import minesweeper.model.Location;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperException;

public class MinesweeperGUI extends Application{

    private final Button[][] buttons = new Button[this.rows][this.cols]; 
    private final String STATUS = "Status: ";
    private final int rows = 20;
    private final int cols = 20;
    private final int maxCellsSize = 600;
    private final int mineCount = 50;
    private Minesweeper minesweeper;
    private Label statusLabel;
    private Label minesVal;
    private Label movesVal;

    @Override
    public void start(Stage stage) throws Exception {
        minesweeper = new Minesweeper(rows, cols, mineCount);
        BorderPane pane = new BorderPane();

        // center pane
        GridPane center = new GridPane();
        for(int row = 0; row < this.rows; row++){
            for(int col = 0; col < this.cols; col++){
                Button button = makeCellButton(row, col);
                center.add(button, row+1, col+1);
            }
        }
        pane.setCenter(center);

        // left pane
        GridPane left = new GridPane();
        minesVal = makeLabel(String.valueOf(minesweeper.getMineCount()), 18, Color.BLACK, Color.TEAL, 30);
        Label score = makeLabel("Mines", 18, Color.BLACK, Color.TEAL, 80);
        movesVal = makeLabel(String.valueOf(minesweeper.getMoveCount()), 18, Color.BLACK, Color.TEAL, 30);
        Label moves = makeLabel("Moves", 18, Color.BLACK, Color.TEAL, 80);
        Button hint = makeButton("Hint");
        Button solve = makeButton("Solve");
        Button reset = makeButton("Reset");
        Button quit = makeButton("Quit");
        hint.setOnAction(e -> {try {getHint();} catch (MinesweeperException me) {statusLabel.setText(STATUS + me.getMessage());}});
        solve.setOnAction(e -> solve());
        reset.setOnAction(e -> {try {start(stage);} catch (MinesweeperException me) {} catch (Exception e1) {}});
        quit.setOnAction(e -> stage.close());

        left.add(score, 0, 1);
        left.add(minesVal, 1, 1);
        left.add(moves, 0, 2);
        left.add(movesVal, 1, 2);
        left.add(hint, 0, 3, 2, 1);
        left.add(solve, 0, 4, 2, 1);
        left.add(reset, 0, 5, 2, 1);
        left.add(quit, 0, 6, 2, 1);
        pane.setLeft(left);

        // bottom pane
        GridPane bottom = new GridPane();
        statusLabel = makeLabel(STATUS + "None!", 14, Color.BLACK, Color.LIGHTYELLOW, 710);
        statusLabel.setAlignment(Pos.CENTER_LEFT);
        statusLabel.setPadding(new Insets(5));
        bottom.add(statusLabel, 1, 0);
        pane.setBottom(bottom);

        // initialization
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        stage.show();
    }

    private Button makeCellButton(int col, int row){
        Button button = new Button();
        int cellSize = maxCellsSize/rows;
        button.setMinSize(cellSize, cellSize);
        button.setPadding(Insets.EMPTY);
        buttons[row][col] = button;

        Location location = minesweeper.getBoard()[row][col];
        updateCell(location);

        MinesweeperChanger changer = new MinesweeperChanger(this);
        minesweeper.register(changer);

        MinesweeperMoveMaker moveMaker = new MinesweeperMoveMaker(this, location);
        button.setOnAction(moveMaker);

        return button;
    }

    public Label makeLabel(String text, int font_size, Color textColor, Color backgroundColor, int width){
        Label label = new Label(text);
        label.setTextFill(textColor);
        label.setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(0), Insets.EMPTY)));
        label.setFont(new Font("Calibri", font_size));
        label.setMaxSize(width, 40);
        label.setMinSize(width, 40);
        return label;
    }

    // makes a regular button for restart and quit
    private static Button makeButton(String text){
        Button button = new Button(text);
        button.setMinSize(110, 40);
        button.setPadding(Insets.EMPTY);
        return button;
    }

    private void getHint() throws MinesweeperException{
        Location hint = minesweeper.getHint();
        statusLabel.setText(STATUS + "Give " + hint + " a try.");
        this.buttons[hint.getRow()][hint.getCol()].setStyle("-fx-background-color: #94ffb0; -fx-border-color: d4d4d4; "); 
    }

    public void updateCell(Location location){
        if(!location.isCovered()){
            if(location.getPiece() == Artifact.MINE){
                buttons[location.getRow()][location.getCol()].setStyle("-fx-background-image: url('media/images/mine24.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-border-color: #d4d4d4; ");
            }else if(location.getPiece() == Artifact.BLANK){
                buttons[location.getRow()][location.getCol()].setStyle("-fx-background-color: #808080; -fx-border-color: #d4d4d4; ");
            }else{
                buttons[location.getRow()][location.getCol()].setStyle("-fx-background-color: #808080; -fx-border-color: #d4d4d4; ");
                String string = Character.toString(location.getPiece().getSymbol());
                buttons[location.getRow()][location.getCol()].setText(string);
                buttons[location.getRow()][location.getCol()].setFont(Font.font("Arial", FontWeight.BOLD, 20));
                switch(string){
                    case "1":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.BLUE);
                        break;
                    case "2":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.GREEN);
                        break;
                    case "3":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.RED);
                        break;
                    case "4":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.DARKBLUE);
                        break;
                    case "5":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.DARKGREEN);
                        break;
                    case "6":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.DARKRED);
                        break;
                    case "7":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.PURPLE);
                        break;
                    case "8":
                        buttons[location.getRow()][location.getCol()].setTextFill(Color.ORANGE);
                        break;
                }
            } 
        }
    }

    public void makeMove(Location location){
        try{
            minesweeper.makeSelection(location.getRow(), location.getCol());
            movesVal.setText(String.valueOf(minesweeper.getMoveCount()));
            if(minesweeper.getGameState() == GameState.LOST){
                statusLabel.setText(STATUS + "BOOM! Better luck next time!");
                statusLabel.setBackground(new Background(new BackgroundFill(Color.rgb(255, 138, 138, 1.0), new CornerRadii(0), Insets.EMPTY)));
            }else if(minesweeper.getGameState() == GameState.WON){
                statusLabel.setText(STATUS + "Congratulations! You won!");
                statusLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(0), Insets.EMPTY)));
            }
        }catch(MinesweeperException me){
            statusLabel.setText(STATUS + me.getMessage());
        }
    }

    private void solve(){
        while(this.minesweeper.getGameState() == GameState.IN_PROGRESS){
            try {
                Location location = this.minesweeper.getHint();
                this.statusLabel.setText("Selection: "  + location);
                makeMove(location);
            } catch (MinesweeperException e) {
                // squash
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
