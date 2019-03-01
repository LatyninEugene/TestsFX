package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WebServer extends Thread{

    Socket s;

    public WebServer(Socket s) {
        this.s = s;

        // � ��������� ����� �������������� ����� (��. �-� run())
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public static void main(String[] args) {
        try
        {
            // ���������� ����� �� ���������, ���� 80
            ServerSocket server = new ServerSocket(80, 0,
                    InetAddress.getByName("localhost"));

            System.out.println("server is started");

            // ������� ����
            while(true)
            {
                // ��� ������ �����������, ����� ���� ��������� ��������� �������
                // � ����� �������������� �����
                new WebServer(server.accept());
            }
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // ����� ����������
    }

    @Override
    public void run() {
        try
        {
            // �� ������ ������� ���� ����� �������� ������
            InputStream is = s.getInputStream();
            // � ������ �� - ����� ������ �� ������� � �������
            OutputStream os = s.getOutputStream();

            // ������ ������ � 64 ���������
            byte buf[] = new byte[64*1024];
            // ������ 64�� �� �������, ��������� - ���-�� ������� �������� ������
            int r = is.read(buf);

            // ������ ������, ���������� ��������� �� ������� ����������
            String request = new String(buf, 0, r);

            // �������� ���� �� ��������� (��. ���� �-� "getPath")
            String path = getPath(request);

            // ���� �� ������� �� ������� �������� ����, ��
            // ���������� "400 Bad Request"
            if(path == null)
            {
                // ������ ������ ������
                String response = "HTTP/1.1 400 Bad Request\n";

                // ���� � GMT
                DateFormat df = DateFormat.getTimeInstance();
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                response = response + "Date: " + df.format(new Date()) + "\n";

                // ��������� ���������
                response = response
                        + "Connection: close\n"
                        + "Server: SimpleWEBServer\n"
                        + "Pragma: no-cache\n\n";

                // ������� ������:
                os.write(response.getBytes());

                // ��������� ����������
                s.close();

                // �����
                return;
            }


            // ���� ���� ���������� � �������� �����������,
            // �� ���� ��������� ���� index.html
            File f = new File(path);
            boolean flag = !f.exists();
            if(!flag) if(f.isDirectory())
            {
                if(path.lastIndexOf(""+File.separator) == path.length()-1)
                    path = path + "index.html";
                else
                    path = path + File.separator + "index.html";
                f = new File(path);
                flag = !f.exists();
            }

            // ���� �� ���������� ���� ���� �� ������
            // �� ������� ������ "404 Not Found"
            if(flag)
            {
                // ������ ������ ������
                String response = "HTTP/1.1 404 Not Found\n";

                // ���� � GMT
                DateFormat df = DateFormat.getTimeInstance();
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                response = response + "Date: " + df.format(new Date()) + "\n";

                // ��������� ���������
                response = response
                        + "Content-Type: text/plain\n"
                        + "Connection: close\n"
                        + "Server: SimpleWEBServer\n"
                        + "Pragma: no-cache\n\n";

                // � ������� ���������
                response = response + "File " + path + " not found!";

                // ������� ������:
                os.write(response.getBytes());

                // ��������� ����������
                s.close();

                // �����
                return;
            }

            // ���������� MIME ����� �� ����������
            // MIME �� ��������� - "text/plain"
            String mime = "text/plain";

            // �������� � ����� ���������� (�� �����)
            r = path.lastIndexOf(".");
            if(r > 0)
            {
                String ext = path.substring(r);
                if(ext.equalsIgnoreCase("html"))
                    mime = "text/html";
                else if(ext.equalsIgnoreCase("htm"))
                    mime = "text/html";
                else if(ext.equalsIgnoreCase("gif"))
                    mime = "image/gif";
                else if(ext.equalsIgnoreCase("jpg"))
                    mime = "image/jpeg";
                else if(ext.equalsIgnoreCase("jpeg"))
                    mime = "image/jpeg";
                else if(ext.equalsIgnoreCase("bmp"))
                    mime = "image/x-xbitmap";
            }

            // ������ �����

            // ������ ������ ������
            String response = "HTTP/1.1 200 OK\n";

            // ���� �������� � GMT
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));

            // ����� ��������� ����������� ����� � GMT
            response = response + "Last-Modified: " + df.format(new Date(f.lastModified())) + "\n";

            // ����� �����
            response = response + "Content-Length: " + f.length() + "\n";

            // ������ � MIME ����������
            response = response + "Content-Type: " + mime + "\n";

            // ��������� ���������
            response = response
                    + "Connection: close\n"
                    + "Server: SimpleWEBServer\n\n";

            // ������� ���������:
            os.write(response.getBytes());

            // � ��� ����:
            FileInputStream fis = new FileInputStream(path);
            r = 1;
            while(r > 0)
            {
                r = fis.read(buf);
                if(r > 0) os.write(buf, 0, r);
            }
            fis.close();

            // ��������� ����������
            s.close();
        }
        catch(Exception e)
        {e.printStackTrace();} // ����� ����������
    }

    protected String getPath(String header)
    {
        // ���� URI, ��������� � HTTP �������
        // URI ������ ������ ��� ������� POST � GET, ����� ������������ null
        String URI = extract(header, "GET ", " "), path;
        if(URI == null) URI = extract(header, "POST ", " ");
        if(URI == null) return null;

        // ���� URI ������� ������ � ������ ���������
        // �� ������� �������� � ��� �����
        path = URI.toLowerCase();
        if(path.indexOf("http://", 0) == 0)
        {
            URI = URI.substring(7);
            URI = URI.substring(URI.indexOf("/", 0));
        }
        else if(path.indexOf("/", 0) == 0)
            URI = URI.substring(1); // ���� URI ���������� � ������� /, ������� ���

        // �������� �� URI ����� �������, ������� ����� �������� ? � #
        int i = URI.indexOf("?");
        if(i > 0) URI = URI.substring(0, i);
        i = URI.indexOf("#");
        if(i > 0) URI = URI.substring(0, i);

        // ������������ URI � ���� �� ����������
        // ��������������, ��� ��������� ����� ��� ��, ��� � ������
        // ����� ���� ����� �������������� path
        path = "." + File.separator;
        char a;
        for(i = 0; i < URI.length(); i++)
        {
            a = URI.charAt(i);
            if(a == '/')
                path = path + File.separator;
            else
                path = path + a;
        }

        return path;
    }


    // "��������" �� ������ str �����, ����������� ����� �������� start � end
    // ���� ������ end ���, �� ������ ������ ����� start
    // ���� ����� �� ������, ������������ null
    // ��� ������ ������ ������ �� "\n\n" ��� "\r\n\r\n", ���� ������� ������������
    protected String extract(String str, String start, String end)
    {
        int s = str.indexOf("\n\n", 0), e;
        if(s < 0) s = str.indexOf("\r\n\r\n", 0);
        if(s > 0) str = str.substring(0, s);
        s = str.indexOf(start, 0)+start.length();
        if(s < start.length()) return null;
        e = str.indexOf(end, s);
        if(e < 0) e = str.length();
        return (str.substring(s, e)).trim();
    }
}

