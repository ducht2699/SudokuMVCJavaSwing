/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import view.View;
import model.Sudoku;
import controller.Controller;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author ducho
 */
public class MainGame {

    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException {
        Sudoku m = new Sudoku("WorkDataDefault.txt");
        View v = new View();
        

        Controller c = new Controller(m, v);
        c.initController();
    }
}
