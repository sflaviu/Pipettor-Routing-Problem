package main;

import controller.Controller;

/**
 *  main class. The file name is hard-coded in the application, and must be changed from here in order to change the input.
 */
public class Main {


    public static void main(String[] args)
    {
        Controller c= Controller.getController();
        c.start("exampleInput.txt");

    }
}
