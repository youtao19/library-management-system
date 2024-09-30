package 图书管理系统.Borroewr;

import 图书管理系统.GetConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangeBorrower extends JFrame {

    JLabel ID, card, pwd, name, age;
    JTextField ID_text, card_text, pwd_text, name_text, age_text;

    Connection con = null;

    Object [] objects;

    public ChangeBorrower(Object[] objects) {
        this.objects = objects;

        setTitle("修改借阅者信息");

        setSize(300, 300);
        init();
        setLocationRelativeTo(null);

        setDefaultCloseOperation(2);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ChangeBorrower(null);
    }

    private void init() {

        setLayout(null);

        ID = new JLabel("借阅者编号");
        ID.setBounds(10, 10, 100, 30);
        add(ID);

        ID_text = new JTextField();
        ID_text.setBounds(100, 10, 100, 30);
        add(ID_text);

        card = new JLabel("借阅卡号");
        card.setBounds(10, 50, 100, 30);

        add(card);

        card_text = new JTextField();

        card_text.setBounds(100, 50, 100, 30);

        add(card_text);

        pwd = new JLabel("借阅密码");

        pwd.setBounds(10, 90, 100, 30);

        add(pwd);

        pwd_text = new JTextField();

        pwd_text.setBounds(100, 90, 100, 30);

        add(pwd_text);

        name = new JLabel("借阅者姓名");

        name.setBounds(10, 130, 100, 30);

        add(name);

        name_text = new JTextField();

        name_text.setBounds(100, 130, 100, 30);

        add(name_text);

        age = new JLabel("借阅者年龄");

        age.setBounds(10, 170, 100, 30);

        add(age);

        age_text = new JTextField();

        age_text.setBounds(100, 170, 100, 30);

        add(age_text);

        borrowerkData(objects);

        JButton submit = new JButton("提交");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = ID_text.getText();
                String card = card_text.getText();
                String pwd = pwd_text.getText();
                String name = name_text.getText();
                String age = age_text.getText();

                if(id.equals("") || card.equals("") || pwd.equals("") || name.equals("") || age.equals("")) {
                    JOptionPane.showMessageDialog(null, "请填写完整信息！");
                    return;
                } else {
                    try{
                        con = GetConnection.getconnection();

                        String sql = "UPDATE borrower SET 编号 = ?, 借阅卡号 = ?, 密码 = ?, 姓名 = ?, 年龄 = ? WHERE 编号 = ?";
                        PreparedStatement prsm = con.prepareStatement(sql);
                        prsm.setString(1, id);
                        prsm.setString(2, card);
                        prsm.setString(3, pwd);
                        prsm.setString(4, name);
                        prsm.setString(5, age);
                        prsm.setString(6, objects[0].toString());

                        int ok = prsm.executeUpdate();
                        if(ok > 0){
                            JOptionPane.showMessageDialog(null, "修改成功！");
                        }

                        con.close();

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });


        submit.setBounds(100, 210, 100, 30);

        add(submit);

    }

    private void borrowerkData(Object[] objects) {
        ID_text.setText(objects[0].toString());
        card_text.setText(objects[1].toString());
        pwd_text.setText(objects[2].toString());
        name_text.setText(objects[3].toString());
        age_text.setText(objects[4].toString());
    }
}
