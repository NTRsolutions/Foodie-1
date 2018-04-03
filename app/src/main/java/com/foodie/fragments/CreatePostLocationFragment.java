package com.foodie.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foodie.HomeActivity;
import com.foodie.R;
import com.foodie.utils.CommonUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.ArrayList;

/**
 * Created by WebPlanetDeveloper on 4/3/2018.
 */

public class CreatePostLocationFragment extends Fragment {
    private String locationStr = "";
    private double workLat,workLong;

    private Context mCtx;
    SupportPlaceAutocompleteFragment places;
    private Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        mCtx = getActivity();
        Button btnLocation = (Button) rootView.findViewById(R.id.btnLocation);

        // Retrieve the PlaceAutocompleteFragment.

        arguments = getArguments();
        /*if (arguments != null && arguments.containsKey("images")){
            ArrayList<String> value = arguments.getStringArrayList("images");
            CommonUtils.showAlertMessage(getActivity(), getString(R.string.error), getString(R.string.error), value.toString()+" Images arraylist", getString(R.string.ok));
        }*/
        //places  = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        places.getView().findViewById(R.id.place_autocomplete_fragment).setBackgroundColor(Color.parseColor("#FFFFFF"));
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        places.setFilter(typeFilter);


        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Log.e("logplace",""+place.getName());
                workLat = place.getLatLng().latitude;
                workLong = place.getLatLng().longitude;
                locationStr = place.getName().toString();
                //Toast.makeText(getActivity(),place.getName()+" lat "+locationStr,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                //Log.e("logplace Error",status.toString());
                //Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();

            }
        });


        //END

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (!locationStr.isEmpty()) {
/*
                    SharedPrefrenceManager.getInstance(mCtx).setTmpSaveData(Constant.CREATE_POST, Constant.LOCATION_NAME, locationStr);
                    SharedPrefrenceManager.getInstance(mCtx).setTmpSaveData(Constant.CREATE_POST, Constant.LATITUDE, String.valueOf(workLat));
                    SharedPrefrenceManager.getInstance(mCtx).setTmpSaveData(Constant.CREATE_POST, Constant.LONGITUDE, String.valueOf(workLong));
*/
                    Bundle bundle = new Bundle();
                    bundle.putString("location",locationStr);
                    bundle.putString("latitude", String.valueOf(workLat));
                    bundle.putString("longitude",String.valueOf(workLong));

                    if (arguments != null && arguments.containsKey("images")){
                        ArrayList<String> images = arguments.getStringArrayList("images");
                        bundle.putStringArrayList("images", images) ;
                        //CommonUtils.showAlertMessage(getActivity(), getString(R.string.error), getString(R.string.error), value.toString()+" Images arraylist", getString(R.string.ok));
                    }
                    Log.e("bundle data",bundle.toString());
                    Fragment foodtype = new FoodTasteTypeFragment();
                    foodtype.setArguments(bundle);
                    ((HomeActivity)getActivity()).attachFragmentOnUi(foodtype,"CreatePost",R.id.nav_create_post);
                   /* FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment fragment = new FoodTasteTypeFragment();
                    ft.add(R.id.flContent, fragment);
                    ft.commitAllowingStateLoss();*/
                } else {
                    CommonUtils.showAlertMessage(mCtx, getString(R.string.error), getString(R.string.error), "Please provide location.", getString(R.string.ok));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
