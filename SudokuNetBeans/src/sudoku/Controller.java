/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ducho
 */
public class Controller {

    private Model model;
    private View view;

    public Controller(Model model, View view) throws IOException, FileNotFoundException, ClassNotFoundException {
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
        view.getRedos().clear();
        view.getUndos().add(new Model(view.getSudoku()));
        Model temp = new Model(view.getSudoku());
        
        boolean result = temp.backtracking();
        
        view.setSudoku(temp);
        view.getUndo().setEnabled(true);
        view.getRedo().setEnabled(false);
        view.getRedos().clear();
        view.update();
        view.repaint();
        if (result) {
            JOptionPane.showMessageDialog(null, "Solution founded by Backtracking", "Notification", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Cannot be solve with your work ", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void actionUndo() {
        view.getRedo().setEnabled(true);
        Model copia = view.getUndos().pop();
        if (!view.getSudoku().isNullCandidates()) {
            view.getRedos().add(new Model(view.getSudoku()));
        } else {
            view.getRedo().setEnabled(false);
        }
        view.setSudoku(new Model(copia));
        if (view.getUndos().isEmpty()) {
            view.getUndo().setEnabled(false);
        }
        view.update();
        view.repaint();
    }

    public void actionRedo() {
        Model copia = view.getRedos().pop();
        view.getUndos().add(new Model(view.getSudoku()));
        view.setSudoku(new Model(copia));
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
        Model newModel = new Model(new Model("WorkDataDefault.txt"));

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
            } while (newModel.getCellValue(row, col) == 0 || newModel.getCellValue(row, col) == -1 );

            newModel.setCellValue(row, col, 0);
            newModel.getCell(row, col).setSolve(false);
        }

        view.setSudoku(new Model(newModel));
        view.getUndo().setEnabled(false);
        view.getUndos().clear();
        view.getRedo().setEnabled(false);
        view.getRedos().clear();
        view.update();
        view.repaint();

    }
}
