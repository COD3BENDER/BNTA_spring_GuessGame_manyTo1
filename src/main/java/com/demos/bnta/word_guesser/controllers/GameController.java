package com.demos.bnta.word_guesser.controllers;

import com.demos.bnta.word_guesser.models.Game;
import com.demos.bnta.word_guesser.models.Guess;
import com.demos.bnta.word_guesser.models.LetterList;
import com.demos.bnta.word_guesser.models.Reply;
import com.demos.bnta.word_guesser.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/games")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames(@RequestParam Optional<Boolean> isComplete, @RequestParam Optional<String> word){ // if its true,
        // return completed games
        // or give all games
        List<Game> games;
        if(isComplete.isPresent()){
            games = gameService.getAllCompletedGames(); // if games are completed then get all games
        }else if(word.isPresent()){
            games = gameService.findGamesByWord(word.get()); // if there is a word then do a word.get--> you do .get because its optional datatype,
            // so you need it

        } else{
            games = gameService.getAllGames(); // otherwise default to getting all games
        }
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id){
        Optional<Game> game = gameService.getGameById(id);
        if (game.isPresent()){
            return new ResponseEntity<>(game.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Reply> submitGuess(@RequestBody Guess guess, @PathVariable int id){
        // if a class is a object used in the mapping and not an entity, then its dto
        Reply reply = gameService.processGuess(guess, id);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }


    @GetMapping(value = "/guessed")
    public ResponseEntity<LetterList> checkGuesses(){
        ArrayList<String> guesses = gameService.getGuessedLetters();
        LetterList guessedLetters = new LetterList(guesses);
        return new ResponseEntity<>(guessedLetters, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reply> startNewGame(@RequestParam int playerId){ // it will find the player attach it to the game
        Reply reply = gameService.startNewGame(playerId);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

}
