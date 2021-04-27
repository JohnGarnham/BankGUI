/**
 * BankList.java
 *
 * Version:
 *     $Id: BankList.java,v 1.1 2006/04/18 01:43:12 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *     $Log: BankList.java,v $
 *     Revision 1.1  2006/04/18 01:43:12  jeg3600
 *     Initial revision
 *
 */

/**
 * A sorted resizable array
 *
 * @author John Garnham
 */

 
public class BankList {

    // An array to store bank account
    private BankAccount[] accounts;

    // The current number of accounts
    private int numberOfAccounts = 0;

    /**
     * Constructor for a bank list
     *
     * @param size The size of the bank list
     */
    public BankList() {
        // The array starts off at a size of 1
        // and grows as neccessary
        accounts = new BankAccount[1];

    }

    /**
     * Add a new account to the array
     *
     * @param newAccount The new account
     */
    public void add(BankAccount newAccount) throws Exception {

        numberOfAccounts++;

        if (numberOfAccounts == 1) {
            System.out.println("First account made");
            // The first account to be added
            accounts[0] = newAccount;
        
        } else {


            int index = 0;
            // Java is bitchy about excpetions
            try {
               index = Math.abs(BinarySearch(newAccount.getAccount()));
            } catch (Exception e) {
                System.err.println("Duplicant account found");
            }

            System.out.println("Index is: " + index);
            System.out.println("Array length is : " + 
                               accounts.length);
            
            // Loop from the index to the end
            for(int i = accounts.length - 1; i >= index; i--) {

                System.out.println("Moving " + i + " to " + (i + 1));
                
                // Shift every account up one space
                put(i + 1, accounts[i]); 

                
            }

            System.out.println("Inserting new account");
            put(index, newAccount);

        }

    }

    // 1 2 3 4 5 6 7 8 9 0

               

    /**
     * Get the account at the specific index
     *
     * @param index The index 
     * @return The bank account at that index
     */
    private BankAccount get(int index) {
        return accounts[index];
    }

    /**
     * Put an account into the list at a specific index
     *
     * @param index Where to store the account
     * @param newAccount The account to store
     */
    private void put(int index, BankAccount newAccount) {
        
        // If the index is out of bounds
        // then resize the array
        if (index >= accounts.length) {

            // Double the array size until the index fits
            int newSize = accounts.length + 1;

            System.out.println("Resizing array to size " + newSize);

            // Make a new array of size newSize
            BankAccount[] newAccounts = new BankAccount[newSize];

            // Copy the old array into the new array
            System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
            accounts = newAccounts;

        }

        accounts[index] = newAccount;

    }

    /**
     * Implements a binary search through the array
     *
     * @param account The account number to search for
     * @return The index of the bank account
     */
    private int BinarySearch(int account) throws Exception {
        
        boolean javasucks = false;

        int low = 0;
        int high = accounts.length - 1;
        int mid = (low + high) / 2;

        while(low < high) {
            mid = (low + high) / 2;
            if (account > accounts[mid].getAccount()) {
                low = mid + 1;
            } else if (account < accounts[mid].getAccount()) {
                javasucks = true;
                high = mid;
            } else {
                // Should not happen
                return mid;
            }
        }

        if (javasucks) {
            return -low;
        } else {
            return -(low + 1); 
        }

    }

    /**
     * Describe 'getBankAccount' method here
     *
     * @param account a value of type 'int'
     * @return a value of type 'BankAccount'
     */
    public BankAccount getBankAccount(int account) 
        throws Exception {

        int index = BinarySearch(account);

        if (index < 0) {
            System.out.println(account + " not found");
            throw new ValidationException("Invalid account number!");
        } else {
            return accounts[index];
        }

    }


    public String toString() {

        String returnString = "";

        for(int i = 0; i < accounts.length; i++) {
            // System.out.println("In tostring " + i);
            returnString += "\n" + i + ":" + accounts[i].toString();
        }

        return returnString;

    }
            
} // BankList
