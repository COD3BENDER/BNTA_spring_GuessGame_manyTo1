package com.demos.bnta.word_guesser.repositories;

import com.demos.bnta.word_guesser.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player,Integer> {
    // jpa<what class are we creating the object for, the datatype of ID as Reference value>
    // we cant use primitive types in angle brackets so you use a reference type
}
