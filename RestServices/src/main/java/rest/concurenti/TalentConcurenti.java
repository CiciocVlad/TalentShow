package rest.concurenti;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import talent.model.Concurent;
import talent.services.rest.ServiceException;

import java.util.concurrent.Callable;

public class TalentConcurenti {
    public static final String URL = "http://localhost:8080/talent";

    private final RestTemplate restTemplate = new RestTemplate();

    private<T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (ResourceAccessException | HttpClientErrorException e) {
            throw new ServiceException(e.getMessage());
        }
        catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    public Concurent[] getAll() {
        return execute(() -> restTemplate.getForObject(String.format("%s/concurenti", URL), Concurent[].class));
    }

    public Concurent getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/concurenti/%s", URL, id), Concurent.class));
    }
}
