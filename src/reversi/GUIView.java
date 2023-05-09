package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GUIView implements IView {
    IModel model;
    IController controller;

    public GUIView() {}

    JLabel message1 = new JLabel();
    JLabel message2 = new JLabel();
    JTextArea board1 = new JTextArea();
    JTextArea board2 = new JTextArea();
    JFrame frame1 = new JFrame();
    JFrame frame2 = new JFrame();

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;

        message1.setFont(new Font("Arial", Font.BOLD, 20));
        message2.setFont(new Font("Arial", Font.BOLD, 20));

        /***************************************************************
         * Frame 1 for white player
         */
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setTitle("Reversi - White Player");
        frame1.setLocationRelativeTo(null);
        frame1.getContentPane().setLayout(new BorderLayout());
        // Add label view in the North position
        JLabel label1 = new JLabel();
        label1 = message1;
        frame1.add(label1, BorderLayout.NORTH);
        // Add board view in the Center position
        board1.setEditable(false);
        board1.setPreferredSize(new Dimension(640, 640));
        board1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame1.add(board1, BorderLayout.CENTER);
        // Add square buttons
        board1.setLayout(new GridLayout(8, 8));
        board1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            SquareButton button1 = new SquareButton(x, y);
            button1.setOpaque(true);
            button1.setPreferredSize(new Dimension(80, 80));
            button1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Set the color of the circle
            if ((x == 3 || x == 4) && (y == 3 || y == 4)) {
                if ((x == 3 && y == 3) || (x == 4 && y == 4)) {
                    button1.setPlayer(1);  // White circle
                } else {
                    button1.setPlayer(2);  // Black circle
                }
            } else {
                button1.setPlayer(0);  // No circle
            }

            button1.addActionListener(new SquareButtonListener(controller, 1, x, y)); // Pass controller reference and square coordinates
            board1.add(button1, BorderLayout.CENTER);
        }
        // Add button panel in the South position
        JPanel buttonPanel1 = new JPanel(new GridLayout(2, 1));
        JButton greedyButton1 = new JButton("Greedy AI (Play White)");
        JButton restartButton1 = new JButton("Restart");
        // ADD button listener
        greedyButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(1);
            }
        });
        buttonPanel1.add(greedyButton1);
        buttonPanel1.add(restartButton1);
        frame1.add(buttonPanel1, BorderLayout.SOUTH);

        /*********************************************************
         * Frame 2 for black player
         */
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setTitle("Reversi - Player 2 (Black)");
        frame2.setLocationRelativeTo(null); // center on screen
        frame2.getContentPane().setLayout(new BorderLayout());
        // Add label view in the North position
        JLabel label2 = new JLabel();
        label2 = message2;
        frame2.add(label2, BorderLayout.NORTH);
        // Add board view in the Center position
        board2.setEditable(false);
        board2.setPreferredSize        (new Dimension(640, 640));
        board2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame2.add(board2, BorderLayout.CENTER);
        // Add square buttons
        board2.setLayout(new GridLayout(8, 8));
        board2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for (int i = 64; i > 0; i--) {
            int x = (i - 1) % 8;
            int y = (i - 1) / 8;
            SquareButton button2 = new SquareButton(x, y);
            button2.setOpaque(true);
            button2.setPreferredSize(new Dimension(80, 80));
            button2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Set the color of the circle
            if ((x == 3 || x == 4) && (y == 3 || y == 4)) {
                if ((x == 3 && y == 4) || (x == 4 && y == 3)) {
                    button2.setPlayer(1);  // White circle
                } else {
                    button2.setPlayer(2);  // Black circle
                }
            } else {
                button2.setPlayer(0);  // No circle
            }

            button2.addActionListener(new SquareButtonListener(controller, 2, x, y)); // Pass controller reference and square coordinates
            board2.add(button2, BorderLayout.CENTER);
        }
        // Add button panel in the South position
        JPanel buttonPanel2 = new JPanel(new GridLayout(2, 1));
        JButton greedyButton2 = new JButton("Greedy AI (Play Black)");
        JButton restartButton2 = new JButton("Restart");
        // ADD button listener
        greedyButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(2);
            }
        });
        buttonPanel2.add(greedyButton2);
        buttonPanel2.add(restartButton2);
        frame2.add(buttonPanel2, BorderLayout.SOUTH);
        // Display the frames
        frame1.pack();
        frame1.setVisible(true);

        frame2.pack();
        frame2.setVisible(true);
    }

    @Override
    public void refreshView() {
        for (Component component : board1.getComponents()) {
            if (component instanceof SquareButton) {
                SquareButton button = (SquareButton) component;
                int x = button.getXCoordinate();
                int y = button.getYCoordinate();
                int player = model.getBoardContents(x,y);
                button.setPlayer(player);
                button.repaint();
            }
        }
        for (Component component : board2.getComponents()) {
            if (component instanceof SquareButton) {
                SquareButton button = (SquareButton) component;
                int x = button.getXCoordinate();
                int y = button.getYCoordinate();
                int player = model.getBoardContents(x,y);
                button.setPlayer(player);
                button.repaint();
            }
        }
    }

    @Override
    public void feedbackToUser(int player, String message) {
        if (player == 1)
            message1.setText(message);
        else if (player == 2)
            message2.setText(message);
    }

    // Inner class for handling square button events
    private class SquareButtonListener implements ActionListener {
        private IController controller;
        private int player;
        private int x;
        private int y;

        public SquareButtonListener(IController controller, int player, int x, int y) {
            this.controller = controller;
            this.player = player;
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.squareSelected(player, x, y);
        }
    }

    // Inner class representing a square button
    private class SquareButton extends JButton {
        private int x;
        private int y;
        private int player;

        public SquareButton(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getXCoordinate() {
            return x;
        }

        public int getYCoordinate() {
            return y;
        }

        public int getPlayer() {
            return player;
        }

        public void setPlayer(int player) {
            this.player = player;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw green square
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw black border
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // Draw circle with border
            if (player == 1) {
                g.setColor(Color.WHITE);
            } else if (player == 2) {
                g.setColor(Color.BLACK);
            }
            int circleSize = (int) (Math.min(getWidth(), getHeight()) * 0.9);
            int circleX = (getWidth() - circleSize) / 2;
            int circleY = (getHeight() - circleSize) / 2;
            g.fillOval(circleX, circleY, circleSize, circleSize);

            g.setColor(Color.BLACK);
            g.drawOval(circleX, circleY, circleSize, circleSize);
        }
    }
}

