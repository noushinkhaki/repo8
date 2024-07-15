package com.paf.tp.repository;

import com.paf.tp.model.TournamentPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {

    Optional<TournamentPlayer> findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);

    Optional<List<TournamentPlayer>> findByTournamentId(Long tournamentId);
}
