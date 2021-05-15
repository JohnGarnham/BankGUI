/**
 * BankServer.java
 * 
 * Version:
 *     $Id: BankServer.java,v 1.1 2006/04/18 01:44:55 jeg3600 Exp jeg3600 $
 * 
 * Revisions:
 *     $Log: BankServer.java,v $
 *     Revision 1.1  2006/04/18 01:44:55  jeg3600
 *     Initial revision
 *
 */

import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

/**
 * Provides the server for the bank
 *
 * @author John Garnham
 */

public class BankServer extends Thread {


    /**
     * What the servers port number is
     */
    private int port;

    /**
     * The socket to use to listen to incoming 
     * connections
     */
    private ServerSocket server;

    /**
     * The file reader
     */
    private BufferedReader fileReader;

    /**
     * The file writer
     */
    private PrintWriter fileWriter;


    /**
     * A collection of bank accounts
     */
    private BankList bankAccounts = new BankList();

    /**
     * The bank server
     *
     * @param args The port number
     */
    public static void main(String[] args) {

        int port = -1;

        // Check for proper command line input

        try {

            if (args.length != 1) {
                throw new Exception("Usage: java BankServer port");
            }

            port = Integer.parseInt(args[0]);
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number specified.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Create the bank server
        BankServer bank = new BankServer(port);

    }


    public BankServer(int port) {

        // Set what the servers port number is
        this.port = port;
       
        try {
        	if(port < 1 || port > 65000) {
        		throw new IOException("Invalid port: " + port);
        	}

            server = new ServerSocket(port);

        } catch (IOException e) {
            System.err.println("Could not create server: " + 
                               e.getMessage() );
            System.exit(0);

        }
        

        // Set up the bank accounts
        
        try {

            // Read the account data file 

            fileReader = new BufferedReader(new FileReader("account.data"));

            String line = fileReader.readLine();
        
            do {

                // The format is ( account-number current-balance-in-cents)
                System.out.println(line);

                String[] parts = line.split(" ");

                int account = Integer.parseInt(parts[0]);
                int amount = Integer.parseInt(parts[1]);


                // Add it to the collection 
                bankAccounts.add(new BankAccount(account, amount));

                System.out.println("***************");
                System.out.println("Bank accounts: " + bankAccounts);
                System.out.println("***************");

                line = fileReader.readLine();
           
            } while (line != null);

        } catch (Exception e) {

            System.err.println(e);

        }


        System.out.println("Server has been started on port #" + port);

        this.start();

    }

    public void run() {

        try {
            
            Scanner keyboard = new Scanner(System.in);


            while(true ) {
            
                // Accept a new connection
                Socket newConnection = server.accept();

                System.out.println("Accepted new connection from " +
                               newConnection);

                // Create a new banking connection thread
                // with this socket
                BankConnection connection = new BankConnection(newConnection, 
                                                               bankAccounts);

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

} // BankServer
