/**
 * ATM.java
 *
 * Version:
 *     $Id: ATM.java,v 1.5 2006/04/03 01:17:24 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *     $Log: ATM.java,v $
 *     Revision 1.5  2006/04/03 01:17:24  jeg3600
 *     Completed withdraw and deposit
 *
 *     Revision 1.4  2006/04/03 01:17:08  jeg3600
 *     Completed get balance implementation
 *
 *     Revision 1.3  2006/04/03 01:15:48  jeg3600
 *     Completed GUI design
 *
 *     Revision 1.2  2006/03/31 03:40:06  jeg3600
 *     Starting working on the GUI
 *
 *     Revision 1.1  2006/03/29 01:40:33  jeg3600
 *     Initial revision
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

/**
 *
 * The ATM interface
 *
 * @author John Garnham
 */

public class ATM extends JFrame implements ActionListener {


    /**
     * Types of operations
     */
    private enum Operation{ NONE, GET_BALANCE, WITHDRAW, DEPOSIT}

    /**
     * What the current operation is
     */
    private Operation operation = Operation.NONE;

    /**
     * A temporary value
     */
    private Operation tempOperation = Operation.NONE;

    /**
     * A proxy server to reach the bank with
     */
    
    private Proxy bank;

    /**
     * Text area to show what the user input is
     */
    
    private JTextArea input;
    /**
     * Text area to show bank messages to the user
     */
    
    private JTextArea output;

    /**
     * Whether or not the account has been verified
     */
    private boolean verified = false;

    /**
     * The current account number
     */
    private String accountNumber = "";

    /**
     * The current amount of money
     */
    private String amount = "";

    /**
     * The ATM's welcome message. This message is used so often that it
     * is convenient to put it in a variable
     */
    private static String WELCOME_MSG = "\nWelcome to ACME Banking \n\n" + 
                                       "Please enter your account number.\n";


    /**
     * The ATM's menu. This message is used so often that it is convenient
     * to put it in a variable
     */
    private static String MENU = "Welcome. Your account has been " +
                                       " verified!\n" +
                                       "Please select one of the following " +
                                       " operations from the menu\n" +
                                       "0. Get Balance\n" +
                                       "1. Deposit\n" +
                                       "2. Withdraw";

    private static DecimalFormat money = new DecimalFormat(".00");

    public ATM(String host, int port) {

       try {
            bank = new Proxy(host, port);
        } catch (Exception e) {
            System.err.println("Could not connect to bank server.");
            System.exit(1);
        }

        setTitle("Welcome to ACME Bank");
        setSize(360,300);
        setLocation(150,250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
 
        Container contentPane = getContentPane();
        setLayout(new GridLayout(2, 0));
        
        JPanel messagePanel = new JPanel(new BorderLayout());

        // The information area
        output = new JTextArea();
        output.append("\nWelcome to ACME Banking \n");
        output.append("\nPlease enter your account number.\n\n");
        
        output.setForeground(Color.WHITE);
        output.setBackground(Color.BLACK);

        messagePanel.add(output, BorderLayout.CENTER);

        input = new JTextArea();

        input.setForeground(Color.RED);
        input.setBackground(Color.BLACK);

        messagePanel.add(input, BorderLayout.SOUTH);

        contentPane.add(messagePanel);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JPanel numberPanel = new JPanel(new GridLayout(2, 5));

        JButton[] numbers = new JButton[10];

        // Create the numeric pad 
        for(int i = 0; i < 10; i++) {
            numbers[i] = new JButton(Integer.toString(i));
            numbers[i].setBackground(Color.BLUE);
            numbers[i].setForeground(Color.BLACK);
            numbers[i].addActionListener(this);
            numberPanel.add(numbers[i]);
        }

        JPanel yesNoPanel = new JPanel(new GridLayout(0, 2));

        JButton yesButton = new JButton("OK");
        yesButton.setBackground(Color.GREEN);
        yesButton.addActionListener(this);
        
        JButton noButton = new JButton("Cancel");
        noButton.setBackground(Color.GREEN);
        noButton.addActionListener(this);

        yesNoPanel.add(yesButton);
        yesNoPanel.add(noButton);

        buttonPanel.add(numberPanel, BorderLayout.CENTER);
        buttonPanel.add(yesNoPanel, BorderLayout.SOUTH);

        contentPane.add(buttonPanel);

        
        try {

            //bank = new Proxy(host, port);
        
        } catch (Exception e) {
            
            System.err.println(e.getMessage());
        }

        setVisible(true);

    }

    public static void main(String[] args) {

        try {
            
            if (args.length != 2) {
                throw new Exception("Invalid arguments: java ATM bankserver port");
            }

            String host = args[0];
        
            int port;

            try {
                port = Integer.parseInt(args[1]);
            } catch (Exception e) {
                throw new Exception("Illegal port number.");
            }

            ATM atm = new ATM(host, port);        
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent event) {

        try {


            // The session is not verified yet. The user needs to enter
            // his account number.
            if (! verified) {
                    
                // Account numbers are 6 digits long
                if ( accountNumber.length() < 6) {

                    // Invalid account number length
                    if (event.getActionCommand().equals("OK")) {

                        // If the account number has not begun 
                        // to be entered, display instructions
                        if (accountNumber.length() == 0) {

                            // Show the welcome message
                            output.setText(WELCOME_MSG);

                        } else {

                            output.setText("Invalid account number!\n " +
                                           "Account numbers are six digits " +
                                           "long. Please try again.");
                            input.setText("");
                            accountNumber = "";
                        }

                    } else if (event.getActionCommand().equals("Cancel")) {
                        
                        output.setText(WELCOME_MSG);

                    } else {
                        
                        // Show the welcome message
                        output.setText(WELCOME_MSG);

                        // Grab the next number of the account number
                        accountNumber += event.getActionCommand();

                        // Show it on the screen
                        input.setText(accountNumber + "\n");

                    }

                // The user tries to validate his account 
                } else {

                    int account = Integer.parseInt(accountNumber);

                    input.setText("");
                    
                    try {

                        bank.validate(account);

                        // The account is now verified 
                        // Show the user a list of operations
                        // that he can perform on his account
                        output.setText(MENU);

                        verified = true;

                    } catch (ValidationException e) {
                        
                        // The user specified an account that
                        // wasn't in the system
                        output.setText("Invalid account number!\n" + 
                                       "Please try again.");
                        
                        input.setText("");
                        accountNumber = "";

                    } 

                }
            
            // The account has been verified. The user may now
            // perform operations on his account.
            } else {
           
                if (event.getActionCommand().equals("OK")) {

                    if (tempOperation == Operation.GET_BALANCE) {

                        try {

                            tempOperation = Operation.NONE;
                            operation = Operation.NONE;

                            input.setText("");

                            int balance = bank.getBalance();

                            // Convert to dollars
                            double inDollars = balance / 100.0;

                            // Print out the user's current account balance
                            output.setText("Your current account balance is " +
                                           "$" +  money.format(inDollars) ); 

                        } catch (ValidationException e) {
                            
                            // This should never happen
                            output.setText(e.getMessage());
                        }
                        
                    } else if (tempOperation == Operation.DEPOSIT) {

                        tempOperation = Operation.NONE;
                        operation = Operation.DEPOSIT;

                        output.setText("Please enter the amount of money " 
                                       + " you  wish to deposit");

                        input.setText("");

                    } else if (tempOperation == Operation.WITHDRAW) {

                        tempOperation = Operation.NONE;
                        operation = Operation.WITHDRAW;

                        output.setText("Please enter the amount of money " +
                                       " you  wish to withdraw");

                        input.setText("");
                    
                    } else if (operation == Operation.DEPOSIT) {

                        try {

                            int numAmount = Integer.parseInt(amount);

                            amount = "";

                            input.setText("");

                            operation = Operation.NONE;

                            bank.deposit(numAmount);

                            double inDollars = numAmount / 100.0;

                            output.setText("$" + money.format(inDollars) 
                                           + " has " +
                                           " successfully been deposited to " +
                                           " your account");

                        } catch (ValidationException e) {
                            
                            // This should never happen
                            output.setText(e.getMessage());

                        } catch (NumberFormatException e) {

                            output.setText("Please enter a valid number!");

                        }

                    } else if (operation == Operation.WITHDRAW) {

                        try {

                            int numAmount = Integer.parseInt(amount);
                            
                            amount = "";

                            input.setText("");

                            operation = Operation.NONE;
                        
                            bank.withdraw(numAmount);

                            double inDollars = numAmount / 100.0;

                            output.setText("$ " + money.format(inDollars) + 
                                           " has " +
                                           "successfully been withdrawn " +
                                           "from your account");

                            
                        } catch (ValidationException e) {
                            
                            // This should never happen
                            output.setText(e.getMessage());

                        } catch (NumberFormatException e) {

                            output.setText("Please enter a valid number");

                        }


                    } else if (operation == Operation.NONE) {

                        output.setText(MENU);

                    }

                        


                } else if (event.getActionCommand().equals("Cancel")) {

                    // The user wants to cancel his session
                    // clear his personal data
                    if (operation == Operation.NONE) {
                        
                        verified = false;
                        accountNumber = "";
                        input.setText("");

                        // Show the welcome message
                        output.setText(WELCOME_MSG);

                    } else {

                        input.setText("");
                        amount = "";

                        output.setText(MENU);

                        operation = Operation.NONE;
                    }

                // The user has pressed a key on the number pad
                } else {

                    if (operation == Operation.NONE) {

                        if (event.getActionCommand().equals("0")) {
                            tempOperation = Operation.GET_BALANCE;
                            input.setText("Get balance.");
                        } else if (event.getActionCommand().equals("1")) {
                            tempOperation = Operation.DEPOSIT;
                            input.setText("Deposit.");
                        } else if (event.getActionCommand().equals("2")) {
                            tempOperation = Operation.WITHDRAW;
                            input.setText("Withdraw.");
                        }

                    } else if (operation == Operation.DEPOSIT) {
                        
                        // Amount is in cents

                        amount += event.getActionCommand();
                        
                        // Convert to dollars
                        double inDollars = Integer.parseInt(amount) / 100.0;

                        input.setText("$ " + money.format(inDollars));

                    } else if (operation == Operation.WITHDRAW) {

                        amount += event.getActionCommand();

                        // Convert to dollars
                        double inDollars = Integer.parseInt(amount) / 100.0;

                        input.setText("$ " + money.format(inDollars));

                    }

                }
                    
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

} // ATM
