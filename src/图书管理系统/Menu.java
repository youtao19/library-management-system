package 图书管理系统;
import javax.swing.*;

public class Menu extends JFrame{
    JMenuBar menubar = new JMenuBar();
    JMenu menu1,menu2,menu3,menu4;
    JMenuItem item1,item2;
    public Menu(){
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        //基本业务模块 借书，还书，预定
        JMenuItem borrowItem = new JMenuItem("借书处理");
        menu3.add(borrowItem);
        JMenuItem returnItem = new JMenuItem("还书处理");
        menu3.add(returnItem);
        JMenuItem reverseItem = new JMenuItem("预定图书处理");
        menu3.add(reverseItem);

        //图书维护模块 添加物理图书，修改物理图书，删除物理图书，添加书种，修改书中，删除书种
        JMenuItem addItem = new JMenuItem("添加物理图书");
        menu4.add(addItem);
        JMenuItem revisItem = new JMenuItem("修改物理图书");
        menu4.add(revisItem);
        JMenuItem delItem = new JMenuItem("修改物理图书");
        menu4.add(delItem);
        JMenuItem add_bookItem = new JMenuItem("添加书种");
        menu4.add(add_bookItem);
        JMenuItem rever_bookItem = new JMenuItem("修改书种");
        menu4.add(rever_bookItem);
        JMenuItem del_bookItem = new JMenuItem("删除书种");
        menu4.add(del_bookItem);

        //借阅者模块 添加借阅者，修改借阅者，删除借阅者
        JMenuItem adduserItem = new JMenuItem("添加借阅者");
        menu2.add(adduserItem);
        JMenuItem reveruserItem = new JMenuItem("修改借阅者");
        menu2.add(reveruserItem);
        JMenuItem deluserItem = new JMenuItem("删除借阅者");
        menu2.add(deluserItem);

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
