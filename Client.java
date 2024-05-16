import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client extends JFrame{

    Socket s; 
    BufferedReader br;
    PrintWriter out;

    //Declare component
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);



    //constructor
    public Client(){
        try {
            /*System.out.println("Sending request to server");
            s = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done....");
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());*/
            createGUI();
            /*startReading();
            startWriting();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        //For GUI
        this.setTitle("Client Messanger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //For Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("clong.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        //this.getContentPane().setBackground(new Color(15, 242, 231));



        //For Frame Layout
        this.setLayout(new BorderLayout());

        //For adding component to frame
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
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
                    System.out.println("Server turminated the chat");
                    s.close();
                    break;
                }

                System.out.println("Server: "+msg);
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