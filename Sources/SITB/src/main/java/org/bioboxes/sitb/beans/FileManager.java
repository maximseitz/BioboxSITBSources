/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template uploadFile, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bioboxes.sitb.beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.faces.application.FacesMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.core.util.FileUtils;
import sun.management.FileSystem;

/**
 *
 * @author Mark
 */

public class FileManager {
   
    
    // in case the uploadFile is uploaded
    private UploadedFile uploadFile;
    private File savedFile;
    private final static String STANDARD_OUTPUT = "upload";
    private InputStream inpStr;
    
    // standard : the ziped .fastq-uploadFile
    private String inputPath;
    
    // if we have only 1 unziped .fastq-uploadFile
    private String inputPathUnziped;
    
    // if we want to help to shuffle 2 unziped .fastq-files (additional service)
    private String inputPathUnziped1;
    private String inputPathUnziped2;
    
    public FileManager(){
        
    }

    public String getInputPath() {
        return inputPath;
    }
    
     public UploadedFile getFile() {
        return uploadFile;
    }
 
    public void setFile(UploadedFile file) {
        this.uploadFile = file;
        this.inputPath = file.getFileName();
    }
    
    public void handleFileUpload(FileUploadEvent event)  {
        // care: the inputPath is part of the output for the following
        this.uploadFile = event.getFile();
        char b = java.io.File.pathSeparatorChar;
        this.inputPath = this.STANDARD_OUTPUT+this.uploadFile.getFileName();
        try {
            this.savedFile = new File(this.inputPath);
            if (!savedFile.exists()) {
                    savedFile.createNewFile();
            }
            this.inpStr = this.uploadFile.getInputstream();
            Files.copy(this.inpStr, this.savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        }
        catch (IOException e) {
			e.printStackTrace();
        }
        this.inputPath=(this.savedFile.getAbsolutePath());
    }
     
 
    public String getInputPathUnziped() {
        return inputPathUnziped;
    }

    public String getInputPathUnziped1() {
        return inputPathUnziped1;
    }

    public String getInputPathUnziped2() {
        return inputPathUnziped2;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public void setInputPathUnziped(String inputPathUnziped) {
        this.inputPathUnziped = inputPathUnziped;
    }

    public void setInputPathUnziped1(String inputPathUnziped1) {
        this.inputPathUnziped1 = inputPathUnziped1;
    }

    public void setInputPathUnziped2(String inputPathUnziped2) {
        this.inputPathUnziped2 = inputPathUnziped2;
    }
}
