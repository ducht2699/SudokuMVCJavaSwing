/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Sudoku;
import view.View;

/**
 *
 * @author ducho
 */
public class Controller {

    private Sudoku model;
    private View view;

    public Controller(Sudoku model, View view) throws IOException, FileNotFoundException, ClassNotFoundException {
        this.model = model;
        this.view = view;
        initView();
    }

    public void initView() throws IOException, FileNotFoundException, ClassNotFoundException {
        view.setModel(model);
        view.initComponents();
    }

    public void initController() {
        view.getSolve().addActionListener(e -> actionSolve());
        view.getUndo().addActionListener(e -> actionUndo());
        view.getRedo().addActionListener(e -> actionRedo());
        view.getSave().addActionListener(e -> {
            try {
                actionSave();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        view.getLoad().addActionListener(e -> {
            try {
                actionLoad();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        view.getEasy().addActionListener(e -> {
            try {
                actionMode(35);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        view.getMedium().addActionListener(e -> {
            try {
                actionMode(27);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        view.getHard().addActionListener(e -> {
            try {
                actionMode(19);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void actionSolve() {
        if (!view.isIsChecking()) {
            view.setIsChecking(true);
            Sudoku temp = new Sudoku(view.getSudoku());
            temp.backtracking();

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int a = view.getSudoku().getCell(i, j).getValue();
                    int b = temp.getCell(i, j).getValue();

                    if (a != b) {
                        view.getBoard()[i][j].setBackground(Color.RED);
                    }

                }
            }
        } else {
            view.setIsChecking(false);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    switch (view.getSudoku().getShape()[i].charAt(j)) {
                        case ('1'):
                            view.getBoard()[i][j].setBackground(new Color(238, 248, 255));
                            break;
                        case ('2'):
                            view.getBoard()[i][j].setBackground(new Color(250, 255, 0));
                            break;
                        case ('3'):
                            view.getBoard()[i][j].setBackground(new Color(198, 141, 141));
                            break;
                        case ('4'):
                            view.getBoard()[i][j].setBackground(new Color(239, 233, 133));
                            break;
                        case ('5'):
                            view.getBoard()[i][j].setBackground(new Color(255, 252, 201));
                            break;
                        case ('6'):
                            view.getBoard()[i][j].setBackground(new Color(160, 217, 232));
                            break;
                        case ('7'):
                            view.getBoard()[i][j].setBackground(new Color(91, 246, 143));
                            break;
                        case ('8'):
                            view.getBoard()[i][j].setBackground(new Color(255, 157, 112));
                            break;
                        case ('9'):
                            view.getBoard()[i][j].setBackground(new Color(255, 227, 224));
                            break;
                    }
                }
            }
        }

        view.update();
        view.repaint();

    }

    public void actionUndo() {
        view.getRedo().setEnabled(true);
        Sudoku copia = view.getUndos().pop();
        if (!view.getSudoku().isNullCandidates()) {
            view.getRedos().add(new Sudoku(view.getSudoku()));
        } else {
            view.getRedo().setEnabled(false);
        }
        view.setSudoku(new Sudoku(copia));
        if (view.getUndos().isEmpty()) {
            view.getUndo().setEnabled(false);
        }
        view.update();
        view.repaint();
    }

    public void actionRedo() {
        Sudoku copia = view.getRedos().pop();
        view.getUndos().add(new Sudoku(view.getSudoku()));
        view.setSudoku(new Sudoku(copia));
        view.getUndo().setEnabled(true);
        if (view.getRedos().size() == 0) {
            view.getRedo().setEnabled(false);
        }
        view.update();
        view.repaint();
    }

    public void actionSave() throws IOException, FileNotFoundException, ClassNotFoundException {
        view.saveWork();
        //view.save();
    }

    public void actionLoad() throws IOException, FileNotFoundException, ClassNotFoundException {
        view.loadWork();
        view.load();
        view.update();
    }

    public void actionMode(int size) throws IOException {
        //generate new model with random solution
        Sudoku newModel = new Sudoku(new Sudoku("WorkDataDefault.txt"));

        Random rd = new Random();
        int value = rd.nextInt(9);
        int col = rd.nextInt(9);
        int row = rd.nextInt(9);

        newModel.setCellValue(row, col, value + 1);
        newModel.backtracking();

        //get 35 different random positions erased
        size = 81 - size;
        for (int i = 0; i < size; i++) {
            do {
                col = rd.nextInt(9);
                row = rd.nextInt(9);
            } while (newModel.getCellValue(row, col) == 0 || newModel.getCellValue(row, col) == -1);

            newModel.setCellValue(row, col, 0);
            newModel.getCell(row, col).setSolve(false);
        }

        view.setSudoku(new Sudoku(newModel));
        view.getUndo().setEnabled(false);
        view.getUndos().clear();
        view.getRedo().setEnabled(false);
        view.getRedos().clear();
        view.update();
        view.repaint();

    }
}
