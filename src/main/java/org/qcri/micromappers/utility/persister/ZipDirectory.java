package org.qcri.micromappers.utility.persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jlucas on 2/1/17.
 */
public class ZipDirectory {

    private static Logger logger = Logger.getLogger(ZipDirectory.class);

    private final int TWITTER_LIMIT_DOWNLOAD = 50000;
    private String zipFileName;
    private String source;

    public ZipDirectory(String zipFileName, String source){
        this.zipFileName = zipFileName;
        this.source = source;
    }

    public void generateZipFile(HttpServletResponse response){
        try{

            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

            File fileToZip = new File(this.source);

            this.zipFile(fileToZip, fileToZip.getName(), zipOut);

            zipOut.close();
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void generateZipFile(String destination){
        try{

            FileOutputStream fos = new FileOutputStream(new File(destination, this.zipFileName + ".zip"));
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            File fileToZip = new File(this.source);

            this.zipFile(fileToZip, fileToZip.getName(), zipOut);

            zipOut.close();
            fos.close();
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        try{
            if (fileToZip.isHidden()) {
                return;
            }
            if (fileToZip.isDirectory()) {
                File[] children = fileToZip.listFiles();

                if(fileToZip.getName().equalsIgnoreCase("TWITTER") && children.length > TWITTER_LIMIT_DOWNLOAD){

                  /**  Arrays.sort(children, new Comparator() {
                        public int compare(Object o1, Object o2) {

                            if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                                return -1;
                            } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                                return +1;
                            } else {
                                return 0;
                            }
                        }

                    });**/
                    for(int i=children.length-1; i > (children.length-TWITTER_LIMIT_DOWNLOAD); i--){
                        this.zipFile(children[i], fileName + "/" + children[i].getName(), zipOut);
                    }
                    return;
                }
                else{
                    for (File childFile : children) {
                        this.zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                    }
                    return;
                }
            }

            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;

            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            fis.close();
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }
    }
/**
    public static void main(String[] args) {
        String collecitonCode = "usa_right wing_1485943752931";
        String destination = "/Users/jlucas/Documents/JavaDev/data/resources/collection";
        String source = "/Users/jlucas/Documents/JavaDev/data/resources/collection/usa_right wing_1485943752931";
        ZipDirectory zw = new ZipDirectory(collecitonCode, destination, source);
    }
 **/
}
