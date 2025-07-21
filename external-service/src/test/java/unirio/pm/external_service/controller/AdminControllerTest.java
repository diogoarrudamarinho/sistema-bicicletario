package unirio.pm.external_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import unirio.pm.external_service.services.AdminService;

@ExtendWith(MockitoExtension.class)

class AdminControllerTest {
    
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController controller;

    @Test
    void testLimparTodosBancos() {
        doNothing().when(adminService).restaurarBanco();
        
        ResponseEntity<Void> resp = controller.restaurarBanco();

        assertEquals(HttpStatus.OK,resp.getStatusCode());
        verify(adminService).restaurarBanco();
    }
}
