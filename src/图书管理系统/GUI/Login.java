package 图书管理系统.GUI;

import 图书管理系统.GetConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Login extends JFrame implements ActionListener {
    private JLabel label1, label2;
    private JTextField area;
    private JPasswordField pass;
    private JTextField inputCode;
    private JButton button;
    private JButton register;
    private JTextArea showCode;
    private JLabel code;

    String code1 = null;
    Register signUp;

    Connection con = null;


    public Login() {
        setTitle("用户登录");
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); // 调整窗口大小
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init() {
        setLayout(null);

        JLabel icon = new JLabel(new ImageIcon("image/晚上风景 女孩 闭眼 心情 厚涂 4k高清壁纸_彼岸图网.png"));
        icon.setSize(400,300);


        label1 = new JLabel("用户名");
        label1.setBounds(50, 30, 80, 20); // 调整位置和大小
        add(label1);

        area = new JTextField(10);
        area.setBounds(140, 30, 150, 30); // 调整位置和大小
        add(area);

        label2 = new JLabel("密码");
        label2.setBounds(50, 70, 80, 20); // 调整位置和大小
        add(label2);
        //密码框
        pass = new JPasswordField();
        pass.setBounds(140, 70, 150, 30); // 调整位置和大小
        add(pass);
        //登录按钮
        button = new JButton("登录");
        button.setBounds(80, 170, 100, 30); // 调整位置和大小
        add(button);

        code = new JLabel("验证码");
        code.setBounds(50,110,80,20);
        add(code);

        //输入验证码
        inputCode = new JTextField();
        inputCode.setBounds(140,110,50,30);

        add(inputCode);

        code1 = getCode();

        //展示验证码
        showCode = new JTextArea();
        showCode.setBounds(200,110,50,30);
        showCode.setText(code1);
        add(showCode);
        showCode.setEditable(false);


        //创建注册按钮
        register = new JButton("注册");
        register.setBounds(220, 170, 100, 30);
        add(register);
        register.addActionListener(this);
        //登录按钮的监视器
        button.addActionListener(this);
        this.getContentPane().add(icon);
    }
    public static void main(String[] args) {
        new Login();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        //点击登录按钮
        if(object == button){

            //数据库连接
            try{
                con = GetConnection.getconnection();
            }
            catch (Exception ee){}


            //area,pass,inputCode

            String user = area.getText();
            char[] password = pass.getPassword();
            String pwd = new String(password);
            boolean flag = false;

            //检查是否为输入
            if(user.length() ==0 || pwd.length() == 0){
                JOptionPane.showMessageDialog(null,"请输入用户名或密码");
                return;
            }
            //检验验证码是否正确
            String code2 = inputCode.getText().toString();
            if(!code2.equalsIgnoreCase(code1)){
                JOptionPane.showMessageDialog(null,"验证码错误");
                code1 = getCode();
                showCode.setText(code1);
                return;
            }
            //检验用户名和密码

            String sql = "select user,pwd from account where user = ? and pwd = ?";
            try {
                PreparedStatement prsm = con.prepareStatement(sql);
                prsm.setString(1,user);
                prsm.setString(2,pwd);
                ResultSet rs = prsm.executeQuery();

                if(rs.next()){
                    flag = true;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }




            if (!flag){
                JOptionPane.showMessageDialog(null,"用户名或密码错误");
                return;
            }

            this.setVisible(false);
            new Menu();
        }
        //点击注册按钮
        else if(object == register){
            signUp = new Register();
        }


    }

    //获得验证码
    private static String getCode() {
        Random r = new Random();
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i));
            list.add((char) ('A' + i));
        }
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int idx = r.nextInt(list.size());
            char c = list.get(idx);
            code.append(c);
        }

        int x = r.nextInt(10);
        code.append(x);

        char[] arr = code.toString().toCharArray();

        int idx2 = r.nextInt(4);
        char tmp = arr[idx2];
        arr[idx2] = arr[arr.length - 1];
        arr[arr.length - 1] = tmp;

        return new String(arr);
    }
}
