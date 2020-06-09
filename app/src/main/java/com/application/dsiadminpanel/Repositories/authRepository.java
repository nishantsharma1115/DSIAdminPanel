package com.application.dsiadminpanel.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.application.dsiadminpanel.Common.Constants;
import com.application.dsiadminpanel.dataClass.RequestCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class authRepository {
    private final MutableLiveData<RequestCall> downloadMutableLiveData;

    public authRepository() {
        this.downloadMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RequestCall> loginUser(String email, String password) {

        final RequestCall r = new RequestCall();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        downloadMutableLiveData.setValue(r);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                                r.setMessage("Finished");
                            } else {
                                r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                                r.setMessage("No data Found");
                            }
                            downloadMutableLiveData.setValue(r);
                        } else {
                            r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                            r.setMessage("Error while login");
                            downloadMutableLiveData.postValue(r);
                        }
                    }
                });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> signUpUser(final String email, String password) {
        final RequestCall r = new RequestCall();

        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        r.setUserId(null);
        downloadMutableLiveData.setValue(r);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                                r.setUserId(user.getUid());
                                r.setMessage("Finished");
                            }
                            downloadMutableLiveData.setValue(r);
                        } else {
                            r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                            r.setMessage("Error while Authentication");
                            downloadMutableLiveData.postValue(r);
                        }
                    }
                });

        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> removeEmployee(String employeeId) {
        final RequestCall r = new RequestCall();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        downloadMutableLiveData.setValue(r);

        DB.child("Pending Form").child(employeeId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                r.setMessage("Removed");
                downloadMutableLiveData.setValue(r);
            }
        });
        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> checkForAdmin(final String email, final String password) {
        final RequestCall r = new RequestCall();
        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please Wait....");
        downloadMutableLiveData.setValue(r);

        DB.child("ADMIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isEmailVerified = false, isPasswordVerified = false;
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (Objects.equals(ds.getKey(), "emailId")) {
                            String value = ds.getValue(String.class);
                            if (value != null) {
                                isEmailVerified = value.equals(email);
                            }
                        } else if (Objects.equals(ds.getKey(), "password")) {
                            String value = ds.getValue(String.class);
                            if (value != null) {
                                isPasswordVerified = value.equals(password);
                            }
                        }
                    }
                    if (isEmailVerified && isPasswordVerified) {
                        r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                        r.setMessage("Admin");
                    } else {
                        r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                        r.setMessage("Not Admin");
                    }
                }
                downloadMutableLiveData.setValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.setValue(r);
            }
        });
        return downloadMutableLiveData;
    }
}
