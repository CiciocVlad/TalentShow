package talent.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import talent.model.Concurent;
import talent.model.Jurat;
import talent.model.Result;
import talent.persistence.repository.IConcurent;
import talent.persistence.repository.IJurat;
import talent.persistence.repository.IResult;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
@RequestMapping("/talent")
public class TalentController {
    private static final String template = "Hello, %s";

    @Autowired
    private IConcurent repoConcurent;
    @Autowired
    private IJurat repoJurat;
    @Autowired
    private IResult repoResult;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "numeConcurent", defaultValue = "unknown") String name) {
        return String.format(template, name);
    }

    @RequestMapping(value = "concurenti", method = RequestMethod.GET)
    public Concurent[] getAll() {
        return StreamSupport.stream(repoConcurent.findAll().spliterator(), false).toArray(Concurent[]::new);
    }

    @RequestMapping(value = "jurati/{jurat}", method = RequestMethod.GET)
    public Map<Concurent, String> getResults(@PathVariable String jurat) {
        Map<Concurent, String> resultMap = new HashMap<>();
        Iterable<Result> resultList = repoResult.searchByJuratId(repoJurat.findByUsername(jurat).getId());
        for (Result result : resultList)
            resultMap.put(result.getConcurent(), result.getResult());
        return resultMap;
    }

    @RequestMapping(value = "concurenti/{concurent}", method = RequestMethod.GET)
    public Map<Jurat, String> getResultsForConcurent(@PathVariable String concurent) {
        String name = concurent.replace('-', ' ');
        Map<Jurat, String> resultMap = new HashMap<>();
        Iterable<Result> resultList = repoResult.searchByConcurentId(repoConcurent.findByName(name).getId());
        for (Result result : resultList)
            resultMap.put(result.getJurat(), result.getResult());
        return resultMap;
    }
}
