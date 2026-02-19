package Enterprise.Banking.Backend.dto;

public class AccountResponse {

    private String accountNumber;
    private Double balance;

    public AccountResponse() {}

    public AccountResponse(String accountNumber, Double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }

    public Double getBalance() { return balance; }
}
