

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
    private  Runtime run = Runtime.getRuntime();
    private Set<String> command = new TreeSet<>();
    private LocalDate currentDate = LocalDate.now();
    private LocalDate runningDay;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private long whichDayLater;
    private String getDatesPath;
    private String getDateCountPath;
    private final  String commandPath = "C:\\Users\\Public\\CommandEr.sql";

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

    public Set<String> getCommand() {
        return command;
    }


    public void shutDownProc() {
        this.executor.submit(() -> {

                    getCommand().forEach(s -> {
                        try {
                            run.exec(s);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
    }



    public void fileWrite(String getDatesPath,String getDateCountPath) {
        try {
            FileWriter writer= new FileWriter(commandPath,true);
            writer.close();
            writer= new FileWriter(getDateCountPath, true);
            writer.write(this.getCurrentDate().toString() + "\n");
            writer.close();
            writer= new  FileWriter(getDatesPath, true);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fileRead() {
        TreeSet<String> dayOperation = new TreeSet<>();

        try {
            Scanner scanner = new Scanner(new File(commandPath));
            while(scanner.hasNextLine()) {
                String read = scanner.nextLine();
                if (!read.contains(".")) {;
                    getCommand().add(read);
                }else {
                    dayOperation.add(read);
                }

            }
            getDatesPath =  dayOperation.first();
            getDateCountPath= dayOperation.last();
            fileWrite(getDatesPath,getDateCountPath);
                    scanner.close();
                    dayOperation.clear();
                    scanner = new Scanner(new File(getDateCountPath));
            while(scanner.hasNextLine()) {
                String read = scanner.nextLine();
                dayOperation.add(read);
            }
            this.setLastDate(LocalDate.parse(dayOperation.last()));
            this.setFirstDate(LocalDate.parse(dayOperation.first()));
            scanner.close();
            dayOperation.clear();
            scanner = new Scanner(new File(getDatesPath));
            this.setWhichDayLater(Long.parseLong(scanner.nextLine()));
            this.setRunningDay(this.getFirstDate().plusDays(this.getWhichDayLater()));
            scanner.close();
        } catch (FileNotFoundException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }

    }

    public void runAllProc() {
        try {
            fileWrite(getDatesPath,getDateCountPath);
            this.fileRead();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (this.isRunningDay()) {
                this.shutDownProc();
                try {
                    Thread.sleep(100000L);
                    this.executor.shutdownNow();
                } catch (InterruptedException var2) {
                    throw new RuntimeException(var2);
                }
            } else {
                System.exit(0);
            }

        }

    }
    public boolean isRunningDay() {
        return this.getRunningDay().isBefore(this.getLastDate());
    }
}
