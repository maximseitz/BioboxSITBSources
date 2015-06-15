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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.faces.application.FacesMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.logging.log4j.core.util.FileUtils;
import org.bioboxes.sitb.controller.WizardControl;
import sun.management.FileSystem;

/**
 *
 * @author Mark
 */

public class FileManager {
   
    
    // in case the uploadFile is uploaded
    private UploadedFile uploadFile;
    private File savedFile;
    // replace "upload" with the user later on
    private final static String STANDARD_OUTPUT = "upload";
    private InputStream inpStr;
    private String shownPath;
    private String userPlainPath;
    
    private WizardControl wizCtrl;
    
    // standard : the ziped .fastq-uploadFile
    private String inputPath;
    
    
    
    public FileManager(WizardControl wizCtrl){
        this.wizCtrl = wizCtrl;
        
    }

    public String getInputPath() {
        return inputPath;
    }
    
     public UploadedFile getFile() {
        return uploadFile;
    }
 
    public void setFile(UploadedFile file) {
        this.uploadFile = file;
        this.shownPath = file.getFileName();
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
        this.shownPath=(this.savedFile.getName());
        this.wizCtrl.setPathApplied(true);
    }
    
    public void handlePlainPathInput(ActionEvent actionEvent){
        this.inputPath = this.userPlainPath;
        this.shownPath = this.userPlainPath;
        this.wizCtrl.setPathApplied(true);
    }
     
    public String getShownPath() {
        return shownPath;
    }

    public void setShownPath(String shownPath) {
        this.shownPath = shownPath;
    }

    public String getUserPlainPath() {
        return userPlainPath;
    }

    public void setUserPlainPath(String userPlainPath) {
        this.userPlainPath = userPlainPath;
    }
    
}
