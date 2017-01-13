/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.rdcit.ocSync.map.Mapper;
import org.rdcit.ocSync.model.Structure;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "StructureView")
public class StructureView implements Serializable {

    List<Structure> lStructure;
    @ManagedProperty(value = "#{Mapper}")
    Mapper mapper;

    @PostConstruct
    public void init() {
        lStructure = (List<Structure>) mapper.mapping()[1];
    }

    public List<Structure> getlStructure() {
        return lStructure;
    }

    public void setlStructure(List<Structure> lStructure) {
        this.lStructure = lStructure;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public StructureView() {
    }

}
