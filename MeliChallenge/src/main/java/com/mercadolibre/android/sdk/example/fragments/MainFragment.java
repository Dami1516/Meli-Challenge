package com.mercadolibre.android.sdk.example.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mercadolibre.android.sdk.example.MainActivity;
import com.mercadolibre.android.sdk.example.R;
import com.mercadolibre.android.sdk.example.adapter.ItemAdapter;
import com.mercadolibre.android.sdk.example.api.MeliSDK;
import com.mercadolibre.android.sdk.example.model.Item;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Item> listaItems;
    private View view;
    private MeliSDK meliSDK;
    private EditText busqueda;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        listaItems = new ArrayList<>();
        Button btn = view.findViewById(R.id.buttonSearch);
        btn.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler1);
        recyclerView.requestFocus();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemAdapter adapter = new ItemAdapter(getContext(),listaItems);

        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.pg_loading).setVisibility(View.VISIBLE);

        busqueda = view.findViewById(R.id.tvSearch);
        busqueda.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    buscar();
                    return true;
                }
                return false;
            }
        });
        meliSDK = ((MainActivity)getActivity()).getMeliSDK();
        TextView appBarTV = getActivity().findViewById(R.id.appbar_text_view);
        appBarTV.setText("Inicio");
        String toSearch = ((MainActivity) getActivity()).getToSearch();
        meliSDK.getItems("/sites/MLA/search?q=" + toSearch, this);
        return view;
    }

    private void buscar(){
        String toSearch = busqueda.getText().toString();
        ((MainActivity) getActivity()).setToSearch(toSearch);
        meliSDK.getItems("/sites/MLA/search?q=" + toSearch, this);
    }

    public void setearLista(ArrayList<Item> lista){
        Log.d("ENTRO","Se reseteo la lista");
        listaItems = lista;
        view.findViewById(R.id.pg_loading).setVisibility(View.INVISIBLE);
        ItemAdapter adapter = new ItemAdapter(getContext(),lista);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TextView appBarTV = getActivity().findViewById(R.id.appbar_text_view);
                appBarTV.setText("Producto");
                ItemsFragment fragment = new ItemsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("item", listaItems.get(position).getId());
                fragment.setArguments(bundle);
                ft.replace(R.id.f_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Item> getLista(){
        return listaItems;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSearch){
            if (busqueda.getText().toString().equals("")){
                busqueda.requestFocus();
            }
            else
            {
                buscar();
                recyclerView.requestFocus();
                view.clearFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }
}
