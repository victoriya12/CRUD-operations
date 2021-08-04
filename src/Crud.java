import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Crud {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtPrice;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField txtID;
    private JTextField txtQuantity;
    private JButton searchButton;
    private JButton showAllButton;
    private JTable products;
    private JButton show;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Crud");
        frame.setContentPane(new Crud().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public Crud() {
        connect();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, price, quantity;

                name = txtName.getText();
                price = txtPrice.getText();
                quantity = txtQuantity.getText();

                try {
                    pst = con.prepareStatement("Insert into products (name,price,quantity) values(?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, quantity);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added successfully!");
                    txtName.setText("");
                    txtPrice.setText("");
                    txtQuantity.setText("");
                    txtName.requestFocus();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, price, quantity;
                try {
                    String id = txtID.getText();
                    pst = con.prepareStatement("select name,price,quantity from products where id=?");
                    pst.setString(1, id);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        name = rs.getString(1);
                        price = rs.getString(2);
                        quantity = rs.getString(3);

                        txtName.setText(name);
                        txtPrice.setText(price);
                        txtQuantity.setText(quantity);
                    } else {
                        txtName.setText("");
                        txtPrice.setText("");
                        txtQuantity.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid product ID");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id, name, price, quantity;
                id = txtID.getText();
                name = txtName.getText();
                price = txtPrice.getText();
                quantity = txtQuantity.getText();

                try {
                    pst = con.prepareStatement("update products set name=?,price=?,quantity=? where id=?");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, quantity);
                    pst.setString(4, id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record updated!");

                    txtName.setText("");
                    txtPrice.setText("");
                    txtQuantity.setText("");
                    txtID.setText("");
                    txtName.requestFocus();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id;
                id = txtID.getText();

                try {
                    pst = con.prepareStatement("delete from products where id=?");
                    pst.setString(1, id);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record deleted");
                    txtName.setText("");
                    txtPrice.setText("");
                    txtQuantity.setText("");
                    txtID.setText("");
                    txtName.requestFocus();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultTableModel dtm=new DefaultTableModel();
                    dtm.addColumn("ID");
                    dtm.addColumn("Name");
                    dtm.addColumn("Price");
                    dtm.addColumn("Quantity");
                    String sql="select * from products";
                    Statement st=con.createStatement();
                    pst=con.prepareStatement(sql);
                    ResultSet rs=pst.executeQuery();
                    while (rs.next()){
                        Object object= new Object []{rs.getString("id"), rs.getString("name"),
                                rs.getString("price"), rs.getString("quantity")};
                        dtm.addRow((Object[]) object);
                    }
                    products.setModel(dtm);
                    products.setAutoResizeMode(0);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/products", "root", "");
            System.out.printf("Successfully connected to the database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
