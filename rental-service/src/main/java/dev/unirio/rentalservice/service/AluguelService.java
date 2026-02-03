package dev.unirio.rentalservice.service;

import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.dto.AluguelRequestDTO;

public interface AluguelService {
    AluguelDTO alugar(AluguelRequestDTO request);
    AluguelDTO devolver(AluguelRequestDTO request);
}
