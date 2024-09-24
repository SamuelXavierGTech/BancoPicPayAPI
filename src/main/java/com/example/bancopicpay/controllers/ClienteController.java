package com.example.bancopicpay.controllers;
import com.example.bancopicpay.models.Cliente;
import com.example.bancopicpay.services.ClienteService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final Validator validator;

    @Autowired
    public ClienteController(ClienteService clienteService, Validator validator) {
        this.clienteService = clienteService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    public List<Cliente> buscarTodosProdutos() {
        return clienteService.buscarTodosClientes();
    }

    @PutMapping("/inserir")
    public ResponseEntity<String> inserir(@RequestBody Cliente cliente) {
        try {
            if(cliente != null) {
                clienteService.salvarCliente(cliente);
                return ResponseEntity.ok("Cliente inserido com sucesso!");
            } else {
                throw new DataIntegrityViolationException("Valor nulo!");
            }
        } catch (DataIntegrityViolationException dive){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluir(@RequestParam String cpf) {
        try {
            if(cpf != null) {
                clienteService.excluirCliente(cpf);
                return ResponseEntity.ok("Cliente excluido com sucesso!");
            } else {
                throw new DataIntegrityViolationException("Valor nulo!");
            }
        } catch (DataIntegrityViolationException dive){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/atualizar/{cpf}")
    public ResponseEntity<String> atualizarCliente(@Valid @PathVariable String cpf, @RequestBody Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteService.buscarPorCpf(cpf);
        if (clienteExistente != null) {
            Cliente cliente = clienteExistente;
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            clienteService.salvarCliente(cliente);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/atualizarParcial/{cpf}")
    public ResponseEntity<?> atualizarParcial(@PathVariable String cpf, @RequestBody Map<String, Object> update) {

        try {
            Cliente cliente = clienteService.buscarPorCpf(cpf);

            if (update.containsKey("nome")) {
                cliente.setNome((String) update.get("nome"));
            }
            if (update.containsKey("email")) {
                cliente.setEmail((String) update.get("email"));
            }
            if (update.containsKey("telefone")) {
                cliente.setTelefone((String) update.get("telefone"));
            }

            DataBinder binder = new DataBinder(cliente);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();

            if (result.hasErrors()) {
                Map erros = validarCliente(result);
                return ResponseEntity.badRequest().body(erros);
            }
            Cliente produtoSalvo = clienteService.salvarCliente(cliente);
            return ResponseEntity.ok(produtoSalvo);
        } catch (RuntimeException r){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        result.getFieldErrors().forEach(fieldError ->
                errorMsg.append(fieldError.getDefaultMessage()).append("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    public Map<String, String> validarCliente(BindingResult resultado) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return erros;
    }
}
