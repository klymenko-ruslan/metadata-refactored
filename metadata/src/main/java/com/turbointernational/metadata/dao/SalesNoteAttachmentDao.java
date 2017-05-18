package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.SalesNoteAttachment;

@Repository
public class SalesNoteAttachmentDao extends AbstractDao<SalesNoteAttachment> {

    public SalesNoteAttachmentDao() {
        super(SalesNoteAttachment.class);
    }

}
