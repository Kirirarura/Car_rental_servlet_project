package com.pavlenko.kyrylo.controller.command.impl.common;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.command.impl.mapper.CatalogMapper;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.validator.FieldValidator;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.entity.Brand;
import com.pavlenko.kyrylo.model.entity.Quality;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.BrandService;
import com.pavlenko.kyrylo.model.service.CarService;
import com.pavlenko.kyrylo.model.service.QualityService;
import com.pavlenko.kyrylo.model.service.util.PaginationInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;


/**
 * Returns Catalog page. Takes parameters from user input
 *  * to sort or/and select one specific characteristic of car.
 */
public class GetCatalogCommand implements Command {

    private final CarService carService;
    private final QualityService qualityService;
    private final BrandService brandService;
    private final CatalogMapper catalogMapper = new CatalogMapper();

    private static final Integer START_PAGE_NUMBER = 1;
    private static final String PAGES_NUMBER = "pagesNumber";
    private static final String PAGE = "page";
    private static final String ROLE_ATTRIBUTE = "role";




    public GetCatalogCommand(CarService carService, QualityService qualityService, BrandService brandService) {
        this.carService = carService;
        this.qualityService = qualityService;
        this.brandService = brandService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        Map<String, String> filterParam = catalogMapper.fetchFilterParametersFromRequest(request);
        String role = (String) request.getSession().getAttribute(ROLE_ATTRIBUTE);
        int activePageNumber = getActivePageNumber(request);

        try {
            PaginationInfo paginationResultData =
                    carService.getPaginationResultData(filterParam, activePageNumber, role);
            request.setAttribute(PAGES_NUMBER, paginationResultData.getPagesCount());
            request.setAttribute(CAR_LIST, paginationResultData.getCarListPage());

            List<Brand> brandList = brandService.findAllBrands();
            List<Quality> qualityClassList = qualityService.findAllQualityClasses();
            request.getSession().setAttribute(BRAND_LIST, brandList);
            request.getSession().setAttribute(QUALITY_CLASS_LIST, qualityClassList);
        } catch (DataBaseException e) {
            request.setAttribute(STATUS, StatusesContainer.CAR_INFO_LOADING_EXCEPTION);
        }

        catalogMapper.insertInfoIntoRequest(filterParam, request);
        return JspFilePath.CATALOG;
    }

    private int getActivePageNumber(HttpServletRequest request) {
        String pageNumberString = request.getParameter(PAGE);

        if (!FieldValidator.fieldIsEmpty(pageNumberString)) {
            return Integer.parseInt(pageNumberString);
        } else {
            return START_PAGE_NUMBER;
        }
    }



}
