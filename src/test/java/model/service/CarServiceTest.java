package model.service;

import com.pavlenko.kyrylo.model.dao.CarDao;
import com.pavlenko.kyrylo.model.dto.CarDto;
import com.pavlenko.kyrylo.model.entity.Brand;
import com.pavlenko.kyrylo.model.entity.Car;
import com.pavlenko.kyrylo.model.entity.Quality;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.CarService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class CarServiceTest {

    CarDao carDao = mock(CarDao.class);
    CarService carService = new CarService(carDao);


    private static final Brand CAR_BRAND = new Brand(Brand.BrandEnum.getRandomBrand());
    private static final Quality CAR_QUALITY = new Quality(Quality.QualityEnum.getRandomQuality());
    private static final String CAR_MODEL = "model";
    private static final String CAR_PRICE = "50";
    private static final String CAR_DESCRIPTION_EN = "description";
    private static final String CAR_DESCRIPTION_UA = "опис";
    private static final String PRICE = "price";
    private static final String STATUS = "status";
    private static final String QUALITY = "quality";

    private static final CarDto CAR_DTO = new CarDto(
            CAR_BRAND,
            CAR_MODEL,
            CAR_PRICE,
            CAR_QUALITY,
            CAR_DESCRIPTION_EN,
            CAR_DESCRIPTION_UA
            );

    @Test
    void testFindCarById() throws DataBaseException {
        carService.findById(1L);
        verify(carDao, times(1)).findById(1L);
    }


    @Test
    void testFindAllStatuses() throws DataBaseException {
        carService.findAllStatuses();
        verify(carDao, times(1)).findAllStatuses();
    }



    @Test
    void testEditCarInfoPriceCase() throws DataBaseException {
        String price = "50";
        carService.editCarInfo(PRICE, price, 1L, 1L);
        verify(carDao, times(1)).editCarPrice(new BigDecimal(price), 1L);
    }

    @Test
    void testEditCarInfoQualityCase() throws DataBaseException {
        carService.editCarInfo(QUALITY, "NEW", 1L, 1L);
        verify(carDao, times(1)).editCarQuality(1L, 1L);
    }

    @Test
    void testEditCarInfoStatusCase() throws DataBaseException {
        carService.editCarInfo(STATUS, "UNDER_REPAIR", 1L, 1L);
        verify(carDao, times(1)).editCarStatus(1L, 1L);
    }

    @Test
    void testEditCarInfoDescriptionEnCase() throws DataBaseException {
        String info = "info";
        String label = "descriptionEn";
        carService.editCarInfo(label, info , 1L, 1L);
        verify(carDao, times(1)).editCarDescriptionEn(1L, info);
    }
    @Test
    void testEditCarInfoDescriptionUaCase() throws DataBaseException {
        String info = "info";
        String label = "descriptionUa";
        carService.editCarInfo(label, info , 1L, 1L);
        verify(carDao, times(1)).editCarDescriptionUa(1L, info);
    }

    @Test
    void testDeleteCar() throws DataBaseException {
        carService.deleteCarById(1L);
        verify(carDao, times(1)).delete(1L);
    }

    @Test
    void testRegisterNewCar() throws DataBaseException {
        carService.registerNewCar(CAR_DTO);
        Car car = new Car(CAR_DTO);
        verify(carDao, times(1)).create(car);
    }


}
