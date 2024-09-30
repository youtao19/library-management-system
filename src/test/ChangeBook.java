package test;

import 图书管理系统.GetConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeBook extends JFrame implements ActionListener {
    JLabel Nolabel,name,quantity,type;
    JTextField inputNo,inputName,inputQuantity,inputType;
    JButton change = new JButton("Change");
    JButton cancel = new JButton("Cancel");

    Object[] objects;


    public ChangeBook(Object[] objects){
        this.objects = objects;
        setSize(400,400);
        setLocationRelativeTo(null);

        UiInit();

        setVisible(true);
    }

    private void UiInit() {

        JPanel p1 = new JPanel();
        p1.setLayout(null);
        Nolabel = new JLabel("序号:");
        inputNo = new JTextField();
        Nolabel.setBounds(100,50,100,50);
        inputNo.setBounds(140,60,150,30);

        p1.add(Nolabel);
        p1.add(inputNo);


        name = new JLabel("书名:");
        inputName = new JTextField();
        name.setBounds(100,90,100,50);
        inputName.setBounds(140,100,150,30);
        p1.add(name);
        p1.add(inputName);

        quantity = new JLabel("数量:");
        inputQuantity = new JTextField();
        quantity.setBounds(100,130,100,50);
        inputQuantity.setBounds(140,140,150,30);
        p1.add(quantity);
        p1.add(inputQuantity);

        type = new JLabel("类型:");
        inputType = new JTextField();
        type.setBounds(100,170,100,50);
        inputType.setBounds(140,180,150,30);
        p1.add(type);
        p1.add(inputType);

        add(p1,BorderLayout.CENTER);

        JPanel p2 = new JPanel();
        p2.add(change);
        p2.add(cancel);

        add(p2,BorderLayout.SOUTH);

        //修改按钮
        change.addActionListener(this);
        //取消按钮
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

    }

    public static void main(String[] args) {
        new ChangeBook(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        String id = objects[0].toString();
        String name = inputName.getText();
        String type = inputType.getText();
        int quantity = 0;
        String q = inputQuantity.getText();
        Connection con = null;
        int ID = 0;

        //判断是否输入
        if(id.length() == 0 || name.length() == 0 || type.length() == 0 || q.length() == 0){
            JOptionPane.showMessageDialog(null,"请输入信息");
            return;
        }


        try{
            quantity = Integer.parseInt(inputQuantity.getText());
            ID = Integer.parseInt(inputNo.getText());
            con = GetConnection.getconnection();
        } catch (Exception ee) {
        }
        if(con == null) return;
        String sql = "select * from physicbooks where 序号 = ?";
        //判断当前数据是否存在
        boolean flag = true;
        try{
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1,id);
            ResultSet rs = prsm.executeQuery();
            if(!rs.next()){
                JOptionPane.showMessageDialog(null,"当前编号不存在");
                return;
            }

            //将数据更改
            sql = "update physicbooks set 序号 = ?,书名 = ? ,数量 = ?,书种 = ? where 序号 = ?";
            prsm = con.prepareStatement(sql);
            prsm.setInt(1,ID);
            prsm.setString(2,name);
            prsm.setInt(3,quantity);
            prsm.setString(4,type);
            prsm.setString(5,id);
            int count = prsm.executeUpdate();
            if(count > 0){
                JOptionPane.showMessageDialog(null,"修改成功");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
