/**
 * BankConnection.java
 *
 * Version:
 *     $Id$
 *
 * Revisons:
 *     $Log$
 */

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

/**
 * A socket connection
 *
 * @author John Garnham
 */

public class BankConnection extends Thread {

    /**
     * The object reader
     */
    private ObjectInputStream reader;

    /**
     * The object writer
     */
    private ObjectOutputStream writer;

    private BankList bankAccounts;

    public BankConnection(Socket socket, BankList accounts) {

        try {
            
            writer = new ObjectOutputStream(socket.getOutputStream());

            reader = new ObjectInputStream(socket.getInputStream());

            bankAccounts = accounts;

            this.start();

        } catch (Exception e) {

            System.err.println(e.getMessage());

        } 


    }

    public void run() {

        // The banking message read from the client
        BankingMessage in = null;

        // The banking message to send back to the client
        BankingMessage out = null;

        try {

            do {            

                in = (BankingMessage) reader.readObject();

                int account = in.getAccount();

                if (in.getOperation() == 
                    BankingMessage.Type.VALIDATE) {
                    
                    System.out.println("**************");
                    System.out.println("VALIDATION REQUEST");
                    
                    try {
                        BankAccount tempAccount = bankAccounts.getBankAccount(account);
                        System.out.println(tempAccount);
                        out = new BankingMessage(BankingMessage.Type.SUCCESS_RESPONSE, account);
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("FAILURE");
                        out = new BankingMessage(BankingMessage.Type.FAILURE_RESPONSE, account);
                    }

                    System.out.println("************");

                } else if(in.getOperation() ==
                          BankingMessage.Type.INQUIRE_QUERY) {

                    System.out.println("**************");
                    System.out.println("INQUIRE QUERY");
                    
                    try {
                        BankAccount tempAccount = bankAccounts.getBankAccount(account);
                        System.out.println(tempAccount);
                        out = new BankingMessage(BankingMessage.Type.SUCCESS_RESPONSE,
                                                 account, tempAccount.getBalance());
                    } catch (Exception e) {
                        System.out.println("FAILURE");
                        out = new BankingMessage(BankingMessage.Type.FAILURE_RESPONSE,
                                                 account);
                    } 

                    System.out.println("************");
                    
                } else if(in.getOperation() ==
                          BankingMessage.Type.DEPOSIT_QUERY) {
                    
                    int amount = in.getAmount();

                    System.out.println("************");
                    System.out.println("DEPOSIT");
                
                    try {
                        BankAccount tempAccount = bankAccounts.getBankAccount(account);
                        tempAccount.deposit(amount);
                        System.out.println(tempAccount);
                        out = new BankingMessage(BankingMessage.Type.SUCCESS_RESPONSE,
                                                 account, amount);
                    } catch (Exception e) {
                        System.out.println("FAILURE");
                        out = new BankingMessage(BankingMessage.Type.FAILURE_RESPONSE,
                                                 account);
                    }

                    System.out.println("************");

                } else if (in.getOperation() ==
                           BankingMessage.Type.WITHDRAW_QUERY) {

                    int amount = in.getAmount();
                    
                    System.out.println("************");
                    System.out.println("WITHDRAW");

                    try {
                        BankAccount tempAccount = bankAccounts.getBankAccount(account);
                        tempAccount.withdraw(amount);
                        System.out.println(tempAccount);
                        out = new BankingMessage(BankingMessage.Type.SUCCESS_RESPONSE,
                                             account, amount);
                    } catch (Exception e) {
                        System.out.println("FAILURE");
                        out = new BankingMessage(BankingMessage.Type.FAILURE_RESPONSE,
                                                 account);
                    }
                
                    System.out.println("************");
                    
                } 

                writer.writeObject(out);
            
            } while(in.getAccount() != -1);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


        
} // BankConnection
