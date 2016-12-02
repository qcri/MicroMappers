package org.qcri.micromappers.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jlucas on 11/28/16.
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;

    public boolean downloadFile(String fileURL, String saveDir, String fileName){
        boolean downloadFileCompleted = false;
        HttpURLConnection httpConn = null;
        try{
            URL url = new URL(fileURL);
            httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {

                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                fileName = getFileName(fileURL, disposition, fileName);

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = saveDir + File.separator + fileName;
                String saveFilePathProcessed = saveDir + File.separator + fileName + FilePathSpec.GDELT_PROCESSED_EXTENTION;
                // check file exists
                if(!new File(saveFilePath).exists() && !new File(saveFilePathProcessed).exists()){
                    // opens an output stream to save into file
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();
                }

                downloadFileCompleted = true;
                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }


        }
        catch(IOException e1){
            System.out.println("IOException : " + e1);
        }
        catch(Exception e2){
            System.out.println("Exception : " + e2);
        }
        finally{
            httpConn.disconnect();
        }

        return downloadFileCompleted;
    }

    private String getFileName(String fileURL, String disposition, String fileName){

        if(fileName!=null && !fileName.isEmpty()){
            return fileName;
        }

        if (disposition != null) {
            // extracts file name from header field
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10,
                        disposition.length() - 1);
            }
        } else {
            // extracts file name from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                    fileURL.length());
        }

        return fileName;
    }
}