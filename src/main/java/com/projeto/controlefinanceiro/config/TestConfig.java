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

		User u1 = new User(null, "camilla@gmail.com", "123", 205.00, 1500.00, 300.00);
		User u2 = new User(null, "tacithiana@gmail.com", "tacithiana", 200.00, 1100.00, 900.00);
		User u3 = new User(null, "yasmim@gmail.com", "yasmim", 650.00, 1650.00, 1000.00);

		Transaction t1 = new Transaction(null, "Camisa", 50.00);
		Transaction t2 = new Transaction(null, "Bolsa", 100.00);
		Transaction t3 = new Transaction(null, "Garrafa termica", 55.00);

		t1.setUser(u1);
		t2.setUser(u1);
		t3.setUser(u2);

		u1.setTransactions(Arrays.asList(t1, t2));
		u2.setTransactions(Arrays.asList(t3));

		userService.insert(u1);
		userService.insert(u2);
		userService.insert(u3);
		transactionRepo.saveAll(Arrays.asList(t1, t2, t3));

		return true;
	}
}