package unirio.pm.external_service.services;

import unirio.pm.external_service.dto.EmailDTO;

public interface EmailService{
    String helloWorld();
    EmailDTO enviarEmail(EmailDTO email);
}

