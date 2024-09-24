package com.example.bancopicpay.services;

import com.example.bancopicpay.models.Cliente;
import com.example.bancopicpay.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
            return  clienteRepository.save(cliente);
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findById(cpf).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    @Transactional
    public Cliente excluirCliente(String cpf) {
        Cliente cliente = buscarPorCpf(cpf);
        clienteRepository.deleteClienteByCpf(cpf);
        return cliente;
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeLikeIgnoreCase(nome);
    }
}
