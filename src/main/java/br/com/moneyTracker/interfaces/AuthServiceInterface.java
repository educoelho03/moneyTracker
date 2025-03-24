package br.com.moneyTracker.interfaces;

import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;

public interface AuthServiceInterface {
    DataResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO);
    DataResponseDTO registerUser(AuthRegisterRequestDTO authRegisterRequestDTO);
}
