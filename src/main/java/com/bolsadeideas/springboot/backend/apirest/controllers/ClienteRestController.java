package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.ClienteServiceImpl;
//import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private ClienteServiceImpl clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/{id}")

	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente = null;
		Map<String,Object> response = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		}catch(DataAccessException e){
			response.put("mensaje", "Error en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
	}
	//@ResponseStatus(HttpStatus.OK)//200
	/*public Cliente show(@PathVariable Long id) {
		return clienteService.findById(id);
	}*/
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@RequestBody Cliente cliente){
		Cliente clienteNew = null;
		Map<String,Object> response = new HashMap<>();
		try {
			clienteNew = clienteService.save(cliente);
		}catch(DataAccessException e){
			response.put("mensaje", "Error en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mesaje","El clientes ha sido creado con éxito");
		response.put("cliente",clienteNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	//@ResponseStatus(HttpStatus.CREATED)//201
	/*public Cliente create(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}*/
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente,@PathVariable Long id){
		Cliente clienteActual = null;
		Cliente clienteUpdated = null;
		Map<String,Object> response = new HashMap<>();
		try{
			clienteActual = clienteService.findById(id);
		}catch(DataAccessException e){
			response.put("mensaje","Error en la BD");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(clienteActual == null) {
			response.put("mensaje","Error, el cliente con ID: ".concat(id.toString()).concat("no existe en la BD"));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try{
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			
			clienteUpdated = clienteService.save(clienteActual);
		}catch(DataAccessException e){
			response.put("mensaje","Error al actualizar cliente en la BD");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El cliente ha sido actulizado con éxito");
		response.put("cliente", clienteUpdated);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
		
	}
	//@ResponseStatus(HttpStatus.CREATED)//201
	/*public Cliente update(@RequestBody Cliente cliente,@PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);
		
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setEmail(cliente.getEmail());
		
		return clienteService.save(clienteActual);
	}*/
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Cliente clienteDeleted = null;
		Map<String,Object> response = new HashMap<>();
		try{
			clienteDeleted = clienteService.findById(id);
		}catch(DataAccessException e){
			response.put("mensaje","Error en la BD");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(clienteDeleted == null) {
			response.put("mensaje","Error, el cliente con ID: ".concat(id.toString()).concat("no existe en la BD"));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try{
			clienteService.delete(id);
		}catch(DataAccessException e){
			response.put("mensaje","Error al intentar eliminar el cliente con ID: ".concat(id.toString()));
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El cliente ha sido eliminado con éxito");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	//@ResponseStatus(HttpStatus.NO_CONTENT)//204
	/*public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}*/
	
}
