/**
 * Proxy.java
 *
 * Version:
 *     $Id: Proxy.java,v 1.3 2006/04/03 01:21:08 jeg3600 Exp $
 *
 * Revisions:
 *     $Log: Proxy.java,v $
 *     Revision 1.3  2006/04/03 01:21:08  jeg3600
 *     Completed proxy
 *
 *     Revision 1.2  2006/03/31 03:42:14  jeg3600
 *     Added constructor to connect to the bank server
 *
 *     Revision 1.1  2006/03/29 01:40:38  jeg3600
 *     Initial revision
 *
 */

import java.net.*;
import java.io.*;

/**
 * 
 * A proxy that sends messages to the bank and to interpret
 * messages that come back.
 *
 * @author John Garnham
 */

 public class Proxy {


     /**
      * What the bank server's hostname is
      */
     private String host;

     /**
      * What the bank server's port is
      */
     private int port;

     private Socket clientSocket;

     private ObjectOutputStream writer;

     private ObjectInputStream reader;

     private int account;

     /**
      * Describe constructor here.
      *
      * @param host The hostname of the bank server
      * @param port The port to connect to on the bank server
      */
     public Proxy(String host, int port) throws Exception{

         try {
         
             this.host = host;
             this.port = port;

             // Form a raw socket to the remote bank server
             clientSocket = new Socket(InetAddress.getByName(host), port);

             // Create an object writer connected to this socket
             writer = new ObjectOutputStream(clientSocket.getOutputStream());

             // Create an object reader connected to this socket 
             reader = new ObjectInputStream(clientSocket.getInputStream());

         } catch (Exception e) {
             throw new Exception("Error: Could not connect to server");
         }

     }


     public void validate(int account) throws Exception {


         writer.writeObject(new BankingMessage(BankingMessage.Type.VALIDATE, 
                                                     account)
                            );

         BankingMessage response = (BankingMessage)reader.readObject();

         if (response.getOperation().equals(BankingMessage.Type.FAILURE_RESPONSE)) {
             throw new ValidationException("Could not validate account");
         } else {
             this.account = account;
         }
         
     }

     public int getBalance() throws Exception{

         writer.writeObject(new BankingMessage(BankingMessage.Type.INQUIRE_QUERY, 
                                                     account)
                            );

         BankingMessage response = (BankingMessage)reader.readObject();

         if (response.getOperation().equals(BankingMessage.Type.FAILURE_RESPONSE)){
             throw new ValidationException("Could not receive balance");
         } else {
             return response.getAmount();
         }
 

     }

     public void deposit(int amount) throws Exception {

         writer.writeObject(new BankingMessage(BankingMessage.Type.DEPOSIT_QUERY, 
                                                     account,
                                                     amount)
                            );

         BankingMessage response = (BankingMessage) reader.readObject();

         if (response.getOperation().equals(BankingMessage.Type.FAILURE_RESPONSE)){
             throw new ValidationException("Error while transferring funds");
         } 

     }

     public void withdraw(int amount) throws Exception {

         writer.writeObject(new BankingMessage(BankingMessage.Type.WITHDRAW_QUERY,
                                                     account,
                                                     amount)
                           );
         
         BankingMessage response = (BankingMessage)reader.readObject();

         if (response.getOperation().equals(BankingMessage.Type.FAILURE_RESPONSE)){
             throw new ValidationException("Error while transferring funds.");
         }
     }
         

     /**
      * Close the proxy down
      *
      */
     public void close() throws IOException {

         writer.close();
         reader.close();
         clientSocket.close();
         
     }
         

} // Proxy
