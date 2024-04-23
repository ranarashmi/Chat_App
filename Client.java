import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client{

    Socket s; 
    BufferedReader br;
    PrintWriter out;

    public Client(){
        try {
            System.out.println("Sending request to server");
            s = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done....");
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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