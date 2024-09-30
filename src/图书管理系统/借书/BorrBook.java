package 图书管理系统.借书;

import 图书管理系统.GetConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class BorrBook extends JFrame implements ActionListener {
    JTable table = new JTable();
    Connection con;
    Object[] name;
    JButton borrow = new JButton("借书");
    JButton cancel = new JButton("返回");
    JLabel label = new JLabel("图书名称:");
    JTextField text = new JTextField(10);
    JLabel label1 = new JLabel("图书作者:");
    JTextField text1 = new JTextField(10);
    JButton search = new JButton("查询");
    JLabel label2 = new JLabel("图书类别");
    JComboBox<String> comboBox = new JComboBox<>();

    public BorrBook() {
        setTitle("借书");
        setSize(600, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        UiInit();
        setDefaultCloseOperation(2);
        setVisible(true);
    }

    private void UiInit() {
        getTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("刷新");
        JPanel panel = new JPanel();

        panel.add(borrow);
        panel.add(refresh);
        panel.add(cancel);
        add(panel, BorderLayout.SOUTH);

        JPanel panel1 = new JPanel();
        panel1.add(label);
        panel1.add(text);
        panel1.add(label1);
        panel1.add(text1);
        panel1.add(label2);

        // 给下拉框添加数据
        addcombox();

        panel1.add(comboBox);
        panel1.add(search);
        add(panel1, BorderLayout.NORTH);

        // 监视器添加

        // 返回
        cancel.addActionListener(e -> dispose());

        search.addActionListener(e -> query());

        // 借书
        borrow.addActionListener(this);
        refresh.addActionListener(e -> getTable());
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

    private void getTable() {
        // 连接数据库
        try {
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 查询图书
        String sql = "select * from physicbooks";

        try {
            PreparedStatement prsm = con.prepareStatement(sql);
            ResultSet rs = prsm.executeQuery();
            // 获得列名
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();

            name = new Object[count];
            for (int i = 1; i <= count; i++) {
                name[i - 1] = metaData.getColumnName(i);
            }

            // 定义一个模型表格
            DefaultTableModel tableModel = new DefaultTableModel(name, 0);
            while (rs.next()) {
                Object[] row = new Object[count];
                for (int i = 1; i <= count; i++) {
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

    public static void main(String[] args) {
        new BorrBook();
    }

    public String getID() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return table.getValueAt(row, 0).toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getID() == null) {
            JOptionPane.showMessageDialog(null, "请选择要借阅的图书！");
            return;
        }

        // 借书逻辑
        showBorrow(this);
    }

    private void showBorrow(JFrame frame) {
        // 检查图书是否可借阅
        try {
            String sql = "select 图书状态 from physicbooks where 序号 = ?";
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1, getID());
            ResultSet rs = prsm.executeQuery();
            while (rs.next()) {
                String bookstatus = rs.getString("图书状态");
                if (bookstatus.equals("1")) {
                    JOptionPane.showMessageDialog(null, "当前图书不可借阅");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // ID输入框
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
        JLabel idLabel = new JLabel("ID:    ");
        JTextField idField = new JTextField(10);
        idPanel.add(idLabel);
        idPanel.add(Box.createHorizontalStrut(10)); // 添加水平间距
        idPanel.add(idField);
        panel.add(idPanel);
        panel.add(Box.createVerticalStrut(15)); // 添加垂直间距

        // 密码输入框
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel("密码:");
        JPasswordField passwordField = new JPasswordField(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createHorizontalStrut(10)); // 添加水平间距
        passwordPanel.add(passwordField);
        panel.add(passwordPanel);

        // 显示输入对话框
        int result = JOptionPane.showConfirmDialog(frame, panel, "请输入ID和密码", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());

            // 验证ID和密码
            if (id.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "ID和密码不能为空！");
                return;
            }

            // 查询借阅者的ID和密码是否正确
            try {
                String sql = "SELECT * FROM borrower WHERE 借阅卡号 = ?";
                PreparedStatement prsm = con.prepareStatement(sql);
                prsm.setString(1, id);
                ResultSet rs = prsm.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "当前ID不存在");
                    return;
                }
                // 查密码
                String pw = rs.getString("密码");
                if (!pw.equals(password)) {
                    JOptionPane.showMessageDialog(null, "密码错误");
                    return;
                }

                // 将书的ID和用户ID存到表中
                sql = "INSERT INTO brbook (书籍编号, 借阅者ID) VALUES (?, ?)";
                prsm = con.prepareStatement(sql);
                prsm.setString(1, getID());
                prsm.setString(2, id);
                int count = prsm.executeUpdate();

                // 将书籍可借约修改为不可借阅
                sql = "update physicbooks set 图书状态 = true where 序号 = ?";
                prsm = con.prepareStatement(sql);
                prsm.setString(1, getID());
                int ok = prsm.executeUpdate();

                JOptionPane.showMessageDialog(null, "借阅成功");
                getTable();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
