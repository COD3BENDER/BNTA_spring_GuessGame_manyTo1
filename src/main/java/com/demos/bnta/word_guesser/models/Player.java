package com.demos.bnta.word_guesser.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "players") // makes it a sql database table
public class Player {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // serial (auto incremented)
    private int id;

    @Column
    private String name;

    @OneToMany(mappedBy = "player")// java naming convention  // one player has many games
    @JsonIgnoreProperties({"player"}) // ignore games property so you dont get infinite recursion as game has player and player is a player
    private List<Game> games;

    public Player(String name){
        this.name = name;
        this.games = new ArrayList<>();
    }

    public Player(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
