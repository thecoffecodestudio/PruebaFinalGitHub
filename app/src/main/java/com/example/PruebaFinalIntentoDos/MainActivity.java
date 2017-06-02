package com.example.PruebaFinalIntentoDos;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends ActionBarActivity {
    //Objetos para el inicio de sesion en FB
    private CallbackManager cM;
    private LoginButton lB;

    //Objetos para el banner
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        adView = (AdView) findViewById(R.id.adView);
        //El servidor nos manda los anuncios que va mostrar
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);


        FacebookSdk.sdkInitialize(getApplicationContext());
        cM = CallbackManager.Factory.create();

        getFbKeyHash("5XUUU0Lfz/iTOMYKpfQQ8/dkDi4=");

        setContentView(R.layout.activity_main);

        lB = (LoginButton) findViewById(R.id.login_button);

        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(MainActivity.this, "Sesion iniciada", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(MainActivity.this, "Sesion Cancelada", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(MainActivity.this, "Error en inicio de sesion", Toast.LENGTH_LONG).show();

            }
        });
    }

    //inicio para los métodos de ciclo de vida del banner. me permiten pausar, reanudar y destruir el elemento
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
    }

    //Fin para los metodos del ciclo de vida del banner
    //Inicio del metodo para validar el KeyHash recibe como parametro el Keyhash generado
    public void getFbKeyHash(String packageName) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    //Fin del metodo para validar el keyhash
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        cM.onActivityResult(reqCode, resCode, i);
    }


}
