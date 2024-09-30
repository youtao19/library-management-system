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

public class EditPbook extends JFrame {
    JTextField name, author, price, type, isbn;
    JLabel nameL, authorL, priceL, typeL, isbnL;
    JButton update = new JButton("更新");
    JButton cancel = new JButton("取消");

    Connection con = null;

    Object[] objects;

    public EditPbook(Object[] objects) {
        this.objects = objects;

        setLayout(new FlowLayout());

        UiInit();
        listen();

        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void listen() {
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookUpdate();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void bookUpdate() {
        String isbn = this.isbn.getText();

        if (isbn.equals("")) {
            JOptionPane.showMessageDialog(this, "请输入ISBN");
            return;
        }

        try {
            con = GetConnection.getconnection();
            String sql = "update physicbooks set 书名=?, 作者=?, 价格=?, 类型=? where ISBN=?";
            PreparedStatement prsm = con.prepareStatement(sql);

            prsm.setString(1, name.getText());
            prsm.setString(2, author.getText());
            prsm.setDouble(3, Double.parseDouble(price.getText()));
            prsm.setString(4, type.getText());
            prsm.setString(5, isbn);

            prsm.executeUpdate();
            JOptionPane.showMessageDialog(null, "更新成功");

            prsm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBookData(String isbn) {
        try {
            con = GetConnection.getconnection();
            String sql = "select * from physicbooks where ISBN = ?";
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1, isbn);
            ResultSet rs = prsm.executeQuery();

            if (rs.next()) {
                name.setText(rs.getString("书名"));
                author.setText(rs.getString("作者"));
                price.setText(rs.getString("价格"));
                type.setText(rs.getString("类型"));
            } else {
                JOptionPane.showMessageDialog(this, "未找到此ISBN的图书");
            }

            rs.close();
            prsm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        buttonbox.add(update);
        buttonbox.add(Box.createHorizontalStrut(20));
        buttonbox.add(cancel);

        vbox.add(Box.createVerticalStrut(20));
        vbox.add(buttonbox);

        add(vbox);

        if (objects != null) {
            bookData(objects);
        }

        setSize(400, 300);
        setResizable(false);
    }

    private void bookData(Object[] objects) {
        name.setText(objects[1].toString());
        author.setText(objects[2].toString());
        price.setText(objects[3].toString());
        type.setText(objects[4].toString());
        isbn.setText(objects[5].toString());
    }

}
