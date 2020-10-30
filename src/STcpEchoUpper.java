
import java.io.*;
import java.net.*;

public class STcpEchoUpper {

    public static void main(String[] args) throws IOException {
        int udd=0;
        String pagina="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>Hi there</title>\n" + //banana
                "</head>\n" +
                "<body>\n" +
                "  This is a page \n" +
                "  a simple page\n" +
                "</body>\n" +
                "</html>";
        int PORTA = 2000;  // porta di ascolto
        ServerSocket S;

        S = new ServerSocket(PORTA);
        Socket ss = S.accept();
        //ss connesso con client

        BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
        PrintWriter bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
        String riga=br.readLine();
        boolean b=false;
        while (!riga.equals("")) {
            System.out.println(">"+riga);
            try {
                if (riga.startsWith("GET")) {
                    b=true;
                }
            }
            catch(NullPointerException e){
                System.out.println(e);
            }
            riga=br.readLine();
        }
        if(b){
            invio(pagina,bw);
        }
        bw.close();
        br.close();
        S.close();
    }

    public static void invio(String pagina, PrintWriter bw){
        bw.println("HTTP/1.1 200 OK");
        bw.println("Content-Length: "+pagina.length());
        bw.println();
        bw.println(pagina);
    }
}
