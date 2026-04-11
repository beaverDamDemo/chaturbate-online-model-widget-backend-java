package com.example.widgetbackend.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

    Optional<Favorite> findByUserIdAndRoomName(Long userId, String roomName);

    boolean existsByUserIdAndRoomName(Long userId, String roomName);

    void deleteByUserIdAndRoomName(Long userId, String roomName);

    @Query("SELECT f.roomName AS roomName, COUNT(f) AS favoriteCount FROM Favorite f GROUP BY f.roomName ORDER BY COUNT(f) DESC")
    List<RoomStats> findRoomStats();

    @Query("SELECT DISTINCT f.roomName FROM Favorite f")
    List<String> findDistinctRoomNames();

    @Modifying
    void deleteByUserId(Long userId);

    @Modifying
    @Query("UPDATE Favorite f SET f.online = :online, f.statusCheckedAt = :checkedAt WHERE f.roomName IN :roomNames")
    void updateOnlineStatusForRooms(@Param("roomNames") List<String> roomNames, @Param("online") boolean online,
            @Param("checkedAt") java.time.Instant checkedAt);

    interface RoomStats {
        String getRoomName();

        Long getFavoriteCount();
    }
}
