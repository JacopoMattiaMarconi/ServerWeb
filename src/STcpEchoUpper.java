
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.net.*;

public class STcpEchoUpper {
    public static void main(String[] args) throws IOException {
        while (true) {
            int PORTA = 2000;  // porta di ascolto
            ServerSocket S;

            S = new ServerSocket(PORTA);
            Socket ss = S.accept();
            //ss connesso con client

            BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            String riga = br.readLine();
            boolean b = false;
            String riconoscimento="";
            while (!riga.equals("")) {
                System.out.println(">" + riga);
                try {
                    if (riga.startsWith("GET")) {
                        b = true;
                        riconoscimento=riga;
                    }
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                riga = br.readLine();
            }
            riconoscimento=riconoscimento.split(" ")[1].substring(1); //prende la riga, divide per spazi
                                                                         //e toglie il "/" iniziale

            //OutputStream os= new OutputStream(ss.getOutputStream());
            StringBuilder pagina = new StringBuilder();
            try {
                BufferedReader br2 = new BufferedReader(new FileReader(riconoscimento));
                String line = "";
                line = br2.readLine();
                while (line != null) {
                    pagina.append(line);
                    line = br2.readLine();
                }
                br2.close();
            }
            catch (FileNotFoundException e){
                System.out.println("ERROR 404");
                pagina.append("<html>\n" +
                        "<head>\n" +
                        "<h1>\n" +
                        "ERROR 404: page not found\n" +
                        "</h1>\n" +
                        "</head>\n" +
                        "<body>\n"+
                        "per sapere i file presenti clicca qui:\n"+
                        "<br>"+
                        "http://127.0.0.1:2000/info.html\n"+
                        "</body>"+
                        "</html>");
            }

            System.out.println("\n-------------------------------\n");

            PrintWriter bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
            if (b) {
                invio(pagina, bw);
            }
            bw.close();
            br.close();
            S.close();
        }
    }

    public static void invio(StringBuilder pagina, PrintWriter bw){
        bw.println("HTTP/1.1 200 OK");
        bw.println("Content-Length: "+pagina.length());
        bw.println();
        bw.println(pagina);
    }
}
