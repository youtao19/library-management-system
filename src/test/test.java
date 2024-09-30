package test;

import 图书管理系统.book.ViewBook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class test {
    public static void main(String[] args) {
        new ViewBook();
    }
    public class LibrarySystem {
        // JDBC URL, 用户名和密码
        static final String JDBC_URL = "jdbc:mysql://localhost:3306/borrower";
        static final String JDBC_USER = "root";
        static final String JDBC_PASSWORD = "1219";

        public void main(String[] args) {
            SwingUtilities.invokeLater(() -> new LibrarySystem().createAndShowGUI());
        }

        private void createAndShowGUI() {
            JFrame frame = new JFrame("Library System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            // 设置表格列名
            tableModel.addColumn("id");
            tableModel.addColumn("用户名");
            tableModel.addColumn("密码");
            tableModel.addColumn("出生日期");
            tableModel.addColumn("年龄");
            tableModel.addColumn("借阅卡号");

            // 从数据库加载数据
            loadDataFromDatabase(tableModel);

            // 创建按钮并添加到面板
            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("添加");
            JButton deleteButton = new JButton("删除");
            JButton updateButton = new JButton("修改");
            JButton refreshButton = new JButton("刷新");

            buttonPanel.add(addButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(refreshButton);

            // 添加按钮的事件监听器
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 实现添加功能
                    addRow(tableModel);
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 实现删除功能
                    deleteRow(tableModel, table.getSelectedRow());
                }
            });

            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 实现修改功能
                    updateRow(tableModel, table.getSelectedRow());
                }
            });

            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 实现刷新功能
                    tableModel.setRowCount(0); // 清空表格
                    loadDataFromDatabase(tableModel); // 重新加载数据
                }
            });

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }

        private void loadDataFromDatabase(DefaultTableModel tableModel) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String query = "SELECT * FROM your_table"; // 根据需要修改查询语句
                try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
                    // 获取ResultSet的元数据
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // 添加表格数据
                    while (resultSet.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = resultSet.getObject(i);
                        }
                        tableModel.addRow(rowData);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "数据库连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void addRow(DefaultTableModel tableModel) {
            // 创建一个包含输入字段的对话框
            JTextField nameField = new JTextField(10);
            JTextField passwordField = new JTextField(10);
            JTextField birthDateField = new JTextField(10);
            JTextField ageField = new JTextField(10);
            JTextField cardNumberField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("用户名:"));
            panel.add(nameField);
            panel.add(new JLabel("密码:"));
            panel.add(passwordField);
            panel.add(new JLabel("出生日期:"));
            panel.add(birthDateField);
            panel.add(new JLabel("年龄:"));
            panel.add(ageField);
            panel.add(new JLabel("借阅卡号:"));
            panel.add(cardNumberField);

            int result = JOptionPane.showConfirmDialog(null, panel, "添加借阅者", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // 将新数据添加到数据库和表格中
                try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                    String query = "INSERT INTO your_table (用户名, 密码, 出生日期, 年龄, 借阅卡号) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, nameField.getText());
                        preparedStatement.setString(2, passwordField.getText());
                        preparedStatement.setString(3, birthDateField.getText());
                        preparedStatement.setInt(4, Integer.parseInt(ageField.getText()));
                        preparedStatement.setString(5, cardNumberField.getText());
                        preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "添加数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }

                tableModel.addRow(new Object[]{null, nameField.getText(), passwordField.getText(), birthDateField.getText(), ageField.getText(), cardNumberField.getText()});
            }
        }

        private void deleteRow(DefaultTableModel tableModel, int selectedRow) {
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0); // 获取id列的值
                try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                    String query = "DELETE FROM your_table WHERE id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, id);
                        preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "删除数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }

                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "请选择要删除的行", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void updateRow(DefaultTableModel tableModel, int selectedRow) {
            if (selectedRow >= 0) {
                // 获取选中行的现有数据
                Object[] rowData = new Object[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    rowData[i] = tableModel.getValueAt(selectedRow, i);
                }

                // 创建一个包含现有数据的对话框
                JTextField nameField = new JTextField(rowData[1].toString(), 10);
                JTextField passwordField = new JTextField(rowData[2].toString(), 10);
                JTextField birthDateField = new JTextField(rowData[3].toString(), 10);
                JTextField ageField = new JTextField(rowData[4].toString(), 10);
                JTextField cardNumberField = new JTextField(rowData[5].toString(), 10);

                JPanel panel = new JPanel(new GridLayout(5, 2));
                panel.add(new JLabel("用户名:"));
                panel.add(nameField);
                panel.add(new JLabel("密码:"));
                panel.add(passwordField);
                panel.add(new JLabel("出生日期:"));
                panel.add(birthDateField);
                panel.add(new JLabel("年龄:"));
                panel.add(ageField);
                panel.add(new JLabel("借阅卡号:"));
                panel.add(cardNumberField);

                int result = JOptionPane.showConfirmDialog(null, panel, "修改借阅者", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    // 更新数据库中的数据
                    try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                        String query = "UPDATE your_table SET 用户名 = ?, 密码 = ?, 出生日期 = ?, 年龄 = ?, 借阅卡号 = ? WHERE id = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setString(1, nameField.getText());
                            preparedStatement.setString(2, passwordField.getText());
                            preparedStatement.setString(3, birthDateField.getText());
                            preparedStatement.setInt(4, Integer.parseInt(ageField.getText()));
                            preparedStatement.setString(5, cardNumberField.getText());
                            preparedStatement.setInt(6, (int) rowData[0]); // 使用原始ID
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "更新数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }

                    // 更新表格中的数据
                    tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                    tableModel.setValueAt(passwordField.getText(), selectedRow, 2);
                    tableModel.setValueAt(birthDateField.getText(), selectedRow, 3);
                    tableModel.setValueAt(ageField.getText(), selectedRow, 4);
                    tableModel.setValueAt(cardNumberField.getText(), selectedRow, 5);
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择要修改的行", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
