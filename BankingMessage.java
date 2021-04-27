

import java.io.Serializable;

/**
 * 
 *
 * @author jeh
 *
 */
public class BankingMessage implements Serializable {

    /**
     * ID used to check if class versions on sending (writing) side and
     * receiving (reading) side are the same
     */
    private static final long serialVersionUID = 392096742206099573L;

    /**
     * All possible message types
     */
    public enum Type {
        VALIDATE, INQUIRE_QUERY, DEPOSIT_QUERY, WITHDRAW_QUERY,
        SUCCESS_RESPONSE, FAILURE_RESPONSE
    };
    
    /**
     * Storage for the message type tag
     */
    private final Type operation;
    
    /**
     * Storage for the account involved in this message
     */
    private int account;
    
    /**
     * Storage for the amount of money involved in this transaction,
     * if needed. The amount is assumed to be in cents.
     */
    private int amount;
    
    /**
     * Create a message and fill in all its information.
     * 
     * @param operation the message type
     * @param account the account involved
     * @param amount how much money is involved, in cents
     */
    public BankingMessage( Type operation, int account, int amount ) {
        this.operation = operation;
        this.account = account;
        this.amount = amount;
    }

    /**
     * Create a message and fill in all its information.
     * This version is used by messages that do not
     * require an amount.
     * 
     * @param operation the message type
     * @param account the account involved
     */
    public BankingMessage( Type operation, int account ) {
        this.operation = operation;
        this.account = account;
    }

    /**
     * @return this message's type tag.
     */
    public Type getOperation() {
        return operation;
    }

    /**
     * @return this message's account number.
     */
    public int getAccount() {
        return account;
    }

    /**
     * Change the account in this message.
     * @param account the new account
     */
    public void setAccount(int account) {
        this.account = account;
    }

    /**
     * @return this message's currency amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Change the currency amount in this message.
     * @param amount the new amount in cents
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

}
