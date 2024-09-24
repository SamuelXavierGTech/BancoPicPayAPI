package com.example.bancopicpay.services;

import com.example.bancopicpay.models.Conta;
import com.example.bancopicpay.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {
    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public List<Conta> buscarTodasContas() {
        return contaRepository.findAll();
    }

    public Conta salvarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Optional<Conta> buscarPorNumero(String numeroConta) {
        return contaRepository.findById(numeroConta);
    }

    public boolean excluirConta (String numeroConta) {
        Optional<Conta> conta = buscarPorNumero(numeroConta);
        if (conta.isPresent()) {
            contaRepository.deleteContaByNumeroConta(numeroConta);
            return true; // Indicar exclusão bem-sucedida
        } else {
            return false; // Indicar conta não encontrada
        }
    }

    public List<Conta> buscarPorSaldo (double saldo) {
        return contaRepository.findBySaldo(saldo);
    }


}
