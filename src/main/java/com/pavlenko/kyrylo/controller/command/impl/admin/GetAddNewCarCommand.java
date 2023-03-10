package com.pavlenko.kyrylo.controller.command.impl.admin;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.entity.Brand;
import com.pavlenko.kyrylo.model.entity.Quality;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.BrandService;
import com.pavlenko.kyrylo.model.service.QualityService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;

/**
 * Returns add new car page.
 */
public class GetAddNewCarCommand implements Command {

    private final QualityService qualityService;
    private final BrandService brandService;

    public GetAddNewCarCommand(QualityService qualityService, BrandService brandService) {
        this.qualityService = qualityService;
        this.brandService = brandService;
    }

    /**
     * Loads Brand and Quality lists.
     * Returns add new car page.
     */
    @Override
    public String execute(HttpServletRequest request) {

        try {
            List<Brand> brandList = brandService.findAllBrands();
            List<Quality> qualityClassList = qualityService.findAllQualityClasses();
            request.getSession().setAttribute(BRAND_LIST, brandList);
            request.getSession().setAttribute(QUALITY_CLASS_LIST, qualityClassList);
        } catch (DataBaseException e) {
            request.setAttribute(STATUS, StatusesContainer.CAR_INFO_LOADING_EXCEPTION);
        }
        return JspFilePath.ADMIN_ADD_NEW_CAR;
    }
}
