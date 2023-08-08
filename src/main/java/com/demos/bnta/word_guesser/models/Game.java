package com.demos.bnta.word_guesser.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "word")
    private String word;
    @Column(name = "guesses")
    private int guesses;
    @Column(name = "complete")
    private boolean complete;
    @ManyToOne // most of the time you dont need any mapping as its been done on One to many side
    @JoinColumn(name = "player_id") // needs the foreign key restriction this annotation will give you that.
    @JsonIgnoreProperties({"games"}) // ignore games property so you dont get infinite recursion as player has list of games and games also has game
    private Player player; // the mapped by annotation name has to match the many to one variable name. (mappedBy = "player") --> player

    public Game(String word, Player player) {
        this.guesses = 0;
        this.complete = false;
        this.word = word;
        this.player = player;
    }

    public Game() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getGuesses() {
        return guesses;
    }

    public void setGuesses(int guesses) {
        this.guesses = guesses;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
