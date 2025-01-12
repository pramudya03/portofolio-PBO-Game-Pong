import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener, KeyListener {
    private int paddle1Y = 350;
    private int paddle2Y = 350;
    private int ballX = 400;
    private int ballY = 300;
    private int ballXSpeed = -5;
    private int ballYSpeed = 5;
    private final int PADDLE_HEIGHT = 100;
    private final int BALL_SIZE = 20;
    private boolean gameOver = false;
    private int score1 = 0;
    private int score2 = 0;
    private final Timer timer;
    private int timeLeft = 60; // Permainan berlangsung selama 60 detik

    public Game() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(5, this);
        timer.start();

        // Timer untuk waktu permainan
        new Timer(1000, _ -> {
            if (!gameOver) {
                timeLeft--;
                if (timeLeft <= 0) {
                    gameOver = true;
                }
                repaint();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            if (score1 > score2) {
                g.drawString("Player 1 Wins!", getWidth() / 2 - 200, getHeight() / 2);
            } else if (score2 > score1) {
                g.drawString("Player 2 Wins!", getWidth() / 2 - 200, getHeight() / 2);
            } else {
                g.drawString("It's a Draw!", getWidth() / 2 - 150, getHeight() / 2);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press 'R' to Restart", getWidth() / 2 - 100, getHeight() / 2 + 50);
            return;
        }

        // Draw paddles
        g.setColor(Color.WHITE);
        int PADDLE_WIDTH = 10;
        g.fillRect(20, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(getWidth() - 30, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw scores
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1: " + score1, 50, 30);
        g.drawString("Player 2: " + score2, getWidth() - 150, 30);

        // Draw time left
        g.drawString("Time Left: " + timeLeft + "s", getWidth() / 2 - 60, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        // Move the ball
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Ball collision with top and bottom walls
        if (ballY <= 0 || ballY >= getHeight() - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball collision with paddle 1
        if (ballX <= 30 && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        }

        // Ball collision with paddle 2
        if (ballX >= getWidth() - 50 && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        }

        // Ball out of bounds (score update)
        if (ballX <= 0) {
            score2 += 3; // Paddle 2 scores
            resetBall();
        } else if (ballX >= getWidth()) {
            score1 += 3; // Paddle 1 scores
            resetBall();
        }

        repaint();
    }

    private void resetBall() {
        ballX = getWidth() / 2;
        ballY = getHeight() / 2;
        ballXSpeed = -ballXSpeed;
        ballYSpeed = 2;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_R) {
            // Restart game
            gameOver = false;
            ballX = 400;
            ballY = 300;
            ballXSpeed = -2;
            ballYSpeed = 2;
            paddle1Y = 250;
            paddle2Y = 250;
            score1 = 0;
            score2 = 0;
            timeLeft = 60;
            timer.start();
            repaint();
        }

        // Paddle 1 controls
        if (key == KeyEvent.VK_W && paddle1Y > 0) {
            paddle1Y -= 10;
        }
        if (key == KeyEvent.VK_S && paddle1Y < getHeight() - PADDLE_HEIGHT) {
            paddle1Y += 10;
        }

        // Paddle 2 controls
        if (key == KeyEvent.VK_UP && paddle2Y > 0) {
            paddle2Y -= 10;
        }
        if (key == KeyEvent.VK_DOWN && paddle2Y < getHeight() - PADDLE_HEIGHT) {
            paddle2Y += 10;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
