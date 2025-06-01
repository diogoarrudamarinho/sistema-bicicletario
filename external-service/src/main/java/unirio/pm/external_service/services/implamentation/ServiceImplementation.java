package unirio.pm.external_service.services.implamentation;

import org.springframework.stereotype.Service;

import unirio.pm.external_service.services.Services;

@Service
public class ServiceImplementation implements Services {
    
    @Override
    public String helloWorld(){
        return "Hello world";
    }
}
