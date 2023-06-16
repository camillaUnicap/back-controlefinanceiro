package com.projeto.controlefinanceiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.controlefinanceiro.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByEmail(String email);
	public User findById(int id);
}
