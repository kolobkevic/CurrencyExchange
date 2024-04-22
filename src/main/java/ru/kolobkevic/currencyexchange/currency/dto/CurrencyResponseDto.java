package ru.kolobkevic.currencyexchange.currency.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class CurrencyResponseDto {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;
}
