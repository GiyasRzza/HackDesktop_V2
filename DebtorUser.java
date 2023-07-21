

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DebtorUser extends SysInfoProc {
    private JPanel mainPanel;

    public void runAllProc() {
        super.runAllProc();
    }

    public DebtorUser() {
        this.add(this.mainPanel);
        this.setTitle("My System Info");
        this.setSize(100, 100);
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.runAllProc();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException(var2);
        } catch (InstantiationException var3) {
            throw new RuntimeException(var3);
        } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
        } catch (UnsupportedLookAndFeelException var5) {
            throw new RuntimeException(var5);
        }

        SwingUtilities.invokeLater(() -> {
            DebtorUser systemInfo = new DebtorUser();
            systemInfo.setVisible(false);
        });
    }
}
