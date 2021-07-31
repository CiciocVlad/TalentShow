package talent.services;

import talent.model.Concurent;
import talent.model.Jurat;
import talent.model.Result;

public interface ITalentServices {
    void login(Jurat jurat, ITalentObserver client) throws TalentException;
    void logout(Jurat jurat, ITalentObserver client);
    Iterable<Concurent> getAllConcurenti() throws TalentException;
    Iterable<Result> getAllResults() throws TalentException;
    void addResult(Result result, Jurat jurat) throws TalentException;
}
