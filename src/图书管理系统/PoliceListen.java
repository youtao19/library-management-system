package 图书管理系统;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class PoliceListen implements ActionListener{

    private final JFrame frame;
    public PoliceListen(JFrame frame){
        this.frame = frame;
    }

    //点击登录跳转到主界面
    public void actionPerformed(ActionEvent e){
        frame.dispose();
        @SuppressWarnings("unused")
        Menu menu = new Menu();
    }
}

