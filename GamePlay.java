import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 5;
    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;

    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1,1, 697, 597);

        //drawing map
        map.draw((Graphics2D)g);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590,30);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,597);
        g.fillRect(0,0,697,3);
        g.fillRect(697,0,3,597);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX,550,100,8);

        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX,ballPosY,20,20);

        if (ballPosY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Game Over! Scores: "+score, 190,300);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press enter to restart", 200,350);

        }

        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20,20).intersects(new Rectangle(playerX, 550, 100,8)) ) {
                ballYdir = -ballYdir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j*map.brickWidth+80;
                        int brickY = i*map.brickHeight+50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX,ballPosY,20,20);
                        Rectangle brickRect = rect;

                        if(brickRect.intersects(ballRect)) {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x|| ballPosX + 1 >= brickRect.x + brickWidth) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }

                    }
                }
            }

            System.out.println("x:"+ballPosX);
            System.out.println("y:"+ballPosY);
            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if (ballPosX <= 0) {
                ballXdir = -ballXdir;
            }
            if (ballPosY <= 0) {
                ballYdir = -ballYdir;
            }
            if (ballPosX >= 700) {
                ballXdir -= ballXdir;
            }
            if (ballPosX >= 600) {
                ballXdir -= ballXdir;
            }

        }
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                playerX = 310;
                ballPosX = 120;
                ballPosY = 350;

                ballXdir = -1;
                ballYdir = -2;

                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);

                repaint();

            }
        }
    }
    public void moveRight() {
        play = true;
        playerX += 20;
    }
    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}