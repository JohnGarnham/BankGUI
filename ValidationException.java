/**
 * ValidationException.java
 *
 * Version:
 *      $Id: ValidationException.java,v 1.1 2006/03/29 02:05:48 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *      $Log: ValidationException.java,v $
 *      Revision 1.1  2006/03/29 02:05:48  jeg3600
 *      Initial revision
 *
 */

/**
 * An exception which is raised when the bank is
 * unable to fulfill a request (bad account number,
 * insufficient funds, etc.) 
 *
 * @author John Garnham
 */

public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

} // ValidationException
