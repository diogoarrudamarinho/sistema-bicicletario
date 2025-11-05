package dev.unirio.externalservice.service;

import dev.unirio.externalservice.dto.EmailDTO;

public interface EmailService{
    EmailDTO enviarEmail(EmailDTO email);
}

