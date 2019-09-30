package com.mercadolibre.android.sdk.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadolibre.android.sdk.example.api.MeliSDK;
import com.mercadolibre.android.sdk.example.fragments.ItemsFragment;
import com.mercadolibre.android.sdk.example.fragments.MainFragment;
import com.mercadolibre.android.sdk.example.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemsFragment.OnFragmentInteractionListener {

    private MeliSDK meliSDK;
    private String toSearch="Computadora";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupUI();
    }

    @Override
    public void onResume(){
        super.onResume();
        User usuario = meliSDK.getUsuario();
        refrescarUsuario(usuario);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public MeliSDK getMeliSDK(){
        return meliSDK;
    }

    public void setToSearch(String toSearch){
        this.toSearch = toSearch;
    }

    public String getToSearch(){
        return toSearch;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        TextView appBarTV = findViewById(R.id.appbar_text_view);
        drawer.closeDrawers();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.inicio) {
            appBarTV.setText("Inicio");
            MainFragment fragment = new MainFragment();
            ft.replace(R.id.f_container, fragment);
            ft.commit();

        } else if (id == R.id.irML) {
            Uri uri = Uri.parse("https://www.mercadolibre.com.ar/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if (id == R.id.acerca) {
            Uri uri = Uri.parse("https://www.linkedin.com/in/damian-fuentes-79a748127/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setupUI() {
        meliSDK = new MeliSDK(this.getApplicationContext(),true, this);
        meliSDK.inicializar();
        //Seteo el fragment inicial
        TextView appBarTV = findViewById(R.id.appbar_text_view);
        appBarTV.setText("Inicio");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MainFragment fragment = new MainFragment();
        ft.replace(R.id.f_container, fragment);
        ft.commit();


        User usuario = meliSDK.getUsuario();
        refrescarUsuario(usuario);
    }

    public void refrescarUsuario(User usuario){
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        ImageView avatar = header.findViewById(R.id.avatarEnMenu);
        TextView nombreMenu = header.findViewById(R.id.nombreEnMenu);
        TextView ciudadMenu = header.findViewById(R.id.ciudadEnMenu);
        TextView puntosMenu = header.findViewById(R.id.puntosEnMenu);
        avatar.setImageBitmap(usuario.getImagen());
        nombreMenu.setText(usuario.getNombre());
        ciudadMenu.setText(usuario.getCiudad());
        puntosMenu.setText("Posee "+usuario.getPuntos()+" puntos");

    }
}
