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

@ManagedBean
@ViewScoped
public class WizardControl implements Serializable{
    private boolean skip;

    
    public boolean isSkip() {
       return this.skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }

     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            this.skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
             return event.getNewStep();
        }
        
    }

}
