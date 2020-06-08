package com.application.dsiadminpanel.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.dsiadminpanel.Repositories.authRepository;
import com.application.dsiadminpanel.dataClass.Employee;
import com.application.dsiadminpanel.dataClass.RequestCall;

public class authViewModel extends ViewModel {
    private final authRepository repository;

    public authViewModel() {
        repository = new authRepository();
    }

    public MutableLiveData<RequestCall> viewModelLogin(String email, String password) {
        return repository.loginUser(email, password);
    }

    public MutableLiveData<RequestCall> vieModelSignUp(String email, String password) {
        return repository.signUpUser(email, password);
    }

    public MutableLiveData<RequestCall> removeEmployee(String employeeId) {
        return repository.removeEmployee(employeeId);
    }

    public MutableLiveData<RequestCall> checkForAdmin(String email, String password) {
        return repository.checkForAdmin(email, password);
    }
}
