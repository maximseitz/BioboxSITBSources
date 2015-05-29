/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bioboxes.sitb.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.bioboxes.sitb.beans.Assembler;

/**
 *
 * @author jsteiner
 */
@ManagedBean
@SessionScoped
public class SessionController {

    private List<Assembler> assembler;

    private Assembler selectedAssembler;

    @PostConstruct
    public void init() {
        assembler = new ArrayList<Assembler>();
        assembler.add(new Assembler(1, "bioboxes/megahit"));
    }
    
    public void startBioBox() {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(selectedAssembler.getName() + " selected!"));
    }

    public Assembler getSelectedAssembler() {
        return selectedAssembler;
    }

    public void setSelectedAssembler(Assembler selectedAssembler) {
        this.selectedAssembler = selectedAssembler;
    }

    public List<Assembler> getAssembler() {
        return assembler;
    }

    public void setAssembler(List<Assembler> assembler) {
        this.assembler = assembler;
    }

}
