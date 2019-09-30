package com.mercadolibre.android.sdk.example.api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mercadolibre.android.sdk.ApiResponse;
import com.mercadolibre.android.sdk.Identity;
import com.mercadolibre.android.sdk.Meli;
import com.mercadolibre.android.sdk.example.LoginScreen;
import com.mercadolibre.android.sdk.example.MainActivity;
import com.mercadolibre.android.sdk.example.R;
import com.mercadolibre.android.sdk.example.fragments.ItemsFragment;
import com.mercadolibre.android.sdk.example.fragments.MainFragment;
import com.mercadolibre.android.sdk.example.model.Item;
import com.mercadolibre.android.sdk.example.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MeliSDK {

    private Context context;
    private boolean log;
    private User usuario;
    private MainActivity activity;
    private MainFragment fragment;
    private ItemsFragment itemsFragment;

    public MeliSDK(Context context, boolean log, MainActivity ActivityPrincipal){
        this.context = context;
        this.log = log;
        usuario = new User();
        this.activity = ActivityPrincipal;
    }

    public void inicializar(){
        // Set SDK to log events
        Meli.setLoggingEnabled(true);
        // Initialize the MercadoLibre SDK
        Meli.initializeSDK(context);
        //Inicializo al usuario
        new GetAsyncUserProfile().execute(new Command() {
            ApiResponse executeCommand() {
                String userId = getUserID();
                try{
                    ApiResponse response = Meli.get("/users/" + userId);
                    return response;
                }
                catch (Exception e){
                    Toast.makeText(context,"Parece que hay problemas de conexión, por favor intenta mas tarde", Toast.LENGTH_LONG);
                    Log.e("ERROR Conexion","Al parecer hubo problemas de conexion");
                    return null;
                }

            }
        });
    }


    public User getUsuario(){
        if (usuario.getNombre().equals("Anonimo") && Meli.getCurrentIdentity(context)!=null){
            new GetAsyncUserProfile().execute(new Command() {
                ApiResponse executeCommand() {
                    String userId = getUserID();
                    try{
                        ApiResponse response = Meli.get("/users/" + userId);
                        return response;
                    }
                    catch (Exception e){
                        Log.e("ERROR Conexion","Al parecer hubo problemas de conexion");
                        Toast.makeText(context,"Parece que hay problemas de conexión, por favor intenta mas tarde", Toast.LENGTH_LONG);
                        return null;
                    }

                }
            });
        }
        return usuario;
    }

    public ArrayList<Item> getItems(final String ruta, MainFragment fragment){
        ArrayList<Item> lista = new ArrayList<>();
        this.fragment = fragment;
        //Inicializo al usuario
        new GetAsyncItems().execute(new Command() {
            ApiResponse executeCommand() {
                String userId = getUserID();
                try{
                    ApiResponse response = Meli.get(ruta);
                    return response;
                }
                catch (Exception e){
                    Log.e("ERROR Conexion","Al parecer hubo problemas de conexion");
                    Toast.makeText(context,"Parece que hay problemas de conexión, por favor intenta mas tarde", Toast.LENGTH_LONG);
                    return null;
                }

            }
        });
        return lista;
    }

    public Item getItemAuth(final String ruta, ItemsFragment fragment){
        Item item = new Item();
        this.itemsFragment= fragment;
        //Inicializo al usuario
        new GetAsyncItemAuth().execute(new Command() {
            ApiResponse executeCommand() {
                String userId = getUserID();
                try{
                    ApiResponse response = Meli.getAuth(ruta,Meli.getCurrentIdentity(context));
                    return response;
                }
                catch (Exception e){
                    Log.e("ERROR Conexion","Al parecer hubo problemas de conexion");
                    Toast.makeText(context,"Parece que hay problemas de conexión, por favor intenta mas tarde", Toast.LENGTH_LONG);
                    return null;
                }

            }
        });
        return item;
    }

    private String getUserID() {
        Identity identity = Meli.getCurrentIdentity(context);
        if (identity == null) {
            return null;
        } else {
            return identity.getUserId();
        }
    }

    private void setearUserProfile(ApiResponse apiResponse){
        if (getUserID()==null) {
            Intent intent = new Intent(context, LoginScreen.class);
            context.startActivity(intent);
            Toast.makeText(context,"Su sesión a caducado, por favor logeese nuevamente",Toast.LENGTH_SHORT);
        }
        JSONObject contenido=null;
        try {
            contenido = new JSONObject(apiResponse.getContent());
            usuario.setCiudad(contenido.getJSONObject("address").getString("city"));
            usuario.setNombre(contenido.getString("nickname"));
            usuario.setPuntos(String.valueOf(contenido.getInt("points")));
            if (!contenido.getString("logo").equals("null")) {
                DownloadImageProfileTask task = new DownloadImageProfileTask(usuario);
                task.execute(contenido.getString("logo"));
            }
            else
            {
                usuario.setImagen(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.app_icon));
            }
            activity.refrescarUsuario(usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetAsyncUserProfile extends AsyncTask<Command, Void, ApiResponse> {

        @Override
        protected void onPreExecute() {
            //findViewById(R.id.pg_loading).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            //findViewById(R.id.pg_loading).setVisibility(View.GONE);
            try {
                if (apiResponse != null) {
                    setearUserProfile(apiResponse);
                }
            }
            catch (Exception e){
                Toast.makeText(context,"Algo sucede con la conexión a internet, intentalo en unos momentos nuevamente por favor",Toast.LENGTH_LONG);
            }
        }

        @Override
        protected ApiResponse doInBackground(Command... params) {
            return params[0].executeCommand();
        }
    }

    private void setearItems(ApiResponse apiResponse){
        JSONObject respuesta = null;
        JSONArray resultados = null;
        ArrayList<Item> lista = new ArrayList<>();
        try {
            respuesta = new JSONObject(apiResponse.getContent());
            resultados = respuesta.getJSONArray("results");

            for (int i=0;i<15;i++){
                JSONObject obj= (JSONObject) resultados.get(i);
                int visibilidadEnvioGratis=0;
                if (obj.getJSONObject("shipping").optBoolean("free_shipping"))
                    visibilidadEnvioGratis=1;
                Item item = new Item(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.app_icon), obj.getString("title"),String.valueOf(obj.getDouble("price")),visibilidadEnvioGratis);
                item.setId(obj.getString("id"));
                DownloadImageTask task = new DownloadImageTask(item);
                task.execute(obj.getString("thumbnail"));
                //lista.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setearLista(lista);
    }

    private class GetAsyncItems extends AsyncTask<Command, Void, ApiResponse> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            try {
                if (apiResponse != null) {
                    setearItems(apiResponse);
                }
            } catch (Exception e) {
                Toast.makeText(context,"Algo sucede con la conexión a internet, intentalo en unos momentos nuevamente por favor",Toast.LENGTH_LONG);
            }
        }

        @Override
        protected ApiResponse doInBackground(Command... params) {
            return params[0].executeCommand();
        }
    }

    private void setearItemAuth(ApiResponse apiResponse){
        JSONObject respuesta = null;
        if (apiResponse.getResponseCode()== ApiResponse.ApiResponseCode.RESPONSE_CODE_ERROR){
            Intent intent = new Intent(context, LoginScreen.class);
            context.startActivity(intent);
        }
        Item item=null;
        try {
            respuesta = new JSONObject(apiResponse.getContent());
            int visibilidadEnvioGratis=0;
            if (respuesta.getJSONObject("shipping").optBoolean("free_shipping"))
                visibilidadEnvioGratis=1;
            item = new Item(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.app_icon), respuesta.getString("title"),String.valueOf(respuesta.getDouble("price")),visibilidadEnvioGratis);

            JSONArray arrayFotos = respuesta.getJSONArray("pictures");
            String[] fotos = new String[arrayFotos.length()];
            for (int i=0;i<arrayFotos.length();i++){
                fotos[i]=((JSONObject)arrayFotos.get(i)).getString("url");
            }
            item.setFotos(fotos);
            item.setEstado((respuesta.getString("condition").equals("new"))?"Nuevo":"Usado");
            int mp=0;
            if (respuesta.optBoolean("accepts_mercadopago"))
                mp=1;
            item.setMercadoPago(mp);
            item.setVendidos(String.valueOf(respuesta.getInt("sold_quantity")));
            item.setPrecioAnterior(respuesta.getString("original_price"));
            item.setPermaLink(respuesta.getString("permalink"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        itemsFragment.setearItem(item);
    }

    private class GetAsyncItemAuth extends AsyncTask<Command, Void, ApiResponse> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            try {
                if (apiResponse != null) {
                    setearItemAuth(apiResponse);
                }
            } catch (Exception e) {
                Toast.makeText(context,"Algo sucede con la conexión a internet, intentalo en unos momentos nuevamente por favor",Toast.LENGTH_LONG);
            }
        }

        @Override
        protected ApiResponse doInBackground(Command... params) {
            return params[0].executeCommand();
        }
    }

    private class DownloadImageProfileTask extends AsyncTask<String, Void, Bitmap> {
        User user;
        public DownloadImageProfileTask(User user) {
            this.user = user;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                Log.e("ERROR URL", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ERROR IO", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
        }

        protected void onPostExecute(Bitmap result) {
            usuario.setImagen(result);
            activity.refrescarUsuario(usuario);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Item item;
        public DownloadImageTask(Item item) {
            this.item = item;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                Log.e("ERROR URL", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("ERROR IO", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
        }

        protected void onPostExecute(Bitmap result) {
            item.setImagen(result);
            ArrayList<Item> lista = fragment.getLista();
            lista.add(item);
            fragment.setearLista(lista);
        }
    }

    private abstract class Command {
        abstract ApiResponse executeCommand();
    }

}
