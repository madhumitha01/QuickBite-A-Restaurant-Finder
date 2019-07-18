

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UserForm extends JFrame {
    static UserForm frame;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JPasswordField passwordField;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new UserForm();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public UserForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 390);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblAddLibrarian = new JLabel("Add User");
        lblAddLibrarian.setForeground(Color.DARK_GRAY);
        lblAddLibrarian.setFont(new Font("Tahoma", Font.PLAIN, 22));

        JLabel lblName = new JLabel("Name:");

        JLabel lblPassword = new JLabel("Password:");

        JLabel lblEmail = new JLabel("Email ID:");

        JLabel lblAddress = new JLabel("Address:");

        JLabel lblContactNo = new JLabel("Contact No:");

        textField = new JTextField();
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setColumns(10);

        passwordField = new JPasswordField();

        JButton btnNewButton = new JButton("Add User");



        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = textField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String email = textField_1.getText();
                String address = textField_2.getText();
                String contact = textField_3.getText();

                if (name == null || name.trim().equals("")) {
                    JOptionPane.showMessageDialog(UserForm.this, "Username can't be blank",
                            "Login Error!", JOptionPane.ERROR_MESSAGE);
                }
                else if (password == null || password.trim().equals("")) {
                    JOptionPane.showMessageDialog(UserForm.this, "Password can't be blank",
                            "Login Error!", JOptionPane.ERROR_MESSAGE);
                }
                else if (email == null || email.trim().equals("")|| !email.contains("@gmail.com")) {
                    JOptionPane.showMessageDialog(UserForm.this, "Please provide a valid email ID for sending the confirmation mail",
                            "Login Error!", JOptionPane.ERROR_MESSAGE);
                }
                else if (contact.length()!=10){
                    JOptionPane.showMessageDialog(UserForm.this, "Please provide a valid contact number",
                            "Login Error!", JOptionPane.ERROR_MESSAGE);
                }

                else{
                    try{
                        Double.parseDouble(contact);
                int i = UserDao.save(name, password, email, address, contact);
                if (i > 0) {

                    //JOptionPane.showMessageDialog(UserForm.this, "Please check your email id ");
                    Properties props = new Properties();
                    props.put("mail.smtp.auth","true");
                    props.put("mail.smtp.starttls.enable","true");
                    props.put("mail.smtp.host","smtp.gmail.com");
                    //props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.port","587");

                    Session session = Session.getDefaultInstance(props,
                            new javax.mail.Authenticator(){
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication("quickbitefinder@gmail.com","shubhammadhu");

                                }
                            }
                            );

                    try{
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("quickbitefinder@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("quickbitefinder@gmail.com, madhumitharajinikanthan@gmail.com"));

                        message.setSubject("QuickBite: Registration");
                        //message.setText("Hi, This email confirms your registration for QuickBite application. You can use the username and password for your further login.");

                        // This mail has 2 part, the BODY and the embedded image
                        MimeMultipart multipart = new MimeMultipart("related");

                        // first part (the html)
                        BodyPart messageBodyPart = new MimeBodyPart();
                        String htmlText = "<style>\n" +
                                "body {\n" +
                                "  background-color: black;\n" +
                                " \n" +
                                "  color: white;\n" +
                                "  font-family: Arial, Helvetica, sans-serif;\n" +
                                "}\n" +
                                "</style>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>QuickBite</h1>\n" +
                                "<p>Dear Customer,</p>\n" +
                                "<p>Your new QuickBite account has been created successfully. Welcome to the QB Community!</p>\n" +
                                "<p>From now on, please log in to your account using your username and password.<p>\n" +
                                "\n" +
                                "<h5>Thank you for registering! <br/> <br/> The QuickBite Team.<h5>\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "</body><img src=\"cid:image\">";
                        messageBodyPart.setContent(htmlText, "text/html");
                        // add it
                        multipart.addBodyPart(messageBodyPart);

                        // second part (the image)
                        messageBodyPart = new MimeBodyPart();
                        DataSource fds = new FileDataSource(
                                "C:\\Users\\pavithra\\Desktop\\QuickBite\\Restaurant-Search\\1.png");

                        messageBodyPart.setDataHandler(new DataHandler(fds));
                        messageBodyPart.setHeader("Content-ID", "<image>");

                        // add image to the multipart
                        multipart.addBodyPart(messageBodyPart);

                        // put everything together
                        message.setContent(multipart);

                        Transport.send(message);
                        System.out.println("Registration confirm");
                    }catch(Exception ae){
                        JOptionPane.showMessageDialog(null,ae);
                    }


                    JOptionPane.showMessageDialog(UserForm.this, "User added successfully!");
                    UserSuccess.main(new String[]{});
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(UserForm.this, "Sorry, unable to save!",
                            "Login Error!", JOptionPane.ERROR_MESSAGE);
                }
                    }
                    catch (NumberFormatException exp){
                        JOptionPane.showMessageDialog(UserForm.this, "Contact number cannot be characters",
                                "Login Error!", JOptionPane.ERROR_MESSAGE);
                    }
            }}
        });
        btnNewButton.setForeground(Color.DARK_GRAY);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Restaurant.main(new String[]{});
                frame.dispose();
            }
        });
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(20)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(lblPassword, GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                        .addComponent(lblName)
                                        .addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblAddress, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblContactNo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(58)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(passwordField))
                                .addContainerGap(107, Short.MAX_VALUE))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap(151, Short.MAX_VALUE)
                                .addComponent(lblAddLibrarian)
                                .addGap(144))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap(160, Short.MAX_VALUE)
                                .addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                                .addGap(133))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap(200, Short.MAX_VALUE)
                                .addComponent(btnBack)
                                .addGap(169))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(lblAddLibrarian)
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(lblName)
                                                .addGap(18)
                                                .addComponent(lblPassword))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblEmail)
                                        .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblAddress)
                                        .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblContactNo)
                                        .addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                                .addComponent(btnBack)
                                .addGap(19))
        );
        contentPane.setLayout(gl_contentPane);
    }

}
