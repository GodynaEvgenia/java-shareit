package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r " +
            "FROM Request r " +
            "join r.requestor as rq " +
            "WHERE rq.id = :requestorId ")
    List<Request> getAllByOwner(Long requestorId);

    @Query("SELECT r " +
            "FROM Request r " +
            "join r.requestor as rq " +
            "WHERE rq.id <> :requestorId ")
    List<Request> getAllByAnotherUsers(Long requestorId);

    Optional<Request> findById(Long requestId);
}
