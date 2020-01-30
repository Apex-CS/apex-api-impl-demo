package com.fis.api.product.BPPRetrieveProductDetail;

import com.ethlo.jsons2xsd.Config;
import com.ethlo.jsons2xsd.Jsons2Xsd;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class jsd2xml {

    public static void main(String[] args) throws IOException {

        System.out.println(args);

        if(args.length == 1){
            System.out.println("running convert for: "+ args[0]);
            execute(args[0]);
        }
        else{
            System.out.println("args " + args.length);
        }
    }

    public static void execute(String path) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(path));

        paths.filter(Files::isRegularFile)
                /*.filter(fileName -> fileName.endsWith(".json"))*/
                .forEach(file -> {
                    System.out.println("converting > " + file.toFile().getName());
                    try {
                        Document doc = convert(file.toFile());
                        Dom2File(doc, new File(getXSDName(file.toFile().getName())));
                    } catch (IOException | TransformerException e ) {
                        e.printStackTrace();
                    }
                });
    }

    public static void Dom2File(Document document, File file) throws TransformerException, IOException {
        DOMSource source = new DOMSource(document);
        FileWriter writer = new FileWriter(file);
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
 
    }

    public static String getEntityName(String fileName){
        return fileName.replace(".json", "");
    }

    public static String getXSDName(String fileName){
        return fileName.replace(".json", ".xml");
    }

    public static Document convert(File file) throws IOException {
        String entity = getEntityName(file.getName());
        Reader reader = new FileReader(file);
        Config cfg = new Config.Builder()
                .targetNamespace("http://example.com/myschema.xsd")
                .name( "array" )
                .build();
        Document doc = Jsons2Xsd.convert(reader, cfg);
        return doc;
    }


}
