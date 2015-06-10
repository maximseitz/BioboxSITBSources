/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bioboxes.sitb.beans;

import java.util.ArrayList;


/**
 *
 * @author mugarov
 */
public class AssemblerHandler {
    private ArrayList<Assembler> assemblers;
    
    public AssemblerHandler(){
        this.assemblers = new ArrayList();
        
    }
    
    public void load(){
        /**TODO:
        Init a proper list of the assemblers here
         */ 
        this.assemblers.add(new Assembler(1, "bioboxes/megahit", "megahit description"));
        this.assemblers.add(new Assembler(2, "bioboxes/sparse", "sparse description"));
        this.assemblers.add(new Assembler(3, "bioboxes/sga", "sga description"));
    }

    public ArrayList<Assembler> getAssemblers() {
        return assemblers;
    }

    public void setAssemblers(ArrayList<Assembler> assemblers) {
        this.assemblers = assemblers;
    }
}
