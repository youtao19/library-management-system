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

public class Register extends JFrame implements ActionListener {
    JTextField inputUser;
    JTextField inputCode;
    JPasswordField password;
    JPasswordField againPassword;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JTextArea showCode;
    JButton button;
    Connection con;
    PreparedStatement prsm;

    // 用于储存用户名;

    public Register() {
        setLayout(null);
        setTitle("注册界面");
        setBounds(250, 250, 600, 400);

        setLocationRelativeTo(null);
        init();

        setDefaultCloseOperation(2);
        setVisible(true);

    }

    private void init() {
        label1 = new JLabel("用户名：");
        label1.setBounds(150, 50, 50, 50);
        label2 = new JLabel("密码:");
        label2.setBounds(150, 100, 50, 50);
        label3 = new JLabel("确认密码：");
        label3.setBounds(150, 150, 70, 50);
        label4 = new JLabel("再次输入密码");

        inputUser = new JTextField();
        inputUser.setBounds(230, 60, 150, 30);
        password = new JPasswordField();
        password.setBounds(230, 110, 150, 30);
        inputCode = new JTextField();
        againPassword = new JPasswordField();
        againPassword.setBounds(230, 160, 150, 30);
        showCode = new JTextArea();
        showCode.setBounds(430, 160, 50, 30);

        button = new JButton("注册");
        button.setBounds(250, 250, 100, 50);

        add(label1);
        add(label2);
        add(label3);

        // String code = getCode();
        // showCode.setText(code);

        add(inputUser);
        add(password);
        add(againPassword);
        // add(inputCode);
        // add(showCode);

        // 给按钮添加监视器
        button.addActionListener(this);

        add(button);

    }


    //点击注册按钮
    @Override
    public void actionPerformed(ActionEvent e){

        String user = inputUser.getText();
        char[] inputpass = password.getPassword();
        char[] againpassword = againPassword.getPassword();

        try{
            con = GetConnection.getconnection();
        }
        catch (Exception ee){}

        if(con == null) return;

        // 检查用户名

        //检查用户民格式是否正确
        boolean flag = checkUser(user);
        if (!flag) {
            return;
        }

        //检查用户名是否存在

        String sql = "select * from account where user = ?";
        try{
            prsm = con.prepareStatement(sql);
            prsm.setString(1,user);
            ResultSet rs = prsm.executeQuery();
            //如果不存在
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"当前用户名已存在");
                return;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }


        // 检查密码

        if(inputpass.length != againpassword.length){
            JOptionPane.showMessageDialog(null,"请确认输入相同的密码");
            return;
        }

        for (int i = 0; i < inputpass.length; i++) {
            if(inputpass[i] != againpassword[i]){
                JOptionPane.showMessageDialog(null,"两次密码输入不同");
                return;
            }
        }

        String pwd = new String(inputpass);

        //添加账号密码到数据库
        try {
            sql = "insert into account(user,pwd) values (?,?)";
            prsm = con.prepareStatement(sql);
            prsm.setString(1,user);
            prsm.setString(2,pwd);
            int account = prsm.executeUpdate();
        }
        catch (Exception ee){
            System.out.println(ee.toString());
        }


        JOptionPane.showMessageDialog(null, "注册成功");
    }

    private static boolean checkUser( String username) {
        // 检查用户名长度
        int len = username.length();
        if (len > 15 || len < 3) {
            JOptionPane.showMessageDialog(null, "请输入用户名或密码");
            return false;
        }

        // 不能全部是数字
        boolean flag = false;

        for (int i = 0; i < len; i++) {
            char c = username.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                flag = true;
                break;
            }
        }

        if (flag) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "用户名不符合要求");
            return false;
        }

    }
}
