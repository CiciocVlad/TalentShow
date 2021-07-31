package talent.persistence.repository;

import talent.model.Concurent;

public interface IConcurent extends Repository<Long, Concurent> {
    Concurent findByName(String name);
}
