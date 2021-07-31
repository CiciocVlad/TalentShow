package talent.model;

public class Result extends Entity<Long> {
    private Concurent concurent;
    private Jurat jurat;
    private String result;

    public Result() { }

    public Result(Concurent concurent, Jurat jurat, String result) {
        this.concurent = concurent;
        this.jurat = jurat;
        this.result = result;
    }

    public Concurent getConcurent() {
        return this.concurent;
    }

    public Jurat getJurat() {
        return this.jurat;
    }

    public String getResult() {
        return this.result;
    }

    public void setConcurent(Concurent concurent) {
        this.concurent = concurent;
    }

    public void setJurat(Jurat jurat) {
        this.jurat = jurat;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%s %s --- %s\n", concurent.getName(), jurat.getUsername(), result);
    }
}
