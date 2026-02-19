package Enterprise.Banking.Backend.dto;


public class TransactionRequest {

    private String accountNumber;
    private Double amount;

    public TransactionRequest() {}

    public String getAccountNumber() { return accountNumber; }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
