package com.zs.assignment10;
import com.zs.assignment10.DAO.DaoImplementation;
import com.zs.assignment10.DAO.DaoInterface;
import com.zs.assignment10.controller.Menu;
import com.zs.assignment10.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        DaoImplementation daoImplementation=new DaoImplementation();
        logger.info("Application Started");
        daoImplementation.createProductTable();
        Scanner scanner = new Scanner(System.in);
        DaoInterface dao = new DaoImplementation();
        ProductService service = new ProductService(dao);
        Menu menu = new Menu(scanner, service);
        menu.showMenu();
        menu.choiceEntry();

    }
}
