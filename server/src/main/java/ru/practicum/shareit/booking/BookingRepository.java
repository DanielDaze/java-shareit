package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByIdDesc(long userId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(long userId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findAllByBookerIdAndStartAfterOrderByIdDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdOrderByIdDesc(long userId);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByIdDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByIdDesc(long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByIdDesc(long userId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByIdDesc(long userId, LocalDateTime now);

    Optional<Booking> findTop1BookingByItem_IdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime end,
                                                                                    BookingStatus status);

    Optional<Booking> findTop1BookingByItem_IdAndStartAfterAndStatusOrderByEndAsc(Long itemId, LocalDateTime now,
                                                                                  BookingStatus status);

    Optional<Booking> findTop1BookingByItemIdAndBookerIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, Long bookerId,
                                                                                            LocalDateTime now,
                                                                                            BookingStatus status);
}
