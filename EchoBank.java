

/*

 * $Id: EchoBank.java,v 1.2 2006/03/25 20:53:24 vcss233 Exp $

 * Revisions:

 *     $Log: EchoBank.java,v $

 *     Revision 1.2  2006/03/25 20:53:24  vcss233

 *     more progress - can talk to the bank a little bit

 *

 *     Revision 1.1  2006/03/24 05:14:37  vcss233

 *     unknown

 *

 *     Revision 1.3  2006/03/24 03:41:41  vcss233

 *     More work on termination

 *

 *     Revision 1.2  2006/03/23 22:33:17  vcss233

 *     Set text box to be HTML (I hope)
Add port number to EchoBank

 *

 *     Revision 1.1  2006/03/23 04:26:13  vcss233

 *     Simple test server written

 *

 */



import java.io.EOFException;

import java.io.IOException;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.ServerSocket;

import java.net.Socket;

import java.net.SocketException;



/**

 * // Description

 *

 * @author jeh

 *

 */

public class EchoBank {



    public static void main( String[] args ) throws IOException {

        boolean usageError = false;

        if ( args.length != 1 ) usageError = true;

        int port = 0;

        try {

            port = Integer.parseInt( args[ 0 ] );

        }

        catch( NumberFormatException nfe ) {

            usageError = true;

        }

        if ( usageError ) {

            System.err.println( "Usage: java EchoBank server-port" );

            System.exit( 1 );

        }

        ServerSocket mainGuy = new ServerSocket( port );



        Socket conn = null;

        while ( true ) {

            conn = mainGuy.accept();

            System.out.println( "Accepted " + conn );

            ObjectOutputStream out =

                new ObjectOutputStream( conn.getOutputStream() );

            ObjectInputStream in =

                new ObjectInputStream( conn.getInputStream() );

            try {

                BankingMessage msg = null;

                do {

                    msg = (BankingMessage)in.readObject();

                    System.out.println( "Received message " +

                            msg.getAccount() + " " + msg.getAmount() );

                    BankingMessage response = new BankingMessage(

                            BankingMessage.Type.SUCCESS_RESPONSE,

                            msg.getAccount(), 100000 );

                    out.writeObject( response );

                    System.out.println( "   Sent message " +

                            response.getAccount() + " " +

                            response.getAmount() );

                } while ( msg.getAccount() != -1 );

            }

            catch ( SocketException se ) {

                System.out.println( "ATM has terminated the connection." );

            }

            catch ( EOFException ioe ) {

                System.out.println( "ATM has terminated the connection." );

            }

            catch( ClassNotFoundException cnfe ) {

                System.err.println( cnfe );

                cnfe.printStackTrace();

                return;

            }

            finally {

                conn.close();

            }

        }

    }



}

