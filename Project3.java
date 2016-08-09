import java.awt.*;
import java.util.*;

public class Project3{

   public static final int PANEL_WIDTH = 500;
   public static final int PANEL_HEIGHT = 400;
   public static final int SLEEP_TIME = 50;
   public static final int BALL_SIZE = 10;
   public static final int INITIAL_BALL_VELOCITY_X = 4;
   public static final int INITIAL_BALL_VELOCITY_Y = 0;
   public static final int PADDLE_LENGTH = 50;
   public static final int COMPPADDLE_LENGTH = 50;
   public static final int PADDLE_X = PANEL_WIDTH - 40;
   public static final int COMPPADDLE_X = 40;
   public static final Color BALL_COLOR = Color.RED;
   public static final Color BACKGROUND_COLOR = Color.WHITE;
   public static final Color PADDLE_COLOR = Color.BLACK;
   public static final int UP_ARROW = 38;
   public static final int DOWN_ARROW = 40;
   public static final int KEY_SPACE = ' ';
   public static final int KEY_NEW_GAME = 'N';
   public static final int PADDLE_MOVE_AMOUNT = 5;
   public static final int MAX_COMPUTER_PADDLE_MOVE = 2;
   public static final int SCORE_FONT_SIZE = 30;
   public static final int MAX_SCORE=5;
   public static final Color SCORES_COLOR=Color.BLUE;
   public static final int KEY_SCORE = 'S';
   public static final int BOX_WIDTH=180;
   public static final int MIN_X_VELOCITY = 3;
   public static final int MAX_X_VELOCITY = 6;
   public static final int MIN_Y_VELOCITY = -5;
   public static final int MAX_Y_VELOCITY = 5;
   public static int[] compScoreArray=new int[10];
   public static int[] userScoreArray=new int[10];
   public static int compGamesWon, userGamesWon;
   public static int gamesPlayed;
   public static int boxX;
   public static int boxY;
   public static int boxHeight;
   public static int ballX;
   public static int ballY;
   public static int ballVelocityX;
   public static int ballVelocityY;
   public static int paddleY, compPaddleY, userScore, compScore;
   public static Font normalFont;
   public static Font scoresFont;
   public static boolean gameOver=false;
   public static boolean scoresDisplayed=false;
   public static Random rand = new Random();

   public static void main(String[] args) {
      DrawingPanel panel = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
      Graphics g = panel.getGraphics( );
      g.drawString("Project 3 written by Kyle Widmann",10,15);
      ballX = PANEL_WIDTH/2;
      ballY = PANEL_HEIGHT/2;
      ballVelocityX = 0;
      ballVelocityY = 0;
      paddleY = PANEL_HEIGHT/2 - PADDLE_LENGTH/2;
      compPaddleY = PANEL_HEIGHT/2 - PADDLE_LENGTH/2;
      userScore=0;
      compScore=0;
      gamesPlayed=0;
      boxX = (PANEL_WIDTH - BOX_WIDTH)/2;
      boxHeight= 20+(gamesPlayed)*15;
      boxY=(PANEL_HEIGHT - boxHeight)/2;
      compGamesWon=0;
      userGamesWon=0;
      normalFont=g.getFont();
      scoresFont= new Font(normalFont.getName(), normalFont.getStyle(), SCORE_FONT_SIZE);
      drawBall(g,BALL_COLOR);
      drawPaddle(g,PADDLE_COLOR);
      drawComputerPaddle(g,PADDLE_COLOR);
      drawScores(g, SCORES_COLOR);
      startGame(panel, g);

   }

   //contains the loop to run the game
   public static void startGame(DrawingPanel panel, Graphics g) {
      while(true) {
         handleKeys(panel,g);
         if(!gameOver && !scoresDisplayed){
           moveBall(g);
           detectHit();
           drawPaddle(g, PADDLE_COLOR);
           moveComputerPaddle(g);
         }
         panel.sleep(SLEEP_TIME);
      }
   }

   //draws the ball
   public static void drawBall(Graphics g, Color c) {
      g.setColor(c);
      g.fillOval(ballX-BALL_SIZE/2,ballY-BALL_SIZE/2,BALL_SIZE,BALL_SIZE);
   }

   //moves the ball
   public static void moveBall(Graphics g) {
      drawBall(g,BACKGROUND_COLOR);
      ballX += ballVelocityX;
      ballY += ballVelocityY;
      if ((ballX > PANEL_WIDTH)  || (ballX < 0)) {
         if(ballX>PANEL_WIDTH){
           drawScores(g, BACKGROUND_COLOR);
           addToComputerScore(g);
         }else{
           drawScores(g, BACKGROUND_COLOR);
           addToUserScore(g);
         }resetBall(g);
      }
      if(compScore==5 || userScore==5){
        drawBall(g,BACKGROUND_COLOR);
        ballVelocityX=0;
        ballVelocityY=0;
        g.setFont(normalFont);
        g.setColor(Color.BLACK);
        gameOver=true;
        if(gamesPlayed<9){
          if(compScore==5){
            g.drawString("The computer won... The final score was " + compScore + " - " + userScore, 150, 300);
            compGamesWon++;
          }else{
            g.drawString("You won! The final score was " + userScore + " - " + compScore, 150, 300);
            userGamesWon++;
          }
        }else{
          if(compScore==5){
            compGamesWon++;
          }else{
            userGamesWon++;
          }
          if(userGamesWon > compGamesWon){
            g.drawString("MATCH COMPLETE!! You beat the computer " +userGamesWon+" games to "+compGamesWon, 100, 300);
            }else if(compGamesWon > userGamesWon){
            g.drawString("MATCH COMPLETE!! The computer beat you " +compGamesWon+" games to "+userGamesWon, 100, 300);
            }else{
            g.drawString("MATCH COMPLETE!! You tied the computer. 5 games to 5.", 150, 300);
            }
            compGamesWon=0;
            userGamesWon=0;
        }
      }drawBall(g,BALL_COLOR);
   }

   //drwas the user's paddle
   public static void drawPaddle(Graphics g, Color c) {
      g.setColor(c);
      g.drawLine(PADDLE_X,paddleY,PADDLE_X,paddleY+PADDLE_LENGTH);
   }

   //draws the comptuer paddle
   public static void drawComputerPaddle(Graphics g, Color c) {
      g.setColor(c);
      g.drawLine(COMPPADDLE_X,compPaddleY,COMPPADDLE_X,compPaddleY+COMPPADDLE_LENGTH);
   }

   //calls appropriate method to handle the input from the user
   public static void handleKeys(DrawingPanel panel, Graphics g) {
      int keyCode = panel.getKeyCode();
      if (keyCode == UP_ARROW)
         movePaddle(g,-PADDLE_MOVE_AMOUNT);
      else if (keyCode == DOWN_ARROW)
         movePaddle(g,PADDLE_MOVE_AMOUNT);
      else if (keyCode == KEY_SPACE){
         resetBall(g);
      }else if(gameOver){
        if(keyCode == KEY_NEW_GAME){
         newGame(g);
        }
      }else if(keyCode==KEY_SCORE){
        if(!scoresDisplayed){
          //draw the scores and set to true
          g.setColor(Color.BLACK);
          drawScore(g);
          scoresDisplayed=true;
        }else{
          //erase hte scores and set to false
          g.setColor(BACKGROUND_COLOR);
          drawScore(g);
          scoresDisplayed=false;
        }
      }
   }

   //allows the user to move the computer paddle
   public static void movePaddle(Graphics g, int amount) {
      drawPaddle(g, BACKGROUND_COLOR);
      paddleY += amount;
      if (paddleY < 0)
         paddleY = 0;
      if (paddleY + PADDLE_LENGTH > PANEL_HEIGHT)
         paddleY = PANEL_HEIGHT - PADDLE_LENGTH;
      drawPaddle(g,PADDLE_COLOR);
   }

   //Causes the computer paddle to follow the ball
   public static void moveComputerPaddle(Graphics g) {
      drawComputerPaddle(g, BACKGROUND_COLOR);
      int yDiff = ballY-(compPaddleY+(PADDLE_LENGTH/2));
      if(yDiff<=MAX_COMPUTER_PADDLE_MOVE && yDiff>=-MAX_COMPUTER_PADDLE_MOVE){
        compPaddleY=ballY-(PADDLE_LENGTH/2);
      }else if(yDiff<-MAX_COMPUTER_PADDLE_MOVE){
        compPaddleY=compPaddleY-MAX_COMPUTER_PADDLE_MOVE;
      }else{
        compPaddleY=compPaddleY+MAX_COMPUTER_PADDLE_MOVE;
      }
      if (compPaddleY < 0)
         compPaddleY = 0;
      if (compPaddleY + PADDLE_LENGTH > PANEL_HEIGHT)
         compPaddleY = PANEL_HEIGHT - PADDLE_LENGTH;
      drawComputerPaddle(g, PADDLE_COLOR);
   }

   //detects when the ball hits the paddle or edge of screen
   public static void detectHit() {
     //detects hit on User paddle
     if ((ballVelocityX > 0) &&
          (ballY + BALL_SIZE/2 >= paddleY) &&
          (ballY - BALL_SIZE/2 <= paddleY + PADDLE_LENGTH) &&
          (ballX + BALL_SIZE/2 >= PADDLE_X) &&
         (ballX - BALL_SIZE/2 <= PADDLE_X)){
       boxHeight= 20+(gamesPlayed)*15;
       ballVelocityX = -ballVelocityX;
       updateYVelocity();
     //detects hit on computer paddle
   }else if ((ballVelocityX < 0) &&
          (ballY + BALL_SIZE/2 >= compPaddleY) &&
          (ballY - BALL_SIZE/2 <= compPaddleY + PADDLE_LENGTH) &&
          (ballX + BALL_SIZE/2 >= COMPPADDLE_X) &&
          (ballX - BALL_SIZE/2 <= COMPPADDLE_X)){
        if(Math.abs(ballVelocityX) < BALL_SIZE){
          ballVelocityX = -ballVelocityX+1;
        }else{
          ballVelocityX = BALL_SIZE;
        }
      }if ((ballVelocityY < 0) &&
          (ballY - BALL_SIZE/2 <= 0))
         ballVelocityY = -ballVelocityY;
      else if ((ballVelocityY >0) &&
               (ballY + BALL_SIZE/2 >= PANEL_HEIGHT))
         ballVelocityY = -ballVelocityY;
   }

   //resets the ball to the center of the screen
   public static void resetBall(Graphics g) {
      drawBall(g,BACKGROUND_COLOR);
      ballX = PANEL_WIDTH/2;
      ballY = PANEL_HEIGHT/2;
      randomBallVelocity();
   }

   //draw the updated score on the screen
   public static void drawScores(Graphics g, Color c){
     g.setFont(scoresFont);
     g.setColor(c);
     g.drawString(String.valueOf(compScore),100,100);
     g.drawString(String.valueOf(userScore),375,100);
   }

   //add to the Comp. score
   public static void addToComputerScore(Graphics g){
     compScore++;
     drawScores(g, SCORES_COLOR);
     compScoreArray[gamesPlayed]=compScore;

   }

   //add to the users score
   public static void addToUserScore(Graphics g){
     userScore++;
     drawScores(g, SCORES_COLOR);
     userScoreArray[gamesPlayed]=userScore;

   }

   //draw the score box and scores
   public static void drawScore(Graphics g){
     boxHeight = 20+(gamesPlayed)*15;
     g.drawRect(boxX, boxY, BOX_WIDTH, boxHeight);
     g.setFont(normalFont);
     g.drawString("Computer("+compGamesWon+")          Player("+userGamesWon+")  ", boxX+15, boxY+15);
     for(int i=0; i<=gamesPlayed-1; i++){
       g.drawString(String.valueOf(compScoreArray[i]), boxX+40 , boxY+(30+i*15));
       g.drawString(String.valueOf(userScoreArray[i]), boxX+130, boxY+(30+i*15));
     }
   }

   //Start a new game
   public static void newGame(Graphics g){
     // start a new game.  Erase message, set score to 0, gameOver to false, call resetBall
     g.setColor(BACKGROUND_COLOR);
     g.fillRect(75,250,384,100);
     drawScores(g, BACKGROUND_COLOR);
     compScore=0;
     userScore=0;
     drawScores(g, SCORES_COLOR);
     gameOver=false;
     resetBall(g);
     if(gamesPlayed<9){
       gamesPlayed++;
     }else{
       gamesPlayed=0;
     }
   }

   public static void updateYVelocity(){
     int ballCenter = ballY+(BALL_SIZE/2);
     int paddleCenter = paddleY+(PADDLE_LENGTH/2);
     int yDiff = ballCenter-paddleCenter;
     //if neg number ball on top half of paddle
     ballVelocityY = ballVelocityY + (yDiff/8);
   }

   //get a random X and Y velocity
   public static void randomBallVelocity(){
     ballVelocityX = rand.nextInt(4)+3;
     ballVelocityY = rand.nextInt(11)-5;
   }
}