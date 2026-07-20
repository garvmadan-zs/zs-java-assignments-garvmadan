package com.zs.assignment4.controller;
import com.zs.assignment4.model.SearchResult;
import com.zs.assignment4.services.CatalogService;
import java.util.Scanner;

public class Controller {
    private final Scanner scanner;
    private final CatalogService catalogService;

    public Controller(Scanner scanner) {
        this.scanner = scanner;
        this.catalogService = new CatalogService();
    }

    public void showMenu() {
        Menu menu= new Menu();
        while (true) {

            menu.displayMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        displayHierarchy();
                        break;
                    case "2":
                        searchCatalog();
                        break;
                    case "3":
                        showCache();
                        break;
                    case "4":
                        System.out.println("Exiting assignment 4 demo.");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Unexpected error: " + ex.getMessage());
            }
        }
    }

    private void displayHierarchy() {
        System.out.println("\n" + catalogService.displayHierarchy());
    }

    private void searchCatalog() {
        System.out.print("Enter category, subcategory, or product name/SKU: ");
        String keyword = scanner.nextLine();
        SearchResult result = catalogService.search(keyword);

        System.out.println("\nSearch result from cache: " + result.isFromCache());
        if (!result.isFromCache()) {
            System.out.println("The item was first added to the LRU cache and then returned.");
        }
        System.out.println("\n" + result.getHierarchy());
        System.out.println(result.getContent());
    }

    private void showCache() {
        System.out.println(catalogService.displayCache());
    }
}
