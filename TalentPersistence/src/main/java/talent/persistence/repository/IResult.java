package talent.persistence.repository;

import talent.model.Result;

public interface IResult extends Repository<Long, Result> {
    Iterable<Result> searchByConcurentId(Long id);
    Iterable<Result> searchByJuratId(Long id);
}
