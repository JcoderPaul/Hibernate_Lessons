package oldboy.mapper;

import oldboy.dto.CompanyReadDto;
import oldboy.entity.Company;
import org.hibernate.Hibernate;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company object) {
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(
                object.getId(),
                object.getCompanyName(),
                object.getLocales()
        );
    }
}
