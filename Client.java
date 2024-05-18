import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client extends JFrame{

    Socket s; 
    BufferedReader br;
    PrintWriter out;

    //Declare component
    private final JLabel heading = new JLabel("Client Area");
    private final JTextArea messageArea = new JTextArea();
    private final JTextField messageInput = new JTextField();
    private final Font font = new Font("Roboto",Font.PLAIN,20);



    //constructor
    public Client(){
        try {
            System.out.println("Sending request to server");
            s = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done....");
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        //For GUI
        this.setTitle("Client Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //For Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("images.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        //this.getContentPane().setBackground(new Color(15, 242, 231));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEnabled(false);
        messageArea.setDisabledTextColor(Color.BLACK);



        //For Frame Layout
        this.setLayout(new BorderLayout());

        //For adding component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    //StartReading
    public void startReading(){

        Runnable r1 = ()->{
            System.out.println("Reading started...");
         try {
            while (!s.isClosed()) {
                    String msg = br.readLine();
                if(msg.equals("exit")){
                    JOptionPane.showMessageDialog(this,"Server terminated the chat");
                    messageInput.setEnabled(false);
                    s.close();
                    break;
                }
                messageArea.append("Server: "+msg+ "\n");
                }
            } catch (Exception e) {
                // e.printStackTrace();
               System.out.println("Connection closed");
            }
        };

        new Thread(r1).start();

    }

    //StartWriting
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("Writting started...");
         try {
            while (!s.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        s.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
               System.out.println("Connection closed");
            }
        };
        new Thread(r2).start();
    }
 


    public static void main(String args[]){
        System.out.println("This is client......");
        new Client();
    }
}