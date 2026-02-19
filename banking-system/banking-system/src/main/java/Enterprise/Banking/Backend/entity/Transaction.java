package Enterprise.Banking.Backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // DEPOSIT / WITHDRAW

    private Double amount;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime transactionTime;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Constructor
    public Transaction() {
        this.transactionTime = LocalDateTime.now();
    }

    public Transaction(String type, Double amount, Account account) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.transactionTime = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getTransactionTime() { return transactionTime; }

    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }
}
