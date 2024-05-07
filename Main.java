import java.util.LinkedList;
import java.io.*;
import java.net.*;

class AppConfig {
    public static String SERVER_HOST = "localhost";
    public static int SERVER_PORT = 5050;
}

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();

        try { Thread.sleep(100); } 
        catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println();

        Student student = new Student("#0001", 100);
        student.registerCard();

        System.out.println("[CLIENT] student ID: " + student.getID() + "; personal student's money: " + student.getMoney() + ";");
        System.out.println("[SERVER] student ID: " + student.getID() + "; balance: " + student.getBalance() + ";");

        student.topUpBalance(50);

        System.out.println("[CLIENT] student ID: " + student.getID() + "; personal student's money: " + student.getMoney() + ";");
        System.out.println("[SERVER] student ID: " + student.getID() + "; balance: " + student.getBalance() + ";");

        student.payForTheFare();

        System.out.println("[CLIENT] student ID: " + student.getID() + "; personal student's money: " + student.getMoney() + ";");
        System.out.println("[SERVER] student ID: " + student.getID() + "; balance: " + student.getBalance() + ";");

        student.receiveBalance();

        System.out.println("[CLIENT] student ID: " + student.getID() + "; personal student's money: " + student.getMoney() + ";");
        System.out.println("[SERVER] student ID: " + student.getID() + "; balance: " + student.getBalance() + ";");
    }
}

class Server extends Thread {
    private boolean isRunning;
    private ServerSocket serverSocket;
    private LinkedList<Card> cards;

    public Server() {
        this.isRunning = false;
        this.cards = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            this.isRunning = true;
            this.serverSocket = new ServerSocket(AppConfig.SERVER_PORT);
            System.out.println("Server started successfully on port: " + AppConfig.SERVER_PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, cards);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error while starting the server: " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            this.isRunning = false;
            this.serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error while stopping the server: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private LinkedList<Card> cards;

    public ClientHandler(Socket clientSocket, LinkedList<Card> cards) {
        this.clientSocket = clientSocket;
        this.cards = cards;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Handle client requests here

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error in client handler: " + e.getMessage());
        }
    }
}

class Student {
    private String ID;
    private int money;
    private int balance;

    public Student(String ID, int money) {
        this.ID = ID;
        this.money = money;
        this.balance = 0;
    }

    public String getID() { return this.ID; }

    public int getMoney() { return this.money; }

    public int getBalance() { return this.balance; }

    public void registerCard() {
        // Simulating card registration
        this.balance = 0;
    }

    public void topUpBalance(int money) {
        // Simulating balance top-up
        this.money -= money;
        this.balance += money;
    }

    public void payForTheFare() {
        // Simulating fare payment
        if (this.balance >= 10) {
            this.balance -= 10;
        }
    }

    public void receiveBalance() {
        // Simulating balance reception
        this.money += this.balance;
        this.balance = 0;
    }
}

class Card {
    private String studentID;
    private int balance;

    public Card(String studentID, int balance) {
        this.studentID = studentID;
        this.balance = balance;
    }

    public String getStudentID() { return this.studentID; }

    public int getBalance() { return this.balance; }
}
