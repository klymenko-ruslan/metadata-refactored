package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.dao.PartTypeDao;
import com.turbointernational.metadata.service.ImageService;
import com.turbointernational.metadata.util.View;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

import static com.turbointernational.metadata.service.ImageService.PART_TYPE_LEGEND_HEIGHT;
import static com.turbointernational.metadata.service.ImageService.PART_TYPE_LEGEND_WIDTH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = {"/parttype", "/metadata/parttype"})
public class PartTypeController {

    @Autowired
    private PartTypeDao partTypeDao;

    @Autowired
    private ImageService imageService;

    @Value("${images.originals}")
    private File originalImagesDir;

    @Secured("ROLE_READ")
    @ResponseBody
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/json/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public PartType getPartType(@PathVariable("id") Long id) {
        PartType partType = partTypeDao.findOne(id);
        return partType;
    }

    @RequestMapping(value = "/json/list", method = GET)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_READ")
    public List<PartType> getAllPartTypes() {
        List<PartType> retVal = partTypeDao.findAll();
        return retVal;
    }

    @Transactional
    @RequestMapping(value = "/{id}/ptlegend/image", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_PART_IMAGES")
    public PartType addLegendImage(@PathVariable Long id,
                                   @RequestPart("file") @Valid @NotNull @NotBlank
                                           MultipartFile mpf) throws Exception {
        PartType partType = partTypeDao.findOne(id);
        String currImgFilename = partType.getLegendImgFilename();
        if (currImgFilename != null) {
            imageService.delResizedImage(currImgFilename);
        }
        String ptidstr = id.toString();
        String now = new Long(System.currentTimeMillis()).toString();
        String filenameOriginal = ptidstr + "_ptlgndorig_" + now + ".jpg";
        String filenameScaled = ptidstr + "_ptlgnd_" + now + ".jpg";
        File originalFile = new File(originalImagesDir, filenameOriginal);
        mpf.transferTo(originalFile);
        imageService.generateResizedImage(filenameOriginal, filenameScaled,
                PART_TYPE_LEGEND_WIDTH, PART_TYPE_LEGEND_HEIGHT, true);
        partType.setLegendImgFilename(filenameScaled);
        return partType;
    }

}
