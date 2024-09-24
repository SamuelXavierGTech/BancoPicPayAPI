package com.example.bancopicpay.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Conta {

    @Id
    @NotNull
    @Size(min = 5, max = 5, message = "O numero da conta deve ter 5 digitos" )
    @Column(name = "numero_conta")
    private String numeroConta;

    @NotNull
//    @Digits(integer = 10, fraction = 2, message = "O saldo deve ter 10 digitos" )
    private double saldo;

    @NotNull
    @Column(name = "limite_especial")
    private double limiteEspecial;

    @NotNull
    @CPF(message = "O cpf informado é inválido" )
    @Column(name = "cliente_cpf")
    private String cpfCliente;


    public Conta() {
    }


    public String getNumeroConta() {
        return numeroConta;
    }
    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    public double getLimiteEspecial() {
        return limiteEspecial;
    }
    public void setLimiteEspecial(double limiteEspecial) {
        this.limiteEspecial = limiteEspecial;
    }
    public String getCpfCliente() {
        return cpfCliente;
    }
    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }


    @Override
    public String toString() {
        return "Conta{" +
                "numero_conta='" + numeroConta + '\'' +
                ", saldo=" + saldo +
                ", limite_especial=" + limiteEspecial +
                ", cpf_cliente='" + cpfCliente + '\'' +
                '}';
    }
}
