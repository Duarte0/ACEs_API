package org.example.aces_api.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "dataNascimento")
    private LocalDate dataNascimento;

    @Column(name = "sexo", length = 1)
    private Character sexo;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "cartaoSUS", length = 20)
    private String cartaoSUS;

    @Column(name = "telefone", length = 20)
    private String telefone;

    public Paciente() {
    }

    public Paciente(Endereco endereco, String nome, LocalDate dataNascimento, Character sexo, String cpf, String cartaoSUS, String telefone) {
        this.endereco = endereco;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.cpf = cpf;
        this.cartaoSUS = cartaoSUS;
        this.telefone = telefone;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCartaoSUS() {
        return cartaoSUS;
    }

    public void setCartaoSUS(String cartaoSUS) {
        this.cartaoSUS = cartaoSUS;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}