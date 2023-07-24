import javax.swing.*;

public class DebtorUser extends SysInfoProc {
    private JPanel mainPanel;

    public DebtorUser() {
        add(mainPanel);
        setTitle("Info");
        setResizable(false);
        setSize(200,200);
        setVisible(false);
        runAllProc();

    }

    public static void main(String[] args) {
        DebtorUser  user = new DebtorUser();
    }

}
