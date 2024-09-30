package 图书管理系统.book;

import 图书管理系统.GetConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ViewBook extends JFrame {
    JTable table;
    Object [] name;
    Object [][] data;
    JButton addbutton,midibutton,delbutton,repaint;
    Connection con;
    JLabel label = new JLabel("图书名称:");
    JTextField text = new JTextField(10);
    JLabel label1 = new JLabel("图书作者:");
    JTextField text1 = new JTextField(10);
    JButton search = new JButton("查询");
    JLabel label2 = new JLabel("图书类别");
    JComboBox<String> comboBox = new JComboBox<>();


    public ViewBook(){
        setTitle("查看图书");
        setSize(600,500);
        setLocationRelativeTo(null);
        UiInit();



        setVisible(true);
        setDefaultCloseOperation(2);
    }

    private void UiInit() {
        //获得数据库中的数据
        table = new JTable();
        getTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        addbutton = new JButton("添加");
        midibutton = new JButton("修改");
        delbutton = new JButton("删除");
        repaint = new JButton("刷新");

        JPanel panel = new JPanel();
        panel.add(addbutton);
        panel.add(midibutton);
        panel.add(delbutton);
        panel.add(repaint);

        JPanel panel1 = new JPanel();

        JPanel panel2 = new JPanel();
        panel2.add(label);
        panel2.add(text);
        panel2.add(label1);
        panel2.add(text1);
        panel2.add(label2);

        // 给下拉框添加数据
        addcombox();

        panel2.add(comboBox);
        panel2.add(search);
        add(panel2, BorderLayout.NORTH);

        //刷新
        repaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTable();
            }
        });

        //添加
        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPbook();
            }
        });

        //修改
        midibutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow() != -1)
                    new EditPbook(getSelectedRowData());
            }
        });

        //删除
        delbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBook();
            }
        });

        search.addActionListener(e -> query());

        add(panel,BorderLayout.SOUTH);


    }

    private void getTable() {

        //连接数据库
        try {
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //查询图书
        String sql = "select * from physicbooks";

        try {
            PreparedStatement prsm = con.prepareStatement(sql);
            ResultSet rs = prsm.executeQuery();
            //获得列名
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();

            name = new Object[count];
            for(int i = 1;i <= count;i++){
                name[i - 1] = metaData.getColumnName(i);
            }

            //定义一个模型表格
            DefaultTableModel tableModel = new DefaultTableModel(name,0);
            while (rs.next()){
                Object [] row = new Object[count];
                for(int i = 1;i <= count;i++){
                    Object value = rs.getObject(i);
                    // 检查 value 是否为布尔类型的 false
                    if (value instanceof Boolean) {
                        if ((Boolean) value) {
                            row[i - 1] = "不可借阅"; // 布尔值 true 显示为“不可借阅”
                        } else {
                            row[i - 1] = "可借阅"; // 布尔值 false 显示为“可借阅”
                        }
                    } else {
                        row[i - 1] = value;
                    }
                }
                tableModel.addRow(row);
            }
            table.setModel(tableModel);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //获得选中的行数据
    public Object[] getSelectedRowData(){
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的图书");
            return null;
        }

        Object[] rowData = new Object[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) {
            rowData[i] = table.getValueAt(selectedRow, i);
        }

        return rowData;
    }
    //删除功能
    private void deleteSelectedBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的图书");
            return;
        }

        String id = table.getValueAt(selectedRow, 0).toString(); // 假设第一列是编号

        // 确认删除操作
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这本图书吗?", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // 删除数据库中的记录
        String sql = "DELETE FROM physicbooks WHERE 序号 = ?";
        try {
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1, id);
            prsm.executeUpdate();

            // 刷新表格
            getTable();

            JOptionPane.showMessageDialog(this, "图书删除成功");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "删除图书时出错");
        }
    }
    //给下拉框添加组件
    private void addcombox() {
        try {
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String query = "SELECT 书类名 FROM type"; // 修改为你的查询语句

        // 使用List来存储数据
        ArrayList<String> itemsList = new ArrayList<>();

        try (
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String item = rs.getString("书类名");
                if (!itemsList.contains(item)) {
                    itemsList.add(item);
                    comboBox.addItem(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据加载失败: " + e.getMessage());
        }
    }
    // 点击查询按钮
    private void query() {
        // 获取输入的数据
        String name = text.getText().toString();
        String author = text1.getText().toString();
        String category = comboBox.getSelectedItem().toString();


        try {
            // 创建SQL查询语句
            String sql = "SELECT * FROM physicbooks WHERE 书名 = ? AND 作者 = ? AND 类型 = ?";
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1, name);
            prsm.setString(2, author);
            prsm.setString(3, category);

            ResultSet rs = prsm.executeQuery();

            // 创建表模型
            DefaultTableModel tableModel = new DefaultTableModel();

            // 获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // 设置列名
            Object[] columnNames = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }
            tableModel.setColumnIdentifiers(columnNames);

            // 添加行数据
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    // 检查 value 是否为布尔类型的 false
                    if (value instanceof Boolean) {
                        if ((Boolean) value) {
                            row[i - 1] = "不可借阅"; // 布尔值 true 显示为“不可借阅”
                        } else {
                            row[i - 1] = "可借阅"; // 布尔值 false 显示为“可借阅”
                        }
                    } else {
                        row[i - 1] = value;
                    }
                }
                tableModel.addRow(row);
            }

            // 设置表模型
            table.setModel(tableModel);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库错误: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        new ViewBook();
    }
}
