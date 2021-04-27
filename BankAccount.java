/**
 * BankAccount.java
 *
 * Version:
 *      $Id: BankAccount.java,v 1.3 2006/04/18 01:38:13 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *      $Log: BankAccount.java,v $
 *      Revision 1.3  2006/04/18 01:38:13  jeg3600
 *      Completed toString method for testing
 *
 *      Revision 1.2  2006/04/18 01:37:57  jeg3600
 *      Added all methods. Fixed bugs
 *
 *      Revision 1.1  2006/04/18 01:37:36  jeg3600
 *      Initial revision
 *
 */

/**
 * Stores the data for a bank account
 *
 * @author John Garnham
 */


public class BankAccount {

    /**
     * The amount of money in the account
     * in cents
     */
    private int balance;
    
    /**
     * The account number related to this
     * bank account
     */
    private int account;

    /**
     * Describe constructor here.
     *
     * @param newAmmount a value of type 'int'
     */
    public BankAccount(int accountNumber, int newAmmount) {
        
        account = accountNumber;
        balance = newAmmount;

    }


    /**
     * Get the amount of money currently in the account
     *
     * @return The current balance of the account in cents
     */
    public int getBalance() {

        return balance;

    }

    /**
     * Get the account number related to this bank account
     *
     * @return The account number related to this account
     */
    public int getAccount() {

        return account;

    }

    /**
     * Withdraw money from the account
     *
     * @param amount The amount to withdraw
     * from the account in cents
     */
    public void withdraw(int amount) throws ValidationException {

        if (amount < balance) {
            balance -= amount;
        } else {
            throw new ValidationException("Insufficient funds.");
        }

    }

    /**
     * Deposit money to the account
     *
     * @param amount The amount to deposit to 
     * the account in cents
     */
    public void deposit(int amount) {

        balance += amount;

    }

    /**
     * Returns a string representation of the bank account
     *
     * @return A string representation of the bank account
     */
    public String toString() {

        return "Account #" + account + " Balance: " + 
            balance;
       
    }

} // BankAccount
