package com.example.bancopicpay.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Cliente {

    @Id
    @NotNull(message = "O CPF não pode ser nulo")
    @CPF(message = "O cpf é invalido" )
    private String cpf;

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 250, message = "O nome deve ter pelo menos 2 caracteres")
    private String nome;

    @NotNull(message = "O email não pode ser nulo")
    @Size(min = 10, max = 250, message = "O email deve ter pelo menos 10 caracteres")
    @Email(message = "O email é invalido")
    private String email;

    @NotNull(message = "O telefone não pode ser nulo")
    @Size(min = 11, max = 15, message = "O telefone deve ter pelo menos 11 caracteres e no maximo 15")
    private String telefone;

    public Cliente() {
    }


    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
