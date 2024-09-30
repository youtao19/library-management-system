package 图书管理系统.Borroewr;

import 图书管理系统.GetConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AddBorrower extends JFrame implements ActionListener {
    JLabel idJlabel;
    JLabel password,againpwd;
    JTextField inputID;
    JTextField inputName;
    JButton button;
    JLabel userJLabel;
    JLabel ageJLabel;
    JButton cancel;
    JTextField inputAge;
    JPasswordField passwordField,againpwdField;
    Connection con;


    static char[] a = {123};
    public static ArrayList<Borrower> list = new ArrayList<>();

    public AddBorrower(){
        setLayout(null);
        setSize(400,380);
        setLocationRelativeTo(null);
        idJlabel = new JLabel("输入卡号：");
        idJlabel.setBounds(100,0,100,50);
        add(idJlabel);
        inputID = new JTextField();
        inputID.setBounds(150,10,150,30);
        add(inputID);
        password = new JLabel("输入密码");
        password.setBounds(100,50,100,50);
        add(password);
        passwordField = new JPasswordField();
        passwordField.setBounds(150,60,150,30);
        add(passwordField);
        againpwd = new JLabel("确认密码");
        againpwd.setBounds(100,100,100,50);
        add(againpwd);
        againpwdField = new JPasswordField();
        againpwdField.setBounds(150,110,150,30);
        add(againpwdField);
        userJLabel = new JLabel("输入姓名:");
        userJLabel.setBounds(100,150,100,50);
        add(userJLabel);

        inputName = new JTextField();
        inputName.setBounds(150,160,150,30);
        add(inputName);
        ageJLabel = new JLabel("输入年龄");
        ageJLabel.setBounds(100,200,100,50);
        add(ageJLabel);
        inputAge = new JTextField();
        inputAge.setBounds(150,210,150,30);
        add(inputAge);
        button = new JButton("添加");
        button.setBounds(110,270,70,40);
        add(button);
        button.addActionListener(this);
        cancel = new JButton("返回");
        cancel.setBounds(240,270,70,40);
        cancel.addActionListener(this);
        add(cancel);

        setDefaultCloseOperation(2);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AddBorrower();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        //点击注册按钮
        if (object.equals(button)){

            //连接数据库
            try {
                con = GetConnection.getconnection();
            } catch (Exception ex) {}



            String id = inputID.getText().toString();
            String name = inputName.getText().toString();
            int age = 0;
            try{
                age = Integer.parseInt(inputAge.getText());
            }catch (Exception ee){}
            char[] password = passwordField.getPassword();
            char[] password2 = againpwdField.getPassword();
            String pwd = new String(password);
            //检查是否为空
            if (id.length() == 0 || name.length() == 0 || age == '\0' || password.length == 0){
                JOptionPane.showMessageDialog(null,"请输入信息");
                return;
            }

            //判断ID是否存在
            boolean check = false;
            String sql = "select card from borrower where card = ?";

            try{
                PreparedStatement prsm = con.prepareStatement(sql);
                prsm.setString(1,id);
                ResultSet rs = prsm.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(null,"当前卡号已存在");
                    return;
                }

            }catch (SQLException ee){}

            //检查密码是否相同

            if(!Arrays.equals(password,password2)){
                JOptionPane.showMessageDialog(null,"两次密码输入不相同");
                return;
            }

            //检查年龄是否合法

            if(age < 0){
                JOptionPane.showMessageDialog(null,"请输入正确的年龄");
                return;
            }

            //将数据插入到数据库

            sql = "insert into borrower (借阅卡号,密码,姓名,年龄) values (?,?,?,?)";
            try{
                PreparedStatement prsm = con.prepareStatement(sql);
                prsm.setString(1,id);
                prsm.setString(2,pwd);
                prsm.setString(3,name);
                prsm.setInt(4,age);
                int count = prsm.executeUpdate();
            } catch (SQLException ex) {

            }
            JOptionPane.showMessageDialog(null,"添加成功");
        }
        //点击取消按钮
        else if(object.equals(cancel)){
            this.setVisible(false);
        }
    }
}
