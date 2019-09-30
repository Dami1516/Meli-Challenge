package com.mercadolibre.android.sdk.example.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mercadolibre.android.sdk.example.MainActivity;
import com.mercadolibre.android.sdk.example.R;
import com.mercadolibre.android.sdk.example.adapter.SlidingImage_Adapter;
import com.mercadolibre.android.sdk.example.api.MeliSDK;
import com.mercadolibre.android.sdk.example.model.Item;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;



public class ItemsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private MeliSDK meliSDK;
    private Item item;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private String[] urls = new String[] {
            "http://hrica.gob.pe/images/cargando.gif"};


    // TODO: Rename and change types of parameters
    private String id;
    private OnFragmentInteractionListener mListener;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        id = bundle.getString("item", "MLA631252126");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_items, container, false);
        meliSDK = ((MainActivity)getActivity()).getMeliSDK();
        meliSDK.getItemAuth("/items/"+id,this);
        Button btn = view.findViewById(R.id.btnVisitar);
        btn.setOnClickListener(this);
        init();
        return view;
    }

    public void setearItem(Item item){
        this.item = item;
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),item.getFotos()));
        ((TextView)view.findViewById(R.id.tvEstado)).setText(item.getEstado());
        ((TextView)view.findViewById(R.id.tvVendidos)).setText(item.getVendidos()+" vendidos");
        ((TextView)view.findViewById(R.id.tvNombre)).setText(item.getNombre());
        if (item.getPrecioAnterior().equals(item.getPrecio()))
            ((TextView)view.findViewById(R.id.tvPrecioOriginal)).setVisibility(View.INVISIBLE);
        else {
            ((TextView) view.findViewById(R.id.tvPrecioOriginal)).setText("$ "+item.getPrecioAnterior());
            ((TextView)view.findViewById(R.id.tvPrecioOriginal)).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.tvPrecioOriginal)).setPaintFlags(((TextView)view.findViewById(R.id.tvPrecioOriginal)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ((TextView)view.findViewById(R.id.tvPrecio)).setText("$ "+item.getPrecio());
        ((TextView)view.findViewById(R.id.tvEnvioGratis)).setVisibility(item.getEnvioGratis());
        ((TextView)view.findViewById(R.id.tvMercadoPago)).setVisibility(item.getMercadoPago());
    }

    private void init() {

        mPager = view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),urls));

        CirclePageIndicator indicator = view.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnVisitar){
            Uri uri = Uri.parse(item.getPermaLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
