
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                    System.out.println(e.toString());
                }
                riga = br.readLine();
            }

            riconoscimento=riconoscimento.split(" ")[1].substring(1); //prende la riga, divide per spazi
                                                                         //e toglie il "/" iniziale
            String estensione=riconoscimento.split("\\.")[1]; //prende l'estensione del file

            StringBuilder pagina = new StringBuilder();
            byte[] immagine = new byte[0];
            try {
                if(estensione.equals("html")) { //se è un file html
                    BufferedReader br2 = new BufferedReader(new FileReader(riconoscimento));
                    String line;
                    line = br2.readLine();
                    while (line != null) {
                        pagina.append(line);
                        line = br2.readLine();
                    }
                    br2.close();
                }
                else {
                    immagine = Files.readAllBytes(Paths.get(riconoscimento));
                }
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

            OutputStream ops=ss.getOutputStream(); //invio immagini
            PrintWriter bw = new PrintWriter(new OutputStreamWriter(ss.getOutputStream()), true);
            if (b && estensione.equals("html")) {
                invio(pagina, bw);
            }
            else if (b){
                invioFileMultimediali(immagine, ops);
            }
            bw.close();
            br.close();
            S.close();
        }
    }

    public static void invio(StringBuilder pagina, PrintWriter bw){
        bw.println("HTTP/1.1 200 OK");
        bw.println("Connection: keep-alive");
        bw.println("Content-Length: "+pagina.length());
        bw.println();
        bw.println(pagina);
    }

    public static void invioFileMultimediali(byte[] immagine, OutputStream ops) throws IOException {
        ops.write("HTTP/1.1 200 OK\n".getBytes());
        ops.write("Connection: keep-alive\n".getBytes());
        ops.write(("Content-Length: "+immagine.length+"\n").getBytes());
        ops.write("\n".getBytes());
        ops.write(immagine);
    }
}
