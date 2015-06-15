/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bioboxes.sitb.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Mark
 */


public class WizardControl implements Serializable{
    private boolean pathApplied;
    
    public WizardControl(){
        this.pathApplied = false;
    }
    
    public String onFlowProcess(FlowEvent event) {
        if(event.getOldStep().equals("input") && !this.pathApplied){
            return event.getOldStep();
        }
        else{
            return event.getNewStep();
        }
    }

    public boolean isPathApplied() {
        return pathApplied;
    }

    public void setPathApplied(boolean pathApplied) {
        this.pathApplied = pathApplied;
    }

}
