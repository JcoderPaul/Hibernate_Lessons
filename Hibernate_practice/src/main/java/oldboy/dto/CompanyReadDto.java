package oldboy.dto;

import java.util.Map;
/*
Records представляют классы, которые предназначены для
создания контейнеров НЕИЗМЕНЯЕМЫХ данных. Кроме того,
records позволяют упростить разработку, сократив объем
кода.
*/
public record CompanyReadDto(Integer id,
                             String name,
                             Map<String, String> locales) {
}
