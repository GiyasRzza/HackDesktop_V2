

public interface MySystemProc {
    void shutDownProc();

    void fileWrite(String getDatesPath,String getDateCountPath);

    boolean isRunningDay();

    void fileRead();

    void runAllProc();
}
