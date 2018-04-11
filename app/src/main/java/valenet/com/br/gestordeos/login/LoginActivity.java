package valenet.com.br.gestordeos.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.forgot_password.ForgotPasswordActivity;
import valenet.com.br.gestordeos.os_type.OsTypeActivity;

public class LoginActivity extends AppCompatActivity implements Login.LoginView {

    @BindView(R.id.edit_text_email)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.text_view_forgot_password)
    TextView textViewForgotPassword;
    @BindView(R.id.btn_entrar)
    AppCompatButton btnEntrar;

    private Login.LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this.presenter = new LoginPresenterImp(this);
    }

    @Override
    public void navigateToForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToOsType() {
        Intent intent = new Intent(this, OsTypeActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.text_view_forgot_password, R.id.btn_entrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_view_forgot_password:
                navigateToForgotPassword();
                break;
            case R.id.btn_entrar:
                presenter.login(editTextEmail.getText().toString(),
                                editTextPassword.getText().toString());
                break;
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showErrorEmptyUser() {

    }

    @Override
    public void showErrorEmptyPassword() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showErrorLogin(String error) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
