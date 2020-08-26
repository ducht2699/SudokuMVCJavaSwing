package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import jdk.jfr.events.FileReadEvent;

public class View extends JFrame {

    private Model sudoku;

    private JPanel[][] board;

    //Buttons
    private JButton solve;

    private JButton undo;

    public void setModel(Model m) {
        this.sudoku = m;
    }

    public JButton getEasy() {
        return easy;
    }

    public JButton getMedium() {
        return medium;
    }

    public JButton getHard() {
        return hard;
    }

    public JButton getSave() {
        return save;
    }

    public JButton getLoad() {
        return load;
    }

    private JButton redo;

    private JButton easy;

    private JButton medium;

    private JButton hard;

    private JButton save;

    private JButton load;

    //Labels
    private JLabel text;

    public JButton getSolve() {
        return solve;
    }

    public void setSudoku(Model sudoku) {
        this.sudoku = sudoku;
    }

    public Model getSudoku() {
        return sudoku;
    }

    public Stack<Model> getUndos() {
        return undos;
    }

    public Stack<Model> getRedos() {
        return redos;
    }


    public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
        undos.clear();
        redos.clear();
        redo.setEnabled(false);
        undo.setEnabled(false);
        
    }


    public JButton getUndo() {
        return undo;
    }

    public JButton getRedo() {
        return redo;
    }

    public JLabel[][] getCands() {
        return cands;
    }

    private JLabel[][] cands;

    //Undo
    private Stack<Model> undos = new Stack<Model>();

    private Stack<Model> redos = new Stack<Model>();

    public View() {
    }

    public void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Sudoku by Team 7");
        this.setSize(546, 647);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);


        /*---------------------------------------------------------------------------------------------*/
        board = new JPanel[9][9];
        int value;
        final String[] shape = sudoku.getShape();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new JPanel();
                board[i][j].setBounds(60 * j, 60 * i, 60, 60);
                board[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                board[i][j].setLayout(null);

                text = new JLabel();
                text.setFont(new Font("forte", 9, 40));
                text.setForeground(Color.black);
                board[i][j].add(text);
                cands = new JLabel[3][3];
                for (int k = 0; k < 3; k++) {

                    for (int l = 0; l < 3; l++) {
                        int numbCand = k * 3 + l;
                        cands[k][l] = new JLabel("" + numbCand);
                        cands[k][l].setFont(new Font("forte", 9, 15));
                        cands[k][l].setForeground(Color.black);
                        board[i][j].add(cands[k][l]);
                    }

                }

                switch (shape[i].charAt(j)) {
                    case ('1'):
                        board[i][j].setBackground(new Color(238, 248, 255));
                        break;
                    case ('2'):
                        board[i][j].setBackground(new Color(250, 255, 0));
                        break;
                    case ('3'):
                        board[i][j].setBackground(new Color(198, 141, 141));
                        break;
                    case ('4'):
                        board[i][j].setBackground(new Color(239, 233, 133));
                        break;
                    case ('5'):
                        board[i][j].setBackground(new Color(255, 252, 201));
                        break;
                    case ('6'):
                        board[i][j].setBackground(new Color(160, 217, 232));
                        break;
                    case ('7'):
                        board[i][j].setBackground(new Color(91, 246, 143));
                        break;
                    case ('8'):
                        board[i][j].setBackground(new Color(255, 157, 112));
                        break;
                    case ('9'):
                        board[i][j].setBackground(new Color(255, 227, 224));
                        break;
                }
                value = sudoku.getCellValue(i, j);
                if (value != 0) {
                    final int x = i;
                    final int y = j;
                    text.setText("" + value);
                    text.setBounds(20, 10, 40, 40);
                } else {
                    for (int cand : sudoku.getCellValues(i, j)) {
                        final int x = i;
                        final int y = j;
                        final int numb = cand;
                        cands[(cand - 1) / 3][(cand - 1) % 3].setText("" + cand);
                        cands[(cand - 1) / 3][(cand - 1) % 3].setBounds(10 + 16 * ((cand - 1) % 3), 8 + 16 * ((cand - 1) / 3), 15, 15);
                        cands[(cand - 1) / 3][(cand - 1) % 3].addMouseListener(new MouseListener() {
                            public void mouseClicked(MouseEvent e) {
                                undos.add(new Model(sudoku));
                                redos.clear();
                                undo.setEnabled(true);
                                redo.setEnabled(false);
                                sudoku.setCellValue(x, y, numb);
                                sudoku.test(x, y);
                                update();
                                repaint();
                                if (sudoku.isSolve()) {
                                    JOptionPane.showMessageDialog(null, "Sudoku solved!!!", "Notification", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }

                            public void mousePressed(MouseEvent e) {
                            }

                            public void mouseReleased(MouseEvent e) {
                            }

                            public void mouseEntered(MouseEvent e) {
                                JLabel label = (JLabel) board[x][y].getComponent(numb);
                                label.setFont(new Font("forte", 9, 16));
                                label.setForeground(Color.blue);
                                label.setText("" + numb);
                                label.setBounds(10 + 16 * ((numb - 1) % 3), 8 + 16 * ((numb - 1) / 3), 15, 15);
                                repaint();
                            }

                            public void mouseExited(MouseEvent e) {
                                JLabel label = (JLabel) board[x][y].getComponent(numb);
                                label.setFont(new Font("forte", 9, 15));
                                label.setForeground(Color.black);
                                label.setText("" + numb);
                                label.setBounds(10 + 16 * ((numb - 1) % 3), 8 + 16 * ((numb - 1) / 3), 15, 15);
                                repaint();
                            }
                        });
                    }
                }
                this.add(board[i][j]);
            }
        }
        /*---------------------------------------------------------------------------------------------*/
        //generate Buttons
        //ngang: 546, dọc: 80
        //line 1
        easy = new JButton("Easy");
        easy.setFont(new Font("forte", 9, 16));
        easy.setBounds(115, 545, 75, 32);

        medium = new JButton("Medium");
        medium.setFont(new Font("forte", 9, 10));
        medium.setBounds(195, 545, 75, 32);

        hard = new JButton("Hard");
        hard.setFont(new Font("forte", 9, 16));
        hard.setBounds(275, 545, 75, 32);

        solve = new JButton("Solve");
        solve.setFont(new Font("forte", 9, 16));
        solve.setBounds(355, 545, 75, 32);

        //line 2
        undo = new JButton("Undo");
        undo.setFont(new Font("forte", 9, 16));
        undo.setBounds(115, 582, 75, 32);

        redo = new JButton("Redo");
        redo.setFont(new Font("forte", 9, 16));
        redo.setBounds(195, 582, 75, 32);

        save = new JButton("Save");
        save.setFont(new Font("forte", 9, 16));
        save.setBounds(275, 582, 75, 32);

        load = new JButton("Load");
        load.setFont(new Font("forte", 9, 16));
        load.setBounds(355, 582, 75, 32);

        if (undos.isEmpty()) {
            undo.setEnabled(false);
        } else {
            undo.setEnabled(true);
        }
        if (redos.isEmpty()) {
            redo.setEnabled(false);
        } else {
            redo.setEnabled(true);
        }

        this.add(solve);
        this.add(undo);
        this.add(redo);
        this.add(easy);
        this.add(hard);
        this.add(medium);
        this.add(save);
        this.add(load);
        this.setVisible(true);
    }

    public JPanel[][] getBoard() {
        return board;
    }

    public JLabel getText() {
        return text;
    }

    public void doInvisibleCandidates(JPanel panel) {
        for (int k = 0; k < 10; k++) {
            JLabel candLabel = (JLabel) panel.getComponent(k);
            candLabel.setVisible(false);
        }
    }

    public void update() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                doInvisibleCandidates(board[i][j]);
                int value = sudoku.getCellValue(i, j);
                if (value != 0 && value != -1) {
                    board[i][j].remove(0);
                    JLabel label = new JLabel();
                    label.setFont(new Font("forte", 9, 40));
                    label.setForeground(Color.black);
                    label.setText("" + value);
                    label.setBounds(20, 10, 40, 40);
                    label.setVisible(true);
                    board[i][j].add(label, 0);
                }
                if (value == 0) {
                    JLabel label = (JLabel) board[i][j].getComponent(0);
                    label.setVisible(false);
                    for (int cand : sudoku.getCell(i, j)) {
                        JLabel candLabel = (JLabel) board[i][j].getComponent(cand);
                        candLabel.setText("" + cand);
                        candLabel.setVisible(true);
                    }
                }
                if (value == -1) {
                    ImageIcon imagen = new ImageIcon("calavera.gif");
                    JLabel label = (JLabel) board[i][j].getComponent(0);
                    label.setIcon(imagen);
                    label.setBounds(0, 0, 60, 60);
                    label.setVisible(true);
                }
            }
        }
    }

    public void saveWork() throws IOException {
        sudoku.saveWork();
    }
    
    public void loadWork() throws IOException {
        sudoku.LoadWork();
    }
}
