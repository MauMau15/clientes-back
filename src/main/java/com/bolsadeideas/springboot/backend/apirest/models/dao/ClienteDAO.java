package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
/*
 * Defining our DAO wich is gonna be filled up with our query in the rest controller
 * */
public interface ClienteDAO extends CrudRepository<Cliente, Long>{
	
}
