/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author ducho
 */
public class Cell extends Observable implements Serializable, Observer, Iterable<Integer> {

        private List<Integer> values = new ArrayList<Integer>();

        private boolean isSolved = false;

        private int row;

        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            for (int n = 1; n <= 9; n++) {
                values.add(n);
            }
        }

        public synchronized void addObserver(String[] shape, Cell[][] cells) {
            int mycell = Integer.parseInt("" + shape[row].charAt(col));
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    boolean isSame = (i == row) && (j == col);
                    boolean isSameLine = (i == row) || (j == col);
                    boolean isSecondary = false;
                    int mycontext = Integer.parseInt("" + shape[i].charAt(j));
                    if (mycell == mycontext) {
                        isSecondary = true;
                    }
                    if (!isSame && (isSameLine || isSecondary)) {
                        super.addObserver(cells[i][j]);
                    }
                }
            }
        }

        public void setValue(int value) {
            values.clear();
            values.add(value);
            isSolved = true;
            super.setChanged();
            super.notifyObservers(value);
        }

        @Override
        public void update(Observable o, Object arg) {
            values.remove(arg);
        }

        public int getValue() {
            return (getValues().size() == 1) ? getValues().get(0) : (getValues().size() == 0) ? -1 : 0;
        }

        public boolean isSolve() {
            return this.isSolved;
        }
        
        public void setSolve(boolean b) {
            this.isSolved = b;
        }

        public List<Integer> getValues() {
            return values;
        }

        @Override
        public String toString() {
            String cadena = "";
            for (int i = 0; i < values.size(); i++) {
                cadena += String.format("%s ", Integer.toString(values.get(i)));
            }
            return cadena;
        }

        public Iterator<Integer> iterator() {
            return values.iterator();
        }

        public void clone(Cell celda) {
            List<Integer> copia = new ArrayList<Integer>();
            copia.addAll(celda.getValues());
            values.clear();
            values.addAll(copia);
            isSolved = celda.isSolve();
        }

    }
