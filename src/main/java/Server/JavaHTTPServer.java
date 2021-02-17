package Server;

import Risorse.*;
import Database.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.*;
import java.net.*;
import java.util.*;

public class JavaHTTPServer implements Runnable{ 

    //static final File WEB_ROOT = new File("/home/cabox/workspace/HTTPWebServer/file");
    static final String WEB_ROOT = "/file";
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    static final String FILE_REDIRECT = "301.html";
    static final String FILE_JSON = "puntiVendita.json";
    static final String FILE_XML = "puntivendita.xml";
    static final String XML_REQUEST = "file.xml";
    static final String JSON_REQUEST = "file.json";
    
    // port and ip to listen connection
    static final int PORT = 3000;

    // verbose mode
    static final boolean verbose = true;

    // client Connection via Socket Class
    private Socket connect;

    // var used for serialize and deserialize
    ObjectMapper json = new ObjectMapper();
    XmlMapper xml = new XmlMapper();
    
    // create and connect to the database
    Database db = new Database();

    public JavaHTTPServer(Socket c) {
            connect = c;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            // we listen until user halts server execution
            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (verbose) {
                        System.out.println("Connecton opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }
        } catch (IOException e) {
                System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // we manage our particular client connection
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken().toLowerCase();

            // check for supported method
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                if (verbose) {
                        System.out.println("501 Not Implemented : " + method + " method.");
                }

                String contentMimeType = "text/html";
                //read content to return to client
                byte[] fileData = readFileData(WEB_ROOT + "/" + METHOD_NOT_SUPPORTED);
                int fileLength = fileData.length;

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // GET or HEAD method and special request 
                if(fileRequested.equals("/" + FILE_XML)){
                    
                    // deserialize from json
                    ArrayList<PuntoVendita> pv = json.readValue(WEB_ROOT + "/"+ FILE_JSON, new TypeReference<ArrayList<PuntoVendita>>(){}); 
                    
                    String xmlFile = xml.writeValueAsString(pv); // serialize to xml
                    byte[] fileData = xmlFile.getBytes();
                    
                    fileFound(xmlFile, fileRequested, dataOut, fileData, out); // send output
                    System.out.println("File puntivendita.xml returned");
                    return;

                } else if (fileRequested.equals("/" + JSON_REQUEST)){

                    String jsonFile = json.writeValueAsString(db.getAlunni()); // serialize to json
                    byte[] fileData = jsonFile.getBytes();
                    
                    fileFound(jsonFile, fileRequested, dataOut, fileData, out); // send output
                    System.out.println("File file.json returned");
                    return;               
                    
                } else if(fileRequested.equals("/" + XML_REQUEST)){
                    
                    String xmlFile = xml.writeValueAsString(db.getAlunni()); // serialize to xml
                    byte[] fileData = xmlFile.getBytes();
                                  
                    fileFound(xmlFile, fileRequested, dataOut, fileData, out); // send output
                    System.out.println("File file.xml returned");                 
                    return;
                    
                } else if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }    
                
                String content = getContentType(fileRequested);

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(WEB_ROOT + fileRequested);
                    int fileLength = fileData.length;
   
                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                        System.out.println("File " + fileRequested + " of type " + content + " returned");
                }
            }
        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            } 

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }

    // return file data
    private byte[] readFileData(String filePath) throws IOException {
            InputStream fileIn = null;
            byte[] fileData = null;
            
            try {
                fileIn = getClass().getResourceAsStream(filePath);  
                System.out.println(filePath);
                System.out.println(fileIn);
                fileData = new byte[fileIn.available()];
                fileIn.read(fileData);
            } finally {
                if (fileIn != null) fileIn.close();
            }

            return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
            if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
                return "text/html";
            else if (fileRequested.endsWith(".xml"))
                return "text/xml";
            else if (fileRequested.endsWith(".json"))
                return "text/json";
            else
                return "text/plain";
    }
    
    // return the file and the header (FILE FOUND)
    private void fileFound (String file, String fileRequested, BufferedOutputStream dataOut, byte[] fileData, PrintWriter out) throws IOException {
            // send HTTP Headers
            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java HTTP Server : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + getContentType(fileRequested));
            out.println("Content-length: " + file.length());
            out.println(); // blank line between headers and content, very important !
            out.flush(); // flush character output stream buffer

            dataOut.write(fileData, 0, file.length());
            dataOut.flush();
    }

    // return the file and th header (FILE NOT FOUND)
    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
            int fileLength;
            String content = "text/html";
            byte[] fileData;

            //File not found
            if(fileRequested.endsWith(".html")){         
                fileData = readFileData(WEB_ROOT + "/" + FILE_NOT_FOUND);
                fileLength = fileData.length;

                out.println("HTTP/1.1 404 File Not Found");
                out.println("Server: Java HTTP Server : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + content);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

                if (verbose) {
                        System.out.println("File " + fileRequested + " not found");
                }
            //File redicted
            }else {
                fileData = readFileData(WEB_ROOT + "/" + FILE_REDIRECT);
                fileLength = fileData.length;

                out.println("HTTP/1.1 301 File Redirect");
                out.println("Server: Java HTTP Server : 1.0");
                out.println("Date: " + new Date());
                out.println("Location: " + fileRequested + "/");
                out.println("Content-type: " + content);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

                if (verbose) {
                    System.out.println("File " + fileRequested + " redirect");
                }
            }            
    }	
}