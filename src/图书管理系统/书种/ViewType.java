package 图书管理系统.书种;

import 图书管理系统.GetConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewType extends JFrame {
    JTable table;
    JButton addType = new JButton("添加");
    JButton deleteType = new JButton("删除");
    JButton updateType = new JButton("修改");
    JButton refresh = new JButton("刷新");
    JButton cancel = new JButton("cancel");
    Connection con = null;


    public ViewType() {
        setLayout(new BorderLayout());

        UiInit();

        //监视器
        listen();

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(2);
        setVisible(true);

    }
    //监视器
    private void listen() {
        addType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 添加书种
                addType();
            }
        });

        updateType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 修改书种
                updatetype();
            }
        });

        deleteType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 删除书种
                deletetype();
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 刷新书种列表
                getTable();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 取消操作
                dispose();
            }
        });


    }

    //删除书籍
    private void deletetype() {
        String id = getSelectedId();
        if (id != null) {
            try {
                con = GetConnection.getconnection();
                String sql = "DELETE FROM type WHERE ID = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, id);
                ps.executeUpdate();
                getTable();
                JOptionPane.showMessageDialog(null, "删除成功");
                ps.close();
                con.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //修改书籍
    private void updatetype() {
        //创建一个面板，并在面板上添加两个输入框
        JPanel panel = new JPanel(new FlowLayout());
        JTextField typeNameField = new JTextField(15);
        JTextField remarksField = new JTextField(15);

        //添加标签和输入框到面板
        Box namebox = Box.createHorizontalBox();
        namebox.add(new JLabel("书种名称:"));
        namebox.add(Box.createHorizontalStrut(10));
        namebox.add(typeNameField);

        Box remarksbox = Box.createHorizontalBox();
        remarksbox.add(new JLabel("输入备注:"));
        remarksbox.add(Box.createHorizontalStrut(10));
        remarksbox.add(remarksField);

        Box vbox = Box.createVerticalBox();
        vbox.add(namebox);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(remarksbox);

        panel.add(vbox);

        //显示对话框
        int result = JOptionPane.showConfirmDialog(null, panel, "修改书种",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String typeName = typeNameField.getText();
            String remarks = remarksField.getText();
            String id = getSelectedId();

            if (typeName != null && !typeName.isEmpty() && remarks != null && !remarks.isEmpty()) {
                try {
                    con = GetConnection.getconnection();
                    String sql = "UPDATE type SET 书类名 = ?, 备注 = ? WHERE ID = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, typeName);
                    ps.setString(2, remarks);
                    ps.setString(3, id);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "修改成功！");

                    getTable();

                    ps.close();
                    con.close();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //添加书种
    private void addType() {
        // 创建一个面板，并在面板上添加两个输入框
        JPanel panel = new JPanel(new FlowLayout());
        JTextField typeNameField = new JTextField(15);
        JTextField remarksField = new JTextField(15);

        Box namebox = Box.createHorizontalBox();
        namebox.add(new JLabel("书种名称:"));
        namebox.add(Box.createHorizontalStrut(10));
        namebox.add(typeNameField);

        Box remarksbox = Box.createHorizontalBox();
        remarksbox.add(new JLabel("输入备注:"));
        remarksbox.add(Box.createHorizontalStrut(10));
        remarksbox.add(remarksField);

        Box vbox = Box.createVerticalBox();
        vbox.add(namebox);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(remarksbox);

        panel.add(vbox);

        // 显示对话框
        int result = JOptionPane.showConfirmDialog(null, panel, "添加书种",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String typeName = typeNameField.getText();
            String remarks = remarksField.getText();

            if (typeName != null && !typeName.isEmpty() && remarks != null && !remarks.isEmpty()) {
                try {
                    con = GetConnection.getconnection();
                    String sql = "INSERT INTO type (书类名, 备注) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, typeName);
                    ps.setString(2, remarks);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "添加成功");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // 刷新表格
                getTable();

            } else {
                JOptionPane.showMessageDialog(null, "书种名称和备注不能为空");
            }
        }
    }



    //界面搭建
    private void UiInit() {
        table = new JTable();

        getTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addType);
        buttonPanel.add(updateType);
        buttonPanel.add(deleteType);
        buttonPanel.add(refresh);
        buttonPanel.add(cancel);

        add(buttonPanel, BorderLayout.SOUTH);


    }

    //获取表格
    private void getTable() {

        try{
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            String sql = "select * from type";
            PreparedStatement ps = con.prepareStatement(sql);

            // 执行查询并填充表格
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            // 获取表格列数
            int numOfCols = rsmd.getColumnCount();

            // 创建表格数据数组
            Object[] name = new Object[numOfCols];
            for (int i = 0; i < numOfCols; i++) {
                name[i] = rsmd.getColumnName(i+1);
            }

            // 填充表格数据
            DefaultTableModel tableModel = new DefaultTableModel(name,0);

            while(rs.next()){
                Object[] rowData = new Object[numOfCols];
                for (int i = 0; i < numOfCols; i++) {
                    rowData[i] = rs.getObject(i+1);
                }
                tableModel.addRow(rowData);
            }

            table.setModel(tableModel);

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    //获取选中行id
    private String getSelectedId() {
        int row = table.getSelectedRow();
        if(row <= 0){
            JOptionPane.showMessageDialog(null,"请选择要删除的行！");
            return null;
        }
        return table.getValueAt(row, 0).toString();
    }



    public static void main(String[] args) {
        new ViewType();
    }


}
