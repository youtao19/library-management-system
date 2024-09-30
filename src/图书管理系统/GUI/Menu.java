package 图书管理系统.GUI;
import 图书管理系统.Borroewr.AddBorrower;
import 图书管理系统.Borroewr.ViewBorrower;
import 图书管理系统.book.AddPbook;
import 图书管理系统.book.ViewBook;
import 图书管理系统.书种.ViewType;
import 图书管理系统.借书.BorrBook;
import 图书管理系统.借书.ReturnBook;
import 图书管理系统.借书.YudingBook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    JMenuBar menubar = new JMenuBar();
    JMenu menu1,menu2,menu3,menu4;
    JMenuItem item1,item2,addItem,adduserItem,viewBorrower;
    JMenuItem add_bookItem,borrowItem,returnItem,reverseItem,viewbook;
    public Menu(){
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon("image/银河星空一个人风景4K.png"));
        add(label);
        init();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //创建菜单条
    private void init(){
        menu1 = new JMenu("功能");
        menu2 = new JMenu("借阅者管理模块");
        menu3 = new JMenu("基本业务模块");
        menu4 = new JMenu("图书维护模块");
        item1 = new JMenuItem("菜单项1");
        item2 = new JMenuItem("菜单项2");
        viewBorrower = new JMenuItem("查看借阅者");

        //基本业务模块 借书，还书，预定
        borrowItem = new JMenuItem("借书处理");
        borrowItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BorrBook();
            }
        });
        menu3.add(borrowItem);
        returnItem = new JMenuItem("还书处理");
        returnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReturnBook();
            }
        });
        menu3.add(returnItem);
        reverseItem = new JMenuItem("预定图书处理");
        reverseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new YudingBook();
            }
        });
        menu3.add(reverseItem);

        //图书维护模块 添加物理图书，修改物理图书，删除物理图书，添加书种，修改书中，删除书种
        addItem = new JMenuItem("添加物理图书");
        menu4.add(addItem);
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPbook();
            }
        });
        add_bookItem = new JMenuItem("添加书种");

        menu4.add(add_bookItem);
        viewbook = new JMenuItem("查看图书");
        menu4.add(viewbook);
        viewbook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewBook();
            }
        });
        //维护书种
        JMenuItem viewbooktype = new JMenuItem("查看书种");
        menu4.add(viewbooktype);
        viewbooktype.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewType();
            }
        });

        //借阅者模块 添加借阅者，修改借阅者，删除借阅者
        adduserItem = new JMenuItem("添加借阅者");
        adduserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBorrower();
            }
        });
        menu2.add(adduserItem);
        menu2.add(viewBorrower);
        viewBorrower.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewBorrower();
            }
        });

        menu1.add(menu2);
        menu1.add(menu3);
        menu1.add(menu4);
        menubar.add(menu1);
        setJMenuBar(menubar);
    }
    public static void main(String[] args) {
        new Menu();
    }

}
