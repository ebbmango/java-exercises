class Pair {
    public int start;
    public int end;
    public int nestings = 0;

    public String[] capture;
    public Pair nestedIn;

    public double value = 0;

    public Pair(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s) [%s]", start, end, nestings);
    }

}
