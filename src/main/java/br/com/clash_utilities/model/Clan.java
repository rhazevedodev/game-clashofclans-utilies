package br.com.clash_utilities.model;

public class Clan {
    private String nome;
    private String tag;

    public Clan() {}

    public Clan(String nome, String tag) {
        this.nome = nome;
        this.tag = tag;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

