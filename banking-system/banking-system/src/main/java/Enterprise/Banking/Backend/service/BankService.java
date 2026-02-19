package Enterprise.Banking.Backend.service;

import Enterprise.Banking.Backend.entity.*;
import Enterprise.Banking.Backend.exception.*;
import Enterprise.Banking.Backend.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class BankService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public BankService(UserRepository userRepository,
                       AccountRepository accountRepository,
                       TransactionRepository transactionRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // USER OPERATIONS
    // =====================================================

    @Transactional
    public User createUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        // 🔥 ENCODE PASSWORD (FIXES INVALID CREDENTIALS)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));
    }

    // =====================================================
    // ACCOUNT OPERATIONS
    // =====================================================

    @Transactional
    public Account createAccount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        String accountNumber = generateUniqueAccountNumber();

        Account account = new Account(accountNumber, user);
        account.setBalance(0.0);

        return accountRepository.save(account);
    }

    public Account getAccount(String accountNumber) {
        return findAccount(accountNumber);
    }

    // =====================================================
    // DEPOSIT
    // =====================================================

    @Transactional
    public Account deposit(String accountNumber, Double amount) {

        validateAmount(amount);

        Account account = findAccount(accountNumber);

        account.setBalance(account.getBalance() + amount);

        transactionRepository.save(
                new Transaction("DEPOSIT", amount, account)
        );

        return account;
    }

    // =====================================================
    // WITHDRAW
    // =====================================================

    @Transactional
    public Account withdraw(String accountNumber, Double amount) {

        validateAmount(amount);

        Account account = findAccount(accountNumber);

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        transactionRepository.save(
                new Transaction("WITHDRAW", amount, account)
        );

        return account;
    }

    // =====================================================
    // TRANSFER
    // =====================================================

    @Transactional
    public String transfer(String fromAccount,
                           String toAccount,
                           Double amount) {

        validateAmount(amount);

        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account sender = findAccount(fromAccount);
        Account receiver = findAccount(toAccount);

        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Debit sender
        sender.setBalance(sender.getBalance() - amount);

        // Credit receiver
        receiver.setBalance(receiver.getBalance() + amount);

        // Save transactions
        transactionRepository.save(
                new Transaction("TRANSFER_OUT", amount, sender)
        );

        transactionRepository.save(
                new Transaction("TRANSFER_IN", amount, receiver)
        );

        return "Transfer successful";
    }

    // =====================================================
    // TRANSACTION HISTORY (READ ONLY)
    // =====================================================

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionHistory(
            String accountNumber,
            int page,
            int size) {

        Account account = findAccount(accountNumber);

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByAccountOrderByTransactionTimeDesc(account, pageable);
    }

    // =====================================================
    // PRIVATE HELPER METHODS
    // =====================================================

    private Account findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with number: " + accountNumber));
    }

    private void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {
            accountNumber = "ACC" + (10000 + new Random().nextInt(90000));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
