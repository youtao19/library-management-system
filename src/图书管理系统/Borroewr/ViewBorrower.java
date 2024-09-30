package 图书管理系统.Borroewr;

import 图书管理系统.GetConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewBorrower extends JFrame {
    JTable table;
    Object [] name;
    Connection con;
    JButton ad,del,modi,repaint;

    public ViewBorrower(){
        setSize(600,400);
        setLocationRelativeTo(null);
        UIinit();
        setVisible(true);
    }

    private void UIinit() {
        setLayout(new BorderLayout());
        table = new JTable();

        //查询表数据
        query();

        add(new JScrollPane(table),BorderLayout.CENTER);
        ad = new JButton("添加");
        ad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBorrower();
            }
        });
        del = new JButton("删除");
        modi = new JButton("修改");

        //修改
        modi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ChangeBorrower(getSelectedRowData());
            }
        });

        //删除
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delBorrower();
            }
        });


        repaint = new JButton("刷新");

        //刷新
        repaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query();
            }
        });

        JPanel panel = new JPanel();
        panel.add(ad);
        panel.add(modi);
        panel.add(del);
        panel.add(repaint);

        add(panel,BorderLayout.SOUTH);

    }

    private void delBorrower() {
        String id = getSelected();

        int confirm = JOptionPane.showConfirmDialog(this,"确认删除当前用户","删除用户",JOptionPane.YES_NO_OPTION);

        if(confirm != JOptionPane.YES_OPTION){
            return;
        }

        String sql = "delete from borrower where 编号 = ?";
        try {
            PreparedStatement prsm = con.prepareStatement(sql);
            prsm.setString(1,id);
            int count = prsm.executeUpdate();
            if (count > 0){
                JOptionPane.showMessageDialog(this,"删除成功");
            }else JOptionPane.showMessageDialog(this,"删除失败");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private void query() {
        //连接数据库
        try{
            con = GetConnection.getconnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //查询数据
        String sql = "select * from borrower";
        try {
            PreparedStatement prsm = con.prepareStatement(sql);
            ResultSet rs = prsm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            //列名
            name = new Object[count];
            for(int i = 1;i <= count;i++){
                name[i -1 ] = metaData.getColumnName(i);
            }

            //数据
            DefaultTableModel tableModel = new DefaultTableModel(name,0);
            while(rs.next()){
                Object []row = new Object[count];
                for(int i = 1;i <= count;i++){
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            table.setModel(tableModel);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getSelected(){
        int selected = table.getSelectedRow();
        String id = table.getValueAt(selected,0).toString();
        return id;
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
    public static void main(String[] args) {
        new ViewBorrower();
    }
}
