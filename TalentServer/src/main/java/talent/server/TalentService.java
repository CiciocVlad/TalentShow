package talent.server;

import talent.model.Concurent;
import talent.model.Jurat;
import talent.model.Result;
import talent.persistence.repository.IConcurent;
import talent.persistence.repository.IJurat;
import talent.persistence.repository.IResult;
import talent.services.ITalentObserver;
import talent.services.ITalentServices;
import talent.services.TalentException;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TalentService implements ITalentServices {
    private final IJurat repoJurat;
    private final IConcurent repoConcurent;
    private final IResult repoResult;

    private Map<String, ITalentObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public TalentService(IJurat repoJurat, IConcurent repoConcurent, IResult repoResult) {
        this.repoJurat = repoJurat;
        this.repoConcurent = repoConcurent;
        this.repoResult = repoResult;
        loggedClients = new ConcurrentHashMap<>();
    }

    public List<Concurent> getAllConcurenti() {
        return StreamSupport.stream(repoConcurent.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Jurat> getAllJurati() {
        return StreamSupport.stream(repoJurat.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Jurat findByUsername(String username) {
        return repoJurat.findByUsername(username);
    }

    public List<Result> getAllResults() {
        return StreamSupport.stream(repoResult.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public String countYesNo(List<Result> resultList) {
        return resultList.stream().filter(res -> res.getResult().equals("Yes")).count() >= 2 ? "Yes" : "No";
    }

    public synchronized void addResult(Result result, Jurat loggedJurat) throws TalentException {
        Concurent concurent = repoConcurent.findOne(result.getConcurent().getId());
        Jurat jurat = repoJurat.findByUsername(result.getJurat().getUsername());
        String res = result.getResult();
        Result result1 = new Result(concurent, jurat, res);
        List<Result> concurentResults = StreamSupport.stream(repoResult.searchByConcurentId(concurent.getId()).spliterator(), false).collect(Collectors.toList());
        if (concurentResults.stream().anyMatch(r -> r.getJurat().getUsername().equals(loggedJurat.getUsername())))
            throw new TalentException("You voted this contestant already");
        concurentResults.add(result1);
        if (concurentResults.size() == 3)
            concurent.setResult(countYesNo(concurentResults));
        else
            concurent.setResult("PENDING");
        repoConcurent.update(concurent);
        repoResult.save(result1);
        notifyClients(result1);
    }

    private void notifyClients(Result result) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (String clientLogged : loggedClients.keySet()) {
            ITalentObserver client = loggedClients.get(clientLogged);
            if (client != null) {
                executor.execute(() -> {
                    try {
                        System.out.printf("Notifying [%s]\n", clientLogged);
                        synchronized (client) {
                            client.newResultAdded(result);
                        }
                    }
                    catch (TalentException | RemoteException e) {
                        System.out.printf("Eror notifying client %s\n", e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    public String hash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes(), 0, pass.length());
            String z = new BigInteger(1, md.digest()).toString(16);
            System.out.println(z);
            return z;
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public synchronized void login(Jurat jurat, ITalentObserver client) throws TalentException {
        Jurat jurat1 = repoJurat.findByUsername(jurat.getUsername());
        if (jurat1 != null) {
            if (!jurat1.getPassword().equals(hash(jurat.getPassword())))
                throw new TalentException(jurat1.toString() + "\n" + jurat.toString());
            if (loggedClients.get(jurat1.getUsername()) != null)
                throw new TalentException("jurat already logged in");
            loggedClients.put(jurat1.getUsername(), client);
        }
        else
            throw new TalentException("could not authenticate");
    }

    @Override
    public void logout(Jurat jurat, ITalentObserver client) {
        loggedClients.remove(jurat.getUsername());
    }
}
