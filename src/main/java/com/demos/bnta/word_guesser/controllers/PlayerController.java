package com.demos.bnta.word_guesser.controllers;

import com.demos.bnta.word_guesser.models.Player;
import com.demos.bnta.word_guesser.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // turns it into a restcontroller bean
@RequestMapping(value = "/players") // every entity will omit /players
public class PlayerController { // most of the time if you have an entity then you most likely will need a controller to handle it

    @Autowired
    PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers(){
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}") // curly brackets indicates it will be a dynamic variable
    public ResponseEntity<Player> getPlayerByID(@PathVariable Integer id){// variable name must match which "/{ whatever is in here}"
        // int is usually faster than Integer..
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Player> addNewPlayer(@RequestBody Player player){// use a request body to give data so we dont use need url referencing
        Player savedPlayer = playerService.savePlayer(player); // now player has a ID
        return new ResponseEntity<>(savedPlayer,HttpStatus.CREATED); // not mandatory but why not
    }

}
