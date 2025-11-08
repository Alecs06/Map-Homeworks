package model.exception;

public class DivideByZeroException extends ArithmeticException   {
    public DivideByZeroException() {
        super("Division by zero");
    }
}
