package ru.kolobkevic.currencyexchange.currency.dto;

import com.googlecode.jmapper.annotations.JMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDto {
    @JMap
    private String code;
    @JMap
    private String fullName;
    @JMap
    private String sign;
}
