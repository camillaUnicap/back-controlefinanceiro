package com.projeto.controlefinanceiro.resources;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.controlefinanceiro.models.Transaction;
import com.projeto.controlefinanceiro.models.User;
import com.projeto.controlefinanceiro.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public List<User> findAll() {
		return userService.findAll();
	}

	@CrossOrigin
	@GetMapping(value = "/{id}")
	public User findById(@PathVariable Integer id) {
		return userService.findById(id);
	}
	
	@CrossOrigin
	@GetMapping(value= "/email")
	public User findByEmail(@RequestParam(value="value") String email) {
		return userService.findByEmail(email);
	}

	@CrossOrigin
	@PostMapping(value = "/create")
	public User insert(@RequestBody User user) {
		return userService.insert(user);
	}

	@PutMapping(value = "/{id}/edite")
	public User update(@RequestBody User user, @PathVariable Integer id) {
		return userService.update(user, id);
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/{id}/remove")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		userService.delete(id);
	}

	@CrossOrigin
	@GetMapping(value = "/{id}/transactions")
	public List<Transaction> findUserTransactions(@PathVariable Integer id) {
		return userService.findUserTransactions(id);
	}

	@GetMapping(value = "/{userId}/transactions/{transactionId}")
	public Transaction findTransactionById(@PathVariable Integer userId, @PathVariable Integer transactionId) throws NotFoundException {
		User user = userService.findById(userId);
		return userService.findTransactionById(user, transactionId);
	}
	
	@CrossOrigin
	@PostMapping(value = "/{id}/transaction")
	public Transaction insertTransaction(@RequestBody Transaction transaction, @PathVariable Integer id) {
		return userService.insertTransaction(transaction, id);
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/{id}/transaction/{transactionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTransaction(@PathVariable Integer id, @PathVariable Integer transactionId) throws NotFoundException {
		userService.deleteTransaction(id, transactionId);
	}

	@PutMapping("/{userId}/transaction/{transactionId}")
	public Transaction updateTransaction(@RequestBody Transaction transaction, @PathVariable Integer userId, @PathVariable Integer transactionId) throws NotFoundException {
		return userService.updateTransaction(transaction, userId, transactionId);
	}
}