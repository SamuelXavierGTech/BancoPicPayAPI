package com.example.bancopicpay.repository;

import com.example.bancopicpay.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, String> {

    Boolean deleteContaByNumeroConta(String numeroConta);

    List<Conta> findBySaldo(double saldo);
}
