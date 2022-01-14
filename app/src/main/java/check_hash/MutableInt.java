package check_hash;

public class MutableInt {
    public int value = 1; // note that we start at 1 since we're counting

    public void increment() {
        ++value;
    }

    public void increment(final int i) {
        value += i;
    }
}
