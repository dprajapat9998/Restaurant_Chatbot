package com.practice.myappvs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.StringJoiner;

@SpringBootApplication
public class ChatBot implements CommandLineRunner {

    private final MenuService menuService;

    public ChatBot(MenuService menuService) {
        this.menuService = menuService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear customerOrder table when the application starts
        menuService.clearCustomerOrders();
    }

    @RestController
    static class ChatbotController {

        private final MenuService menuService;

        public ChatbotController(MenuService menuService) {
            this.menuService = menuService;
        }

        @PostMapping("/backend-endpoint")
        public ResponseEntity<ChatbotResponse> processMessage(@RequestBody ChatbotRequest request) {
            String userMessage = request.getMessage();

            // Your logic to process userMessage and generate a response
            String botResponse = processUserMessage(userMessage);

            return ResponseEntity.ok(new ChatbotResponse(botResponse));
        }

        private String processUserMessage(String userMessage) {
            // Implement your chatbot logic here
            if (userMessage.equalsIgnoreCase("Hii")) {
                return "Hello sir !!! if you want to order something type (order) and if you want to know today's special type (special)";
            }
            if(userMessage.equalsIgnoreCase("help")){
                return "Command for operating chat bot are\norder: to place order (you can direct type the name of food!)\nmenu: to see menu\nspecial: to know todays special \nsee order : to see what you ordered till now \nbill: to generate bill";
            }
            if(userMessage.equalsIgnoreCase("cash") || userMessage.equalsIgnoreCase("card") ){
                return "please pay at counter for this payment method\nMake your payment and wait for 20 minutes; your order will be ready. Thank you!";
            }
            if (userMessage.equalsIgnoreCase("special")) {
                return "Today's special is Sahi Panner \n if you want to order sahi panner type (sahi panner) and if you want to order something else type (order)";
            }
            if (userMessage.equalsIgnoreCase("order")) {
                return "Sure! Please type the name of the item you want to order. type (menu) to open menu";
            }
            if(userMessage.equalsIgnoreCase("tera owner kon h")){
                return "The great Deepak Prajapat";
            }
            if (userMessage.equalsIgnoreCase("menu")) {
                List<Menu> menuItems = menuService.getMenuItems();
                if (menuItems != null && !menuItems.isEmpty()) {
                    StringJoiner response = new StringJoiner("\n", "Here is our menu:\n", "");
                    for (Menu menuItem : menuItems) {
                        response.add(menuItem.getItemName() + " - rs." + menuItem.getPrice());
                    }
                    return response.toString();
                }
            }

            // Check if the userMessage corresponds to an item in the menu for ordering
            Menu orderedItem = menuService.getMenuItemByName(userMessage);
            if (orderedItem != null) {
                // Process the order and save it to the database
                menuService.placeOrder(orderedItem.getItemName());
                return "Thank you for your order! " + orderedItem.getItemName() + " has been added to your cart. You can order more if you want else type (bill) for bill generation";
            }

            // Check if the user wants to see their orders
            if (userMessage.equalsIgnoreCase("see order")) {
                List<CustomerOrder> customerOrders = menuService.getCustomerOrders();
                if (!customerOrders.isEmpty()) {
                    StringJoiner response = new StringJoiner("\n", "Your Orders:\n", "");
                    for (CustomerOrder order : customerOrders) {
                        response.add(order.getItemName() + " - rs." + order.getPrice());
                    }
                    return response.toString();
                } else {
                    return "You don't have any orders yet.";
                }
            } // Check if the user wants to see the bill
            if (userMessage.equalsIgnoreCase("bill")) {
                List<CustomerOrder> customerOrders = menuService.getCustomerOrders();
                if (!customerOrders.isEmpty()) {
                    generatePdfBill(customerOrders);
                    StringJoiner response = new StringJoiner("\n", "Your Bill:\n", "");
                    double totalAmount = 0.0;

                    for (CustomerOrder order : customerOrders) {
                        response.add(order.getItemName() + " - rs." + order.getPrice());
                        totalAmount += order.getPrice();
                    }
                    response.add("\nTotal Amount to be Paid: rs." + totalAmount);
                    response.add("How you want to pay the bill (Type the payment method name)\n");
                    response.add("1.Cash" + "\n");
                    response.add("2.UPI" + "\n");
                    response.add("3.Card" + "\n");

                    return response.toString();
                } else {
                    return "You don't have any orders yet.";
                }
            }
            // Check if the user wants to pay the bill using UPI

            return "Sorry I dont't understand what you want to say!!! You can type help to know relevant commands!";
        }

        private File generatePdfBill(List<CustomerOrder> customerOrders) {
            try {
                Document document = new Document();
                String pdfPath = "D:\\Maven\\myappvs\\bill.pdf"; // Specify the desired path
        
                // Create PdfWriter instance
                PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        
                // Open the document
                document.open();
        
                // Add title with centered alignment
                Paragraph title = new Paragraph("Thanks for visiting TALK-N-Treat!!");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
        
                // Add "BILL" with centered alignment
                Paragraph billHeader = new Paragraph("BILL");
                billHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(billHeader);
        
                // Add content to the document (e.g., customer orders)
                double totalAmount = 0.0;
        
                for (CustomerOrder order : customerOrders) {
                    document.add(new Paragraph(order.getItemName() + " - rs." + order.getPrice()));
                    totalAmount += order.getPrice();
                }
        
                // Add total amount to the document
                document.add(new Paragraph("\nTotal Amount to be Paid: rs." + totalAmount));
        
                // Close the document
                document.close();
        
                return new File(pdfPath);
        
            } catch (Exception e) {
                e.printStackTrace();
                // You might want to handle the exception more gracefully
                return null;
            }
        }
        
        static class ChatbotRequest {
            private String message;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }

        static class ChatbotResponse {
            private String message;

            public ChatbotResponse(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
