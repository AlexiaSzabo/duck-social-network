package org.example.service;

import org.example.domain.Duck;
import org.example.domain.TipRata;
import org.example.exception.NotFoundException;
import org.example.repository.DuckRepository;
import org.example.repository.UserRepository;
import org.example.validation.ValidationStrategy;

import java.util.List;

/**
 * Service for managing Duck entities.
 * Pagination + filtering are done DIRECTLY in DB.
 */
public class DuckService {

    private final DuckRepository duckRepo;
    private final UserRepository userRepo;
    private final ValidationStrategy<Duck> validator;

    public DuckService(DuckRepository duckRepo,
                       UserRepository userRepo,
                       ValidationStrategy<Duck> validator) {

        this.duckRepo = duckRepo;
        this.userRepo = userRepo;
        this.validator = validator;
    }


    public Duck addDuck(Duck d) {
        validator.validate(d);
        return duckRepo.save(d);
    }

    public Duck getDuck(int id) {
        return duckRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Duck", id));
    }

    public void deleteDuck(int id) {
        // ordine corectă
        duckRepo.delete(id);
        userRepo.delete(id);
    }

    // PAGINATION + FILTER (DB)
    /**
     * Returns ducks for one page, optionally filtered by type.
     * type == null → ALL ducks
     */
    public List<Duck> getDuckPage(int page, int pageSize, TipRata type) {
        return duckRepo.getDuckByPage(page, pageSize, type);
    }

    /**
     * Returns total count (used for pagination).
     * type == null → ALL ducks
     */
    public long getDuckCount(TipRata type) {
        return (type == null)
                ? duckRepo.count()
                : duckRepo.countByType(type);
    }
}
