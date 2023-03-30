import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Student {
    Connection con;
    Statement stmt;
    ResultSet result;

    // Establish Connection to the Production Database

    Student() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // AWS RDS Database
            con = DriverManager.getConnection(
                    "jdbc:mysql://sayakdb-aws.c8l2rvtowbt0.eu-north-1.rds.amazonaws.com:3306/studentdb",
                    "admin",
                    "sayak007");
            stmt = con.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    // Theme Customisations

    Color themeColor = new Color(0, 162, 237);// (52, 88, 235);

    class MyButtonBlue extends JButton implements MouseListener {
        MyButtonBlue(String text) {
            super(text);
            setBackground(themeColor);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(themeColor));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(this);
        }

        // Implement MouseListener methods
        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(new Color(8, 142, 204)); // Set background color when mouse enters
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(themeColor); // Set background color back to default when mouse exits
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    class MyButtonWhite extends JButton implements MouseListener {
        MyButtonWhite(String text) {
            super(text);
            setBackground(Color.WHITE);
            setForeground(themeColor);
            setBorder(BorderFactory.createLineBorder(themeColor));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(this);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(Color.WHITE);
            setForeground(themeColor);
            setBorder(BorderFactory.createLineBorder(themeColor));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    // Global Variables

    static private JFrame frame;
    static private JLabel h1, h2;
    static private JPanel loginPanel, studentPanel, teacherPanel, adminPanel;
    // static private

    // Main Window Starting

    void window() {
        frame = new JFrame("Student Management System");
        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        loginPage();
    }

    // Login Page Method

    void loginPage() {
        JLabel userLabel, passLabel, image1, errorMsg;
        JTextField userField;
        JPasswordField passField;
        MyButtonBlue loginButton;

        h1 = new JLabel("Student Management System");
        h1.setBounds(250, 50, 700, 50);
        h1.setFont(new Font("Consolas", Font.PLAIN, 30));

        loginPanel = new JPanel();
        loginPanel.setBackground(Color.white);
        loginPanel.setBounds(80, 80, 880, 520);
        loginPanel.setLayout(null);

        h2 = new JLabel("Login to Your Account");
        h2.setFont(new Font("Consolas", Font.BOLD, 25));
        h2.setForeground(themeColor);
        h2.setBounds(520, 200, 300, 30);

        userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Consolas", Font.BOLD, 15));
        userLabel.setBounds(520, 270, 80, 30);

        userField = new JTextField("nikita.verma@gmail.com");
        userField.setBounds(520, 300, 300, 30);

        passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Consolas", Font.BOLD, 15));
        passLabel.setBounds(520, 340, 80, 25);

        passField = new JPasswordField("08112001");
        passField.setBounds(520, 370, 300, 30);

        ImageIcon imageIcon = new ImageIcon("src//Assets//login.png");
        Image image = imageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        image1 = new JLabel(scaledImageIcon);
        image1.setHorizontalAlignment(JLabel.LEFT);
        image1.setBounds(100, 50, 400, 500);

        errorMsg = new JLabel();
        errorMsg.setForeground(Color.red);
        errorMsg.setBounds(520, 400, 300, 25);

        loginButton = new MyButtonBlue("Login");
        loginButton.setBounds(620, 430, 90, 25);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String entered_User = userField.getText();
                String entered_pass = new String(passField.getPassword());
                try {
                    ResultSet user_result = validateCredentials(entered_User, entered_pass);

                    if (user_result == null) {
                        errorMsg.setText("Incorrect Username or Password !");
                    } else if (user_result.getString(3).equals("student")) {
                        errorMsg.setText("");
                        System.out.println("Login Approved as Student");
                        studentFrame(user_result);
                    } else if (user_result.getString(3).equals("teacher")) {
                        errorMsg.setText("");
                        System.out.println("Login Approved as Teacher");
                        teacherFrame(user_result);
                    } else if (user_result.getString(3).equals("admin")) {
                        errorMsg.setText("");
                        System.out.println("Login Approved as Admin");
                        adminFrame();
                    } else {
                        errorMsg.setText("Incorrect Username or Password !");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        frame.add(loginPanel);
        loginPanel.add(h1);
        loginPanel.add(h2);
        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        loginPanel.add(image1);
        loginPanel.add(errorMsg);
        loginPanel.add(loginButton);

        frame.setVisible(true);
    }

    // Method to Validate the Entered Credential

    public ResultSet validateCredentials(String username, String password) throws SQLException {
        result = stmt
                .executeQuery("select * from users where user_id ='" + username + "' and password= '" + password + "'");
        if (result.next()) {
            if (result.getString(1).equals(username) && result.getString(2).equals(password)) {
                return result;
            }
        }
        return null;
    }

    // Executed when the Entered User is a STUDENT

    void studentFrame(ResultSet user) throws SQLException {
        JLabel name, roll_no, father_name, dob, address, email, contact, course, branch;
        MyButtonBlue modifyButton;

        loginPanel.setVisible(false);

        // Find the Data of the Current User
        ResultSet currentUser = stmt
                .executeQuery("Select * from students,courses where students.Email_id='" + user.getString(1)
                        + "' and students.Course_ID=courses.Course_ID");
        currentUser.next();
        String user_email = currentUser.getString(5);

        studentPanel = new JPanel();
        studentPanel.setBackground(Color.white);
        studentPanel.setBounds(80, 80, 880, 520);
        studentPanel.setLayout(null);

        h1 = new JLabel("Welcome " + currentUser.getString(2) + ",");
        h1.setBounds(50, 40, 400, 50);
        h1.setFont(new Font("Consolas", Font.PLAIN, 30));

        name = new JLabel("Name : " + currentUser.getString(2) + " " + currentUser.getString(3));
        name.setFont(new Font("Consolas", Font.PLAIN, 16));
        name.setBounds(50, 120, 400, 30);

        roll_no = new JLabel("Roll No : " + currentUser.getString(1));
        roll_no.setFont(new Font("Consolas", Font.PLAIN, 16));
        roll_no.setBounds(50, 150, 400, 30);

        father_name = new JLabel("Father's Name : " + currentUser.getString(4));
        father_name.setFont(new Font("Consolas", Font.PLAIN, 16));
        father_name.setBounds(50, 180, 400, 30);

        dob = new JLabel("Date of Birth : " + currentUser.getString(6));
        dob.setFont(new Font("Consolas", Font.PLAIN, 16));
        dob.setBounds(50, 210, 400, 30);

        address = new JLabel("Address : " + currentUser.getString(7));
        address.setFont(new Font("Consolas", Font.PLAIN, 16));
        address.setBounds(50, 240, 600, 30);

        course = new JLabel("Course : " + currentUser.getString(11));
        course.setFont(new Font("Consolas", Font.PLAIN, 16));
        course.setBounds(50, 270, 400, 30);

        branch = new JLabel("Branch : " + currentUser.getString(12));
        branch.setFont(new Font("Consolas", Font.PLAIN, 16));
        branch.setBounds(50, 300, 400, 30);

        contact = new JLabel("Mobile : " + currentUser.getString(8));
        contact.setFont(new Font("Consolas", Font.PLAIN, 16));
        contact.setBounds(550, 120, 300, 30);

        email = new JLabel("Email : " + user_email);
        email.setFont(new Font("Consolas", Font.PLAIN, 16));
        email.setBounds(550, 150, 400, 30);

        modifyButton = new MyButtonBlue("Modify");
        modifyButton.setBounds(760, 460, 90, 25);
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                father_name.setVisible(false);
                address.setVisible(false);
                contact.setVisible(false);
                modifyButton.setVisible(false);
                modifyStudent(user_email, father_name, address, contact, modifyButton);
            }
        });

        // Add Components
        frame.add(studentPanel);
        studentPanel.add(h1);
        studentPanel.add(name);
        studentPanel.add(roll_no);
        studentPanel.add(father_name);
        studentPanel.add(dob);
        studentPanel.add(address);
        studentPanel.add(course);
        studentPanel.add(branch);
        studentPanel.add(contact);
        studentPanel.add(email);
        studentPanel.add(modifyButton);
        logoutBtn(studentPanel);

        frame.setVisible(true);
    }

    void modifyStudent(String email, JLabel father_name, JLabel address, JLabel contact, JButton modifyBtn) {

        JLabel father_name_New, address_New, contact_New;
        JTextField father_name_field, address_field, contact_field;
        MyButtonBlue modifyOK;
        MyButtonWhite modifyCancel;

        father_name_New = new JLabel("Father's Name : ");
        father_name_New.setFont(new Font("Consolas", Font.PLAIN, 16));
        father_name_New.setBounds(50, 180, 150, 30);

        address_New = new JLabel("Address : ");
        address_New.setFont(new Font("Consolas", Font.PLAIN, 16));
        address_New.setBounds(50, 240, 100, 30);

        contact_New = new JLabel("Mobile : ");
        contact_New.setFont(new Font("Consolas", Font.PLAIN, 16));
        contact_New.setBounds(550, 120, 100, 30);

        father_name_field = new JTextField(father_name.getText().replaceAll("Father's Name : ", ""));
        father_name_field.setBounds(190, 180, 200, 25);
        father_name_field.setFont(new Font("Consolas", Font.PLAIN, 14));
        father_name_field.setForeground(Color.BLACK);

        address_field = new JTextField(address.getText().replaceAll("Address : ", ""));
        address_field.setBounds(140, 240, 350, 25);
        address_field.setFont(new Font("Consolas", Font.PLAIN, 14));
        address_field.setForeground(Color.BLACK);

        contact_field = new JTextField(contact.getText().replaceAll("Mobile : ", ""));
        contact_field.setBounds(630, 120, 150, 25);
        contact_field.setFont(new Font("Consolas", Font.PLAIN, 14));
        contact_field.setForeground(Color.BLACK);

        modifyOK = new MyButtonBlue("OK");
        modifyOK.setBounds(760, 460, 90, 25);

        modifyCancel = new MyButtonWhite("Cancel");
        modifyCancel.setBounds(760, 430, 90, 25);

        modifyOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stmt.executeUpdate(
                            "UPDATE students SET Fathers_name='" + father_name_field.getText() + "',Address='"
                                    + address_field.getText() + "',Contact_no='" + contact_field.getText()
                                    + "' where Email_id='" + email + "';");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                modifyOK.setVisible(false);
                modifyCancel.setVisible(false);
                modifyBtn.setVisible(true);
                return;
            }

        });

        modifyCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            };
        });

        studentPanel.add(father_name_New);
        studentPanel.add(address_New);
        studentPanel.add(contact_New);
        studentPanel.add(father_name_field);
        studentPanel.add(address_field);
        studentPanel.add(contact_field);
        studentPanel.add(modifyOK);
        studentPanel.add(modifyCancel);
    }

    // Executed when the Entered User is a TEACHER

    void teacherFrame(ResultSet user) throws SQLException {
        loginPanel.setVisible(false);
        JLabel name, designation,id, dob, address, contact,email, course;
        
        MyButtonBlue modifyButton2;

        // Find the Data of the Curent User
        ResultSet currentUser = stmt
                .executeQuery("Select * from teachers where teachers.Email_id='" + user.getString(1) + "'");
        currentUser.next();
        String user_email = currentUser.getString(5);
        teacherPanel = new JPanel();
        teacherPanel.setBackground(Color.white);
        teacherPanel.setBounds(80, 80, 880, 520);
        teacherPanel.setLayout(null);

        h1 = new JLabel("Welcome " + currentUser.getString(2) + ",");
        h1.setBounds(50, 50, 400, 50);
        h1.setFont(new Font("Consolas", Font.PLAIN, 30));


        name = new JLabel("Name : " + currentUser.getString(2) + " " + currentUser.getString(3));
        name.setFont(new Font("Consolas", Font.BOLD, 20));
        name.setBounds(50, 120, 400, 30);

        id = new JLabel("Roll No : " + currentUser.getString(1));
        id.setFont(new Font("Consolas", Font.BOLD, 20));
        id.setBounds(50, 150, 400, 30);

        designation = new JLabel("Father's Name : " + currentUser.getString(4));
        designation.setFont(new Font("Consolas", Font.BOLD, 20));
        designation.setBounds(50, 180, 400, 30);

        dob = new JLabel("Date of Birth : " + currentUser.getString(7));
        dob.setFont(new Font("Consolas", Font.BOLD, 20));
        dob.setBounds(50, 210, 400, 30);

        address = new JLabel("Address : " + currentUser.getString(8));
        address.setFont(new Font("Consolas", Font.BOLD, 20));
        address.setBounds(50, 240, 600, 30);

        course = new JLabel("Course : " + currentUser.getString(9));
        course.setFont(new Font("Consolas", Font.BOLD, 20));
        course.setBounds(50, 270, 400, 30);

        contact = new JLabel("Mobile : " + currentUser.getString(6));
        contact.setFont(new Font("Consolas", Font.BOLD, 20));
        contact.setBounds(550, 120, 300, 30);

        email = new JLabel("Email : " + user_email);
        email.setFont(new Font("Consolas", Font.PLAIN, 16));
        email.setBounds(550, 150, 400, 30);

        modifyButton2 = new MyButtonBlue("Modify");
        modifyButton2.setBounds(760, 460, 90, 25);
        modifyButton2.addActionListener(new ActionListener() {
        //     @Override
            public void actionPerformed(ActionEvent e) {
                designation.setVisible(false);
                address.setVisible(false);
                contact.setVisible(false);
                modifyButton2.setVisible(false);
                modifyTeacher(user_email, designation, address, contact, modifyButton2);
            }
        });

        // Add Components
        frame.add(teacherPanel);
       teacherPanel.add(h1);
       teacherPanel.add(name);
        teacherPanel.add(id);
        teacherPanel.add(designation);
       teacherPanel.add(dob);
        teacherPanel.add(address);
        teacherPanel.add(course);
        teacherPanel.add(contact);
       teacherPanel.add(email);
        teacherPanel.add(modifyButton2);
        logoutBtn(teacherPanel);

        frame.setVisible(true);
    
    }
    void modifyTeacher(String email, JLabel designation, JLabel address, JLabel contact, JButton modifyBtn) {

        JLabel designation_New, address_New, contact_New;
        JTextField designation_field, address_field, contact_field;
        MyButtonBlue modifyOK;
        MyButtonWhite modifyCancel;

        designation_New = new JLabel("Designation : ");
        designation_New.setFont(new Font("Consolas", Font.BOLD, 16));
        designation_New.setBounds(50, 180, 150, 30);

        address_New = new JLabel("Address : ");
        address_New.setFont(new Font("Consolas", Font.BOLD, 16));
        address_New.setBounds(50, 240, 100, 30);

        contact_New = new JLabel("Mobile : ");
        contact_New.setFont(new Font("Consolas", Font.BOLD, 16));
        contact_New.setBounds(550, 120, 100, 30);

       designation_field = new JTextField(designation.getText().replaceAll("Father's Name : ", ""));
       designation_field.setBounds(190, 180, 200, 25);
       designation_field.setFont(new Font("Consolas", Font.BOLD, 14));
       designation_field.setForeground(Color.BLACK);

        address_field = new JTextField(address.getText().replaceAll("Address : ", ""));
        address_field.setBounds(140, 240, 350, 25);
        address_field.setFont(new Font("Consolas", Font.BOLD, 14));
        address_field.setForeground(Color.BLACK);

        contact_field = new JTextField(contact.getText().replaceAll("Mobile : ", ""));
        contact_field.setBounds(630, 120, 150, 25);
        contact_field.setFont(new Font("Consolas", Font.BOLD, 14));
        contact_field.setForeground(Color.BLACK);

        modifyOK = new MyButtonBlue("OK");
        modifyOK.setBounds(760, 460, 90, 25);

        modifyCancel = new MyButtonWhite("Cancel");
        modifyCancel.setBounds(760, 430, 90, 25);

        modifyOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stmt.executeUpdate(
                            "UPDATE teacher's SET designation='" + designation_field.getText() + "',Address='"
                                    + address_field.getText() + "',Contact_no='" + contact_field.getText()
                                    + "' where Email_id='" + email + "';");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                modifyOK.setVisible(false);
                modifyCancel.setVisible(false);
                modifyBtn.setVisible(true);
                return;
            }

        });

        modifyCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            };
        });

        teacherPanel.add(designation_New);
        teacherPanel.add(address_New);
        teacherPanel.add(contact_New);
        teacherPanel.add(designation_field);
        teacherPanel.add(address_field);
        teacherPanel.add(contact_field);
        teacherPanel.add(modifyOK);
        teacherPanel.add(modifyCancel);
    }

    // Executed when the Entered User is a ADMIN

    void adminFrame() throws SQLException {
        loginPanel.setVisible(false);

        adminPanel = new JPanel();
        adminPanel.setBackground(Color.white);
        adminPanel.setBounds(80, 80, 880, 520);
        adminPanel.setLayout(null);

        h1 = new JLabel("Administrator");
        h1.setBounds(50, 50, 400, 50);
        h1.setFont(new Font("Consolas", Font.PLAIN, 30));

        frame.add(adminPanel);
        adminPanel.add(h1);
        logoutBtn(adminPanel);

        frame.setVisible(true);
    }

    // LOGOUT Button Method

    void logoutBtn(JPanel removePanel) {
        MyButtonBlue logoutButton = new MyButtonBlue("Log Out");
        logoutButton.setBounds(760, 50, 90, 25);
        removePanel.add(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutButton.setEnabled(false);
                JFrame popup = new JFrame("Confirm Logout");
                popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                popup.setLayout(null);
                popup.setSize(350, 200);
                popup.setLocationRelativeTo(studentPanel);
                popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                popup.setVisible(true);

                JPanel popPanel = new JPanel();
                popPanel.setBackground(Color.WHITE);
                popPanel.setBounds(0, 0, 350, 200);
                popPanel.setLayout(null);

                JLabel logoutMSG = new JLabel("Do you want to Log Out?");
                logoutMSG.setFont(new Font("Consolas", Font.BOLD, 17));
                logoutMSG.setBounds(50, 25, 250, 30);

                MyButtonBlue yesBtn = new MyButtonBlue("Yes");
                yesBtn.setBounds(40, 80, 250, 30);
                yesBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removePanel.setVisible(false);
                        loginPanel.setVisible(true);
                        popup.dispose();
                    }
                });

                MyButtonWhite noBtn = new MyButtonWhite("No");
                noBtn.setBounds(40, 115, 250, 30);
                noBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        popup.dispose();
                        logoutButton.setEnabled(true);
                    }
                });

                popup.add(popPanel);
                popPanel.add(logoutMSG);
                popPanel.add(yesBtn);
                popPanel.add(noBtn);
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        Student student = new Student();
        student.window();
    }
}