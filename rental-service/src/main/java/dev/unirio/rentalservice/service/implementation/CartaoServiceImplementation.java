package dev.unirio.rentalservice.service.implementation;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.rentalservice.entity.Cartao;
import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.mapper.CartaoMapper;
import dev.unirio.rentalservice.repository.CartaoRepository;
import dev.unirio.rentalservice.service.CartaoService;

@Service
public class CartaoServiceImplementation implements CartaoService {
    
    private final CartaoRepository repository;
    private final CartaoMapper mapper;

    public CartaoServiceImplementation(CartaoRepository repository, CartaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CartaoDTO buscarCartao(Long ciclistaId){
        return mapper.toDto(
            repository.findByCiclistaId(ciclistaId)
            .orElseThrow(() -> new ObjectNotFoundException("Cartão não encontrado", ciclistaId)));
    }

    @Override
    public CartaoDTO atualizarCartao(Long ciclistaId, CartaoDTO dto) {
        Cartao cartao = repository.findByCiclistaId(ciclistaId)
            .orElseThrow(() -> new ObjectNotFoundException("Cartão não encontrado", ciclistaId));

        mapper.updateEntityFromDto(dto, cartao);
        
        return mapper.toDto(repository.save(cartao));
    }
    
}
