package valenet.com.br.gestordeos.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.login.LoginActivity;
import valenet.com.br.gestordeos.main.MainActivity;
import valenet.com.br.gestordeos.model.realm.LoginLocal;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashScreenImageView)
    ImageView splashScreenImageView;

    private Intent intent;
    private long time = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (LoginLocal.getInstance().getCurrentUser() == null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, time);
    }

}
