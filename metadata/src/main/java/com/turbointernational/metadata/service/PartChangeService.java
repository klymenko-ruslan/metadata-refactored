package com.turbointernational.metadata.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.entity.part.Part;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class PartChangeService {

    private ObjectMapper jsonMapper;

    private static enum ChangeEnum {
        INSERT, UPDATE, DELETE
    }

    private static class Change {

        private final ChangeEnum changeType;
        private final Long partId;
        private final String mnfrPartNum;
        private final Long mnfrId;

        public Change(ChangeEnum changeType, Long partId, String mnfrPartNum, Long mnfrId) {
            this.changeType = changeType;
            this.partId = partId;
            this.mnfrPartNum = mnfrPartNum;
            this.mnfrId = mnfrId;
        }

        public ChangeEnum getChangeType() {
          return changeType;
        }

        public Long getPartId() {
          return partId;
        }

        public String getMnfrPartNum() {
          return mnfrPartNum;
        }

        public Long getMnfrId() {
          return mnfrId;
        }

    }

    @Autowired
    private MessagingService messagingService;

    @PostConstruct
    void init() {
        jsonMapper = new ObjectMapper();
    }

    public void addedBom(Part part) throws IOException {
        changedBom(ChangeEnum.INSERT, part);
    }

    public void addedBoms(Collection<Part> parts) throws IOException {
      Iterator<Part> iter = parts.iterator();
      while(iter.hasNext()) {
        Part p = iter.next();
        changedBom(ChangeEnum.INSERT, p);
      }
    }

    public void updatedBom(Part part) throws IOException {
        changedBom(ChangeEnum.UPDATE, part);
    }

    public void deletedBom(Part part) throws IOException {
        changedBom(ChangeEnum.DELETE, part);
    }

    public void addedInterchange(Part part) throws IOException {
        changedInterchange(ChangeEnum.INSERT, part);
    }

    public void addedInterchanges(Collection<Part> parts) throws IOException {
        Iterator<Part> iter = parts.iterator();
        while(iter.hasNext()) {
            Part p = iter.next();
            changedInterchange(ChangeEnum.INSERT, p);
        }
    }

    public void deleteInterchange(Part part) throws IOException {
        changedInterchange(ChangeEnum.DELETE, part);
    }

    private void changedBom(ChangeEnum changeType, Part part) throws IOException {
        Change ch = new Change(changeType, part.getId(), part.getManufacturerPartNumber(),
                part.getManufacturer().getId());
        byte[] message = jsonMapper.writeValueAsBytes(ch);
        messagingService.bomChanged(message);
    }

    private void changedInterchange(ChangeEnum changeType, Part part) throws IOException {
        Change ch = new Change(changeType, part.getId(), part.getManufacturerPartNumber(),
                part.getManufacturer().getId());
        byte[] message = jsonMapper.writeValueAsBytes(ch);
        messagingService.interchangeChanged(message);
    }

}
