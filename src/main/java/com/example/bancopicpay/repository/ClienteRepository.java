package com.example.bancopicpay.repository;

import com.example.bancopicpay.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Cliente deleteClienteByCpf(String cpf);
    List<Cliente> findByNomeLikeIgnoreCase(String nome);
}
