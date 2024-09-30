package 图书管理系统.book;

import 图书管理系统.GetConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddPbook extends JFrame {
    JTextField name,author,price,type,isbn;
    JLabel nameL,authorL,priceL,typeL,isbnL;
    JButton add = new JButton("添加");
    JButton cancel = new JButton("cancel");

    Connection con = null;

    public AddPbook() {
        setLayout(new FlowLayout());

        UiInit();
        //接口连接
        listen();

        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(2);

    }

    private void listen() {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAdd();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void bookAdd() {

        String name = this.name.getText();
        String author = this.author.getText();
        String price = this.price.getText();
        String type = this.type.getText();
        String isbn = this.isbn.getText();

        if(name.equals("")||author.equals("")||price.equals("")||type.equals("")||isbn.equals("")){
            JOptionPane.showMessageDialog(this,"请输入完整信息");
            return;
        }
        //数据库操作
        try {
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        //检查ISBN是否重复
        try {
            String sql = "select * from physicbooks where ISBN = ?";
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1, isbn);
            ResultSet rs = prsm.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(this,"ISBN重复");
                rs.close(); // 关闭ResultSet
                prsm.close(); // 关闭PreparedStatement
                return;
            }

            rs.close();
            prsm.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try{
            String sql = "insert into physicbooks(书名,作者,价格,类型,ISBN,图书状态) values(?,?,?,?,?,?)";
            PreparedStatement prsm = con.prepareStatement(sql);

            prsm.setString(1, name);
            prsm.setString(2, author);
            prsm.setDouble(3, Double.parseDouble(price));
            prsm.setString(4, type);
            prsm.setString(5, isbn);
            prsm.setBoolean(6,false);
            prsm.executeUpdate();
            JOptionPane.showMessageDialog(null, "添加成功");

            prsm.close();
            con.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void UiInit() {
        name = new JTextField(15);
        author = new JTextField(15);
        price = new JTextField(15);
        type = new JTextField(15);
        isbn = new JTextField(15);
        nameL = new JLabel("书名");
        authorL = new JLabel("作者");
        priceL = new JLabel("价格");
        typeL = new JLabel("类型");
        isbnL = new JLabel("ISBN");
        Box namebox = Box.createHorizontalBox();
        namebox.add(nameL);
        namebox.add(Box.createHorizontalStrut(20));
        namebox.add(name);
        Box authorbox = Box.createHorizontalBox();
        authorbox.add(authorL);
        authorbox.add(Box.createHorizontalStrut(20));
        authorbox.add(author);
        Box pricebox = Box.createHorizontalBox();
        pricebox.add(priceL);
        pricebox.add(Box.createHorizontalStrut(20));
        pricebox.add(price);
        Box typebox = Box.createHorizontalBox();
        typebox.add(typeL);
        typebox.add(Box.createHorizontalStrut(20));
        typebox.add(type);
        Box isbnbox = Box.createHorizontalBox();
        isbnbox.add(isbnL);
        isbnbox.add(Box.createHorizontalStrut(20));
        isbnbox.add(isbn);
        Box vbox = Box.createVerticalBox();
        vbox.add(namebox);
        vbox.add(Box.createVerticalStrut(20));
        vbox.add(authorbox);
        vbox.add(Box.createVerticalStrut(20));
        vbox.add(pricebox);
        vbox.add(Box.createVerticalStrut(20));
        vbox.add(typebox);
        vbox.add(Box.createVerticalStrut(20));
        vbox.add(isbnbox);

        Box buttonbox = Box.createHorizontalBox();
        buttonbox.add(add);
        buttonbox.add(Box.createHorizontalStrut(20));
        buttonbox.add(cancel);

        vbox.add(Box.createVerticalStrut(20));
        vbox.add(buttonbox);

        add(vbox);
        setSize(400, 300);
        setResizable(false);
    }

    public static void main(String[] args) {
        new AddPbook();
    }
}
