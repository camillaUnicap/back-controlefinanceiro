package com.projeto.controlefinanceiro.config;

import java.text.ParseException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.projeto.controlefinanceiro.models.Transaction;
import com.projeto.controlefinanceiro.models.User;
import com.projeto.controlefinanceiro.repositories.TransactionRepository;
import com.projeto.controlefinanceiro.services.UserService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionRepository transactionRepo;

	@Bean
	public boolean instatiateDatabase() throws ParseException {

		User u1 = new User(null, "camilla@gmail.com", "123", 350.00, 0.00, 0.00);
		User u2 = new User(null, "tacithiana@gmail.com", "tacithiana", 90.00, 0.00, 0.00);
		User u3 = new User(null, "yasmim@gmail.com", "yasmim", 0.00, 0.00, 0.00);

		Transaction t1 = new Transaction(null, "Transporte", 150.00);
		Transaction t2 = new Transaction(null, "Comida", 200.00);
		Transaction t3 = new Transaction(null, "Internet", 90.00);

		t1.setUser(u1);
		t2.setUser(u1);
		t3.setUser(u2);

		u1.setTransactions(Arrays.asList(t1, t2));
		u2.setTransactions(Arrays.asList(t3));

		userService.insert(u1);
		userService.insert(u2);
		userService.insert(u3);s
		transactionRepo.saveAll(Arrays.asList(t1, t2, t3));

		return true;
	}
}