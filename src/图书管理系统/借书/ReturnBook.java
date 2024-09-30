package 图书管理系统.借书;

import 图书管理系统.GetConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReturnBook extends JFrame {

    Box base, b1, b2;
    Connection con;
    JTable table = new JTable();
    String borrowerId;
    String password;
    JFrame tableFrame;
    JTextField borrowerIdField = new JTextField(15);
    JPasswordField passwordField = new JPasswordField(15);

    public ReturnBook() {
        setLayout(new BorderLayout());

        UiInit();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void UiInit() {
        b1 = Box.createVerticalBox();
        b2 = Box.createVerticalBox();
        base = Box.createHorizontalBox();
        b1.add(new JLabel("借阅者ID"));
        b1.add(Box.createVerticalStrut(15));
        b1.add(new JLabel("密码"));
        base.add(b1);
        base.add(Box.createHorizontalStrut(15));
        b2.add(borrowerIdField);
        b2.add(Box.createVerticalStrut(15));
        b2.add(passwordField);
        base.add(b2);
        add(base, BorderLayout.CENTER);
        JPanel panel = new JPanel();

        JButton r = new JButton("确认");
        JButton cancel = new JButton("取消");
        panel.add(r);
        panel.add(cancel);
        add(panel, BorderLayout.SOUTH);

        // 取消
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // 确认
        r.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    con = GetConnection.getconnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                // 获取借阅者ID和密码
                borrowerId = borrowerIdField.getText();
                char[] pwd = passwordField.getPassword();
                password = new String(pwd);

                // 检查借阅者ID和密码是否匹配
                try (PreparedStatement prsm = con.prepareStatement("SELECT * FROM borrower WHERE 借阅卡号 = ? AND 密码 = ?")) {
                    prsm.setString(1, borrowerId);
                    prsm.setString(2, password);
                    try (ResultSet rs = prsm.executeQuery()) {
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "输入的ID不存在或密码错误");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // 显示表格窗口
                tableFrame();
            }
        });
    }

    //表格界面
    private void tableFrame() {
        this.setVisible(false);
        tableFrame = new JFrame();
        tableFrame.setLayout(new BorderLayout());
        // 初始化表格数据
        refreshTable();

        tableFrame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton ret = new JButton("还书");
        JButton refresh = new JButton("刷新");
        JButton cancel = new JButton("取消");

        panel.add(ret);
        panel.add(refresh);
        panel.add(cancel);
        tableFrame.add(panel, BorderLayout.SOUTH);

        // 取消按钮操作
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableFrame.dispose();
            }
        });

        // 还书按钮操作
        ret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String bookId = getBookId();
                    if (bookId == null || bookId.equals("")) {
                        JOptionPane.showMessageDialog(null, "请选择一本书");
                        return;
                    }
                    String sql = "DELETE FROM brbook WHERE 书籍编号 = ?";
                    try (PreparedStatement prsm = con.prepareStatement(sql)) {
                        prsm.setString(1, bookId);
                        prsm.executeUpdate();
                    }

                    //将图书设置为false
                    try{
                        sql = "update physicbooks set 图书状态 = false where 序号 = ?";
                        PreparedStatement prsm = con.prepareStatement(sql);
                        prsm.setString(1,bookId);
                        int ok = prsm.executeUpdate();
                    }catch (Exception e1){
                        System.out.println(e1.getMessage());
                    }

                    // 更新表格中的数据
                    refreshTable();

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        // 刷新按钮操作
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        tableFrame.setTitle("借阅书籍");
        tableFrame.setDefaultCloseOperation(2);
        tableFrame.pack();
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }

    //刷新表格
    private void refreshTable() {
        try (PreparedStatement brbookPrsm = con.prepareStatement("SELECT * FROM brbook WHERE 借阅者ID = ?")) {
            brbookPrsm.setString(1, borrowerId);
            try (ResultSet brbookRs = brbookPrsm.executeQuery()) {
                DefaultTableModel tableModel = new DefaultTableModel();
                while (brbookRs.next()) {

                    String bookID = brbookRs.getString("书籍编号");
                    try (PreparedStatement bookPrsm = con.prepareStatement("SELECT * FROM physicbooks WHERE 序号 = ?")) {
                        bookPrsm.setString(1, bookID);
                        try (ResultSet bookRs = bookPrsm.executeQuery()) {

                            if (bookRs.next()) {
                                ResultSetMetaData rsmd = bookRs.getMetaData();
                                int count = rsmd.getColumnCount() - 1;

                                Object[] name = new Object[count];
                                for (int i = 0; i < count; i++) {
                                    name[i] = rsmd.getColumnName(i + 1);
                                }
                                tableModel.setColumnIdentifiers(name);

                                Object[] row = new Object[count];
                                for (int i = 1; i <= count; i++) {
                                    row[i - 1] = bookRs.getObject(i);
                                }
                                tableModel.addRow(row);
                            }
                        }
                    }
                }
                table.setModel(tableModel);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //获得选中的行
    private String getBookId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return table.getValueAt(row, 0).toString();
    }

    public static void main(String[] args) {
        new ReturnBook();
    }
}
