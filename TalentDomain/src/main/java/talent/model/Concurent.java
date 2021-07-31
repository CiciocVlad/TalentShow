package talent.model;

public class Concurent extends Entity<Long> {
    public String name;
    public String result;

    public Concurent() { }

    public Concurent(String name, String result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%s %s\n", name, result);
    }
}
