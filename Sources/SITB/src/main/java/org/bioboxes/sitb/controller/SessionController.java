/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bioboxes.sitb.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.bioboxes.sitb.beans.Assembler;
import org.bioboxes.sitb.beans.FileManager;
import org.bioboxes.sitb.beans.AssemblerHandler;
import org.bioboxes.sitb.mesos.BioboxesMesos;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jsteiner and mugarov  - this is a TEST
 */
@ManagedBean
@SessionScoped
public class SessionController implements Serializable {

    private AssemblerHandler assHand;
    
    private WizardControl wizCtrl;

    private Assembler selectedAssembler;
    
    private FileManager fileManager;

    private final StringBuffer result = new StringBuffer();

    private boolean readCompletely;
    private boolean active;

    @PostConstruct
    public void init() {
        
        this.setWizCtrl(new WizardControl());
        this.setFileManager(new FileManager(this.getWizCtrl()));
        
        // init assemblers
        this.assHand = new AssemblerHandler();
        this.assHand.load();
        
        this.readCompletely = false;
        this.active = false;

    }

    public void startBioBox() {
        if (selectedAssembler == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("You have to select an Assembler first ..."));
        } else {
            final String assemblerName = selectedAssembler.getName();
            this.active = true;
            this.readCompletely = false;

            /**
             * Update JSF components via anonymous Thread-class.
             */
            Thread executeAndReadThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        /**
                         * Create input/output folder.
                         */
                        new File("/tmp/input_data").mkdir();
                        new File("/tmp/output_data").mkdir();

                        try ( /**
                         * Generate yaml file.
                         */ PrintWriter pw = new PrintWriter("/tmp/input_data/biobox.yaml")) {
                            String yaml_content = "---\n"
                                    + "version: \"0.9.0\"\n"
                                    + "arguments:\n"
                                    + "  - fastq:\n"
                                    + "    - value: \"/bbx/input/reads.fq.gz\"\n"
                                    + "      id: \"pe_1\"\n"
                                    + "      type: paired\n"
                                    + "  - fragment_size:\n"
                                    + "    - value: 240\n"
                                    + "      id: pe_1";
                            pw.println(yaml_content);
                        }

                        /**
                         * Get reads.fq.gz.
                         */
                        URL inputData = new URL("https://www.dropbox.com/s/uxgn6cqngctqv74/reads.fq.gz?dl=1");
                        URLConnection con = inputData.openConnection();

                        FileOutputStream fout;
                        try (BufferedInputStream br = new BufferedInputStream(con.getInputStream())) {
                            fout = new FileOutputStream("/tmp/input_data/reads.fq.gz");
                            int i = 0;
                            byte[] bytesIN = new byte[300000];
                            while ((i = br.read(bytesIN)) >= 0) {
                                fout.write(bytesIN, 0, i);
                            }
                        }
                        fout.close();

                        /**
                         * Start Process and read from stdin.
                         */
                        Runtime r = Runtime.getRuntime();
                        Process process = r.exec("docker run "
                                + "--volume='/tmp/input_data:/bbx/input:ro' "
                                + "--volume='/tmp/output_data:/bbx/output:rw' "
                                + "--rm " 
                                + assemblerName 
                                + " default "
                                + "2>&1");

                        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String inputLine = "";
                            
                            while ((inputLine = in.readLine()) != null) {
                                result.append(inputLine);
                                result.append("<br />");
                            }
                        }
                        process.destroy();

                        readCompletely = true;
                        active = false;
                    } catch (IOException iex) {
                        System.out.println("Error!");
                    }
                }
            }
            );
            executeAndReadThread.start();
        }
    }
    
    public void startMesos() {
        BioboxesMesos.startMesosMaster();
        BioboxesMesos.startMesosSlave();
        BioboxesMesos.startMesosNetwork("hello-world", 2);
    }

    
    public void resetExecution() {
        this.readCompletely = false;
        this.active = false;
        this.result.delete(0, this.result.length());
    }
    
    public void scroll() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('scroller').scrollY(1000)");
    }

    public Assembler getSelectedAssembler() {
        return this.selectedAssembler;
    }

    public void setSelectedAssembler(Assembler selectedAssembler) {
        this.selectedAssembler = selectedAssembler;
    }

    public ArrayList<Assembler> getAssembler() {
        return this.assHand.getAssemblers();
    }

    public void setAssembler(ArrayList<Assembler> assemblers) {
        this.assHand.setAssemblers(assemblers);
    }

    public boolean isReadCompletely() {
        return this.readCompletely;
    }

    public void setReadCompletely(boolean readCompletely) {
        this.readCompletely = readCompletely;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public StringBuffer getResult() {
        return result;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public WizardControl getWizCtrl() {
        return wizCtrl;
    }

    public void setWizCtrl(WizardControl wizCtrl) {
        this.wizCtrl = wizCtrl;
    }

    

}
