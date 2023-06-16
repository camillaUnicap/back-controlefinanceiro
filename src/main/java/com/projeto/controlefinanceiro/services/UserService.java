package com.projeto.controlefinanceiro.services;

import java.util.List;
import java.util.Optional;

import com.projeto.controlefinanceiro.repositories.TransactionRepository;
import com.projeto.controlefinanceiro.repositories.UserRepository;
import com.projeto.controlefinanceiro.services.exceptions.ObjectNotFoundException;
import javassist.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.controlefinanceiro.models.Transaction;
import com.projeto.controlefinanceiro.models.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	public List<User> findAll() {
		return userRepo.findAll();
	}

	public User findById(Integer id) {
		return userRepo.findById(id).orElseThrow(
				() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + User.class.getName()));
	}

	public User findByEmail(String email) {
		User usr = userRepo.findByEmail(email);
		if (usr == null) {
			throw new ObjectNotFoundException("Object not found! Email: " + email + ", Type: " + User.class.getName());
		}
		return usr;
	}

	public User insert(User user) {
		user.setPassword(hashPassword(user.getPassword()));
		return userRepo.save(user);
	}

	public User update(User user, Integer id) {
		User usr = findById(id);
		updateData(user, usr);
		return userRepo.save(usr);
	}

	public void delete(Integer id) {
		userRepo.delete(findById(id));
	}

	public List<Transaction> findUserTransactions(Integer id) {
		return transactionRepo.findTransactions(id);
	}

	public Transaction insertTransaction(Transaction transaction, Integer id) {
		User usr = findById(id);

		// update user's data and save
		addOrWithdraw(usr, transaction.getValue());
		insert(usr);

		transaction.setUser(usr);
		return transactionRepo.save(transaction);
	}

	public void deleteTransaction(Integer userId, Integer transactionId) throws NotFoundException {
		User user = findById(userId);
		Transaction transaction = findTransactionById(user, transactionId);

		// Atualize o valor do balance
		Double transactionValue = transaction.getValue();
		removeOrWithdraw(user, transactionValue);
		userRepo.save(user);

		// Exclua a transação
		transactionRepo.delete(transaction);
	}

	public Transaction updateTransaction(Transaction transaction, Integer userId, Integer transactionId)
			throws NotFoundException {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
		Transaction existingTransaction = findTransactionById(user, transactionId);

		updateTransactionData(transaction, existingTransaction);

		return transactionRepo.save(existingTransaction);
	}

	public Transaction findTransactionById(User user, Integer transactionId) throws NotFoundException {
		Optional<Transaction> optionalTransaction = user.getTransactions().stream()
				.filter(transaction -> transaction.getId().equals(transactionId))
				.findFirst();

		return optionalTransaction
				.orElseThrow(() -> new NotFoundException("Transaction not found with ID: " + transactionId));
	}

	private void updateTransactionData(Transaction newTransaction, Transaction existingTransaction) {
		existingTransaction.setName(newTransaction.getName());
		existingTransaction.setValue(newTransaction.getValue());
	}

	// copies a user's data to another, the first argument is the one to be copied,
	// the second is the receiver
	private void updateData(User user, User usr) {
		usr.setEmail(user.getEmail());
		usr.setPassword(user.getPassword());
		usr.setBalance(user.getBalance());
		usr.setRevenue(user.getRevenue());
		usr.setExpenses(user.getExpenses());
	}

	// add or withdraw value from a user
	// if value is positive it adds to balance and revenue
	// if value is negative it removes from the balance and adds to expenses
	private void addOrWithdraw(User user, Double value) {
		user.setBalance(user.getBalance() + value);
	}

	private void removeOrWithdraw(User user, Double value) {
		user.setBalance(user.getBalance() - value);
	}

	// find and delete a transaction in a list of transactions
	private void findTransactionAndDelete(List<Transaction> transactions, Integer id) {
		for (Transaction tr : transactions) {
			if (tr.getId() == id) {
				transactionRepo.deleteById(id);
				break;
			}
		}
	}

	// encrypts a user's password using BCrypt
	private String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}
}
