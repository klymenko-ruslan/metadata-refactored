package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.bom.BOMItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.02.16.
 */
@Service
public class BOMService {

    @Autowired
    private PartDao partDao;

    @Autowired
    private BOMItemDao bomItemDao;

    @Autowired
    private ChangelogDao changelogDao;

    static class FoundBomRecursionException extends Exception {

    };

    @Transactional
    public BOMItem create(Long parentPartId, Long childPartId, Integer quantity) {
        // Create a new BOM item
        Part parent = partDao.findOne(parentPartId);
        Part child = partDao.findOne(childPartId);
        if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
            throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
        }
        BOMItem item = new BOMItem();
        item.setParent(parent);
        item.setChild(child);
        item.setQuantity(quantity);
        parent.getBom().add(item);
        bomItemDao.persist(item);
        // Update the changelog
        changelogDao.log("Added bom item.", item.toJson());
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
        return item;
    }

    @Transactional
    public List<BOMItem> getByParentId(Long id) throws Exception {
        return bomItemDao.findByParentId(id);
    }

    @Transactional
    public void update(Long id, Integer quantity) {
        // Get the item
        BOMItem item = bomItemDao.findOne(id);
        // Update the changelog
        changelogDao.log("Changed BOM item quantity to " + quantity, item.toJson());
        // Update
        item.setQuantity(quantity);
        bomItemDao.merge(item);
    }

    @Transactional
    public void delete(Long id) {
        // Get the object
        BOMItem item = bomItemDao.findOne(id);
        Part parent = item.getParent();
        // Update the changelog
        String strJsonBom = item.toJson();
        changelogDao.log("Deleted BOM item.", strJsonBom);
        // Remove the BOM Item from the parent
        parent.getBom().remove(item);
        partDao.merge(parent);
        // Delete it
        bomItemDao.remove(item);
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }

    void bomRecursionCheck(Part parentPart, Part childPart) throws FoundBomRecursionException {

    }

}
