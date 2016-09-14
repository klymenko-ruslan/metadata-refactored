package com.turbointernational.metadata.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.bom.BOMItemDao;
import com.turbointernational.metadata.web.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.services.BOMService.AddToParentBOMsRequest.ResolutionEnum.REPLACE;
import static java.util.Collections.binarySearch;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.02.16.
 */
@Service
public class BOMService {

    private final static Logger log = LoggerFactory.getLogger(BOMService.class);

    @Autowired
    private PartDao partDao;

    @Autowired
    private BOMItemDao bomItemDao;

    @Autowired
    private ChangelogDao changelogDao;

    public static class AddToParentBOMsRequest {

        enum ResolutionEnum {
            ADD, REPLACE
        };

        public static class Row {

            @JsonView({View.Summary.class})
            private Long partId;

            @JsonView({View.Summary.class})
            private ResolutionEnum resolution;

            @JsonView({View.Summary.class})
            private Integer quontity;

            public Long getPartId() {
                return partId;
            }

            public void setPartId(Long partId) {
                this.partId = partId;
            }

            public ResolutionEnum getResolution() {
                return resolution;
            }

            public void setResolution(ResolutionEnum resolution) {
                this.resolution = resolution;
            }

            public Integer getQuontity() {
                return quontity;
            }

            public void setQuontity(Integer quontity) {
                this.quontity = quontity;
            }

        }

        @JsonView({View.Summary.class})
        private List<Row> rows;

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

    }

    @JsonInclude(ALWAYS)
    public static class AddToParentBOMsResponse {

        @JsonView(View.Summary.class)
        private int added;

        @JsonView(View.Summary.class)
        private int failed;

        @JsonView(View.Summary.class)
        private List<BOMItem> parents;

        public AddToParentBOMsResponse(int added, int failed, List<BOMItem> parents) {
            this.added = added;
            this.parents = parents;
            this.failed = failed;
        }

        public int getAdded() {
            return added;
        }

        public void setAdded(int added) {
            this.added = added;
        }

        public List<BOMItem> getParents() {
            return parents;
        }

        public void setParents(List<BOMItem> parents) {
            this.parents = parents;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }

    }

    /**
     * Signals that a BOMs tree has a circular recursion.
     */
    public static class FoundBomRecursionException extends Exception {

        /**
         * An ID of a part which makes the circular recursion.
         */
        private Long failedId;

        FoundBomRecursionException(Long failedId) {
            this.failedId = failedId;
        }

        public Long getFailedId() {
            return failedId;
        }

    }

    @Transactional(noRollbackFor = {FoundBomRecursionException.class, AssertionError.class})
    public BOMItem create(Long parentPartId, Long childPartId, Integer quantity, boolean rebuildBom) throws FoundBomRecursionException {
        // Create a new BOM item
        Part parent = partDao.findOne(parentPartId);
        Part child = partDao.findOne(childPartId);
        if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
            throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
        }
        bomRecursionCheck(parent, child);
        BOMItem item = new BOMItem();
        item.setParent(parent);
        item.setChild(child);
        item.setQuantity(quantity);
        parent.getBom().add(item);
        bomItemDao.persist(item);
        // Update the changelog
        changelogDao.log("Added bom item.", item.toJson());
        // TODO: Only change what we need to rather than rebuilding everything
        if (rebuildBom) {
            partDao.rebuildBomDescendancy();
        }
        return item;
    }

    @Transactional
    public List<BOMItem> getByParentId(Long partId) throws Exception {
        return bomItemDao.findByParentId(partId);
    }

    @Transactional
    public List<BOMItem> getParentsForBom(Long partId) throws Exception {
        return bomItemDao.findParentsForBom(partId);
    }

    @Transactional
    public List<BOMItem> getByParentAndTypeIds(Long partId, Long partTypeId) throws Exception {
        return bomItemDao.findByParentAndTypeIds(partId, partTypeId);
    }

    @Transactional
    public AddToParentBOMsResponse addToParentsBOMs(Long primaryPartId, AddToParentBOMsRequest request) throws Exception {
        int added = 0;
        int failed = 0;
        List<BOMService.AddToParentBOMsRequest.Row> rows = request.getRows();
        for (AddToParentBOMsRequest.Row r : rows) {
            Long pickedPartId = r.getPartId();
            if (r.getResolution() == REPLACE) {
                // Remove existing BOMs in the picked part.
                Part pickedPart = partDao.findOne(pickedPartId);
                for(Iterator<BOMItem> iterBoms = pickedPart.getBom().iterator(); iterBoms.hasNext();) {
                    BOMItem bomItem = iterBoms.next();
                    String strJsonBom = bomItem.toJson();
                    changelogDao.log("Deleted BOM item.", strJsonBom);
                    iterBoms.remove();
                    bomItemDao.remove(bomItem);
                }
            }
            // Add the primary part to the list of BOMs of the picked part.
            try {
                create(pickedPartId, primaryPartId, r.getQuontity(), false);
                added++;
            } catch(FoundBomRecursionException e) {
                log.warn("Adding of the part [" + primaryPartId + "] to list of BOM for part [" +
                        pickedPartId + "] failed.", e);
                failed++;
            }
        }
        List<BOMItem> parents = getParentsForBom(primaryPartId);
        partDao.rebuildBomDescendancy();
        return new BOMService.AddToParentBOMsResponse(added, failed, parents);
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

    /**
     * Check that two specified parts has no any cycled recursions:
     * <ul>
     *     <li>not in a BOMs tree of the parentPart</li>
     *     <li>not in a BOMs tree of the childPart</li>
     *     <li>not in a union of these BOMs trees</li>
     * </ul>
     *
     * @param parentPart
     * @param childPart
     * @throws FoundBomRecursionException
     */
    void bomRecursionCheck(Part parentPart, Part childPart) throws FoundBomRecursionException {
        List<Long> parentBoms = loadAllBomsOfPart(parentPart);
        List<Long> childBoms = loadAllBomsOfPart(childPart);
        // Try to find an intersection.
        for (Iterator<Long> parentIter = parentBoms.iterator(); parentIter.hasNext(); ) {
            Long id = parentIter.next();
            if (binarySearch(childBoms, id) >= 0) {
                throw new FoundBomRecursionException(id);
            }
        }
    }

    /**
     * Load IDs of all parts which are BOMs for the specified part.
     *
     * The method recursively walks a tree of BOMs of the specified part
     * and fills a sorted list of IDs of the found parts.
     * The list will also include an ID of the specified part.
     *
     * @param part a part to load its BOMs
     * @return (ascending) ordered list of IDs of parts which are BOM for the specified part.
     *          The list also includes ID of the part.
     * @throws FoundBomRecursionException if the BOM's tree contains cycled recursion
     */
    List<Long> loadAllBomsOfPart(Part part) throws FoundBomRecursionException {
        List<Long> retVal = new ArrayList<>(); // ordered list of IDs
        Stack<Iterator<BOMItem>> postponed = new Stack<>();
        Part p = part;
        while (p != null || !postponed.empty()) {
            if (p == null) {
                Iterator<BOMItem> bomIter = postponed.peek();
                if (bomIter.hasNext()) {
                    BOMItem bom = bomIter.next();
                    p = bom.getChild();
                } else {
                    postponed.pop(); // remove from the stack this exhausted iterator
                }
            } else {
                Long id = p.getId();
                if (binarySearch(retVal, id) >= 0) {
                    throw new FoundBomRecursionException(id);
                }
                retVal.add(id);
                Collections.sort(retVal);
                Set<BOMItem> boms = p.getBom();
                postponed.push(boms.iterator());
                p = null;
            }
        }
        return retVal;
    }

}
