package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.KIT;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.KitComponentDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.web.dto.CommonComponent;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service("kitComponentService")
public class KitComponentService {

    private final static Type LISTOFCOMMONCOMPONENTSDTO = new TypeToken<List<CommonComponent>>() {}.getType();

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private KitComponentDao kitComponentDao;

    @Autowired
    private DtoMapperService dtoMapService;

    @Transactional
    public KitComponent create(Long kitId, Long partId, Boolean exclude) throws Exception {
        Kit kit = partDao.getReferenceOnPart(Kit.class, kitId);
        Part part = partDao.getReference(partId);
        KitComponent component = new KitComponent();
        component.setKit(kit);
        component.setPart(part);
        component.setExclude(exclude);
        kitComponentDao.persist(component);
        // Update the changelog.
        Collection<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(kitId, ChangelogPart.Role.PART0));
        relatedParts.add(new RelatedPart(partId, ChangelogPart.Role.PART1));
        String logMsg = String.format("Created kit common component. Kit is %s. Picked part is %s.", formatPart(kit),
                formatPart(part));
        String json = component.toJson();
        changelogService.log(KIT, logMsg, json, relatedParts);
        return component;
    }

    @Transactional
    public void update(Long id, Boolean exclude) throws Exception {
        // Get the item
        KitComponent component = kitComponentDao.findOne(id);
        // Update
        component.setExclude(exclude);
        kitComponentDao.merge(component);
        // Update the changelog.
        Long kitId = component.getKit().getId();
        Long pickedPartId = component.getPart().getId();
        Collection<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(kitId, ChangelogPart.Role.PART0));
        relatedParts.add(new RelatedPart(pickedPartId, ChangelogPart.Role.PART1));
        String logMsg = String.format("Changed kit component mapping exclude to %B.", exclude);
        String json = component.toJson();
        changelogService.log(KIT, logMsg, json, relatedParts);
    }

    @Transactional
    public List<CommonComponent> delete(Long kitId, Long[] ids) throws Exception {
        for (Long id : ids) {
            // Get the object
            KitComponent component = kitComponentDao.findOne(id);
            kitComponentDao.remove(component);
            // Update the changelog.
            Long pickedPartId = component.getPart().getId();
            Collection<RelatedPart> relatedParts = new ArrayList<>(2);
            relatedParts.add(new RelatedPart(kitId, ChangelogPart.Role.PART0));
            relatedParts.add(new RelatedPart(pickedPartId, ChangelogPart.Role.PART1));
            String logMsg = String.format("Deleted kit component [%d]. Kit is %s. Picked part is %s.", id,
                    formatPart(component.getKit()), formatPart(component.getPart()));
            String json = component.toJson();
            changelogService.log(KIT, logMsg, json, relatedParts);
        }
        Page<CommonComponent> pg = list(kitId, null, null, null, null, null);
        return pg.getRecs();
    }

    public Page<CommonComponent> list(Long kitId, Long partId, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        Page<KitComponent> pgKitComponents = kitComponentDao.filter(kitId, partId, sortProperty, sortOrder, offset,
                limit);
        List<KitComponent> recs = pgKitComponents.getRecs();
        List<CommonComponent> ccrecs = dtoMapService.map(recs, LISTOFCOMMONCOMPONENTSDTO);
        Page<CommonComponent> retVal = new Page<CommonComponent>(pgKitComponents.getTotal(), ccrecs);
        return retVal;
    }

}
