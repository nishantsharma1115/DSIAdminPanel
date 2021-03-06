package com.application.dsiadminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.ViewModel.authViewModel;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.application.dsiadminpanel.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class loginActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    ActivityLoginBinding binding;
    authViewModel viewModel;
    String email, password;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(loginActivity.this, employeeDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(authViewModel.class);

        binding.backgroundLayout.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.txtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.background_layout || view.getId() == R.id.txt_login) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        } else if (view.getId() == R.id.txt_signUp) {
            startActivity(new Intent(loginActivity.this, employeeRegistrationFormActivity.class));
        } else if (view.getId() == R.id.btn_login) {
            checkForAdminLogin();
        }
    }

    public void checkForAdminLogin() {
        email = binding.edtEmail.getText().toString();
        password = binding.edtPassword.getText().toString();

        if (email.equals("")) {
            Toast.makeText(loginActivity.this, "Username Can not be Empty", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email Format is incorrect");
            binding.edtEmail.requestFocus();
        } else if (password.equals("")) {
            Toast.makeText(loginActivity.this, "Password Can Not be Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.edtPassword.getText().toString().length() < 8) {
            binding.edtPassword.setError("Password should be 8 character long");
            binding.edtPassword.requestFocus();
        } else {
            viewModel.checkForAdmin(email, password).observe(this, new Observer<RequestCall>() {
                @Override
                public void onChanged(RequestCall requestCall) {
                    if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                        binding.loginProgressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.backgroundLayout.setAlpha((float) 0.4);
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Admin")) {
                        Intent intent = new Intent(loginActivity.this, adminDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Not Admin")) {
                        loginUser();
                    } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_FAILURE) {
                        Toast.makeText(loginActivity.this, requestCall.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loginUser() {
        viewModel.viewModelLogin(email, password).observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("No data Found")) {
                    Toast toast = Toast.makeText(loginActivity.this, "Employee is not registered!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_SUCCESS && requestCall.getMessage().equals("Finished")) {
                    updateUi();
                    binding.loginProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    binding.backgroundLayout.setAlpha(1);
                } else if (requestCall.getStatus() == Constants.OPERATION_IN_PROGRESS) {
                    binding.loginProgressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    binding.backgroundLayout.setAlpha((float) 0.4);
                } else if (requestCall.getStatus() == Constants.OPERATION_COMPLETE_FAILURE) {
                    Toast.makeText(loginActivity.this, requestCall.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.loginProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    binding.backgroundLayout.setAlpha(1);
                }
            }
        });
    }

    public void updateUi() {
        Intent intent = new Intent(loginActivity.this, employeeDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            loginUser();
        }
        return false;
    }
}
