package com.example.bancopicpay.controllers;
import com.example.bancopicpay.models.Cliente;
import com.example.bancopicpay.models.Conta;
import com.example.bancopicpay.services.ClienteService;
import com.example.bancopicpay.services.ContaService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;
    private final Validator validator;

    @Autowired
    public ContaController(ContaService contaService, Validator validator) {
        this.contaService = contaService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    public List<Conta> buscarTodosProdutos() {
        return contaService.buscarTodasContas();
    }

    @PutMapping("/inserir")
    public ResponseEntity<String> atualizar(@RequestBody Conta conta) {
        try {
            if(conta != null) {
                contaService.salvarConta(conta);
                return ResponseEntity.ok("Conta inserido com sucesso!");
            } else {
                throw new DataIntegrityViolationException("Valor nulo!");
            }
        } catch (DataIntegrityViolationException dive){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/excluir/{numeroConta}")
    public ResponseEntity<String> excluir(@PathVariable String numeroConta) {
        try {
            if(numeroConta != null) {
                if(contaService.excluirConta(numeroConta)) {
                    return ResponseEntity.ok("Conta excluida com sucesso!");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                throw new DataIntegrityViolationException("Valor nulo!");
            }
        } catch (DataIntegrityViolationException dive){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/atualizar/{numeroConta}")
    public ResponseEntity<String> atualizarCliente(@Valid @PathVariable String numeroConta, @RequestBody Conta contaAtualizada) {
        Optional<Conta> contaExistente = contaService.buscarPorNumero(numeroConta);
        Conta conta = contaExistente.get();
        conta.setSaldo(contaAtualizada.getSaldo());
        conta.setLimiteEspecial(contaAtualizada.getLimiteEspecial());
        conta.setCpfCliente(contaAtualizada.getCpfCliente());
        contaService.salvarConta(conta);
        return ResponseEntity.ok("Conta atualizado com sucesso!");
    }

    @PatchMapping("/atualizarParcial/{numeroConta}")
    public ResponseEntity<?> atualizarParcial(@PathVariable String numeroConta, @RequestBody Map<String, Object> update) {

        try {
            Optional<Conta> conta = contaService.buscarPorNumero(numeroConta);

            if (update.containsKey("saldo")) {
                conta.get().setSaldo((double) update.get("saldo"));
            }
            if (update.containsKey("limite_especial")) {
                conta.get().setLimiteEspecial((double) update.get("limite_especial"));
            }

            DataBinder binder = new DataBinder(conta);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();

            if (result.hasErrors()) {
                Map erros = validarConta(result);
                return ResponseEntity.badRequest().body(erros);
            }
            Conta contaSalvo = contaService.salvarConta(conta.get());
            return ResponseEntity.ok(contaSalvo);
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

    public Map<String, String> validarConta(BindingResult resultado) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return erros;
    }

}
