package 图书管理系统;

import javax.swing.*;

public class Login extends JFrame {
    private JLabel label1, label2;
    private JTextField area;
    private JPasswordField pass;
    private JButton button;
    PoliceListen listener;

    public Login() {
        setTitle("用户登录");
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250); // 调整窗口大小
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init() {
        setLayout(null);

        label1 = new JLabel("用户名");
        label1.setBounds(50, 30, 80, 20); // 调整位置和大小
        add(label1);

        area = new JTextField(10);
        area.setBounds(140, 30, 200, 20); // 调整位置和大小
        add(area);

        label2 = new JLabel("密码");
        label2.setBounds(50, 70, 80, 20); // 调整位置和大小
        add(label2);
        //密码框
        pass = new JPasswordField();
        pass.setBounds(140, 70, 200, 20); // 调整位置和大小
        add(pass);
        //登录按钮
        button = new JButton("登录");
        button.setBounds(80, 120, 100, 30); // 调整位置和大小
        add(button);

        //创建注册按钮
        JButton register = new JButton("注册");
        register.setBounds(220, 120, 100, 30);
        add(register);
        //登录按钮的监视器
        listener = new PoliceListen(this);
        button.addActionListener(listener);
    }
    public static void main(String[] args) {
        new Login();
    }

}
