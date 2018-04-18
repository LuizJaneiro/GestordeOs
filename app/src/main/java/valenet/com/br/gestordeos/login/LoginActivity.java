package valenet.com.br.gestordeos.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.os_type.OsTypeActivity;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class LoginActivity extends AppCompatActivity implements Login.LoginView {

    @BindView(R.id.edit_text_email)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.btn_entrar)
    AppCompatButton btnEntrar;
    @BindView(R.id.login_view)
    ViewGroup loginView;
    @BindView(R.id.lottie_animation_loading)
    LottieAnimationView lottieAnimationLoading;
    @BindView(R.id.loading_view)
    ViewGroup loadingView;
    @BindView(R.id.text_input_email)
    TextInputLayout textInputEmail;
    @BindView(R.id.text_input_password)
    TextInputLayout textInputPassword;

    private Login.LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.presenter = new LoginPresenterImp(this);

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_EMAIL_LOGIN, Context.MODE_PRIVATE);
        String savedEmail = null;
        String savedPassword = null;
        savedEmail = sharedPref.getString(ValenetUtils.SHARED_PREF_KEY_EMAIL_CLIENT, null);
        savedPassword = sharedPref.getString(ValenetUtils.SHARED_PREF_KEY_PASSWORD_CLIENT, null);

        if(savedEmail != null)
            editTextEmail.setText(savedEmail);

        if(savedPassword != null)
            editTextPassword.setText(savedPassword);

        ClickGuard.guard(btnEntrar);
    }

    @Override
    public void navigateToOsType() {
        Intent intent = new Intent(this, OsTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //changes commit
    }

    @OnClick({R.id.btn_entrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_entrar:
                textInputEmail.setError(null);
                textInputPassword.setError(null);
                presenter.login(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());
                break;
        }
    }

    @Override
    public void hideProgress() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorEmptyUser() {
        textInputEmail.requestFocus();
        textInputEmail.setError(getResources().getString(R.string.error_field_required));
    }

    @Override
    public void showErrorEmptyPassword() {
        textInputPassword.requestFocus();
        textInputPassword.setError(getResources().getString(R.string.error_field_required));
    }

    @Override
    public void showProgress() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorLogin(String error) {
        Toasty.error(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginView() {
        loginView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginView() {
        loginView.setVisibility(View.GONE);
    }

    @Override
    public void saveLoginData(String userName, String password) {
        SharedPreferences sharedPref = this.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_EMAIL_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(ValenetUtils.SHARED_PREF_KEY_EMAIL_CLIENT, userName);
        editor.putString(ValenetUtils.SHARED_PREF_KEY_PASSWORD_CLIENT, password);

        editor.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
