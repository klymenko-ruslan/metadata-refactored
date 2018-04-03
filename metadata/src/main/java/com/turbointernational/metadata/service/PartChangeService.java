package com.turbointernational.metadata.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Deprecated // Interchanges and BOMs migrated to ArangoDb
@Service
public class PartChangeService {

    @Autowired
    private AppService appService;

    private ObjectMapper jsonMapper;

    private static enum ChangeEnum {
        INS, UPD, DEL
    }

    private static class ChangeBOM {

        private final ChangeEnum chtyp;

        /**
         * Parent part ID.
         */
        private final long ppid;

        /**
         * Child part ID.
         *
         * This member is optional (it is not specified for update operation).
         */
        private final Long chpid;

        private ChangeBOM(ChangeEnum changeType, long parentPartId, Long childPartId) {
            this.chtyp = changeType;
            this.ppid = parentPartId;
            this.chpid = childPartId;
        }

        @SuppressWarnings("unused")
        public ChangeEnum getChtyp() {
            return chtyp;
        }

        @SuppressWarnings("unused")
        public long getPpid() {
            return ppid;
        }

        @SuppressWarnings("unused")
        public Long getChpid() {
            return chpid;
        }

    }

    private static class ChangeInterchange {

        private final long intchid0;

        private final Long intchid1;

        private ChangeInterchange(long interchangeId0, Long interchangeId1) {
            this.intchid0 = interchangeId0;
            this.intchid1 = interchangeId1;
        }

        @SuppressWarnings("unused")
        public long getIntchid0() {
            return intchid0;
        }

        @SuppressWarnings("unused")
        public Long getIntchid1() {
            return intchid1;
        }

    }

    @Autowired
    private MessagingService messagingService;

    @PostConstruct
    void init() {
        jsonMapper = new ObjectMapper();
    }

    public void addedBom(Long parentPartId, Long relatedPartId) throws IOException {
        changedBom(ChangeEnum.INS, null, parentPartId, relatedPartId);
    }

    public void addedBoms(Long parentPartId, Collection<Long> relatedPartIds) throws IOException {
        Iterator<Long> iter = relatedPartIds.iterator();
        String grpId = generateGroupId(parentPartId);
        while (iter.hasNext()) {
            Long childPartId = iter.next();
            changedBom(ChangeEnum.INS, grpId, parentPartId, childPartId);
        }
    }

    public void addedToParentBoms(Long childPartId, Collection<Long> parentPartIds) throws IOException {
        Iterator<Long> iter = parentPartIds.iterator();
        String grpId = generateGroupId(childPartId);
        while (iter.hasNext()) {
            Long parentPartId = iter.next();
            changedBom(ChangeEnum.INS, grpId, parentPartId, childPartId);
        }
    }

    public void updatedBom(long partId) throws IOException {
        changedBom(ChangeEnum.UPD, null, partId, null);
    }

    public void deletedBom(Long parentPartId, Long childPartId) throws IOException {
        changedBom(ChangeEnum.DEL, null, parentPartId, childPartId);
    }

    public void changedInterchange(long interchangeGroupId0, Long interchangeGroupId1) throws IOException {
        changedInterchange(null, interchangeGroupId0, interchangeGroupId1);
    }

    private void changedBom(ChangeEnum changeType, String groupId, long parentPartId, Long relatedPartId)
            throws IOException {
        ChangeBOM ch = new ChangeBOM(changeType, parentPartId, relatedPartId);
        byte[] message = jsonMapper.writeValueAsBytes(ch);
        messagingService.bomChanged(groupId, message);
    }

    private void changedInterchange(String groupId, long interchangeId0, Long interchangeId1) throws IOException {
        ChangeInterchange ch = new ChangeInterchange(interchangeId0, interchangeId1);
        byte[] message = jsonMapper.writeValueAsBytes(ch);
        messagingService.interchangeChanged(groupId, message);
    }

    private String generateGroupId(Long prefix) {
        return prefix.toString() + "-" + appService.now();
    }

}
