package com.demos.bnta.word_guesser.services;

import com.demos.bnta.word_guesser.models.*;
import com.demos.bnta.word_guesser.repositories.GameRepository;
import com.demos.bnta.word_guesser.repositories.PlayerRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {


    @Autowired // singleton pattern - autowired allows us to make the class once an use it as many times as we need it
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository; // importing services in each other can give you circular dependancies so autowire the repository instead

    @Autowired
    WordService wordService;



    private String currentWord;

    private ArrayList<String> guessedLetters;


    public GameService() {
    }


    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public ArrayList<String> getGuessedLetters() {
        return guessedLetters;
    }

    public void setGuessedLetters(ArrayList<String> guessedLetters) {
        this.guessedLetters = guessedLetters;
    }

    public Reply processGuess(Guess guess, int id){

        // Find the correct game
        Game game = gameRepository.findById(id).get();

        // Check if game is already complete
        if (game.isComplete()){
            return new Reply(
                    false,
                    game.getWord(),
                    String.format("Already finished game %d", game.getId())
            );
        }

        // Check if letter has been guessed already
        if (this.guessedLetters.contains(guess.getLetter())){
            return new Reply(false, this.currentWord, String.format("Already guessed %s", guess.getLetter()));
        }

        // Only increment guess count if a new letter is chosen
        incrementGuesses(game);
        // Add letter to previous guesses
        this.guessedLetters.add(guess.getLetter());

        // Check for incorrect guess
        if (!game.getWord().contains(guess.getLetter())){
            return new Reply(
                    false,
                    this.currentWord,
                    String.format("%s is not in the word", guess.getLetter())
            );
        }

        // Handle correct guess
        String runningResult = game.getWord();

        for (Character letter : game.getWord().toCharArray()) {
            if (!this.guessedLetters.contains(letter.toString())){
                runningResult = runningResult.replace(letter, '*');
            }
        }

        setCurrentWord(runningResult);

        // Check for win
        if (checkWinCondition(game)){
            game.setComplete(true);
            gameRepository.save(game);
            return new Reply(true, this.currentWord, "You win!");
        } else {
            return new Reply(true, this.currentWord,
                    String.format("%s is in the word", guess.getLetter()));
        }
    }

    private boolean checkWinCondition(Game game){
        return game.getWord().equals(this.currentWord);
    }

    private void incrementGuesses(Game game){
        game.setGuesses(game.getGuesses() + 1);
        gameRepository.save(game);
    }

    public Reply startNewGame(int playerId){ // we need playerId to pass into the method to make one to many connection
        Player player = playerRepository.findById(playerId).get();
        String targetWord = wordService.getRandomWord();
        Game game = new Game(targetWord, player);
        this.currentWord = Strings.repeat("*", targetWord.length());
        this.guessedLetters = new ArrayList<>();
        gameRepository.save(game);
        return new Reply(
                false,
                this.currentWord,
                String.format("Started new game with id %d", game.getId())
        );
    }

    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(int id){
        // optional allows us to retrieve the id if it exists instead of having to do if null return ... else return ...
        return gameRepository.findById(id);
    }
}
