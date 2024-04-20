package ru.kolobkevic.currencyexchange.currency;

import com.googlecode.jmapper.JMapper;
import lombok.RequiredArgsConstructor;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final JMapper<CurrencyResponseDto, Currency> responseMapper =
            new JMapper<>(CurrencyResponseDto.class, Currency.class);
    private final JMapper<Currency, CurrencyRequestDto> requestMapper =
            new JMapper<>(Currency.class, CurrencyRequestDto.class);

    @Override
    public Optional<CurrencyResponseDto> findById(Integer id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        return currency.map(responseMapper::getDestination);
    }

    @Override
    public Optional<CurrencyResponseDto> findByCode(String code) {
        Optional<Currency> currency = currencyRepository.findByCode(code);
        return currency.map(responseMapper::getDestination);
    }

    @Override
    public List<CurrencyResponseDto> findAll() {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            return Collections.emptyList();
        }
        return currencies.stream().map(responseMapper::getDestination).collect(Collectors.toList());
    }

    @Override
    public CurrencyResponseDto save(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyRepository.save(requestMapper.getDestination(currencyRequestDto));
        return responseMapper.getDestination(currency);
    }

    @Override
    public void deleteById(Integer id) {
        currencyRepository.deleteById(id);
    }
}
