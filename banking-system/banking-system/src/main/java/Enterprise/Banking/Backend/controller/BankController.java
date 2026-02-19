package Enterprise.Banking.Backend.controller;

import Enterprise.Banking.Backend.dto.LoginRequest;
import Enterprise.Banking.Backend.dto.TransactionRequest;
import Enterprise.Banking.Backend.dto.TransferRequest;
import Enterprise.Banking.Backend.entity.*;
import Enterprise.Banking.Backend.service.BankService;
import Enterprise.Banking.Backend.util.JwtUtil;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public BankController(BankService bankService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.bankService = bankService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = bankService.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(token);
    }

    // ================= CREATE USER =================

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(bankService.createUser(user));
    }

    // ================= CREATE ACCOUNT =================

    @PostMapping("/accounts/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(bankService.createAccount(userId));
    }

    // ================= GET ACCOUNT DETAILS =================

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankService.getAccount(accountNumber));
    }

    // ================= GET TRANSACTION HISTORY =================

    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                bankService.getTransactionHistory(accountNumber, page, size)
        );
    }

    // ================= DEPOSIT =================

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                bankService.deposit(
                        request.getAccountNumber(),
                        request.getAmount()
                )
        );
    }

    // ================= WITHDRAW =================

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                bankService.withdraw(
                        request.getAccountNumber(),
                        request.getAmount()
                )
        );
    }

    // ================= TRANSFER =================

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(
                bankService.transfer(
                        request.getFromAccount(),
                        request.getToAccount(),
                        request.getAmount()
                )
        );
    }
}
