package com.fis.api.product.BPPRetrieveProductDetail;

import com.ethlo.jsons2xsd.Config;
import com.ethlo.jsons2xsd.Jsons2Xsd;
import com.ethlo.jsons2xsd.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class jsd2xml {

    private static Logger logger =  LoggerFactory.getLogger(jsd2xml.class);

    public static void main(String[] args) throws IOException {

        if(args.length == 1){
            logger.info("running convert for: "+ args[0]);
            execute(args[0]);
        }
        else{
            logger.info("args " + args.length);
        }
    }

    public static void execute(String pathFile) throws IOException {
        Stream<Path> paths = Files.walk(Paths.get(pathFile));

        paths.filter(Files::isRegularFile)
                .filter( f -> f.toFile().getName().endsWith("json") )
                .forEach(path -> {
                    logger.info("converting > " + path.toFile().getName());
                    try {
                        String doc = convert(path.toFile());
                        // logger.info(doc);
                        saveXML( getXSDPathName(path) + getXSDFileName(path)
                                ,doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static String getEntityName(String fileName){
        return fileName.replace(".json", "");
    }

    public static String getXSDPathName(Path path){
        String pathName = path.toFile().getParentFile().getAbsolutePath();
        return pathName.replace("json", "xml\\");
    }

    public static String getXSDFileName(Path path){
        String fileName = path.toFile().getName();
        return fileName.replace(".json", ".xsd");
    }

    public static void saveXML(String fileName, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(fileName));
        logger.info("saving file > "+ fileName);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static String convert(File file) throws IOException {
        String entity = getEntityName(file.getName());
        Reader reader = new FileReader(file);
        Config cfg = new Config.Builder()
                .targetNamespace("http://example.com/myschema.xsd")
                .name( "object" )
                .build();
        Document doc = Jsons2Xsd.convert(reader, cfg);
        final String actual = XmlUtil.asXmlString(doc.getDocumentElement());
        return actual;
    }


}
