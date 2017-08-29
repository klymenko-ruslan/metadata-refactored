package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.BOM;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_CHILD;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_PARENT;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.util.FormatUtils.formatBOMAlternative;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.turbointernational.metadata.dao.BOMAlternativeDao;
import com.turbointernational.metadata.dao.BOMAlternativeHeaderDao;
import com.turbointernational.metadata.dao.BOMItemDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.BOMAlternative;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.web.dto.AltBom;
import com.turbointernational.metadata.web.dto.Part;

@RequestMapping("/{parentPartId}/descendant/{childPartId}/alternatives/")
@Controller
public class BOMAlternativeController {

    @Autowired
    private BOMService bomService;

    @Autowired
    ChangelogService changelogService;

    @Autowired
    PartDao partDao;

    @Autowired
    BOMItemDao bomItemDao;

    @Autowired
    BOMAlternativeDao bomAltDao;

    @Autowired
    BOMAlternativeHeaderDao bomAltHeaderDao;

    @Transactional
    @Secured("ROLE_BOM_ALT")
    @ResponseBody
    @RequestMapping(value = "{altPartId}", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Part[] create(@PathVariable("parentPartId") Long parentPartId, @PathVariable("childPartId") Long childPartId,
            @PathVariable("altPartId") Long altPartId) throws Exception {
        bomService.createAltBom(parentPartId, childPartId, altPartId);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(3);
        relatedParts.add(new RelatedPart(altPartId, PART0));
        relatedParts.add(new RelatedPart(parentPartId, BOM_PARENT));
        relatedParts.add(new RelatedPart(childPartId, BOM_CHILD));
        changelogService.log(BOM,
                "Added bom alternative " + formatBOMAlternative(parentPartId, childPartId, altPartId) + ".",
                relatedParts);
        AltBom altBom = bomService.getAlternatives(parentPartId, childPartId);
        return altBom.getParts();
    }

    @Transactional
    @RequestMapping(value = "/{altItemId}", method = RequestMethod.DELETE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM_ALT")
    public void delete(@PathVariable("bomId") Long bomId, @PathVariable("altItemId") Long altId) throws Exception {
        // Get the BOM item and alternate
        BOMItem item = bomItemDao.findOne(bomId);
        BOMAlternative alt = bomAltDao.findOne(altId);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(3);
        relatedParts.add(new RelatedPart(altId, PART0));
        relatedParts.add(new RelatedPart(item.getParent().getId(), BOM_PARENT));
        relatedParts.add(new RelatedPart(item.getChild().getId(), BOM_CHILD));
        changelogService.log(BOM,
                "Deleted BOM alternative " + /* formatBOMAlternative(alt) + */ ".", alt, relatedParts);
        // Remove the alternate item
        item.getAlternatives().remove(alt);
        partDao.merge(item.getParent());
        // Delete it
        bomAltDao.remove(alt);
    }

}
