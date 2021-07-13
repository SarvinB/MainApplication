package com.example.mainapplication.pages.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mainapplication.data.entities.Admin;
import com.example.mainapplication.data.entities.Customer;
import com.example.mainapplication.data.entities.Seller;
import com.example.mainapplication.data.repository.Repository;
import com.example.mainapplication.data.repository.RepositoryCallback;
import com.example.mainapplication.data.repository.Result;
import com.example.mainapplication.pages.home.HomeActivityAdmin;
import com.example.mainapplication.pages.home.HomeActivityCustomer;
import com.example.mainapplication.pages.home.HomeActivitySeller;
import com.example.mainapplication.objects.Person;
import com.example.mainapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class LoginFragment extends Fragment
{

    GoogleSignInClient mGoogleSignInClient;
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                        handleSignInResult(task);
                    }
                }
            });

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        AppCompatButton enter = view.findViewById(R.id.EnterB);
        TextView register = view.findViewById(R.id.register_text);
        EditText password = view.findViewById(R.id.Password);
        TextView error = view.findViewById(R.id.errorLogin);
        EditText username = view.findViewById(R.id.UsernameLogin);
        TextView forget = view.findViewById(R.id.ForgetPW);
        RadioButton radioAdmin = view.findViewById(R.id.radio_Admin);
        RadioButton radioSeller = view.findViewById(R.id.radio_Seller);
        RadioButton radioCustomer = view.findViewById(R.id.radio_Customer);
        RadioGroup radioGroup = view.findViewById(R.id.radio);
        SignInButton google = view.findViewById(R.id.sign_in_button);
        google.setSize(SignInButton.SIZE_STANDARD);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String user = "";
                final int[] c = {0};

                if (username.length() == 0)
                {
                    username.setError("This field must be full");
                    c[0] = 1;
                }

                if (password.length() == 0)
                {
                    password.setError("This field must be full");
                    c[0] = 1;
                }

                int id = radioGroup.getCheckedRadioButtonId();
                if(radioCustomer.isChecked())
                {
                    user = "Customer";
                }
                else if(radioAdmin.isChecked())
                {
                    user = "Admin";
                }
                else if(radioSeller.isChecked())
                {
                    user = "Seller";
                }
                else
                {
                    Toast.makeText(getContext(), "Please set user", Toast.LENGTH_LONG).show();
                }

                if(c[0] == 0)
                {
                    switch (user) {
                        case "Customer": {
                            Repository.getInstance(getContext()).getAllCustomers(new RepositoryCallback<List<Customer>>() {
                                @Override
                                public void onComplete(Result<List<Customer>> result) {
                                    Intent intent = new Intent(getActivity(), HomeActivityCustomer.class);
                                    if (result instanceof Result.Success) {
                                        for (int i = 0; i < ((Result.Success<List<Customer>>) result).data.size(); i++) {
                                            if (username.getText().toString().equals(((Result.Success<List<Customer>>) result).data.get(i).userName))
                                            {
                                                if(password.getText().toString().equals(((Result.Success<List<Customer>>) result).data.get(i).password))
                                                {
                                                    intent.putExtra("username", username.getText().toString());
                                                    intent.putExtra("email", ((Result.Success<List<Customer>>) result).data.get(i).email);
                                                    getActivity().startActivity(intent);
                                                }
                                                else
                                                {
                                                    error.setText("Invalid password for this username");
                                                }
                                                return;
                                            }
                                        }
                                        error.setText("This username doesn't exist");
                                    }
                                    //POINT: set error
                                    else if (result instanceof Result.Error) {
                                        System.out.println("error");
                                    }
                                }
                            });
                            break;
                        }

                        case "Admin": {
                            Repository.getInstance(getContext()).getAllAdmins(new RepositoryCallback<List<Admin>>() {
                                @Override
                                public void onComplete(Result<List<Admin>> result) {
                                    Intent intent = new Intent(getActivity(), HomeActivityAdmin.class);
                                    if (result instanceof Result.Success) {
                                        for (int i = 0; i < ((Result.Success<List<Admin>>) result).data.size(); i++) {
                                            if (username.getText().toString().equals(((Result.Success<List<Admin>>) result).data.get(i).userName))
                                            {
                                                if(password.getText().toString().equals(((Result.Success<List<Admin>>) result).data.get(i).password))
                                                {
                                                    intent.putExtra("username", username.getText().toString());
                                                    intent.putExtra("email", ((Result.Success<List<Admin>>) result).data.get(i).email);
                                                    getActivity().startActivity(intent);
                                                }
                                                else
                                                {
                                                    error.setText("Invalid password for this username");
                                                }
                                                return;
                                            }
                                        }
                                        error.setText("This username doesn't exist");
                                    } else if (result instanceof Result.Error) {
                                        System.out.println("error");
                                    }
                                }
                            });
                            break;
                        }

                        case "Seller": {
                            Repository.getInstance(getContext()).getAllSellers(new RepositoryCallback<List<Seller>>() {
                                @Override
                                public void onComplete(Result<List<Seller>> result) {
                                    Intent intent = new Intent(getActivity(), HomeActivitySeller.class);
                                    if (result instanceof Result.Success) {
                                        for (int i = 0; i < ((Result.Success<List<Seller>>) result).data.size(); i++) {
                                            if (username.getText().toString().equals(((Result.Success<List<Seller>>) result).data.get(i).userName))
                                            {
                                                if(password.getText().toString().equals(((Result.Success<List<Seller>>) result).data.get(i).password))
                                                {
                                                    intent.putExtra("username", username.getText().toString());
                                                    intent.putExtra("email", ((Result.Success<List<Seller>>) result).data.get(i).email);
                                                    getActivity().startActivity(intent);
                                                }
                                                else
                                                {
                                                    error.setText("Invalid password for this username");
                                                }
                                                return;
                                            }
                                        }
                                        error.setText("This username doesn't exist");
                                    } else if (result instanceof Result.Error) {
                                        System.out.println("error");
                                    }
                                }
                            });
                            break;
                        }
                    }
                }

            }
       });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount acct) {
        if(acct != null)
        {
            Person person = new Person();
//            person.setName(acct.getDisplayName());

            //POINT: SETBIRTHDAY
            //person.setBirthday();
//            person.setEmail(acct.getEmail());
//            person.setLastName(acct.getFamilyName());
//            person.setImage(acct.getPhotoUrl().toString());

            //POINT: SET USER
            person.setUser(Person.User.CUSTOMER);
//            person.setUsername(acct.getGivenName());

            switch (person.getUser())
            {
                case CUSTOMER:
                {
                    Intent intent = new Intent(getContext(), HomeActivityCustomer.class);
                    startActivity(intent);
                    break;
                }

                case ADMIN:
                {
                    Intent intent = new Intent(getContext(), HomeActivityAdmin.class);
                    startActivity(intent);
                    break;
                }

                case SELLER:
                {
                    Intent intent = new Intent(getContext(), HomeActivitySeller.class);
                    startActivity(intent);
                    break;
                }
            }
            getActivity().finish();
        }
        else
        {
            Toast.makeText(getActivity(), "account not find!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mStartForResult.launch(signInIntent);
    }


}
