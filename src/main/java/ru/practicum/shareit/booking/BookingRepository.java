package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByIdDesc(long userId);
    List<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(long userId, LocalDateTime now);
    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(long userId, BookingStatus status);
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(long userId, LocalDateTime now, LocalDateTime now2);
    List<Booking> findAllByBookerIdAndStartAfterOrderByIdDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdOrderByIdDesc(long userId);
    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByIdDesc(long userId, LocalDateTime now);
    List<Booking> findAllByItem_Owner_IdAndStatusOrderByIdDesc(long userId, BookingStatus status);
    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByIdDesc(long userId, LocalDateTime now, LocalDateTime now2);
    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByIdDesc(long userId, LocalDateTime now);
}
