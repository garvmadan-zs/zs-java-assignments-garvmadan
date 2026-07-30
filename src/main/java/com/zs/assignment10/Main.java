package com.zs.assignment10;
import com.zs.assignment10.DAO.DaoImplementation;
import com.zs.assignment10.controller.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        DaoImplementation daoImplementation=new DaoImplementation();
        logger.info("Application Started");
        Scanner sc= new Scanner(System.in);
        daoImplementation.createProductTable();
        Menu menu=new Menu(sc);
        menu.showMenu();
        menu.choiceEntry();

    }


}
