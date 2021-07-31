package talent.persistence.repository;

import talent.model.Jurat;

public interface IJurat extends Repository<Long, Jurat> {
    Jurat findByUsername(String username);
}
