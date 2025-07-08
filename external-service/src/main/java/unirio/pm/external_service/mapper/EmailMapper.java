package unirio.pm.external_service.mapper;

import org.springframework.stereotype.Component;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.entity.Email;

@Component
public class EmailMapper {

    public EmailDTO toDTO(Email entity) {
        if (entity == null) return null;

        EmailDTO dto = new EmailDTO();
        dto.setDestinatario(entity.getDestinatario());
        dto.setAssunto(entity.getAssunto());
        dto.setMensagem(entity.getMensagem());

        return dto;
    }

    public Email toEntity(EmailDTO dto) {
        if (dto == null) return null;

        return new Email(
            dto.getDestinatario(),
            dto.getAssunto(),
            dto.getMensagem()
        );
    }
}