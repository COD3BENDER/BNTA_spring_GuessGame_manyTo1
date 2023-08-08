package com.demos.bnta.word_guesser.services;

import com.demos.bnta.word_guesser.models.Player;
import com.demos.bnta.word_guesser.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // makes it into a service bean you need it to do things likes autowiring
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public List<Player> getAllPlayers(){ // gives back all the player
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Integer id){ // we need optional to retrive one player
        return playerRepository.findById(id);
    }

    public Player savePlayer(Player player){
        playerRepository.save(player); // postman --> gameController (deserializes it) --> comes here and saves to database giving an ID
        return player; // when we return it to the front end it will give the front end the player with id, dont have to do this but its good.
    }



}
